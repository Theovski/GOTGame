package westeros.entities;

import westeros.enums.House;

public class Lannister extends Character {

    public Lannister(String nome) {

        super(nome, House.LANNISTER);
    }

    @Override
    public double calcularDano(Character alvo) {
        
        // Dano bruto = (ataque base + 15%) - defesa do alvo
        double dano = (casa.getAtaqueBase() * (1 + casa.getBonusAtaque())) - alvo.casa.getDefesaBase();

        if (dano < 0) dano = 0;
        return dano;
    }
}
