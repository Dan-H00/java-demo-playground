package dan_javademoplayground.persistence.repository;

import dan_javademoplayground.persistence.model.Agency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AgencyRepository extends CrudRepository<Agency, Long> {

    Page<Agency> findAll(Pageable pageable);

    @Query("SELECT a FROM Agency a LEFT JOIN FETCH a.exchangePools where a.id = :id")
    List<Agency> findByIdWithExchangePools(Long id);
    
}
