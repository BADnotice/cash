package net.hyzenmc.cash.user;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public final class CashUser {

    private final String playerName;
    private double balance;

    public static CashUser newCashUser(String name) {
        return CashUser.builder()
                .playerName(name)
                .build();
    }

}
