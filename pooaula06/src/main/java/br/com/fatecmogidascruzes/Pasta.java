package br.com.fatecmogidascruzes;

import java.util.HashMap;

public class Pasta extends Item {

    HashMap<Tipo,String> subItens;

    public Pasta(String nome, Pasta criador) {

        subItens = new HashMap<>();
        renomear(nome);
        setCriador(criador);

    }
    public Pasta(String nome){

        renomear(nome);
        setCriador(this);

    }
    
    public void addArquivo(String nome){

        subItens.put(Tipo.AQRUIVOS,nome);

    }

    public void addSubPasta(String nome){

        subItens.put(Tipo.PASTAS,nome);

    }

    public void excluir(){
        renomear(null);
        setCriador(null);
    }
}
