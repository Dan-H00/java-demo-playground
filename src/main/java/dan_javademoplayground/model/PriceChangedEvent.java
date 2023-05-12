package dan_javademoplayground.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PriceChangedEvent {

    Long agencyId;
    String tickerFrom;
    String tickerTo;
    double price;
}
