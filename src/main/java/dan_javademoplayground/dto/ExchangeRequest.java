package dan_javademoplayground.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeRequest {

    private long personId;
    private long walletId;
    private long agencyId;
    private String from;
    private String to;
    private Double value;

}
