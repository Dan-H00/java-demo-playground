package dan_javademoplayground.integration;


import dan_javademoplayground.persistence.model.*;
import dan_javademoplayground.persistence.repository.AgencyRepository;
import dan_javademoplayground.persistence.repository.PersonRepository;
import dan_javademoplayground.service.RandomEntityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
public class DataGeneratorIT {

    private final Logger logger = LoggerFactory.getLogger(DataGeneratorIT.class);

    // modified to only 10 persons to not keep the test running for so long while testing
    private static final int NUMBER_OF_RANDOM_PERSONS = 10;
    private static final int NUMBER_OF_RANDOM_AGENCIES = 1;
    private static final int MAX_WALLETS_PER_AGENCY = 1;
    private static final int MAX_WALLETS_PER_PERSON = 3;
    private static final int MAX_EXCHANGE_POOLS = 10;
    private static final double MAX_LIQUIDITY_VALUE = 100_000_000_000.0;
    private static final double MAX_PERSON_LIQUIDITY_VALUE = 10_000.0;


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Test
    void generateRandomPersons() {
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_RANDOM_PERSONS; i++) {
            Person person =
                    RandomEntityGenerator.generateRandomPerson(MAX_WALLETS_PER_PERSON, MAX_PERSON_LIQUIDITY_VALUE, random);
            logger.info("person: " + person.getName());
            personRepository.save(person);
        }
    }

    @Test
    // added a custom Parameter Resolver class
    @ExtendWith(RandomParameterResolver.class)
    void generateRandomAgencies(Random random) {
        for (int i = 0; i < NUMBER_OF_RANDOM_AGENCIES; i++) {
            Agency agency = RandomEntityGenerator
                    .generateRandomAgency(MAX_EXCHANGE_POOLS, MAX_LIQUIDITY_VALUE, random);
            logger.info("agency: " + agency.getName());
            agencyRepository.save(agency);
        }
    }

}
