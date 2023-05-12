package dan_javademoplayground.persistence.repository;

import dan_javademoplayground.persistence.model.Wallet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w LEFT JOIN FETCH w.liquidityList where w.person.id = :id")
    List<Wallet> findWalletsByPersonId(Long id);

}
