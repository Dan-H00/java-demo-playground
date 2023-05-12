package dan_javademoplayground.controller;

import dan_javademoplayground.dto.ExchangeRequest;
import dan_javademoplayground.dto.ExchangeResult;
import dan_javademoplayground.dto.SwapPrice;
import dan_javademoplayground.monitoring.TrackExecutionTime;
import dan_javademoplayground.service.Dex;
import dan_javademoplayground.service.PriceArchive;
import dan_javademoplayground.service.PriceOracle;
import dan_javademoplayground.service.SwapSimulator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DexController {

    private final Dex dex;
    private final SwapSimulator swapSimulator;
    private final PriceOracle priceOracle;
    private final PriceArchive priceArchive;

    DexController(Dex dex, SwapSimulator swapSimulator,
                  PriceOracle priceOracle, PriceArchive priceArchive) {
        this.dex = dex;
        this.swapSimulator = swapSimulator;
        this.priceOracle = priceOracle;
        this.priceArchive = priceArchive;
    }

    @PostMapping("/swap")
    @TrackExecutionTime
    ExchangeResult swap(@RequestBody ExchangeRequest request) {
        return dex.swap(request);
    }

    @PutMapping("/startSwapSimulator")
    @TrackExecutionTime
    String startSwapSimulator(@RequestParam int frequency) {
        swapSimulator.startSwap(frequency);
        return "swap simulator started";
    }

    @PutMapping("/stopSwapSimulator")
    @TrackExecutionTime
    String stopSwapSimulator() {
        swapSimulator.stopSwap();
        return "swap simulator stopped";
    }

    @GetMapping("/price")
    @TrackExecutionTime
    Double stopSwapSimulator(@RequestParam String ticker, @RequestParam String ref) {
        return priceOracle.getEstimatedPrice(ticker, ref);
    }

    @GetMapping("/history")
    @TrackExecutionTime
    List<SwapPrice> getArchiveFor(@RequestParam long agencyId, @RequestParam String from, @RequestParam String to){
        return priceArchive.getArchiveFor(agencyId, from, to);
    }

}
