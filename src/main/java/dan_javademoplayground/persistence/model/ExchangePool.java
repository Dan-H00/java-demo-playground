package dan_javademoplayground.persistence.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "exchange_pool")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangePool {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;


    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "liquidity_one_name")),
            @AttributeOverride(name = "ticker", column = @Column(name = "liquidity_one_ticker")),
            @AttributeOverride(name = "value", column = @Column(name = "liquidity_one_value")),
    })
    @Embedded
    private Liquidity liquidityOne;

    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "liquidity_two_name")),
            @AttributeOverride(name = "ticker", column = @Column(name = "liquidity_two_ticker")),
            @AttributeOverride(name = "value", column = @Column(name = "liquidity_two_value")),
    })
    @Embedded
    private Liquidity liquidityTwo;

}
