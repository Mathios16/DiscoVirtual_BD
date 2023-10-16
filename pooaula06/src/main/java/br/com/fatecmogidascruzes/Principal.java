package br.com.fatecmogidascruzes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Principal {
	
    public static void main(String[] args){

        Scanner entrada = new Scanner(System.in);

        int opcao, tamanho;
        String nome,criador, tipo;
		boolean continua=true;
		Connection conexao;
		Disco areaDeTrabalho;
		try{
			conexao = DriverManager.getConnection("jdbc:h2:./bancoh2");	
			areaDeTrabalho = new Disco(conexao);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			entrada.close();
			return;
		}
        
        do {
        	
	        System.out.println("\nDisco:\n1 - Criar Pasta\n"
										 + "2 - Criar Arquivo\n"
										 + "3 - Procurar Pasta\n"
										 + "4 - Procurar Arquivo\n"
										 + "5 - Excluir Pasta\n"
										 + "6 - Excluir Arquivo\n"
										 + "0 - Sair");
		    opcao = entrada.nextInt();
		    
		    switch(opcao) {
	        case 1:
	        	System.out.println("Insira o nome da pasta: ");
	        	nome = entrada.next();
	        	System.out.println("Insira a pasta pai (se não tiver - raiz): ");
	        	criador = entrada.next();
	        	
				try{
					areaDeTrabalho.addPasta(nome, criador,conexao);
					System.out.println("Pasta "+nome+" adicionada com sucesso!");
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}

	        	break;
	        case 2:
	        	System.out.println("Insira o nome do arquivo: ");
	        	nome = entrada.next();
	        	System.out.println("Insira o tipo do arquivo: ");
	        	tipo = entrada.next();
	        	System.out.println("Insira o tamanho do arquivo: ");
	        	tamanho = entrada.nextInt();
	        	System.out.println("Insira a pasta pai (se não tiver - raiz): ");
	        	criador = entrada.next();

				System.out.println();

				try{
					areaDeTrabalho.addArquivo(nome.toLowerCase(), tipo, tamanho, criador,conexao);
					System.out.println("Arquivo "+nome+" adicionado com sucesso!");
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
	        	
	        	break;
				/*
	        case 3:
        		System.out.println("Deseja procurar qual Pasta (area de trabalho - raiz): ");
        		nome = entrada.next();

				try{
        			Pasta p = (Pasta)areaDeTrabalho.procurar(nome.toLowerCase(), Tipo.PASTAS,conexao);

					System.out.println(areaDeTrabalho.caminho(nome,conexao));
					for(Pasta subP:p.getSubPastas()){
						System.out.println("P - "+subP.getNome()+"\t\t- "+subP.tamanho());
					}
					for(Arquivo arq:p.getArquivos()){
						System.out.println("A - "+arq.getNome()+"."+arq.getTipo()+"\t\t- "+arq.getTamanho());
					}
				}catch(IllegalArgumentException ex){
					System.out.println(ex.getMessage());
				}

	        	break;
			case 4:
        		System.out.println("Deseja procurar qual Arquivo: ");
        		nome = entrada.next();
        		
				try{
        			Arquivo arq = (Arquivo)areaDeTrabalho.procurar(areaDeTrabalho.getRaiz(), nome.toLowerCase(), Tipo.AQRUIVO);

					System.out.println(areaDeTrabalho.caminho(nome, Tipo.PASTA)+"\n");
					System.out.println("A - "+arq.getNome()+"."+arq.getTipo()+" -\t\t"+arq.getTamanho());
				}catch(IllegalArgumentException ex){
						System.out.println(ex.getMessage());
				}

	        	break;
				*/
	        case 5:
	        	System.out.println("Insira o nome da pasta: ");
	        	nome = entrada.next();
	        	System.out.println("Insira a pasta pai (se não tiver - raiz): ");
	        	criador = entrada.next();
	        	
				try{
					areaDeTrabalho.delete(nome.toLowerCase(), Tipo.PASTAS, criador, conexao);
					System.out.println("Pasta "+nome+" excluida com sucesso!");
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}

	        	break;
			case 6:
	        	System.out.println("Insira o nome do arquivo: ");
	        	nome = entrada.next();
	        	System.out.println("Insira a pasta pai (se não tiver - raiz): ");
	        	criador = entrada.next();
	        	
				try{
					areaDeTrabalho.delete(nome.toLowerCase(), Tipo.AQRUIVOS, criador,conexao);
					System.out.println("Arquivo "+nome+" excluido com sucesso!");
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
	        	
	        	break;
	        case 0:
	        	continua=false;
	        	break;
	        default:
	        	System.out.println("Opção inválida");
	        }
		    
        }while(continua);
        
        entrada.close();
    }
}
