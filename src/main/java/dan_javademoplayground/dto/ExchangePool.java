package dan_javademoplayground.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangePool {

    private Long id;

    private Long agencyId;

    private Liquidity liquidityOne;

    private Liquidity liquidityTwo;
}
