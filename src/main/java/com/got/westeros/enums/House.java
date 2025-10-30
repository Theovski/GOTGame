package com.got.westeros.enums;

public enum House {
    STARK("Resistência", 60, 20, 10, 1, 0.0, -0.20, "Melee"),
    LANNISTER("Tático", 50, 20, 10, 2, 0.15, 0.0, "Médio"),
    TARGARYEN("Armor Pen", 45, 20, 10, 3, 0.0, 0.0, "Ranged");

    private final String titulo;
    private final int vidaMaxima;
    private final int ataqueBase;
    private final int defesaBase;
    private final int alcance;
    private final double bonusAtaque;
    private final double reducaoDano;
    private final String range;

    House(String titulo, int vidaMaxima, int ataqueBase, int defesaBase, int alcance, 
          double bonusAtaque, double reducaoDano, String range) {
        this.titulo = titulo;
        this.vidaMaxima = vidaMaxima;
        this.ataqueBase = ataqueBase;
        this.defesaBase = defesaBase;
        this.alcance = alcance;
        this.bonusAtaque = bonusAtaque;
        this.reducaoDano = reducaoDano;
        this.range = range;
    }

    public int getVidaMaxima() { return vidaMaxima; }
    public int getAtaqueBase() { return ataqueBase; }
    public int getDefesaBase() { return defesaBase; }
    public int getAlcance() { return alcance; }
    public double getBonusAtaque() { return bonusAtaque; }
    public double getReducaoDano() { return reducaoDano; }
    public String getTitulo() { return titulo; }
    public String getRange() { return range; }
}
