package westeros.entities;

import westeros.enums.House;

public abstract class Character {

    protected String nome;
    protected House casa;
    protected int vidaAtual;
    protected boolean vivo = true;

    public Character(String nome, House casa) {

        this.nome = nome;
        this.casa = casa;
        this.vidaAtual = casa.getVidaMaxima();
    }

    // Aqui calcula o dano ne, mas tem que personalizar para as casas e tal
    public abstract double calcularDano(Character alvo);

    // Aplicar dano
    public void receberDano(double dano) {
        if (dano <= 0) return;

        // Redução especial dos STARK (-20%)
        if (alvoEhStark()) {
            dano *= (1 + casa.getReducaoDano()); // casa.getReducaoDano() = -0.2
        }

        vidaAtual -= dano;
        if (vidaAtual <= 0) {
            vidaAtual = 0;
            vivo = false;
        }
    }

    private boolean alvoEhStark() {
        return this.casa == House.STARK;
    }

    // Getters - tem sempre que ter isso em todo .java basicamente
    public String getNome() { 
        return nome;
    }
    public House getCasa() { 
        return casa; 
    }
    public int getVidaAtual() { 
        return vidaAtual; 
    }
    public boolean isVivo() { 
        return vivo; 
    }

    @Override

    String.format("%s (%s) - Vida: %d/%d", nome, casa.name(), vidaAtual, casa.getVidaMaxima());

}
