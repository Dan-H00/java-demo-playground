package dan_javademoplayground.service;

import dan_javademoplayground.dto.ExchangeRequest;
import dan_javademoplayground.dto.ExchangeResult;
import dan_javademoplayground.model.PriceChangedEvent;
import dan_javademoplayground.persistence.model.Agency;
import dan_javademoplayground.persistence.model.ExchangePool;
import dan_javademoplayground.persistence.model.Liquidity;
import dan_javademoplayground.persistence.model.Wallet;
import dan_javademoplayground.persistence.repository.AgencyRepository;
import dan_javademoplayground.persistence.repository.ExchangePoolRepository;
import dan_javademoplayground.persistence.repository.PersonRepository;
import dan_javademoplayground.persistence.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Dex implements PriceSource {

    private final Logger logger = LoggerFactory.getLogger(Dex.class);

    private final AgencyRepository agencyRepository;
    private final PersonRepository personRepository;
    private final WalletRepository walletRepository;
    private final ExchangePoolRepository exchangePoolRepository;

    private final Map<String, PriceChangesSubscriber> subscribers;

    public Dex(AgencyRepository agencyRepository,
               PersonRepository personRepository,
               WalletRepository walletRepository,
               ExchangePoolRepository exchangePoolRepository) {
        this.agencyRepository = agencyRepository;
        this.personRepository = personRepository;
        this.walletRepository = walletRepository;
        this.exchangePoolRepository = exchangePoolRepository;

        subscribers = new HashMap<>();
    }

    @Override
    public void subscribeForPriceChanges(PriceChangesSubscriber subscriber) {
        subscribers.put(subscriber.identifier(), subscriber);
    }

    @Override
    public void unSubscribeForPriceChanges(PriceChangesSubscriber subscriber) {
        subscribers.remove(subscriber.identifier());
    }

    @Transactional
    public ExchangeResult swap(ExchangeRequest request) {
        try {
            Agency agency = this.agencyRepository.findById(request.getAgencyId())
                    .orElseThrow(() -> new RuntimeException("Agency not found"));

            personRepository.findById(request.getPersonId())
                    .orElseThrow(() -> new RuntimeException("Person not found"));

            Wallet wallet = walletRepository.findById(request.getWalletId())
                    .orElseThrow(() -> new RuntimeException("Wallet not found"));

            Double exchangedValue = request.getValue();
            // validate liquidity
            List<Liquidity> userLiquidities = new ArrayList<>(wallet.getLiquidityList());
            if (userLiquidities.stream().noneMatch(
                    l -> l.getTicker().equals(request.getFrom())
                            && l.getValue() > exchangedValue)) {
                return ExchangeResult.builder().message("Could not perform swap due to Insufficient funds.")
                        .successful(false)
                        .build();
            }

            ExchangePool exchangePool = searchEligibleLP(request, agency);

            Liquidity liquidityFrom =
                    userLiquidities.stream().filter(l -> l.getTicker().equals(request.getFrom())).findFirst()
                            .get();
            Liquidity liquidityTo =
                    userLiquidities.stream().filter(l -> l.getTicker().equals(request.getTo())).findFirst().orElse(
                            Liquidity.builder().ticker(request.getTo()).value(0.0).build());
            if (userLiquidities.stream().noneMatch(l -> l.getTicker().equals(request.getTo()))){
                userLiquidities.add(liquidityTo);
            }

            double x = exchangePool.getLiquidityOne().getValue();
            double y = exchangePool.getLiquidityTwo().getValue();
            double k = x * y;

            double delta = exchangedValue;
            double returnedValue = 0;
            double exchangePoolNewPrice = 0;

            if (request.getFrom().equals(exchangePool.getLiquidityOne().getTicker())) {
                returnedValue = y - k / (x + delta);
                double newX = x + delta;
                double newY = y - returnedValue;
                exchangePool.getLiquidityOne().setValue(newX);
                exchangePool.getLiquidityTwo().setValue(newY);
                exchangePoolNewPrice = delta/returnedValue;
            } else {
                returnedValue = x - k / (y + delta);
                double newX = x - returnedValue;
                double newY = y + delta;
                exchangePool.getLiquidityOne().setValue(newX);
                exchangePool.getLiquidityTwo().setValue(newY);
                exchangePoolNewPrice = returnedValue/delta;
            }

            PriceChangedEvent e = PriceChangedEvent.builder()
                    .agencyId(agency.getId())
                    .tickerFrom(exchangePool.getLiquidityOne().getTicker())
                    .tickerTo(exchangePool.getLiquidityTwo().getTicker())
                    .price(exchangePoolNewPrice)
                    .build();
            notifySubscribers(e);

            double price = delta/returnedValue;

            liquidityFrom.setValue(liquidityFrom.getValue() - delta);
            liquidityTo.setValue(liquidityTo.getValue() + returnedValue);
            wallet.setLiquidityList(userLiquidities);

            this.exchangePoolRepository.save(exchangePool);
            this.walletRepository.save(wallet);

            com.mcserby.playground.javademoplayground.dto.Liquidity swapped = com.mcserby.playground.javademoplayground.dto.Liquidity.builder()
                    .name(liquidityFrom.getName())
                    .ticker(liquidityFrom.getTicker())
                    .value(delta)
                    .build();

            com.mcserby.playground.javademoplayground.dto.Liquidity result = com.mcserby.playground.javademoplayground.dto.Liquidity.builder()
                    .name(liquidityTo.getName())
                    .ticker(liquidityTo.getTicker())
                    .value(returnedValue)
                    .build();
            String message = "swapped " + swapped + " for " + result + " at a price of " + price;
            logger.info(message);

            return ExchangeResult.builder()
                    .message(message)
                    .successful(true)
                    .swapped(swapped)
                    .result(result)
                    .price(price)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ExchangeResult.builder().message("Could not perform swap. Reason: " + e.getMessage())
                    .successful(false)
                    .build();
        }
    }

    private void notifySubscribers(PriceChangedEvent e) {
        this.subscribers.values().forEach(s -> s.onPriceChanged(e));
    }

    private ExchangePool searchEligibleLP(ExchangeRequest request, Agency agency) {
        return agency.getExchangePools().stream()
                .filter(ep -> (ep.getLiquidityOne().getTicker().equals(request.getFrom())
                        && ep.getLiquidityTwo().getTicker().equals(request.getTo()))
                        || (ep.getLiquidityTwo().getTicker().equals(request.getFrom())
                        && ep.getLiquidityOne().getTicker().equals(request.getTo()))).findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Liquidity pool not found for this token pair: " + request.getFrom() + "/" +
                                request.getTo()));
    }


}
