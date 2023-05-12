package dan_javademoplayground.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Liquidity {

    private Double value;

    private String name;

    private String ticker;

}
