package dan_javademoplayground.persistence.repository;

import dan_javademoplayground.persistence.model.SwapPrice;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PriceArchiveRepository extends CrudRepository<SwapPrice, Long> {

    List<SwapPrice> findByAgencyIdAndTickerFromAndTickerTo(long agencyId, String tickerFrom, String tickerTo);

}
