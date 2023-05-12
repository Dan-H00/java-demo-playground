package dan_javademoplayground.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dan_javademoplayground.mapper.DtoToEntityMapper;
import com.mcserby.playground.javademoplayground.persistence.model.*;
import dan_javademoplayground.persistence.repository.AgencyRepository;
import dan_javademoplayground.persistence.repository.ExchangePoolRepository;
import dan_javademoplayground.persistence.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class PersistenceLayerIT {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private ExchangePoolRepository exchangePoolRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPersonIsPersistedCorrectlyPersistenceLayer() {

        Wallet wallet = Wallet.builder()
                .name("main-wallet")
                .liquidityList(List.of(
                        Liquidity.builder().value(100.0).name("Leu").ticker("RON").build(),
                        Liquidity.builder().value(5.0).name("Bitcoin").ticker("BTC").build(),
                        Liquidity.builder().value(500.0).name("EUro").ticker("EUR").build()))
                .build();
        List<Wallet> wallets = List.of(wallet);
        Person person = Person.builder()
                .name("Serby")
                .wallets(wallets)
                .address("Cluj")
                .photo("profilePic".getBytes(StandardCharsets.UTF_8))
                .build();
        Person saved = personRepository.save(person);
        long personId = saved.getId();
        Optional<Person> maybeQueriedPerson = personRepository.findById(personId);
        assertTrue(maybeQueriedPerson.isPresent());
        Person queriedPerson = maybeQueriedPerson.get();
        assertEquals(person.getName(), queriedPerson.getName());

        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "person"));
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "wallet"));

        personRepository.delete(saved);

        assertEquals(0, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "person"));
        assertEquals(0, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "wallet"));
    }

    @Test
    void testAgencyIsPersistedCorrectlyPersistenceLayer() throws JsonProcessingException {
        Agency agency = createTestAgency();
        Agency saved = agencyRepository.save(agency);
        long agencyId = saved.getId();
        Optional<Agency> maybeQueriedAgency = agencyRepository.findById(agencyId);
        assertTrue(maybeQueriedAgency.isPresent());
        Agency queriedAgency = maybeQueriedAgency.get();
        assertEquals(agency.getName(), queriedAgency.getName());

        assertEquals(2, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "exchange_pool"));
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "agency"));
        assertEquals(1, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "wallet"));

        System.out.println(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString((DtoToEntityMapper.map(saved))));

        for(ExchangePool ep : exchangePoolRepository.findAll()){
            exchangePoolRepository.delete(ep);
        }
        agencyRepository.delete(saved);

        assertEquals(0, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "exchange_pool"));
        assertEquals(0, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "agency"));
        assertEquals(0, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "wallet"));
    }


    @Test
    void testExchangePoolPersistence() throws JsonProcessingException {
        Agency agency = createTestAgency();
        agency.setExchangePools(Collections.emptyList());
        Agency saved = agencyRepository.save(agency);

        Liquidity ron = Liquidity.builder().value(100_000_000.0).name("Leu").ticker("RON").build();
        Liquidity usd = Liquidity.builder().value(100_000_000.0).name("Leu").ticker("RON").build();

        ExchangePool exchangePool1 = ExchangePool.builder()
                .agency(saved)
                .liquidityOne(ron)
                .liquidityTwo(usd)
                .build();
        ExchangePool ex1 = exchangePoolRepository.save(exchangePool1);

        Liquidity btc = Liquidity.builder().value(100_000.0).name("Bitcoin").ticker("BTC").build();
        Liquidity eGold = Liquidity.builder().value(1_000_000.0).name("Egold").ticker("EGLD").build();

        ExchangePool exchangePool2 = ExchangePool.builder()
                .agency(saved)
                .liquidityOne(btc)
                .liquidityTwo(eGold)
                .build();
        ExchangePool ex2 = exchangePoolRepository.save(exchangePool2);

        List<ExchangePool> allExchangePools = StreamSupport.stream(exchangePoolRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(2, allExchangePools.size());

        exchangePoolRepository.delete(ex1);
        exchangePoolRepository.delete(ex2);
        agencyRepository.delete(saved);

        allExchangePools = StreamSupport.stream(exchangePoolRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(0, allExchangePools.size());

        assertEquals(0, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "exchange_pool"));
        assertEquals(0, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "agency"));
        assertEquals(0, JdbcTestUtils.countRowsInTable(this.jdbcTemplate, "wallet"));

    }

    private Agency createTestAgency(){
        List<ExchangePool> exchangePools = new ArrayList<>();
        ExchangePool eurUsd = ExchangePool.builder()
                .liquidityOne(Liquidity.builder().name("Euro").ticker("EUR").value(100_000.0).build())
                .liquidityTwo(Liquidity.builder().name("Dollar").ticker("USD").value(100_000.0).build())
                .build();
        exchangePools.add(eurUsd);
        ExchangePool usdBtc = ExchangePool.builder()
                .liquidityOne(Liquidity.builder().name("Dollar").ticker("USD").value(100_000.0).value(100_000.0).build())
                .liquidityTwo(Liquidity.builder().name("Bitcoin").ticker("BTC").value(1_000.0).build())
                .build();
        exchangePools.add(usdBtc);
        Agency agency = Agency.builder()
                .name("SerbyBank")
                .cui("Cluj2198082820")
                .exchangePools(exchangePools)
                .build();
        agency.setExchangePoolReferences();
        return agency;
    }

}
