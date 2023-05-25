package dan_javademoplayground.mapper;

import dan_javademoplayground.persistence.model.*;

import java.util.Base64;
import java.util.stream.Collectors;

public class DtoToEntityMapper {

    public static dan_javademoplayground.dto.Agency map(Agency agency) {
        return dan_javademoplayground.dto.Agency.builder()
                .id(agency.getId())
                .name(agency.getName())
                .cui(agency.getCui())
                .exchangePools(
                        agency.getExchangePools().stream().map(DtoToEntityMapper::map).collect(Collectors.toList()))
                .build();
    }

    public static Agency map(dan_javademoplayground.dto.Agency agency) {
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

    public static dan_javademoplayground.dto.Person map(Person person) {
        return dan_javademoplayground.dto.Person.builder()
                .id(person.getId())
                .name(person.getName())
                .address(person.getAddress())
                .photo(Base64.getEncoder().encodeToString(person.getPhoto()))
                .wallets(person.getWallets().stream().map(DtoToEntityMapper::map).collect(Collectors.toList()))
                .build();
    }

    public static Person map(dan_javademoplayground.dto.Person person) {
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

    public static dan_javademoplayground.dto.Wallet map(Wallet wallet) {
        return dan_javademoplayground.dto.Wallet.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .liquidityList(wallet.getLiquidityList()
                        .stream()
                        .map(DtoToEntityMapper::map)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Wallet map(dan_javademoplayground.dto.Wallet wallet) {
        return Wallet.builder()
                .id(wallet.getId())
                .name(wallet.getName())
                .liquidityList(wallet.getLiquidityList()
                        .stream()
                        .map(DtoToEntityMapper::map)
                        .collect(Collectors.toList()))
                .build();
    }

    public static dan_javademoplayground.dto.ExchangePool map(ExchangePool exchangePool) {
        return dan_javademoplayground.dto.ExchangePool.builder()
                .id(exchangePool.getId())
                .agencyId(exchangePool.getAgency().getId())
                .liquidityOne(DtoToEntityMapper.map(exchangePool.getLiquidityOne()))
                .liquidityTwo(DtoToEntityMapper.map(exchangePool.getLiquidityTwo()))
                .build();
    }

    public static ExchangePool map(dan_javademoplayground.dto.ExchangePool exchangePool) {
        return ExchangePool.builder()
                .id(exchangePool.getId())
                .agency(null)
                .liquidityOne(DtoToEntityMapper.map(exchangePool.getLiquidityOne()))
                .liquidityTwo(DtoToEntityMapper.map(exchangePool.getLiquidityTwo()))
                .build();
    }

    private static Liquidity map(dan_javademoplayground.dto.Liquidity liquidity) {
        return Liquidity.builder()
                .name(liquidity.getName())
                .ticker(liquidity.getTicker())
                .value(liquidity.getValue())
                .build();
    }

    public static dan_javademoplayground.dto.Liquidity map(Liquidity liquidity) {
        return dan_javademoplayground.dto.Liquidity.builder()
                .name(liquidity.getName())
                .ticker(liquidity.getTicker())
                .value(liquidity.getValue())
                .build();
    }

    public static dan_javademoplayground.dto.SwapPrice map(SwapPrice price) {
        return dan_javademoplayground.dto.SwapPrice.builder()
                .agencyId(price.getAgencyId())
                .price(price.getPrice())
                .localDateTime(price.getLocalDateTime())
                .tickerFrom(price.getTickerFrom())
                .tickerTo(price.getTickerTo())
                .build();
    }
}
