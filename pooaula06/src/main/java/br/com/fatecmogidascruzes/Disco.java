package br.com.fatecmogidascruzes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

public class Disco {

	private Pasta raiz;
	
	public Disco() {
		raiz = new Pasta("raiz");
	}
	
	public Item procurar(String nome, Tipo tp, Connection conexao) throws SQLException {
		
		PreparedStatement sql;
		ResultSet resultado = null;

		switch(tp){
			case PASTAS:
				sql = conexao.prepareStatement("SELECT pst_nome," 
												+"SELECT( pst_nome FROM pastas p2 WHERE p1.pst_pst_id = p2.pst_id )"
												+"FROM pastas p1 "
												+"WHERE pst_nome = ?");
				sql.setString(1, nome);
				resultado = sql.executeQuery();
				resultado.next();
				if( resultado.wasNull() ){
					if(resultado.getString(1).equals(nome))
						return new Pasta(resultado.getString(0), null);
					else
						return new Pasta(resultado.getString(0), 
										(Pasta)procurar(resultado.getString(1),Tipo.PASTAS, conexao));
				}
				break;
			case AQRUIVOS:
				sql = conexao.prepareStatement("SELECT arq_nome, arq_tipo, arq_tamanho"
												+"SELECT( pst_nome FROM pastas p WHERE a.arq_pst_id = p.pst_id )"
												+"FROM arquivos a"
												+"WHERE arq_nome = ?");
				sql.setString(0, nome);
				resultado = sql.executeQuery();
				resultado.next();
				if(resultado.wasNull())
					return new Arquivo(resultado.getString(0),
									resultado.getString(1),
									resultado.getInt(2), 
									(Pasta)procurar(resultado.getString(3),Tipo.PASTAS, conexao));
				break;
		}	
		return null;
	}

	private int procurarId(String nome, Tipo tp, Connection conexao) throws SQLException{

		PreparedStatement sql;
		ResultSet resultado = null;

		switch(tp){
			case PASTAS:
				sql = conexao.prepareStatement("SELECT pst_id"
												+"FROM pastas"
												+"WHERE pst_nome = ?");
				sql.setString(0, nome);
				resultado = sql.executeQuery();
				while(resultado.next())
					if(resultado != null){
						return resultado.getInt(0);
					}
				break;
			case AQRUIVOS:
				sql = conexao.prepareStatement("SELECT arq_id"
												+"FROM arquivos"
												+"WHERE arq_nome = ?");
				sql.setString(0, nome);
				resultado = sql.executeQuery();
					if(resultado != null){
						return resultado.getInt(0);
					}
				break;
		}
		

		return -1;
	}

	public String caminho(String nome, Connection conexao) throws IllegalArgumentException, SQLException {

		Stack<String> pilha = new Stack<>();
		Pasta p = (Pasta)procurar(nome, Tipo.PASTAS, conexao);
		String caminho = "";
		do{
			pilha.push(p.getNome());
			pilha.push("/");
			p = (Pasta)procurar(p.getCriador().getNome(), Tipo.PASTAS, conexao);
		}while( p != p.getCriador() );

		do{
			caminho += pilha.pop();
		}while( !pilha.isEmpty() );

		return caminho;

	}
	
	public void addArquivo(String nome, String tipo, int tamanho, String nomeCriador, Connection conexao) throws IllegalArgumentException, SQLException {

		PreparedStatement sql;
		if( nome.trim().equals(null) || tipo.equals(null) || tamanho < 0 || nomeCriador.trim().equals(null))
			throw new IllegalArgumentException("Dados não podem ser nulos");

		if( procurar(nome, Tipo.AQRUIVOS, conexao).equals(null) ){
			sql = conexao.prepareStatement("INSERT INTO ARQUIVOS VALUES (?,?,?,?,?)");
			sql.setInt(0, 0);
			sql.setString(1, nome);
			sql.setString(2, tipo);
			sql.setInt(3, tamanho);
			sql.setInt(4, procurarId(nomeCriador, Tipo.PASTAS, conexao));
			sql.executeUpdate();
		}
	}

