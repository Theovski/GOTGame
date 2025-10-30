package westeros.entities;

import westeros.enums.House;

public class Targaryen extends GameCharacter {

    public Targaryen(String nome) {
        super(nome, House.TARGARYEN);
    }

    @Override
    public double calcularDano(GameCharacter alvo) {
        double dano = casa.getAtaqueBase();
        if (dano < 0) dano = 0;
        return dano;
    }
}