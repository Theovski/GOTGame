package com.got.westeros.game;

import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.entities.GameCharacter;
import com.got.westeros.players.BotPlayer;
import com.got.westeros.utils.ColorUtil;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class TurnManager {
    private List<GameCharacter> personagemAtivo;
    private Tabuleiro tabuleiro;
    private Scanner teclado;
    private ReplayManager replayManager;
    private Map<GameCharacter, BotPlayer> botsMap; // Mapeia personagem -> bot controlador
    private Map<GameCharacter, List<GameCharacter>> equipeMap; // Mapeia personagem -> lista de aliados

    public TurnManager(List<GameCharacter> personagemAtivo, Tabuleiro tabuleiro, Scanner teclado) {
        this.personagemAtivo = personagemAtivo;
        this.tabuleiro = tabuleiro;
        this.teclado = teclado;
        this.replayManager = new ReplayManager();
        this.botsMap = new HashMap<>();
        this.equipeMap = new HashMap<>();
    }
    
    /**
     * Registra um personagem como sendo controlado por bot
     */
    public void registrarBot(GameCharacter personagem, BotPlayer bot) {
        botsMap.put(personagem, bot);
        bot.setTabuleiro(tabuleiro);
    }
    
    /**
     * Registra a equipe de um personagem (seus aliados)
     */
    public void registrarEquipe(GameCharacter personagem, List<GameCharacter> aliados) {
        equipeMap.put(personagem, aliados);
    }
    
    /**
     * Retorna a lista de aliados de um personagem
     */
    private List<GameCharacter> getEquipe(GameCharacter personagem) {
        return equipeMap.getOrDefault(personagem, List.of());
    }
    
    /**
     * Verifica se um personagem é controlado por bot
     */
    private boolean isBot(GameCharacter personagem) {
        return botsMap.containsKey(personagem);
    }
    
    /**
     * Retorna o ReplayManager para acesso externo
     */
    public ReplayManager getReplayManager() {
        return replayManager;
    }

    public void loopTurnos() {
        int numTurno = 1;
        
        replayManager.registrarAcao("🎮 INÍCIO DO JOGO");
        replayManager.registrarAcao("Personagens participantes: " + personagemAtivo.size());

        try {
            while (jogoContinua()) {
                ColorUtil.printTitle("RODADA " + numTurno, ColorUtil.BOLD_CYAN);
                replayManager.registrarAcao("\n>>> RODADA " + numTurno + " <<<");
                
                for (GameCharacter personagemAtual : personagemAtivo) {
                    if (personagemAtual.isVivo()) {
                        // Define a equipe no tabuleiro para destacar aliados
                        List<GameCharacter> equipe = getEquipe(personagemAtual);
                        tabuleiro.setEquipeAtual(equipe);
                        
                        // Verifica se é bot ou humano
                        if (isBot(personagemAtual)) {
                            // Turno do bot
                            BotPlayer bot = botsMap.get(personagemAtual);
                            TurnoBot turnoBot = new TurnoBot(personagemAtual, tabuleiro, bot, personagemAtivo, replayManager);
                            turnoBot.executarTurno();
                        } else {
                            // Turno humano
                            Turno t1 = new Turno(personagemAtual, tabuleiro, teclado, replayManager);
                            t1.executarTurno();
                        }
                        
                        if (!jogoContinua()) {
                            anunciarVencedor();
                            return;
                        }
                    }
                }
                numTurno++;
            }
        } catch (AbandonoPartidaException e) {
            // Jogador decidiu abandonar
            ColorUtil.printTitle("⚠️ PARTIDA ABANDONADA", ColorUtil.BOLD_YELLOW);
            System.out.println(ColorUtil.warning("\n" + e.getMessage()));
            replayManager.registrarAcao("⚠️ " + e.getMessage());
            ColorUtil.printSeparator(ColorUtil.YELLOW);
            return;
        }
        
        anunciarVencedor();
    }
    
    /**
     * Anuncia o vencedor do jogo
     */
    private void anunciarVencedor() {
        ColorUtil.printTitle("🏆 FIM DE JOGO 🏆", ColorUtil.BOLD_GREEN);
        
        GameCharacter vencedor = personagemAtivo.stream()
            .filter(GameCharacter::isVivo)
            .findFirst()
            .orElse(null);
        
        if (vencedor != null) {
            String nomeVencedor = ColorUtil.houseColor(vencedor.getNome(), vencedor.getCasa().name());
            System.out.println(ColorUtil.success("\n🎉 VENCEDOR: " + nomeVencedor + " da Casa " + vencedor.getCasa().name()));
            System.out.println(ColorUtil.info("Vida restante: " + vencedor.getVidaAtual() + "/" + vencedor.getCasa().getVidaMaxima()));
            
            replayManager.registrarAcao("\n🏆 VENCEDOR: " + vencedor.getNome() + " (" + vencedor.getCasa().name() + ")");
        } else {
            System.out.println(ColorUtil.warning("Empate! Todos os personagens foram derrotados."));
            replayManager.registrarAcao("⚔️ Empate! Todos foram derrotados.");
        }
        
        ColorUtil.printSeparator(ColorUtil.GREEN);
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
