package westeros.entities;

import westeros.enums.House;

public class Stark extends Character {

    public Stark(String nome) {

        super(nome, House.STARK);
    }

    @Override
    public double calcularDano(Character alvo) {

        double dano = casa.getAtaqueBase() - alvo.casa.getDefesaBase();

        if (dano < 0) dano = 0;

        // Se o alvo for Stark, redução no recebimento (feito na função receberDano)
        return dano;
    }
}
