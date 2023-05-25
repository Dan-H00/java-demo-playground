package dan_javademoplayground.persistence.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "agency")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Agency extends MarketActor {

    @Column
    private String cui;

    // added CascadeType.REMOVE for deleting agencies
    @OneToMany(mappedBy = "agency", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<ExchangePool> exchangePools;

    public void setExchangePoolReferences() {
        exchangePools.forEach(e -> e.setAgency(this));
    }

}
