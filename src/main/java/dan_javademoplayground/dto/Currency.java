package dan_javademoplayground.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Currency {

    private String ticker;
}
