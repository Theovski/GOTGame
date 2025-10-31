package com.got.westeros.entities;

import java.util.Random;
import java.util.List;

public class Tabuleiro  {
    private final int linhas = 10;
    private final int colunas = 10;
    
    private GameCharacter[][] tabuleiro;
    private Random aleatorio = new Random();
    private GameCharacter personagemAtual;
    private List<GameCharacter> equipeAtual;
    
    public Tabuleiro(){
       tabuleiro = new GameCharacter[linhas][colunas];
    }
    
    public void setPersonagemAtual(GameCharacter personagem) {
        this.personagemAtual = personagem;
    }
    
    public void setEquipeAtual(List<GameCharacter> equipe) {
        this.equipeAtual = equipe;
    }


    public void posicionaPerson(GameCharacter[] persona, String lado){
        int colunaMin, colunaMax;

        if(lado.equalsIgnoreCase("esquerdo")){
            colunaMin = 0;
            colunaMax = 4;
        } else {
            colunaMin = 5;
            colunaMax = 9;
        }

        for (GameCharacter p : persona){
            
            int lin, col;

            do {
                lin = aleatorio.nextInt(linhas);
                col = aleatorio.nextInt(colunaMax - colunaMin + 1) + colunaMin;
            } while (tabuleiro[lin][col] != null);

            tabuleiro[lin][col] = p;

            p.setLinha(lin);  
            p.setColuna(col);

            String charName = p.getNome();
            System.out.println("  " + charName + " posicionado em [" + lin + "," + col + "]");
        }

    }

    public boolean verificaPosicao(int l, int c){
        return l >= 0 && l < this.linhas && c >= 0 && c < this.colunas;
    }

    public GameCharacter getPersonagem(int linha, int coluna){
        if(verificaPosicao(linha, coluna)){
            return tabuleiro[linha][coluna];
        }
        return null;
    }

    public void exibirTabuleiro(){
        System.out.println();
        
        System.out.print("     ");
        for (int j = 0; j < colunas; j++) {
            System.out.print("  " + j + " ");
        }
        System.out.println();
        
        System.out.print("    ╔");
        for (int j = 0; j < colunas; j++) {
            System.out.print("═══");
            if (j < colunas - 1) {
                System.out.print("╤");
            }
        }
        System.out.println("╗");
        
        for(int i = 0; i < linhas; i++){
            if (i < 10) {
                System.out.print("  " + i + " ");
            }
            System.out.print("║");
            
            for(int j = 0; j < colunas; j++){
                if(tabuleiro[i][j] == null){
                    if ((i + j) % 2 == 0) {
                        System.out.print(" · ");
                    } else {
                        System.out.print("   ");
                    }
                } else {
                    GameCharacter p = tabuleiro[i][j];
                    String simbolo = obterSimbolo(p);
                    
                    if (p == personagemAtual) {
                        System.out.print(" " + simbolo + " ");
                    } 
                    else if (equipeAtual != null && equipeAtual.contains(p)) {
                        System.out.print(" " + simbolo + " ");
                    } 
                    else {
                        System.out.print(" " + simbolo + " ");
                    }
                }
                
                if (j < colunas - 1) {
                    System.out.print("│");
                }
            }
            System.out.println("║");
            
            if (i < linhas - 1) {
                System.out.print("    ╟");
                for (int j = 0; j < colunas; j++) {
                    System.out.print("───");
                    if (j < colunas - 1) {
                        System.out.print("┼");
                    }
                }
                System.out.println("╢");
            }
        }
        
        System.out.print("    ╚");
        for (int j = 0; j < colunas; j++) {
            System.out.print("═══");
            if (j < colunas - 1) {
                System.out.print("╧");
            }
        }
        System.out.println("╝");
        
        exibirLegenda();
        System.out.println();
    }
    
    private String obterSimbolo(GameCharacter personagem) {
        String inicial = personagem.getCasa().name().substring(0, 1);

        return inicial;
    }
    
    private void exibirLegenda() {
        System.out.println("\n    LEGENDA");
        
        System.out.print("    ");
        System.out.print("S - Stark  ");
        System.out.print("L - Lannister  ");
        System.out.print("T - Targaryen");
        System.out.println();
    }

    public boolean moverPersonagem(GameCharacter personagem, int novaLinha, int novaColuna) {
        int lAtual = personagem.getLinha();
        int cAtual = personagem.getColuna();

        if (!verificaPosicao(novaLinha, novaColuna)) {
            return false;
        }

        int diffL = Math.abs(novaLinha - lAtual);
        int diffC = Math.abs(novaColuna - cAtual);

        if (diffL > 1 || diffC > 1 || (diffL == 0 && diffC == 0)) {
            System.out.println("Movimento invalido: So e permitido mover 1 casa (ortogonal ou diagonal)");
            return false;
        }

        if (getPersonagem(novaLinha, novaColuna) != null) {
            System.out.println("Movimento invalido: A casa de destino ja esta ocupada.");
            return false;
        }

        tabuleiro[lAtual][cAtual] = null;
        tabuleiro[novaLinha][novaColuna] = personagem;
        personagem.setLinha(novaLinha);
        personagem.setColuna(novaColuna);
        
        return true;
    }

    public void removerPersonagem(GameCharacter personagem) {
        if (personagem != null && verificaPosicao(personagem.getLinha(), personagem.getColuna())) {
            tabuleiro[personagem.getLinha()][personagem.getColuna()] = null;
        }
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }


    public GameCharacter[][] getTabuleiro() {
        return tabuleiro;
    }


}
