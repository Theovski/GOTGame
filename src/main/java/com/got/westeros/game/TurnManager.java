package com.got.westeros.game;

import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.entities.GameCharacter;
import com.got.westeros.utils.ColorUtil;
import java.util.List;
import java.util.Scanner;

public class TurnManager {
    private List<GameCharacter> personagemAtivo;
    private Tabuleiro tabuleiro;
    private Scanner teclado;
    private ReplayManager replayManager;

    public TurnManager(List<GameCharacter> personagemAtivo, Tabuleiro tabuleiro, Scanner teclado) {
        this.personagemAtivo = personagemAtivo;
        this.tabuleiro = tabuleiro;
        this.teclado = teclado;
        this.replayManager = new ReplayManager();
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

        while (jogoContinua()) {
            ColorUtil.printTitle("RODADA " + numTurno, ColorUtil.BOLD_CYAN);
            replayManager.registrarAcao("\n>>> RODADA " + numTurno + " <<<");
            
            for (GameCharacter personagemAtual : personagemAtivo) {
                if (personagemAtual.isVivo()) {
                    Turno t1 = new Turno(personagemAtual, tabuleiro, teclado, replayManager);
                    t1.executarTurno();
                    
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
