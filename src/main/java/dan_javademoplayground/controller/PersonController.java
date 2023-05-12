package dan_javademoplayground.controller;

import dan_javademoplayground.dto.Person;
import dan_javademoplayground.mapper.DtoToEntityMapper;
import dan_javademoplayground.monitoring.TrackExecutionTime;
import dan_javademoplayground.persistence.model.Wallet;
import dan_javademoplayground.persistence.repository.PersonRepository;
import dan_javademoplayground.persistence.repository.WalletRepository;
import dan_javademoplayground.service.PriceOracle;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class PersonController {

    private final PersonRepository repository;
    private final WalletRepository walletRepository;
    private final PriceOracle priceOracle;

    public PersonController(
            PersonRepository repository,
            WalletRepository walletRepository,
            PriceOracle priceOracle) {
        this.repository = repository;
        this.walletRepository = walletRepository;
        this.priceOracle = priceOracle;
    }

    @GetMapping("/persons")
    @TrackExecutionTime
    List<Person> all() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(DtoToEntityMapper::map)
                .collect(Collectors.toList());
    }

    @PostMapping("/person")
    @TrackExecutionTime
    @CachePut("person")
    Person newAgency(@RequestBody Person person) {
        return DtoToEntityMapper.map(repository.save(DtoToEntityMapper.map(person)));
    }

    @GetMapping("/person/{id}")
    @TrackExecutionTime
    @Cacheable("person")
    Person getOne(@PathVariable Long id) {
        return repository.findById(id).map(DtoToEntityMapper::map)
                .orElseThrow(() -> new RuntimeException("not found"));
    }

    @PutMapping("/person")
    @TrackExecutionTime
    @CachePut("person")
    Person replace(@RequestBody Person person) {
        return DtoToEntityMapper.map(repository.save(DtoToEntityMapper.map(person)));
    }


    @GetMapping("/person/wallet-value/{walletId}")
    @TrackExecutionTime
    Double getWalletTotalValueIn(@PathVariable Long walletId, @RequestParam String tokenOut){
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));
        return priceOracle.findValue(wallet, tokenOut);
    }

    @DeleteMapping("/person/{id}")
    @TrackExecutionTime
    void delete(@PathVariable Long id) {
        repository.findById(id).ifPresent(
                a -> a.getWallets().forEach(ep -> walletRepository.deleteById(ep.getId())));
        repository.deleteById(id);
    }

    @DeleteMapping("/persons")
    @TrackExecutionTime
    void deleteAll() {
        walletRepository.deleteAll();
        repository.deleteAll();
    }
}
