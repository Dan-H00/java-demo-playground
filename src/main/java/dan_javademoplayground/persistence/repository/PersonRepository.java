package dan_javademoplayground.persistence.repository;

import dan_javademoplayground.persistence.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Page<Person> findAll(Pageable pageable);

}
