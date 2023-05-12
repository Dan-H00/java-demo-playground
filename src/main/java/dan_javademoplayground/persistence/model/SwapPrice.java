package dan_javademoplayground.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "price_history")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SwapPrice {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long agencyId;

    @Column(nullable = false, name = "timestamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

    @Column(nullable = false)
    String tickerFrom;

    @Column(nullable = false)
    String tickerTo;

    @Column(nullable = false)
    double price;
}
