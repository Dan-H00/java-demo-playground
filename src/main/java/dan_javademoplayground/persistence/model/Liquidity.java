package dan_javademoplayground.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Liquidity {

    @Column
    private Double value;

    @Column
    private String name;

    @Column
    private String ticker;

}
