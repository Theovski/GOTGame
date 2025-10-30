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
    
    public static int calcularDistancia(Position p1, Position p2) {
        return Math.max(Math.abs(p1.linha - p2.linha), Math.abs(p1.coluna - p2.coluna));
    }

    public static boolean estaNoAlcance(Position origem, Position alvo, int alcance) {
        return calcularDistancia(origem, alvo) <= alcance;
    }
    

    public static Position moverEmDirecao(Position origem, Position destino) {
        int deltaLinha = destino.linha - origem.linha;
        int deltaColuna = destino.coluna - origem.coluna;

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
