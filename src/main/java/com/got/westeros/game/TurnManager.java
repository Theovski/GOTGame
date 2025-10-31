package com.got.westeros.game;

import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.entities.GameCharacter;
import com.got.westeros.players.BotPlayer;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class TurnManager {
    private List<GameCharacter> personagemAtivo;
    private Tabuleiro tabuleiro;
    private Scanner teclado;
    private ReplayManager replayManager;
    private Map<GameCharacter, BotPlayer> botsMap;
    private Map<GameCharacter, List<GameCharacter>> equipeMap;

    public TurnManager(List<GameCharacter> personagemAtivo, Tabuleiro tabuleiro, Scanner teclado) {
        this.personagemAtivo = personagemAtivo;
        this.tabuleiro = tabuleiro;
        this.teclado = teclado;
        this.replayManager = new ReplayManager();
        this.botsMap = new HashMap<>();
        this.equipeMap = new HashMap<>();
    }
    
    public void registrarBot(GameCharacter personagem, BotPlayer bot) {
        botsMap.put(personagem, bot);
        bot.setTabuleiro(tabuleiro);
    }

    public void registrarEquipe(GameCharacter personagem, List<GameCharacter> aliados) {
        equipeMap.put(personagem, aliados);
    }
    
    private List<GameCharacter> getEquipe(GameCharacter personagem) {
        return equipeMap.getOrDefault(personagem, List.of());
    }
    
    private boolean isBot(GameCharacter personagem) {
        return botsMap.containsKey(personagem);
    }

    public ReplayManager getReplayManager() {
        return replayManager;
    }

    public void loopTurnos() {
        int numTurno = 1;
        
        replayManager.registrarAcao("INICIO DO JOGO");
        replayManager.registrarAcao("Personagens participantes: " + personagemAtivo.size());

        while (jogoContinua()) {
            System.out.println("RODADA " + numTurno);
            replayManager.registrarAcao("\n>>> RODADA " + numTurno + " <<<");
            
            for (GameCharacter personagemAtual : personagemAtivo) {
                if (personagemAtual.isVivo()) {
                    List<GameCharacter> equipe = getEquipe(personagemAtual);
                    tabuleiro.setEquipeAtual(equipe);
                    
                    if (isBot(personagemAtual)) {
                        BotPlayer bot = botsMap.get(personagemAtual);
                        TurnoBot turnoBot = new TurnoBot(personagemAtual, tabuleiro, bot, personagemAtivo, replayManager);
                        turnoBot.executarTurno();
                    } else {
                        Turno t1 = new Turno(personagemAtual, tabuleiro, teclado, replayManager);
                        int resultado = t1.executarTurno();
                        if (resultado == -1) {
                            System.out.println("PARTIDA ABANDONADA");
                            System.out.println("\nO jogador " + personagemAtual.getNome() + " abandonou a partida.");
                            replayManager.registrarAcao("O jogador " + personagemAtual.getNome() + " abandonou a partida.");
                            return;
                        }
                    }
                    
                    if (!jogoContinua()) {
                        anunciarVencedor();
                        return;
                    }
                }
            }
            numTurno++;
        }
        
        anunciarVencedor();
    }
    
    private void anunciarVencedor() {
        System.out.println("FIM DE JOGO");
        
        GameCharacter vencedor = null;
        for (GameCharacter p : personagemAtivo) {
            if (p.isVivo()) {
                vencedor = p;
                break;
            }
        }
        
        if (vencedor != null) {
            String nomeVencedor = vencedor.getNome();
            System.out.println("\n VENCEDOR: " + nomeVencedor + " da Casa " + vencedor.getCasa().name());
            System.out.println("Vida restante: " + vencedor.getVidaAtual() + "/" + vencedor.getCasa().getVidaMaxima());
            
            replayManager.registrarAcao("\n VENCEDOR: " + vencedor.getNome() + " (" + vencedor.getCasa().name() + ")");
        } else {
            System.out.println("Empate! Todos os personagens foram derrotados.");
            replayManager.registrarAcao("Empate! Todos foram derrotados.");
        }
    }

    public boolean jogoContinua() {
        int personagensVivos = 0;
        for (GameCharacter p : personagemAtivo) {
            if (p.isVivo()) {
                personagensVivos++;
            }
        }
        
        if (personagensVivos < 2) {
            return false;
        }
        
        int casasDiferentes = 0;
        boolean temStark = false;
        boolean temLannister = false;
        boolean temTargaryen = false;
        
        for (GameCharacter p : personagemAtivo) {
            if (p.isVivo()) {
                if (p.getCasa().name().equals("STARK")) {
                    temStark = true;
                } else if (p.getCasa().name().equals("LANNISTER")) {
                    temLannister = true;
                } else if (p.getCasa().name().equals("TARGARYEN")) {
                    temTargaryen = true;
                }
            }
        }
        
        if (temStark) casasDiferentes++;
        if (temLannister) casasDiferentes++;
        if (temTargaryen) casasDiferentes++;
        
        return casasDiferentes > 1;
    }
}
