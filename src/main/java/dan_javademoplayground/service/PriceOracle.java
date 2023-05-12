package dan_javademoplayground.service;

import dan_javademoplayground.model.PriceChangedEvent;
import dan_javademoplayground.persistence.model.Liquidity;
import dan_javademoplayground.persistence.model.Wallet;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class PriceOracle implements PriceChangesSubscriber {

    private final ConcurrentHashMap<String, java.lang.Double> priceRepo;
    private final String identifier;

    private final PriceSource priceSource;

    public PriceOracle(PriceSource priceSource) {
        this.priceSource = priceSource;
        this.priceSource.subscribeForPriceChanges(this);
        this.priceRepo = new ConcurrentHashMap<>();
        this.identifier = UUID.randomUUID().toString();
    }

    @PreDestroy
    public void destroy() {
        this.priceSource.unSubscribeForPriceChanges(this);
    }

    public Double getEstimatedPrice(String tickerInQuestion, String referenceTicker) {
        if (tickerInQuestion.equals(referenceTicker)) {
            return 1.0;
        }
        return priceRepo.getOrDefault(tickerInQuestion + "_" + referenceTicker,
                Optional.ofNullable(priceRepo.get(referenceTicker + "_" + tickerInQuestion)).map(p -> 1 / p)
                        .orElse(null));
    }

    @Override
    public String identifier() {
        return this.identifier;
    }

    @Override
    public void onPriceChanged(PriceChangedEvent e) {
        priceRepo.put(e.getTickerFrom() + "_" + e.getTickerTo(), e.getPrice());
    }

    public Double findValue(Wallet wallet, String tokenOut) {
        List<Liquidity> liquidityList = wallet.getLiquidityList();
        Function<Liquidity, Double> estimatedPriceExtractor =
                l -> Optional.ofNullable(getEstimatedPrice(l.getTicker(), tokenOut)).orElseThrow(
                        () -> new RuntimeException("could not compute estimated price for " + l.getTicker()));
        BiFunction<Double, Liquidity, Double> aa =
                (subtotal, l) -> subtotal + l.getValue() * estimatedPriceExtractor.apply(l);
        return liquidityList.stream().reduce(0.0, aa, Double::sum);
    }
}
