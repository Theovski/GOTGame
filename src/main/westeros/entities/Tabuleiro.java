package main.westeros.entities;

import java.util.Random;

public class Tabuleiro  {
    // private Character personagem;


    private final int linhas = 10;
    private final int colunas = 10;
    
    private Character[][] tabuleiro;
    private Random aleatorio = new Random();
    
    public Tabuleiro(){
       tabuleiro = new Character[linhas][colunas];
    }


    public void posicionaPerson(Character[] persona, String lado){
        int colunaMin, colunaMax;

        if(lado.equalsIgnoreCase("esquerdo")){
            colunaMin = 0;
            colunaMax = 4;
        } else {
            colunaMin = 5;
            colunaMax = 9;
        }

        for (Character p : persona){
            
            int lin, col;

            do {
                lin = aleatorio.nextInt(linhas);
                col = aleatorio.nextInt(colunaMax - colunaMin + 1) + colunaMin;
            } while (tabuleiro[lin][col] != null);

            tabuleiro[lin][col] = p;
            System.out.printf("%s (%s) posicionado em [%d, %d]%n",
                    p.getNome(), p.getCasa(), lin, col);
        }

    }

    public void exibirTabuleiro(){
        for(int i = 0; i < linhas; i++){
            for(int j = 0; j < colunas; j++){
                if(tabuleiro[i][j] == null){
                    System.out.print(" [ ]");
                } else { 
                    // exibe posicao e nome
                    System.out.printf("[%s]", tabuleiro[i][j].getCasa().name().substring(0, 3));
                }
            }
            System.out.println();
        }

    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }


    public Character[][] getTabuleiro() {
        return tabuleiro;
    }


}
