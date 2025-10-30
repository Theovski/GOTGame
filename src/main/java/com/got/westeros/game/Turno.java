package com.got.westeros.game;

import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.entities.GameCharacter;
import com.got.westeros.utils.Position;
import com.got.westeros.utils.ColorUtil;
import java.util.Scanner;

public class Turno {
    private GameCharacter personagem;
    private Tabuleiro tabuleiro;
    private Scanner teclado;
    private ReplayManager replayManager;

    public Turno(GameCharacter personagem, Tabuleiro tabuleiro, Scanner teclado, ReplayManager replayManager) {
        this.personagem = personagem;
        this.tabuleiro = tabuleiro;
        this.teclado = teclado;
        this.replayManager = replayManager;
    }

    public void executarTurno() {
        // Registra início do turno
        String nomeColorido = ColorUtil.houseColor(personagem.getNome(), personagem.getCasa().name());
        replayManager.registrarAcao("=== TURNO: " + personagem.getNome() + " (" + personagem.getCasa().name() + ") ===");
        
        System.out.println(ColorUtil.info("\n▶ Vez de " + nomeColorido));
        System.out.println(personagem.toString());
        
        tabuleiro.exibirTabuleiro();

        realizarMovimento();
        tabuleiro.exibirTabuleiro();

        realizaAtaque();
        tabuleiro.exibirTabuleiro();

        System.out.println(ColorUtil.info("✓ Fim da vez de " + personagem.getNome()));
    }

    private void realizarMovimento() {
        System.out.println(ColorUtil.warning("\n🚶 FASE DE MOVIMENTO"));
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        System.out.println("Posição atual: " + posAtual);
        
        System.out.print("Digite a linha para mover (ou -1 para pular): ");
        int novaLinha = teclado.nextInt();
        
        if (novaLinha == -1) {
            String mensagem = personagem.getNome() + " decidiu não se mover";
            System.out.println(ColorUtil.warning(mensagem));
            replayManager.registrarAcao(mensagem);
            return;
        }

        System.out.print("Digite a coluna para mover: ");
        int novaColuna = teclado.nextInt();

        boolean moveu = tabuleiro.moverPersonagem(personagem, novaLinha, novaColuna);
        
        if (moveu) {
            String mensagem = personagem.getNome() + " moveu de " + posAtual + " para [" + novaLinha + "," + novaColuna + "]";
            System.out.println(ColorUtil.success("✓ " + mensagem));
            replayManager.registrarAcao(mensagem);
        } else {
            String mensagem = "Movimento falhou! " + personagem.getNome() + " permanece em " + posAtual;
            System.out.println(ColorUtil.error(mensagem));
            replayManager.registrarAcao(mensagem);
        }
    }

    private void realizaAtaque() {
        System.out.println(ColorUtil.error("\n⚔️ FASE DE ATAQUE"));
        System.out.println("Entre com a posição da linha do alvo (-1 para pular): ");
        int posLinhaAlvo = teclado.nextInt();

        if (posLinhaAlvo == -1) {
            String mensagem = personagem.getNome() + " não atacou";
            System.out.println(ColorUtil.warning(mensagem));
            replayManager.registrarAcao(mensagem);
            return;
        }

        System.out.println("Entre com a posição da coluna do alvo: ");
        int posColunaAlvo = teclado.nextInt();

        if (!tabuleiro.verificaPosicao(posLinhaAlvo, posColunaAlvo)) {
            System.out.println(ColorUtil.error("❌ Posição fora do tabuleiro. Não é possível atacar."));
            return;
        }

        GameCharacter alvo = tabuleiro.getPersonagem(posLinhaAlvo, posColunaAlvo);
       
        if (alvo == null) {
            System.out.println(ColorUtil.error("❌ Não há personagem na posição. Não é possível atacar."));
            return;
        }

        if (alvo == personagem) {
            System.out.println(ColorUtil.error("❌ Você não pode atacar a si mesmo!"));
            return;
        }

        if (alvo.getCasa() == personagem.getCasa()) {
            System.out.println(ColorUtil.error("❌ Não é possível atacar um aliado da mesma casa!"));
            return;
        }

        // VERIFICAÇÃO DE ALCANCE (NOVIDADE!)
        Position posAtacante = new Position(personagem.getLinha(), personagem.getColuna());
        Position posAlvo = new Position(posLinhaAlvo, posColunaAlvo);
        int distancia = Position.calcularDistancia(posAtacante, posAlvo);
        int alcance = personagem.getAlcance();
        
        if (distancia > alcance) {
            String mensagem = String.format("❌ Alvo fora do alcance! Distância: %d | Alcance máximo de %s: %d",
                distancia, personagem.getCasa().name(), alcance);
            System.out.println(ColorUtil.error(mensagem));
            replayManager.registrarAcao(personagem.getNome() + " tentou atacar " + alvo.getNome() + " mas estava fora do alcance");
            return;
        }

        // Execução do ataque
        double danoCausado = personagem.calcularDano(alvo);
        
        String atacanteColorido = ColorUtil.houseColor(personagem.getNome(), personagem.getCasa().name());
        String alvoColorido = ColorUtil.houseColor(alvo.getNome(), alvo.getCasa().name());
        
        System.out.println(ColorUtil.error("\n⚔️ " + atacanteColorido + " ataca " + alvoColorido + "!"));

        alvo.receberDano(danoCausado);
        System.out.println(ColorUtil.error(alvo.getNome() + " sofreu " + String.format("%.1f", danoCausado) + " de dano."));
        
        // Registra ataque no replay
        String mensagemAtaque = String.format("%s atacou %s causando %.1f de dano (Distância: %d)", 
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
