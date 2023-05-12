package dan_javademoplayground.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Wallet {

    private Long id;

    private String name;

    private List<Liquidity> liquidityList;

}
