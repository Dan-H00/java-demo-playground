package dan_javademoplayground.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SwapPrice {

    private Long agencyId;

    private LocalDateTime localDateTime;

    String tickerFrom;

    String tickerTo;

    double price;
}
