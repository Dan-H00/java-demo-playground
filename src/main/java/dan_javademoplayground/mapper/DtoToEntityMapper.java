package dan_javademoplayground.mapper;

import com.mcserby.playground.javademoplayground.persistence.model.*;

import java.util.Base64;
import java.util.stream.Collectors;

public class DtoToEntityMapper {

    public static com.mcserby.playground.javademoplayground.dto.Agency map(Agency agency) {
        return com.mcserby.playground.javademoplayground.dto.Agency.builder()
                .id(agency.getId())
                .name(agency.getName())
                .cui(agency.getCui())
                .exchangePools(
                        agency.getExchangePools().stream().map(DtoToEntityMapper::map).collect(Collectors.toList()))
                .build();
    }

    public static Agency map(com.mcserby.playground.javademoplayground.dto.Agency agency) {
        Agency result = Agency.builder()
                .id(agency.getId())
                .name(agency.getName())
                .cui(agency.getCui())
                .exchangePools(
                        agency.getExchangePools().stream().map(DtoToEntityMapper::map).collect(Collectors.toList()))
                .build();
        result.setExchangePoolReferences();
        return result;
    }

    public static com.mcserby.playground.javademoplayground.dto.Person map(Person person) {
        return com.mcserby.playground.javademoplayground.dto.Person.builder()
                .id(person.getId())
                .name(person.getName())
                .address(person.getAddress())
                .photo(Base64.getEncoder().encodeToString(person.getPhoto()))
                .wallets(person.getWallets().stream().map(DtoToEntityMapper::map).collect(Collectors.toList()))
                .build();
    }

    public static Person map(com.mcserby.playground.javademoplayground.dto.Person person) {
        Person result = Person.builder()
                .id(person.getId())
                .name(person.getName())
                .address(person.getAddress())
                .photo(Base64.getDecoder().decode(person.getPhoto()))
                .wallets(person.getWallets().stream().map(DtoToEntityMapper::map).collect(Collectors.toList()))
                .build();
        result.setPersonReferences();
        return result;
    }

    public static com.mcserby.playground.javademoplayground.dto.Wallet map(Wallet wallet) {
        return com.mcserby.playground.javademoplayground.dto.Wallet.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .liquidityList(wallet.getLiquidityList()
                        .stream()
                        .map(DtoToEntityMapper::map)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Wallet map(com.mcserby.playground.javademoplayground.dto.Wallet wallet) {
        return Wallet.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .liquidityList(wallet.getLiquidityList()
                        .stream()
                        .map(DtoToEntityMapper::map)
                        .collect(Collectors.toList()))
                .build();
    }

    public static com.mcserby.playground.javademoplayground.dto.ExchangePool map(ExchangePool exchangePool) {
        return com.mcserby.playground.javademoplayground.dto.ExchangePool.builder()
                .id(exchangePool.getId())
                .agencyId(exchangePool.getAgency().getId())
                .liquidityOne(DtoToEntityMapper.map(exchangePool.getLiquidityOne()))
                .liquidityTwo(DtoToEntityMapper.map(exchangePool.getLiquidityTwo()))
                .build();
    }

    public static ExchangePool map(com.mcserby.playground.javademoplayground.dto.ExchangePool exchangePool) {
        return ExchangePool.builder()
                .id(exchangePool.getId())
                .agency(null)
                .liquidityOne(DtoToEntityMapper.map(exchangePool.getLiquidityOne()))
                .liquidityTwo(DtoToEntityMapper.map(exchangePool.getLiquidityTwo()))
                .build();
    }

    private static Liquidity map(com.mcserby.playground.javademoplayground.dto.Liquidity liquidity) {
        return Liquidity.builder()
                .name(liquidity.getName())
                .ticker(liquidity.getTicker())
                .value(liquidity.getValue())
                .build();
    }

    public static com.mcserby.playground.javademoplayground.dto.Liquidity map(Liquidity liquidity) {
        return com.mcserby.playground.javademoplayground.dto.Liquidity.builder()
                .name(liquidity.getName())
                .ticker(liquidity.getTicker())
                .value(liquidity.getValue())
                .build();
    }

    public static com.mcserby.playground.javademoplayground.dto.SwapPrice map(SwapPrice price) {
        return com.mcserby.playground.javademoplayground.dto.SwapPrice.builder()
                .agencyId(price.getAgencyId())
                .price(price.getPrice())
                .localDateTime(price.getLocalDateTime())
                .tickerFrom(price.getTickerFrom())
                .tickerTo(price.getTickerTo())
                .build();
    }
}
