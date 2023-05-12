package dan_javademoplayground.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Agency {

    private Long id;

    private String name;

    private String cui;

    private List<ExchangePool> exchangePools;
}
