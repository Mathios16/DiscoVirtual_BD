package br.com.fatecmogidascruzes;

public class Item {
	
	private String nome;
	private Pasta criador;
	
	public String getNome() {
		return nome;
	}
	
	public void renomear(String nome) {
		this.nome = nome;
	}
	
	public Pasta getCriador() {
		return criador;
	}
	
	public void setCriador(Pasta criador) {
		this.criador = criador;
	}
	
	public void excluir() {
		nome = null;
		criador = null;
	}

}
