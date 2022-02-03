package net.hyzenmc.cash.dao;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.Getter;
import net.hyzenmc.cash.CashPlugin;
import net.hyzenmc.cash.dao.adapter.SQLCashUserAdapter;
import net.hyzenmc.cash.user.CashUser;

public final class CashUserDAO {

    private static final String TABLE = "cash_users";

    @Getter
    private static CashUserDAO instance;

    public CashUserDAO() {
        instance = this;
        createTable();
    }

    public void createTable() {
        this.executor().updateQuery(
                "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                        "player_name VARCHAR(16) NOT NULL PRIMARY KEY," +
                        "player_balance DOUBLE NOT NULL" +
                        ");"
        );
    }

    public CashUser selectOne(String playerName) {
        return this.executor().resultOneQuery(
                "SELECT * FROM " + TABLE + " WHERE player_name = ?",
                statement -> {
                    statement.set(1, playerName);
                },
                SQLCashUserAdapter.class
        );
    }

    public void insertOne(CashUser user) {
        this.executor().updateQuery(
                "REPLACE INTO " + TABLE + " VALUES(?, ?)",
                statement -> {
                    statement.set(1, user.getPlayerName());
                    statement.set(2, user.getBalance());
                }
        );
    }

    private SQLExecutor executor() {
        return new SQLExecutor(CashPlugin.getInstance().getSqlConnector());
    }

}
