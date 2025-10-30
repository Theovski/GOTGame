package com.got.westeros.entities;

import com.got.westeros.utils.ColorUtil;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Tabuleiro 10x10 do jogo.
 * Gerencia posicionamento, movimento e visualização dos personagens.
 */
public class Tabuleiro  {
    private final int linhas = 10;
    private final int colunas = 10;
    
    private GameCharacter[][] tabuleiro;
    private Random aleatorio = new Random();
    private GameCharacter personagemAtual; // Para destacar no tabuleiro
    private List<GameCharacter> equipeAtual; // Para destacar toda a equipe
    
    public Tabuleiro(){
       tabuleiro = new GameCharacter[linhas][colunas];
    }
    
    /**
     * Define qual personagem está jogando no momento (para destacar no tabuleiro)
     */
    public void setPersonagemAtual(GameCharacter personagem) {
        this.personagemAtual = personagem;
    }
    
    /**
     * Define a equipe completa do jogador atual (para destacar aliados)
     */
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

            String nomeColorido = ColorUtil.houseColor(p.getNome(), p.getCasa().name());
            System.out.printf("  %s posicionado em [%d,%d]%n",
                    nomeColorido, lin, col);
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

    /**
     * Exibe o tabuleiro de forma colorida e formatada com bordas Unicode.
     * Cada casa tem cor baseada na casa do personagem.
     */
    public void exibirTabuleiro(){
        System.out.println(); // Espaço antes do tabuleiro
        
        // Linha superior com números das colunas
        System.out.print("     "); // Espaço para alinhamento
        for (int j = 0; j < colunas; j++) {
            System.out.printf("  %d ", j);
        }
        System.out.println();
        
        // Borda superior
        System.out.print("    ╔");
        for (int j = 0; j < colunas; j++) {
            System.out.print("═══");
            if (j < colunas - 1) {
                System.out.print("╤");
            }
        }
        System.out.println("╗");
        
        // Linhas do tabuleiro
        for(int i = 0; i < linhas; i++){
            // Número da linha
            System.out.printf("  %d ║", i);
            
            // Células
            for(int j = 0; j < colunas; j++){
                if(tabuleiro[i][j] == null){
                    // Célula vazia - padrão xadrez sutil
                    if ((i + j) % 2 == 0) {
                        System.out.print(" · ");
                    } else {
                        System.out.print("   ");
                    }
                } else {
                    // Célula com personagem - colorido por casa
                    GameCharacter p = tabuleiro[i][j];
                    String simbolo = obterSimbolo(p);
                    String cor = ColorUtil.getHouseColor(p.getCasa().name());
                    
                    // Destaca o personagem atual com fundo verde escuro
                    if (p == personagemAtual) {
                        System.out.print(ColorUtil.BG_GREEN + ColorUtil.BLACK + simbolo + ColorUtil.RESET);
                    } 
                    // Destaca aliados da equipe com fundo azul claro
                    else if (equipeAtual != null && equipeAtual.contains(p)) {
                        System.out.print(ColorUtil.BG_CYAN + ColorUtil.BLACK + simbolo + ColorUtil.RESET);
                    } 
                    // Inimigos sem destaque
                    else {
                        System.out.print(cor + simbolo + ColorUtil.RESET);
                    }
                }
                
                // Separador vertical
                if (j < colunas - 1) {
                    System.out.print("│");
                }
            }
            System.out.println("║");
            
            // Linha separadora entre células (exceto última)
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
        
        // Borda inferior
        System.out.print("    ╚");
        for (int j = 0; j < colunas; j++) {
            System.out.print("═══");
            if (j < colunas - 1) {
                System.out.print("╧");
            }
        }
        System.out.println("╝");
        
        // Legenda
        exibirLegenda();
        System.out.println(); // Espaço depois do tabuleiro
    }
    
    /**
     * Retorna símbolo visual para o personagem baseado em sua casa
     */
    private String obterSimbolo(GameCharacter personagem) {
        String inicial = personagem.getCasa().name().substring(0, 1);
        int vida = personagem.getVidaAtual();
        int vidaMax = personagem.getCasa().getVidaMaxima();
        
        // Mostra inicial + indicador de vida
        if (vida >= vidaMax * 0.7) {
            return " " + inicial + "●"; // Vida alta - círculo cheio
        } else if (vida >= vidaMax * 0.3) {
            return " " + inicial + "◐"; // Vida média - meio círculo
        } else {
            return " " + inicial + "○"; // Vida baixa - círculo vazio
        }
    }
    
    /**
     * Exibe legenda explicando as cores e símbolos
     */
    private void exibirLegenda() {
        System.out.println("\n    " + ColorUtil.BOLD_CYAN + "═══ LEGENDA ═══" + ColorUtil.RESET);
        
        System.out.print("    ");
        System.out.print(ColorUtil.houseColor("S●", "STARK") + " Stark  ");
        System.out.print(ColorUtil.houseColor("L●", "LANNISTER") + " Lannister  ");
        System.out.print(ColorUtil.houseColor("T●", "TARGARYEN") + " Targaryen");
        System.out.println();
        
        System.out.println("    ● = Vida alta (70%+)  ◐ = Vida média (30-70%)  ○ = Vida baixa (<30%)");
        System.out.println();
        
        // Legenda de identificação de times
        if (personagemAtual != null) {
            System.out.println("    " + ColorUtil.BG_GREEN + ColorUtil.BLACK + " ✓ " + ColorUtil.RESET + 
                " = Seu personagem atual (" + personagemAtual.getNome() + ")");
        }
        
        if (equipeAtual != null && !equipeAtual.isEmpty()) {
            System.out.println("    " + ColorUtil.BG_CYAN + ColorUtil.BLACK + " ◆ " + ColorUtil.RESET + 
                " = Seus aliados (mesma equipe)");
        }
        
        System.out.println("    " + ColorUtil.info("Sem fundo") + " = Inimigos (equipe adversária)");
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
            System.out.println(ColorUtil.error("❌ Movimento inválido: Só é permitido mover 1 casa (ortogonal ou diagonal)."));
            return false;
        }

        if (getPersonagem(novaLinha, novaColuna) != null) {
            System.out.println(ColorUtil.error("❌ Movimento inválido: A casa de destino já está ocupada."));
            return false;
        }

        tabuleiro[lAtual][cAtual] = null;
        tabuleiro[novaLinha][novaColuna] = personagem;
        personagem.setLinha(novaLinha);
        personagem.setColuna(novaColuna);
        
        // Não printa aqui - deixa o Turno printar com cores
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