	public void updateArquivo(String nomeAntigo, String nome, String tipo, int tamanho, String nomeCriador, Connection conexao) throws IllegalArgumentException, SQLException {

		PreparedStatement sql;
		if( nomeAntigo.trim().equals(null) || nome.trim().equals(null) || tipo.equals(null) || tamanho < 0 || nomeCriador.trim().equals(null))
			throw new IllegalArgumentException("Dados não podem ser nulos");

		if( !procurar(nomeAntigo, Tipo.AQRUIVOS, conexao).equals(null) ){
			sql = conexao.prepareStatement("UPDATE ARQUIVOS SET"
															+"arq_nome = ?"
															+"arq_tipo = ?"
															+"arq_tamanho = ?"
															+"arq_pst_id = ?"
										  +"WHERE arq_id = ?");
			sql.setString(0, nome);
			sql.setString(1, tipo);
			sql.setInt(2, tamanho);
			sql.setInt(3, procurarId(nomeCriador, Tipo.PASTAS, conexao));
			sql.setInt(4, procurarId(nomeAntigo, Tipo.AQRUIVOS, conexao));
			sql.executeUpdate();
		}
	}
	
	public void addPasta(String nome, String nomeCriador,  Connection conexao) throws IllegalArgumentException, SQLException {

		PreparedStatement sql;
		if( nome.trim().equals(null) || nomeCriador.trim().equals(null))
			throw new IllegalArgumentException("Dados não podem ser nulos");

		if( procurar(nome, Tipo.PASTAS, conexao).equals(null) ){
			sql = conexao.prepareStatement("INSERT INTO ARQUIVOS VALUES (?,?,?)");
			sql.setInt(0, 0);
			sql.setString(1, nome);
			sql.setInt(2, procurarId(nomeCriador, Tipo.PASTAS, conexao));
			sql.executeUpdate();
		}

	}

	public void updatePasta(String nomeAntigo, String nome, String nomeCriador,  Connection conexao) throws IllegalArgumentException, SQLException {

		PreparedStatement sql;
		if( nome.trim().equals(null) || nomeCriador.trim().equals(null))
			throw new IllegalArgumentException("Dados não podem ser nulos");

		if( !procurar(nomeAntigo, Tipo.PASTAS, conexao).equals(null) ){
			sql = conexao.prepareStatement("UPDATE PASTAS SET"
															+"pst_nome = ?"
															+"pst_pst_id = ?"
										  +"WHERE pst_id = ?");
			sql.setString(0, nome);
			sql.setInt(1, procurarId(nomeCriador, Tipo.PASTAS, conexao));
			sql.setInt(2, procurarId(nomeAntigo, Tipo.PASTAS, conexao));
			sql.executeUpdate();
		}

	}
	
	public void delete(String nome, Tipo tipo, String nomeCriador, Connection conexao) throws IllegalArgumentException, SQLException {

		PreparedStatement sql;
		ResultSet resultado;
		if( nome.trim().equals(null) || tipo.equals(null) || procurar(nome, tipo, conexao).equals(null) )
			throw new IllegalArgumentException("Dados não podem ser nulos");
		
		sql = conexao.prepareStatement("SELECT arq_nome FROM ARQUIVOS WHERE arq_pst_id = ?");
		sql.setInt(0, procurarId(nomeCriador, tipo, conexao));
		resultado = sql.executeQuery();
		while(resultado.next()){
			del(resultado.getString(0), tipo, conexao);
		}

		sql = conexao.prepareStatement("SELECT pst_nome FROM PASTAS WHERE pst_pst_id = ?");
		sql.setInt(0, procurarId(nomeCriador, tipo, conexao));
		resultado = sql.executeQuery();
		while(resultado.next()){
			delete(resultado.getString(0), tipo, nome, conexao);
		}

		del(nome, tipo, conexao);
		
	}

	private void del(String nome, Tipo tipo, Connection conexao) throws SQLException {
		
		PreparedStatement sql;
		sql = conexao.prepareStatement("DELETE FROM ? WHERE ? = ?");
		sql.setString(0, tipo.toString());
		if(tipo == Tipo.AQRUIVOS)
			sql.setString(1,"arq_id");
		else
			sql.setString(1,"pst_id");
		sql.setInt(2, procurarId(nome, Tipo.PASTAS, conexao));
		sql.executeUpdate();

	}

	public Pasta getRaiz() {
		return raiz;
	}

	public void setRaiz(Pasta raiz) {
		this.raiz = raiz;
	}
	
}
