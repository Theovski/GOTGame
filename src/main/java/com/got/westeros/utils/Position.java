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
        int diffLinha = Math.abs(p1.linha - p2.linha);
        int diffColuna = Math.abs(p1.coluna - p2.coluna);
        
        if (diffLinha > diffColuna) {
            return diffLinha;
        }
        return diffColuna;
    }

    public static boolean estaNoAlcance(Position origem, Position alvo, int alcance) {
        return calcularDistancia(origem, alvo) <= alcance;
    }
    

    public static Position moverEmDirecao(Position origem, Position destino) {
        int deltaLinha = destino.linha - origem.linha;
        int deltaColuna = destino.coluna - origem.coluna;

        int direcaoLinha = 0;
        if (deltaLinha > 0) {
            direcaoLinha = 1;
        } else if (deltaLinha < 0) {
            direcaoLinha = -1;
        }
        
        int direcaoColuna = 0;
        if (deltaColuna > 0) {
            direcaoColuna = 1;
        } else if (deltaColuna < 0) {
            direcaoColuna = -1;
        }
        
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
