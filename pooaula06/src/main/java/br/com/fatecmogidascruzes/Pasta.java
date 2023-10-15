package br.com.fatecmogidascruzes;

public class Pasta extends Item {

    public Pasta(String nome, Pasta criador) {

        renomear(nome);
        setCriador(criador);

    }
    public Pasta(String nome){

        renomear(nome);
        setCriador(this);

    }

    public void excluir(){
        renomear(null);
        setCriador(null);
    }
}
