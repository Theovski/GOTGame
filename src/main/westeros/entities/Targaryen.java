package westeros.entities;

import westeros.enums.House;

public class Targaryen extends Character {

    public Targaryen(String nome) {

        super(nome, House.TARGARYEN);
    }

    @Override

    public double calcularDano(Character alvo) {
        
        // Ignora defesa base
        double dano = casa.getAtaqueBase();

        if (dano < 0) dano = 0;
        return dano;
    }
}
