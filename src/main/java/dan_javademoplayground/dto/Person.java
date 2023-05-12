package dan_javademoplayground.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Person {

    private Long id;

    private String name;

    private String address;

    private String photo;

    private List<Wallet> wallets;
}
