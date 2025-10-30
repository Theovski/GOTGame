package com.got.westeros.game;

import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.entities.GameCharacter;
import com.got.westeros.players.BotPlayer;
import com.got.westeros.utils.Position;
import java.util.List;
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

    public void executarTurno() {
        String charName = personagem.getNome();
        replayManager.registrarAcao("=== TURNO BOT: " + charName + " (" + personagem.getCasa().name() + ") ===");
        
        System.out.println("\n Vez do Bot: " + charName);
        System.out.println(personagem.toString());
        
        tabuleiro.setPersonagemAtual(personagem);
        tabuleiro.exibirTabuleiro();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {

        }

        realizarMovimentoBot();
        tabuleiro.exibirTabuleiro();

        realizarAtaqueBot();
        tabuleiro.exibirTabuleiro();

        System.out.println(" Fim da vez de " + charName);
    }

    private void realizarMovimentoBot() {
        System.out.println("\n FASE DE MOVIMENTO (BOT)");
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        System.out.println("Posição atual: " + posAtual);
        
        int[] movimento = bot.decidirMovimento(personagem, todosPersonagens);
        
        if (movimento == null) {
            String mensagem = personagem.getNome() + " (Bot) decidiu não se mover";
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
            return;
        }

        int novaLinha = movimento[0];
        int novaColuna = movimento[1];

        boolean moveu = tabuleiro.moverPersonagem(personagem, novaLinha, novaColuna);
        
        if (moveu) {
            String mensagem = personagem.getNome() + " (Bot) moveu de " + posAtual + " para [" + novaLinha + "," + novaColuna + "]";
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
        } else {
            String mensagem = "Movimento falhou! " + personagem.getNome() + " (Bot) permanece em " + posAtual;
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
        }
    }

    private void realizarAtaqueBot() {
        System.out.println("\n FASE DE ATAQUE (BOT)");
        
        int[] alvoPos = bot.decidirAtaque(personagem, todosPersonagens);
        
        if (alvoPos == null) {
            String mensagem = personagem.getNome() + " (Bot) não atacou";
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
            return;
        }

        int posLinhaAlvo = alvoPos[0];
        int posColunaAlvo = alvoPos[1];

        GameCharacter alvo = tabuleiro.getPersonagem(posLinhaAlvo, posColunaAlvo);
       
        if (alvo == null) {
            System.out.println("Erro na IA: Não há personagem na posição escolhida.");
            return;
        }

        Position posAtacante = new Position(personagem.getLinha(), personagem.getColuna());
        Position posAlvo = new Position(posLinhaAlvo, posColunaAlvo);
        int distancia = Position.calcularDistancia(posAtacante, posAlvo);
        int alcance = personagem.getAlcance();
        
        if (distancia > alcance) {
            String mensagem = String.format("Erro na IA: Alvo fora do alcance! Distância: %d | Alcance: %d",
                distancia, alcance);
            System.out.println(mensagem);
            replayManager.registrarAcao(personagem.getNome() + " (Bot) falhou ao atacar - fora do alcance");
            return;
        }

        double danoCausado = personagem.calcularDano(alvo);
        
        String atakker = personagem.getNome();
        String target = alvo.getNome();
        
        System.out.println("\n " + atakker + " (Bot) ataca " + target + "!");

        alvo.receberDano(danoCausado);
        System.out.println(target + " sofreu " + String.format("%.1f", danoCausado) + " de dano.");
        
        String mensagemAtaque = String.format("%s (Bot) atacou %s causando %.1f de dano (Distância: %d)", 
            atakker, target, danoCausado, distancia);
        replayManager.registrarAcao(mensagemAtaque);
        
        if (!alvo.isVivo()) {
            String mensagemMorte = target + " foi derrotado!";
            System.out.println(mensagemMorte);
            replayManager.registrarAcao("💀 " + mensagemMorte);
            tabuleiro.removerPersonagem(alvo);
        } else {
            System.out.println(target + " ficou com " + alvo.getVidaAtual() + " de vida.");
        }
    }
}
