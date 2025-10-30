package westeros.entities;

import westeros.enums.House;

public abstract class GameCharacter {
    protected String nome;
    protected House casa;
    protected int vidaAtual;
    protected boolean vivo = true;
    protected int linha;
    protected int coluna;

    public GameCharacter(String nome, House casa) {
        this.nome = nome;
        this.casa = casa;
        this.vidaAtual = casa.getVidaMaxima();
    }

    public abstract double calcularDano(GameCharacter alvo);

    public void receberDano(double dano) {
        if (dano <= 0) return;

        // Redução especial dos STARK (-20%)
        if (this.casa == House.STARK) {
            dano *= (1 + casa.getReducaoDano());
        }

        vidaAtual -= dano;
        if (vidaAtual <= 0) {
            vidaAtual = 0;
            vivo = false;
        }
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public House getCasa() { return casa; }
    public int getVidaAtual() { return vidaAtual; }
    public boolean isVivo() { return vivo; }
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }
    public void setLinha(int linha) { this.linha = linha; }
    public void setColuna(int coluna) { this.coluna = coluna; }

    @Override
    public String toString() {
        return String.format("%s (%s) - Vida: %d/%d - Pos: [%d,%d]", 
            nome, casa.name(), vidaAtual, casa.getVidaMaxima(), linha, coluna);
    }
}