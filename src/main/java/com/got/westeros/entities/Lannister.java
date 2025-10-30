package com.got.westeros.entities;

import com.got.westeros.enums.House;

public class Lannister extends GameCharacter {

    public Lannister(String nome) {
        super(nome, House.LANNISTER);
    }

    @Override
    public double calcularDano(GameCharacter alvo) {
        double dano = (casa.getAtaqueBase() * (1 + casa.getBonusAtaque())) - alvo.casa.getDefesaBase();
        if (dano < 0) dano = 0;
        return dano;
    }
}
