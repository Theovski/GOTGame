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
    
    @Override
    public String toString() {
        return "[" + linha + "," + coluna + "]";
    }
}
