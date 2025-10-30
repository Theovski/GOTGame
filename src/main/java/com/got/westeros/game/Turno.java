package com.got.westeros.game;

import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.entities.GameCharacter;
import com.got.westeros.utils.Position;
import com.got.westeros.utils.ColorUtil;
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
        // Registra início do turno
        String nomeColorido = ColorUtil.houseColor(personagem.getNome(), personagem.getCasa().name());
        replayManager.registrarAcao("=== TURNO: " + personagem.getNome() + " (" + personagem.getCasa().name() + ") ===");
        
        System.out.println(ColorUtil.info("\n▶ Vez de " + nomeColorido));
        System.out.println(personagem.toString());
        System.out.println(ColorUtil.info("Casa " + personagem.getCasa().name() + 
            " | Vida: " + personagem.getVidaAtual() + "/" + personagem.getCasa().getVidaMaxima() +
            " | Alcance: " + personagem.getAlcance()));
        
        // Define personagem atual para destaque no tabuleiro
        tabuleiro.setPersonagemAtual(personagem);
        tabuleiro.exibirTabuleiro();

        realizarMovimento();
        System.out.println(); // Espaço antes do próximo tabuleiro
        tabuleiro.exibirTabuleiro();

        realizaAtaque();
        System.out.println(); // Espaço antes do próximo tabuleiro
        tabuleiro.exibirTabuleiro();

        System.out.println(ColorUtil.success("\n✓ Fim da vez de " + personagem.getNome()));
        ColorUtil.printSeparator(ColorUtil.CYAN);
    }

    private void realizarMovimento() {
        System.out.println(ColorUtil.warning("\n🚶 FASE DE MOVIMENTO"));
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        System.out.println("Posição atual: " + posAtual);
        System.out.println(ColorUtil.info("Digite -1 para pular o movimento"));
        System.out.println(ColorUtil.error("Digite -99 para ABANDONAR a partida"));
        
        // Validar linha (-99, -1 a 9)
        int novaLinha = InputValidator.validarInteiro(teclado, "Digite a linha para mover: ", -99, 9);
        
        // Verificar se jogador quer abandonar
        if (novaLinha == -99) {
            throw new AbandonoPartidaException(personagem.getNome());
        }
        
        if (novaLinha == -1) {
            String mensagem = personagem.getNome() + " decidiu não se mover";
            System.out.println(ColorUtil.warning(mensagem));
            replayManager.registrarAcao(mensagem);
            return;
        }

        // Validar coluna (0 a 9)
        int novaColuna = InputValidator.validarInteiro(teclado, "Digite a coluna para mover: ", 0, 9);

        // Verificar se movimento é válido ANTES de tentar
        int diffL = Math.abs(novaLinha - personagem.getLinha());
        int diffC = Math.abs(novaColuna - personagem.getColuna());
        
        if (diffL > 1 || diffC > 1) {
            System.out.println(ColorUtil.error("❌ Movimento inválido! Só é permitido mover 1 casa por turno."));
            System.out.println(ColorUtil.warning("   Você está em [" + personagem.getLinha() + "," + 
                personagem.getColuna() + "] e tentou ir para [" + novaLinha + "," + novaColuna + "]"));
            System.out.println(ColorUtil.info("   Dica: Você pode mover ortogonalmente ou diagonalmente (1 casa)."));
            String mensagem = personagem.getNome() + " tentou movimento inválido e permaneceu em " + posAtual;
            replayManager.registrarAcao(mensagem);
            return;
        }
        
        if (diffL == 0 && diffC == 0) {
            System.out.println(ColorUtil.warning("⚠️ Você já está nesta posição!"));
            String mensagem = personagem.getNome() + " decidiu não se mover";
            replayManager.registrarAcao(mensagem);
            return;
        }
        
        boolean moveu = tabuleiro.moverPersonagem(personagem, novaLinha, novaColuna);
        
        if (moveu) {
            String mensagem = personagem.getNome() + " moveu de " + posAtual + " para [" + novaLinha + "," + novaColuna + "]";
            System.out.println(ColorUtil.success("✓ " + mensagem));
            replayManager.registrarAcao(mensagem);
        } else {
            // Só chega aqui se a posição estiver ocupada (já validamos o resto)
            System.out.println(ColorUtil.error("❌ Movimento falhou! A posição [" + novaLinha + "," + novaColuna + "] está ocupada."));
            System.out.println(ColorUtil.info("   Dica: Escolha uma célula vazia no tabuleiro."));
            String mensagem = "Movimento falhou! " + personagem.getNome() + " permanece em " + posAtual;
            replayManager.registrarAcao(mensagem);
        }
    }

    private void realizaAtaque() {
        System.out.println(ColorUtil.error("\n⚔️ FASE DE ATAQUE"));
        System.out.println(ColorUtil.info("Digite -1 para pular o ataque"));
        System.out.println(ColorUtil.error("Digite -99 para ABANDONAR a partida"));
        System.out.println(ColorUtil.info("Seu alcance: " + personagem.getAlcance() + " células"));
        
        // Validar linha do alvo (-99, -1 a 9)
        int posLinhaAlvo = InputValidator.validarInteiro(teclado, "Entre com a linha do alvo: ", -99, 9);
        
        // Verificar se jogador quer abandonar
        if (posLinhaAlvo == -99) {
            throw new AbandonoPartidaException(personagem.getNome());
        }

        if (posLinhaAlvo == -1) {
            String mensagem = personagem.getNome() + " não atacou";
            System.out.println(ColorUtil.warning(mensagem));
            replayManager.registrarAcao(mensagem);
            return;
        }

        // Validar coluna do alvo (0 a 9)
        int posColunaAlvo = InputValidator.validarInteiro(teclado, "Entre com a coluna do alvo: ", 0, 9);

        if (!tabuleiro.verificaPosicao(posLinhaAlvo, posColunaAlvo)) {
            System.out.println(ColorUtil.error("❌ Posição fora do tabuleiro. Não é possível atacar."));
            System.out.println(ColorUtil.warning("   Dica: As coordenadas válidas são de 0 a 9."));
            return;
        }

        GameCharacter alvo = tabuleiro.getPersonagem(posLinhaAlvo, posColunaAlvo);
       
        if (alvo == null) {
            System.out.println(ColorUtil.error("❌ Não há personagem na posição [" + posLinhaAlvo + "," + posColunaAlvo + "]."));
            System.out.println(ColorUtil.warning("   Dica: Confira o tabuleiro acima para ver onde estão os inimigos."));
            return;
        }

        if (alvo == personagem) {
            System.out.println(ColorUtil.error("❌ Você não pode atacar a si mesmo!"));
            System.out.println(ColorUtil.warning("   Dica: Você está em [" + personagem.getLinha() + "," + personagem.getColuna() + "]."));
            return;
        }

        if (alvo.getCasa() == personagem.getCasa()) {
            System.out.println(ColorUtil.error("❌ Não é possível atacar um aliado da mesma casa!"));
            System.out.println(ColorUtil.warning("   Dica: " + alvo.getNome() + " é da casa " + alvo.getCasa().name() + " assim como você."));
            return;
        }

        // VERIFICAÇÃO DE ALCANCE
        Position posAtacante = new Position(personagem.getLinha(), personagem.getColuna());
        Position posAlvo = new Position(posLinhaAlvo, posColunaAlvo);
        int distancia = Position.calcularDistancia(posAtacante, posAlvo);
        int alcance = personagem.getAlcance();
        
        if (distancia > alcance) {
            System.out.println(ColorUtil.error("❌ Alvo fora do alcance!"));
            System.out.println(ColorUtil.warning(String.format("   Distância até o alvo: %d células", distancia)));
            System.out.println(ColorUtil.warning(String.format("   Seu alcance máximo (%s): %d células", 
                personagem.getCasa().name(), alcance)));
            System.out.println(ColorUtil.info("   Dica: Você precisa se aproximar " + (distancia - alcance) + 
                " célula(s) para atacar este alvo."));
            replayManager.registrarAcao(personagem.getNome() + " tentou atacar " + alvo.getNome() + 
                " mas estava fora do alcance (distância: " + distancia + ", alcance: " + alcance + ")");
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
