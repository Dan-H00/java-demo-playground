package dan_javademoplayground.controller;

import dan_javademoplayground.dto.Agency;
import dan_javademoplayground.mapper.DtoToEntityMapper;
import dan_javademoplayground.monitoring.TrackExecutionTime;
import dan_javademoplayground.persistence.repository.AgencyRepository;
import dan_javademoplayground.persistence.repository.ExchangePoolRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class AgencyController {

    private final AgencyRepository repository;
    private final ExchangePoolRepository exchangePoolRepository;

    AgencyController(AgencyRepository repository, ExchangePoolRepository exchangePoolRepository) {
        this.repository = repository;
        this.exchangePoolRepository = exchangePoolRepository;
    }

    @GetMapping("/agencies")
    @TrackExecutionTime
    List<Agency> all() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(DtoToEntityMapper::map)
                .collect(Collectors.toList());
    }

    @PostMapping("/agency")
    @TrackExecutionTime
    @CachePut("agency")
    Agency newAgency(@RequestBody Agency agency) {
        return DtoToEntityMapper.map(repository.save(DtoToEntityMapper.map(agency)));
    }

    @GetMapping("/agency/{id}")
    @TrackExecutionTime
    @Cacheable("agency")
    Agency getOne(@PathVariable Long id) {
        return repository.findById(id).map(DtoToEntityMapper::map)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    @PutMapping("/agency")
    @TrackExecutionTime
    @CachePut("agency")
    Agency replace(@RequestBody Agency agency) {
        return DtoToEntityMapper.map(repository.save(DtoToEntityMapper.map(agency)));

    }

    @DeleteMapping("/agencies/{id}")
    @TrackExecutionTime
    void delete(@PathVariable Long id) {
        repository.findById(id).ifPresent(
                a -> a.getExchangePools().forEach(ep -> exchangePoolRepository.deleteById(ep.getId())));
        repository.deleteById(id);
    }

    @DeleteMapping("/agencies")
    @TrackExecutionTime
    void deleteAll() {
        exchangePoolRepository.deleteAll();
        repository.deleteAll();
    }
}
