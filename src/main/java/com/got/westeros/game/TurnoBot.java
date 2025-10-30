package com.got.westeros.game;

import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.entities.GameCharacter;
import com.got.westeros.players.BotPlayer;
import com.got.westeros.utils.Position;
import com.got.westeros.utils.ColorUtil;
import java.util.List;

/**
 * Gerencia o turno automatizado de um personagem controlado por Bot.
 * Usa a IA do BotPlayer para tomar decisões estratégicas.
 */
public class TurnoBot {
    private GameCharacter personagem;
    private Tabuleiro tabuleiro;
    private BotPlayer bot;
    private List<GameCharacter> todosPersonagens;
    private ReplayManager replayManager;

    public TurnoBot(GameCharacter personagem, Tabuleiro tabuleiro, BotPlayer bot, 
                    List<GameCharacter> todosPersonagens, ReplayManager replayManager) {
        this.personagem = personagem;
        this.tabuleiro = tabuleiro;
        this.bot = bot;
        this.todosPersonagens = todosPersonagens;
        this.replayManager = replayManager;
    }

    /**
     * Executa o turno completo do bot: movimento e ataque
     */
    public void executarTurno() {
        // Registra início do turno
        String nomeColorido = ColorUtil.houseColor(personagem.getNome(), personagem.getCasa().name());
        replayManager.registrarAcao("=== TURNO BOT: " + personagem.getNome() + " (" + personagem.getCasa().name() + ") ===");
        
        System.out.println(ColorUtil.info("\n▶ Vez do Bot: " + nomeColorido));
        System.out.println(personagem.toString());
        
        // Define personagem atual para destaque no tabuleiro
        tabuleiro.setPersonagemAtual(personagem);
        tabuleiro.exibirTabuleiro();

        // Pequena pausa para visualização (opcional)
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignora
        }

        realizarMovimentoBot();
        tabuleiro.exibirTabuleiro();

        realizarAtaqueBot();
        tabuleiro.exibirTabuleiro();

        System.out.println(ColorUtil.info("✓ Fim da vez de " + personagem.getNome()));
    }

    /**
     * Fase de movimento do bot usando IA
     */
    private void realizarMovimentoBot() {
        System.out.println(ColorUtil.warning("\n🚶 FASE DE MOVIMENTO (BOT)"));
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        System.out.println("Posição atual: " + posAtual);
        
        // Bot decide para onde mover
        int[] movimento = bot.decidirMovimento(personagem, todosPersonagens);
        
        if (movimento == null) {
            String mensagem = personagem.getNome() + " (Bot) decidiu não se mover";
            System.out.println(ColorUtil.warning(mensagem));
            replayManager.registrarAcao(mensagem);
            return;
        }

        int novaLinha = movimento[0];
        int novaColuna = movimento[1];

        boolean moveu = tabuleiro.moverPersonagem(personagem, novaLinha, novaColuna);
        
        if (moveu) {
            String mensagem = personagem.getNome() + " (Bot) moveu de " + posAtual + " para [" + novaLinha + "," + novaColuna + "]";
            System.out.println(ColorUtil.success("✓ " + mensagem));
            replayManager.registrarAcao(mensagem);
        } else {
            String mensagem = "Movimento falhou! " + personagem.getNome() + " (Bot) permanece em " + posAtual;
            System.out.println(ColorUtil.error(mensagem));
            replayManager.registrarAcao(mensagem);
        }
    }

    /**
     * Fase de ataque do bot usando IA
     */
    private void realizarAtaqueBot() {
        System.out.println(ColorUtil.error("\n⚔️ FASE DE ATAQUE (BOT)"));
        
        // Bot decide quem atacar
        int[] alvoPos = bot.decidirAtaque(personagem, todosPersonagens);
        
        if (alvoPos == null) {
            String mensagem = personagem.getNome() + " (Bot) não atacou";
            System.out.println(ColorUtil.warning(mensagem));
            replayManager.registrarAcao(mensagem);
            return;
        }

        int posLinhaAlvo = alvoPos[0];
        int posColunaAlvo = alvoPos[1];

        GameCharacter alvo = tabuleiro.getPersonagem(posLinhaAlvo, posColunaAlvo);
       
        if (alvo == null) {
            System.out.println(ColorUtil.error("❌ Erro na IA: Não há personagem na posição escolhida."));
            return;
        }

        // Verificação de alcance
        Position posAtacante = new Position(personagem.getLinha(), personagem.getColuna());
        Position posAlvo = new Position(posLinhaAlvo, posColunaAlvo);
        int distancia = Position.calcularDistancia(posAtacante, posAlvo);
        int alcance = personagem.getAlcance();
        
        if (distancia > alcance) {
            String mensagem = String.format("❌ Erro na IA: Alvo fora do alcance! Distância: %d | Alcance: %d",
                distancia, alcance);
            System.out.println(ColorUtil.error(mensagem));
            replayManager.registrarAcao(personagem.getNome() + " (Bot) falhou ao atacar - fora do alcance");
            return;
        }

        // Execução do ataque
        double danoCausado = personagem.calcularDano(alvo);
        
        String atacanteColorido = ColorUtil.houseColor(personagem.getNome(), personagem.getCasa().name());
        String alvoColorido = ColorUtil.houseColor(alvo.getNome(), alvo.getCasa().name());
        
        System.out.println(ColorUtil.error("\n⚔️ " + atacanteColorido + " (Bot) ataca " + alvoColorido + "!"));

        alvo.receberDano(danoCausado);
        System.out.println(ColorUtil.error(alvo.getNome() + " sofreu " + String.format("%.1f", danoCausado) + " de dano."));
        
        // Registra ataque no replay
        String mensagemAtaque = String.format("%s (Bot) atacou %s causando %.1f de dano (Distância: %d)", 
            personagem.getNome(), alvo.getNome(), danoCausado, distancia);
        replayManager.registrarAcao(mensagemAtaque);
        
        if (!alvo.isVivo()) {
            String mensagemMorte = alvo.getNome() + " foi derrotado!";
            System.out.println(ColorUtil.error("💀 " + mensagemMorte));
            replayManager.registrarAcao("💀 " + mensagemMorte);
            tabuleiro.removerPersonagem(alvo);
        } else {
            System.out.println(ColorUtil.warning(alvo.getNome() + " ficou com " + alvo.getVidaAtual() + " de vida."));
        }
    }
}
