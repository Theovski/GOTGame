package com.got.westeros.game;

/**
 * Exceção lançada quando o jogador decide abandonar a partida
 */
public class AbandonoPartidaException extends RuntimeException {
    
    private String nomeJogador;
    
    public AbandonoPartidaException(String nomeJogador) {
        super("O jogador " + nomeJogador + " abandonou a partida.");
        this.nomeJogador = nomeJogador;
    }
    
    public String getNomeJogador() {
        return nomeJogador;
    }
}
