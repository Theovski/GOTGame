package westeros.entities;

import westeros.enums.House;

public class Stark extends GameCharacter {

    public Stark(String nome) {
        super(nome, House.STARK);
    }

    @Override
    public double calcularDano(GameCharacter alvo) {
        double dano = casa.getAtaqueBase() - alvo.casa.getDefesaBase();
        if (dano < 0) dano = 0;
        return dano;
    }
}