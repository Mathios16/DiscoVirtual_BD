package Banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateDB {

	public static void main(String[] args) throws SQLException {
		
		// Abre conex�o
		Connection conexao = DriverManager.getConnection("jdbc:h2:./bancoh2");	
	
		// Cria tabelas
		conexao.createStatement().execute("CREATE TABLE pastas ("
																+ "pst_id integer primary key not null,"
																+ "pst_nome varchar2(25),"
																+ "pst_pst_id integer not null);");
		conexao.createStatement().execute("ALTER TABLE pastas ADD CONSTRAINT fk_pst_pst FOREIGN KEY "
										+ "( pst_pst_id ) REFERENCES pastas ( pst_id );");
		
		conexao.createStatement().execute("CREATE TABLE arquivos ("
																+ "arq_id integer primary key not null,"
																+ "arq_nome varchar2(25),"
																+ "arq_tipo varchar2(10),"
																+ "arq_tamanho integer,"
																+ "arq_pst_id integer not null);");
		conexao.createStatement().execute("ALTER TABLE arquivos ADD CONSTRAINT fk_arq_pst FOREIGN KEY "
										+ "( arq_pst_id ) REFERENCES pastas ( pst_id );");
		
		
		conexao.createStatement().execute("CREATE SEQUENCE \"SEQ_PST\";");
		conexao.createStatement().execute("CREATE OR REPLACE TRIGGER \"TG_SEQ_PST\" BEFORE INSERT ON pastas FOR EACH ROW CALL \"Banco.TriggerPastas\"");
		
		conexao.createStatement().execute("CREATE SEQUENCE \"SEQ_ARQ\";");
		conexao.createStatement().execute("CREATE OR REPLACE TRIGGER \"TG_SEQ_ARQ\" BEFORE INSERT ON arquivos FOR EACH ROW CALL \"Banco.TriggerArquivos\"");

		// Fecha conex�o
		conexao.close();
	}
}
