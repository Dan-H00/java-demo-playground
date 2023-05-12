package dan_javademoplayground.persistence.repository;

import dan_javademoplayground.persistence.model.ExchangePool;
import org.springframework.data.repository.CrudRepository;

public interface ExchangePoolRepository extends CrudRepository<ExchangePool, Long> {
    
}
