package dan_javademoplayground.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeResult {

    private boolean successful;
    private String message;
    private Liquidity swapped;
    private Liquidity result;
    private Double price;

}
