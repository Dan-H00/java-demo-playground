package dan_javademoplayground.integration;

import dan_javademoplayground.persistence.model.Agency;
import dan_javademoplayground.persistence.model.Liquidity;
import dan_javademoplayground.persistence.model.Person;
import dan_javademoplayground.persistence.model.Wallet;
import dan_javademoplayground.persistence.repository.AgencyRepository;
import dan_javademoplayground.persistence.repository.PersonRepository;
import dan_javademoplayground.persistence.repository.WalletRepository;
import dan_javademoplayground.service.RandomEntityGenerator;
import dan_javademoplayground.service.SwapSimulator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class SwapSimulatorTestIT {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private SwapSimulator swapSimulator;

    @Test
    void testSwap() throws InterruptedException {
        Random random = new Random();
        generatePersons(100);
        Agency agency1 = RandomEntityGenerator.generateRandomAgency(20, 1_000_000.0, random);
        agencyRepository.save(agency1);

        Agency agency2 = RandomEntityGenerator.generateRandomAgency(20, 1_000_000.0, random);
        agencyRepository.save(agency2);

        swapSimulator.startSwap(1000);
        Thread.sleep(120_000);
        swapSimulator.stopSwap();
        Thread.sleep(5_000);
    }

    private void generatePersons(int number) {
        Random random = new Random();
        IntStream.range(0, number).forEach(i -> createPerson(RandomEntityGenerator.generateRandomString(20, random)));
    }

    private Person createPerson(String name) {
        Wallet wallet = Wallet.builder()
                .name(name + "-main-wallet")
                .liquidityList(List.of(
                        Liquidity.builder().value(100.0).name("Leu").ticker("RON").build(),
                        Liquidity.builder().value(5.0).name("Bitcoin").ticker("BTC").build(),
                        Liquidity.builder().value(500.0).name("Euro").ticker("EUR").build(),
                        Liquidity.builder().value(1000.0).name("Solana").ticker("SOL").build()))
                .build();
        List<Wallet> wallets = List.of(wallet);
        Person person = Person.builder()
                .name(name)
                .wallets(wallets)
                .address("Cluj")
                .photo("profilePic".getBytes(StandardCharsets.UTF_8))
                .build();
        wallets.forEach(w -> w.setPerson(person));
        return personRepository.save(person);
    }
}