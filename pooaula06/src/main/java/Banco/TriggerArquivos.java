package Banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.h2.api.Trigger;

public class TriggerArquivos implements Trigger{
    
    @Override
    public void init(Connection conn, String schemaName, 
                     String triggerName, String tableName, boolean before, int type)
    throws SQLException {}

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(":NEW.arq_id := SEQ_ARQ.nextval;")
        ) {
            stmt.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {}

    @Override
    public void remove() throws SQLException {}

}
