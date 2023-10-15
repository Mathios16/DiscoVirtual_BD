package br.com.fatecmogidascruzes;

public class Arquivo extends Item {
    private String tipo;
    private double tamanho;

    public Arquivo(String nome, String tipo, double tamanho, Pasta criador) {

        renomear(nome);
        this.tipo = tipo;
        this.tamanho = tamanho;
        setCriador(criador);

    }

    public String getTipo() {
        return tipo;
    }

    public void mudarTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getTamanho() {
        return tamanho;
    }  
    
    public void excluir(){
        renomear(null);
        setCriador(null);
        tipo = null;
        tamanho = 0;
    }
}
