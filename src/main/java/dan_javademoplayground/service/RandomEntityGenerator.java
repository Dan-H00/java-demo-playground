package dan_javademoplayground.service;

import com.mcserby.playground.javademoplayground.persistence.model.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomEntityGenerator {

    private final static Map<String, String> CURRENCIES;
    static {
        CURRENCIES = Map.of(
                "EUR", "Euro",
                "USD", "Dollar",
                "BTC", "Bitcoin",
                "ETH", "Ethereum",
                "DOGE", "Dogecoin",
                "EGLD", "eGold"
        );
    }

    public static Person generateRandomPerson(int maxWalletsPerPerson, double maxLiquidityValue, Random random) {
        List<Wallet> wallets = IntStream.range(0, random.nextInt(maxWalletsPerPerson) + 1)
                .mapToObj(i -> generateRandomWallet(maxLiquidityValue, random))
                .collect(Collectors.toList());
        return Person.builder()
                .name(generateRandomString(20, random))
                .wallets(wallets)
                .address(generateRandomString(50, random))
                .photo(generateRandomString(200, random).getBytes(StandardCharsets.UTF_8))
                .build();
    }

    public static Agency generateRandomAgency(int maxExchangePools, double maxLiquidityValue, Random random) {
        List<ExchangePool> exchangePools = IntStream.range(0, random.nextInt(maxExchangePools)).mapToObj(
                i -> generateRandomExchangePool(maxLiquidityValue, random)).collect(Collectors.toList());
        Agency agency = Agency.builder()
                .name(generateRandomString(20, random))
                .cui(generateRandomString(50, random))
                .exchangePools(exchangePools)
                .build();
        agency.setExchangePoolReferences();
        return agency;
    }

    private static ExchangePool generateRandomExchangePool(double maxLiquidityValue, Random random) {
        List<String> keys = new ArrayList<>(CURRENCIES.keySet());
        Collections.shuffle(keys);
        Liquidity firstCurrency = Liquidity.builder()
                .value(random.nextDouble() * maxLiquidityValue)
                .ticker(keys.get(0))
                .name(CURRENCIES.get(keys.get(0)))
                .build();
        Liquidity secondCurrency = Liquidity.builder()
                .value(random.nextDouble() * maxLiquidityValue)
                .ticker(keys.get(1))
                .name(CURRENCIES.get(keys.get(1)))
                .build();

        return ExchangePool.builder()
                .liquidityOne(firstCurrency)
                .liquidityTwo(secondCurrency)
                .build();
    }


    private static Wallet generateRandomWallet(double maxLiquidityValue, Random random) {
        List<String> keys = new ArrayList<>(CURRENCIES.keySet());
        Collections.shuffle(keys);
        List<Liquidity> liquidityList = keys.subList(0, random.nextInt(10))
                .stream()
                .map(k -> Liquidity.builder()
                        .value(random.nextDouble() * maxLiquidityValue)
                        .name(k)
                        .ticker(CURRENCIES.get(k))
                        .build())
                .collect(Collectors.toList());

        return Wallet.builder()
                .name(generateRandomString(15, random))
                .liquidityList(liquidityList)
                .build();
    }

    public static String generateRandomString(int maxLength, Random random) {
        int leftLimit = 48;
        int rightLimit = 122;
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(maxLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
