package com.got.westeros.utils;

public class Position {
    private int linha;
    private int coluna;
    
    public Position(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }
    
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
    public void setLinha(int linha) { this.linha = linha; }
    public void setColuna(int coluna) { this.coluna = coluna; }
    
    /**
     * Calcula a distância de Chebyshev entre duas posições.
     * Distância ideal para jogos com movimento em 8 direções.
     */
    public static int calcularDistancia(Position p1, Position p2) {
        return Math.max(Math.abs(p1.linha - p2.linha), Math.abs(p1.coluna - p2.coluna));
    }
    
    /**
     * Verifica se uma posição está dentro do alcance de outra
     */
    public static boolean estaNoAlcance(Position origem, Position alvo, int alcance) {
        return calcularDistancia(origem, alvo) <= alcance;
    }
    
    /**
     * Cria uma nova posição movendo 1 célula em direção ao destino.
     * Útil para IA que precisa se aproximar de um alvo.
     */
    public static Position moverEmDirecao(Position origem, Position destino) {
        int deltaLinha = destino.linha - origem.linha;
        int deltaColuna = destino.coluna - origem.coluna;
        
        // Normaliza para -1, 0 ou 1
        int direcaoLinha = Integer.compare(deltaLinha, 0);
        int direcaoColuna = Integer.compare(deltaColuna, 0);
        
        return new Position(
            origem.linha + direcaoLinha,
            origem.coluna + direcaoColuna
        );
    }
    
    @Override
    public String toString() {
        return "[" + linha + "," + coluna + "]";
    }
}
