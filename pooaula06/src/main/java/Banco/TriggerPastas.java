package Banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.h2.api.Trigger;

public class TriggerPastas implements Trigger {

    @Override
    public void init(Connection conn, String schemaName, 
                     String triggerName, String tableName, boolean before, int type)
    throws SQLException {}

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        PreparedStatement sql;
        sql = conn.prepareStatement("BEGIN " 
                                        +"? := SEQ_PST.nextval;"
                                    +" END");
        sql.setString(0, newRow[0].toString());
        sql.executeUpdate();
    }

    @Override
    public void close() throws SQLException {}

    @Override
    public void remove() throws SQLException {}

}