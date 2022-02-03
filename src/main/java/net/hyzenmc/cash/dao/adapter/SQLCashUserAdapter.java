package net.hyzenmc.cash.dao.adapter;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import net.hyzenmc.cash.user.CashUser;

public final class SQLCashUserAdapter implements SQLResultAdapter<CashUser> {

    @Override
    public CashUser adaptResult(SimpleResultSet resultSet) {
        return CashUser.builder()
                .playerName(resultSet.get("player_name"))
                .balance(resultSet.get("player_balance"))
                .build();
    }

}
