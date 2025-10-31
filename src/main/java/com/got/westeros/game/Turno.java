package com.got.westeros.game;

import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.entities.GameCharacter;
import com.got.westeros.utils.Position;
import com.got.westeros.utils.InputValidator;
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

    public int executarTurno() {
        String charName = personagem.getNome();
        replayManager.registrarAcao("=== TURNO: " + charName + " (" + personagem.getCasa().name() + ") ===");
        
        System.out.println("\n Vez de " + charName);
        System.out.println(personagem.toString());
        System.out.println("Casa " + personagem.getCasa().name() + 
            " | Vida: " + personagem.getVidaAtual() + "/" + personagem.getCasa().getVidaMaxima() +
            " | Alcance: " + personagem.getAlcance());
        
        tabuleiro.setPersonagemAtual(personagem);
        tabuleiro.exibirTabuleiro();

        int resultadoMovimento = realizarMovimento();
        if (resultadoMovimento == -1) {
            return -1;
        }
        
        System.out.println();
        tabuleiro.exibirTabuleiro();

        int resultadoAtaque = realizaAtaque();
        if (resultadoAtaque == -1) {
            return -1;
        }
        
        System.out.println();
        tabuleiro.exibirTabuleiro();

        System.out.println("\n Fim da vez de " + charName);
        return 0;
    }

    private int realizarMovimento() {
        System.out.println("\nFASE DE MOVIMENTO");
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        System.out.println("Posicao atual: " + posAtual);
        System.out.println("Digite -1 para pular o movimento");
        System.out.println("Digite -99 para ABANDONAR a partida");
        
        int novaLinha = InputValidator.validarInteiro(teclado, "Digite a linha para mover: ", -99, 9);
        
        if (novaLinha == -99) {
            return -1;
        }
        
        if (novaLinha == -1) {
            String mensagem = personagem.getNome() + " decidiu nao se mover";
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
            return 0;
        }

        int novaColuna = InputValidator.validarInteiro(teclado, "Digite a coluna para mover: ", 0, 9);

        int diffL = Math.abs(novaLinha - personagem.getLinha());
        int diffC = Math.abs(novaColuna - personagem.getColuna());
        
        if (diffL > 1 || diffC > 1) {
            System.out.println("Movimento invalido! So e permitido mover 1 casa por turno.");
            System.out.println("   Voce esta em [" + personagem.getLinha() + "," + 
                personagem.getColuna() + "] e tentou ir para [" + novaLinha + "," + novaColuna + "]");
            System.out.println("   Dica: Voce pode mover ortogonalmente ou diagonalmente (1 casa).");
            String mensagem = personagem.getNome() + " tentou movimento invalido e permaneceu em " + posAtual;
            replayManager.registrarAcao(mensagem);
            return 0;
        }
        
        if (diffL == 0 && diffC == 0) {
            System.out.println("Voce ja esta nesta posicao!");
            String mensagem = personagem.getNome() + " decidiu nao se mover";
            replayManager.registrarAcao(mensagem);
            return 0;
        }
        
        boolean moveu = tabuleiro.moverPersonagem(personagem, novaLinha, novaColuna);
        
        if (moveu) {
            String mensagem = personagem.getNome() + " moveu de " + posAtual + " para [" + novaLinha + "," + novaColuna + "]";
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
        } else {
            System.out.println("Movimento falhou! A posicao [" + novaLinha + "," + novaColuna + "] esta ocupada.");
            System.out.println("   Dica: Escolha uma celula vazia no tabuleiro.");
            String mensagem = "Movimento falhou! " + personagem.getNome() + " permanece em " + posAtual;
            replayManager.registrarAcao(mensagem);
        }
        
        return 0;
    }

    private int realizaAtaque() {
        System.out.println("\nFASE DE ATAQUE");
        System.out.println("Digite -1 para pular o ataque");
        System.out.println("Digite -99 para ABANDONAR a partida");
        System.out.println("Seu alcance: " + personagem.getAlcance() + " celulas");
        
        int posLinhaAlvo = InputValidator.validarInteiro(teclado, "Entre com a linha do alvo: ", -99, 9);
        
        if (posLinhaAlvo == -99) {
            return -1;
        }

        if (posLinhaAlvo == -1) {
            String mensagem = personagem.getNome() + " nao atacou";
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
            return 0;
        }

        int posColunaAlvo = InputValidator.validarInteiro(teclado, "Entre com a coluna do alvo: ", 0, 9);

        if (!tabuleiro.verificaPosicao(posLinhaAlvo, posColunaAlvo)) {
            System.out.println("Posicao fora do tabuleiro. Nao e possivel atacar.");
            System.out.println("   Dica: As coordenadas validas sao de 0 a 9.");
            return 0;
        }

        GameCharacter alvo = tabuleiro.getPersonagem(posLinhaAlvo, posColunaAlvo);
       
        if (alvo == null) {
            System.out.println("Nao ha personagem na posicao [" + posLinhaAlvo + "," + posColunaAlvo + "].");
            System.out.println("   Dica: Confira o tabuleiro acima para ver onde estao os inimigos.");
            return 0;
        }

        if (alvo == personagem) {
            System.out.println("Voce nao pode atacar a si mesmo!");
            System.out.println("   Dica: Voce esta em [" + personagem.getLinha() + "," + personagem.getColuna() + "].");
            return 0;
        }

        if (alvo.getCasa() == personagem.getCasa()) {
            System.out.println("Nao e possivel atacar um aliado da mesma casa!");
            System.out.println("    Dica: " + alvo.getNome() + " e da casa " + alvo.getCasa().name() + " assim como voce.");
            return 0;
        }

        Position posAtacante = new Position(personagem.getLinha(), personagem.getColuna());
        Position posAlvo = new Position(posLinhaAlvo, posColunaAlvo);
        int distancia = Position.calcularDistancia(posAtacante, posAlvo);
        int alcance = personagem.getAlcance();
        
        if (distancia > alcance) {
            System.out.println("Alvo fora do alcance!");
            System.out.println("   Distancia ate o alvo: " + distancia + " celulas");
            System.out.println("   Seu alcance maximo (" + personagem.getCasa().name() + "): " + alcance + " celulas");
            System.out.println("   Dica: Voce precisa se aproximar " + (distancia - alcance) + 
                " celula(s) para atacar este alvo.");
            replayManager.registrarAcao(personagem.getNome() + " tentou atacar " + alvo.getNome() + 
                " mas estava fora do alcance (distancia: " + distancia + ", alcance: " + alcance + ")");
            return 0;
        }

        double danoCausado = personagem.calcularDano(alvo);
        
        String atakker = personagem.getNome();
        String target = alvo.getNome();
        
        System.out.println("\n " + atakker + " ataca " + target + "!");

        alvo.receberDano(danoCausado);
        System.out.println(target + " sofreu " + danoCausado + " de dano.");
        
        String mensagemAtaque = atakker + " atacou " + target + " causando " + danoCausado + " de dano (Distancia: " + distancia + ")";
        replayManager.registrarAcao(mensagemAtaque);
        
        if (!alvo.isVivo()) {
            String mensagemMorte = target + " foi derrotado!";
            System.out.println(mensagemMorte);
            replayManager.registrarAcao(mensagemMorte);
            tabuleiro.removerPersonagem(alvo);
        } else {
            System.out.println(target + " ficou com " + alvo.getVidaAtual() + " de vida.");
        }
        
        return 0;
    }
}
