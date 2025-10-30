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

    public void executarTurno() {
        String charName = personagem.getNome();
        replayManager.registrarAcao("=== TURNO: " + charName + " (" + personagem.getCasa().name() + ") ===");
        
        System.out.println("\n Vez de " + charName);
        System.out.println(personagem.toString());
        System.out.println("Casa " + personagem.getCasa().name() + 
            " | Vida: " + personagem.getVidaAtual() + "/" + personagem.getCasa().getVidaMaxima() +
            " | Alcance: " + personagem.getAlcance());
        
        tabuleiro.setPersonagemAtual(personagem);
        tabuleiro.exibirTabuleiro();

        realizarMovimento();
        System.out.println();
        tabuleiro.exibirTabuleiro();

        realizaAtaque();
        System.out.println();
        tabuleiro.exibirTabuleiro();

        System.out.println("\n Fim da vez de " + charName);
    }

    private void realizarMovimento() {
        System.out.println("\nFASE DE MOVIMENTO");
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        System.out.println("Posição atual: " + posAtual);
        System.out.println("Digite -1 para pular o movimento");
        System.out.println("Digite -99 para ABANDONAR a partida");
        
        int novaLinha = InputValidator.validarInteiro(teclado, "Digite a linha para mover: ", -99, 9);
        
        if (novaLinha == -99) {
            throw new AbandonoPartidaException(personagem.getNome());
        }
        
        if (novaLinha == -1) {
            String mensagem = personagem.getNome() + " decidiu não se mover";
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
            return;
        }

        int novaColuna = InputValidator.validarInteiro(teclado, "Digite a coluna para mover: ", 0, 9);

        int diffL = Math.abs(novaLinha - personagem.getLinha());
        int diffC = Math.abs(novaColuna - personagem.getColuna());
        
        if (diffL > 1 || diffC > 1) {
            System.out.println("Movimento inválido! Só é permitido mover 1 casa por turno.");
            System.out.println("   Você está em [" + personagem.getLinha() + "," + 
                personagem.getColuna() + "] e tentou ir para [" + novaLinha + "," + novaColuna + "]");
            System.out.println("   Dica: Você pode mover ortogonalmente ou diagonalmente (1 casa).");
            String mensagem = personagem.getNome() + " tentou movimento inválido e permaneceu em " + posAtual;
            replayManager.registrarAcao(mensagem);
            return;
        }
        
        if (diffL == 0 && diffC == 0) {
            System.out.println("Você já está nesta posição!");
            String mensagem = personagem.getNome() + " decidiu não se mover";
            replayManager.registrarAcao(mensagem);
            return;
        }
        
        boolean moveu = tabuleiro.moverPersonagem(personagem, novaLinha, novaColuna);
        
        if (moveu) {
            String mensagem = personagem.getNome() + " moveu de " + posAtual + " para [" + novaLinha + "," + novaColuna + "]";
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
        } else {
            System.out.println("Movimento falhou! A posição [" + novaLinha + "," + novaColuna + "] está ocupada.");
            System.out.println("   Dica: Escolha uma célula vazia no tabuleiro.");
            String mensagem = "Movimento falhou! " + personagem.getNome() + " permanece em " + posAtual;
            replayManager.registrarAcao(mensagem);
        }
    }

    private void realizaAtaque() {
        System.out.println("\nFASE DE ATAQUE");
        System.out.println("Digite -1 para pular o ataque");
        System.out.println("Digite -99 para ABANDONAR a partida");
        System.out.println("Seu alcance: " + personagem.getAlcance() + " células");
        
        int posLinhaAlvo = InputValidator.validarInteiro(teclado, "Entre com a linha do alvo: ", -99, 9);
        
        if (posLinhaAlvo == -99) {
            throw new AbandonoPartidaException(personagem.getNome());
        }

        if (posLinhaAlvo == -1) {
            String mensagem = personagem.getNome() + " não atacou";
            System.out.println(mensagem);
            replayManager.registrarAcao(mensagem);
            return;
        }

        int posColunaAlvo = InputValidator.validarInteiro(teclado, "Entre com a coluna do alvo: ", 0, 9);

        if (!tabuleiro.verificaPosicao(posLinhaAlvo, posColunaAlvo)) {
            System.out.println("Posição fora do tabuleiro. Não é possível atacar.");
            System.out.println("   Dica: As coordenadas válidas são de 0 a 9.");
            return;
        }

        GameCharacter alvo = tabuleiro.getPersonagem(posLinhaAlvo, posColunaAlvo);
       
        if (alvo == null) {
            System.out.println("Não há personagem na posição [" + posLinhaAlvo + "," + posColunaAlvo + "].");
            System.out.println("   Dica: Confira o tabuleiro acima para ver onde estão os inimigos.");
            return;
        }

        if (alvo == personagem) {
            System.out.println("Você não pode atacar a si mesmo!");
            System.out.println("   Dica: Você está em [" + personagem.getLinha() + "," + personagem.getColuna() + "].");
            return;
        }

        if (alvo.getCasa() == personagem.getCasa()) {
            System.out.println("Não é possível atacar um aliado da mesma casa!");
            System.out.println("    Dica: " + alvo.getNome() + " é da casa " + alvo.getCasa().name() + " assim como você.");
            return;
        }

        Position posAtacante = new Position(personagem.getLinha(), personagem.getColuna());
        Position posAlvo = new Position(posLinhaAlvo, posColunaAlvo);
        int distancia = Position.calcularDistancia(posAtacante, posAlvo);
        int alcance = personagem.getAlcance();
        
        if (distancia > alcance) {
            System.out.println("Alvo fora do alcance!");
            System.out.println(String.format("   Distância até o alvo: %d células", distancia));
            System.out.println(String.format("   Seu alcance máximo (%s): %d células", 
                personagem.getCasa().name(), alcance));
            System.out.println("   Dica: Você precisa se aproximar " + (distancia - alcance) + 
                " célula(s) para atacar este alvo.");
            replayManager.registrarAcao(personagem.getNome() + " tentou atacar " + alvo.getNome() + 
                " mas estava fora do alcance (distância: " + distancia + ", alcance: " + alcance + ")");
            return;
        }

        double danoCausado = personagem.calcularDano(alvo);
        
        String atakker = personagem.getNome();
        String target = alvo.getNome();
        
        System.out.println("\n " + atakker + " ataca " + target + "!");

        alvo.receberDano(danoCausado);
        System.out.println(target + " sofreu " + String.format("%.1f", danoCausado) + " de dano.");
        
        String mensagemAtaque = String.format("%s atacou %s causando %.1f de dano (Distância: %d)", 
            atakker, target, danoCausado, distancia);
        replayManager.registrarAcao(mensagemAtaque);
        
        if (!alvo.isVivo()) {
            String mensagemMorte = target + " foi derrotado!";
            System.out.println(mensagemMorte);
            replayManager.registrarAcao(mensagemMorte);
            tabuleiro.removerPersonagem(alvo);
        } else {
            System.out.println(target + " ficou com " + alvo.getVidaAtual() + " de vida.");
        }
    }
}
