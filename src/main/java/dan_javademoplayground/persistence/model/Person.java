package dan_javademoplayground.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "person")
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class Person extends MarketActor {

    @Column
    private String address;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    // added CascadeType.REMOVE for deleting persons
    @OneToMany(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<Wallet> wallets;

    public void setPersonReferences() {
        wallets.forEach(w -> w.setPerson(this));
    }

}
