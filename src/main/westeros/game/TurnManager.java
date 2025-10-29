package main.westeros.game;

import java.util.List;
import java.util.Scanner;
import main.westeros.entities.Tabuleiro;
import main.westeros.entities.Turno;
import main.westeros.entities.Character;


public class TurnManager {
    

    private List<Character> personagemAtivo;
    private Tabuleiro tabuleiro;

    private Scanner teclado;

    public void loopTurnos(){
        int numTurno = 1;

        // loop principal: continua enquanto houver personagens vivo
        while(jogoContinua() == true){
            System.out.println("RODADA: " + numTurno);
            // passa em cada personagem
            for(Character personagemAtual : personagemAtivo){
                // so executa se personagem ativo
                if(personagemAtual.isVivo()){
                    Turno t1 = new Turno(personagemAtual, tabuleiro, teclado);
                    t1.executarTurno();
                    if(jogoContinua() == false){
                        return;
                    }
                }
            }
            numTurno++;
        }
        System.out.println("Fim de jogo.");
        teclado.close();
    } 


    public TurnManager(List<Character> personagemAtivo, Tabuleiro tabuleiro, Scanner teclado) {
    this.personagemAtivo = personagemAtivo;
    this.tabuleiro = tabuleiro;
    this.teclado = teclado; // <-- CORRETO: Usa o 'teclado' que foi passado
}


    // verifica se ha pelo menos dois personagens vivos de casas diferentes
    public boolean jogoContinua(){
        if(personagemAtivo.stream().filter(Character::isVivo).count() < 2){
            return false;
        } 
        
        return personagemAtivo.stream().filter(Character::isVivo).map(Character::getCasa).distinct().count() > 1;

    }
}
