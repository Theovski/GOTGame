package com.got.westeros.game;

import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.entities.GameCharacter;
import java.util.List;
import java.util.Scanner;

public class TurnManager {
    private List<GameCharacter> personagemAtivo;
    private Tabuleiro tabuleiro;
    private Scanner teclado;

    public TurnManager(List<GameCharacter> personagemAtivo, Tabuleiro tabuleiro, Scanner teclado) {
        this.personagemAtivo = personagemAtivo;
        this.tabuleiro = tabuleiro;
        this.teclado = teclado;
    }

    public void loopTurnos() {
        int numTurno = 1;

        while (jogoContinua()) {
            System.out.println("\n=== RODADA " + numTurno + " ===");
            
            for (GameCharacter personagemAtual : personagemAtivo) {
                if (personagemAtual.isVivo()) {
                    Turno t1 = new Turno(personagemAtual, tabuleiro, teclado);
                    t1.executarTurno();
                    
                    if (!jogoContinua()) {
                        return;
                    }
                }
            }
            numTurno++;
        }
        System.out.println("Fim de jogo.");
    }

    public boolean jogoContinua() {
        long personagensVivos = personagemAtivo.stream().filter(GameCharacter::isVivo).count();
        if (personagensVivos < 2) {
            return false;
        }
        
        long casasDiferentes = personagemAtivo.stream()
                .filter(GameCharacter::isVivo)
                .map(GameCharacter::getCasa)
                .distinct()
                .count();
        
        return casasDiferentes > 1;
    }
}
