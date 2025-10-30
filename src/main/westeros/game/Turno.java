package westeros.game;

import westeros.entities.Tabuleiro;
import westeros.entities.GameCharacter;
import java.util.Scanner;

public class Turno {
    private GameCharacter personagem;
    private Tabuleiro tabuleiro;
    private Scanner teclado;

    public Turno(GameCharacter personagem, Tabuleiro tabuleiro, Scanner teclado) {
        this.personagem = personagem;
        this.tabuleiro = tabuleiro;
        this.teclado = teclado;
    }

    public void executarTurno() {
        System.out.println("Vez do " + personagem.toString());
        
        tabuleiro.exibirTabuleiro();

        realizarMovimento();
        tabuleiro.exibirTabuleiro();

        realizaAtaque();
        tabuleiro.exibirTabuleiro();

        System.out.println("Fim da vez do " + personagem.getNome());
    }

    private void realizarMovimento() {
        System.out.println("Fase de Movimento. Posição atual: [" + personagem.getLinha() + ", " + personagem.getColuna() + "]");
        System.out.print("Digite a linha para mover (ou -1 para pular): ");
        int novaLinha = teclado.nextInt();
        
        if (novaLinha == -1) {
            System.out.println(personagem.getNome() + " decidiu não mover.");
            return;
        }

        System.out.print("Digite a coluna para mover: ");
        int novaColuna = teclado.nextInt();

        boolean moveu = tabuleiro.moverPersonagem(personagem, novaLinha, novaColuna);
        
        if (!moveu) {
            System.out.println("Movimento falhou. O personagem permanece em [" + personagem.getLinha() + ", " + personagem.getColuna() + "]");
        }
    }

    private void realizaAtaque() {
        System.out.println("Fase de Ataque.");
        System.out.println("Entre com a posicao da linha do alvo: ");
        int posLinhaAlvo = teclado.nextInt();

        if (posLinhaAlvo == -1) {
            System.out.println("Nao houve ataque.");
            return;
        }

        System.out.println("Entre com a posicao da coluna do alvo: ");
        int posColunaAlvo = teclado.nextInt();

        if (!tabuleiro.verificaPosicao(posLinhaAlvo, posColunaAlvo)) {
            System.out.println("Posicao fora do tabuleiro. Nao eh possivel atacar");
            return;
        }

        GameCharacter alvo = tabuleiro.getPersonagem(posLinhaAlvo, posColunaAlvo);
       
        if (alvo == null) {
            System.out.println("Nao ha personagem na posicao. Nao eh possivel atacar");
            return;
        }

        if (alvo == personagem) {
            System.out.println("Ataque impossivel.");
            return;
        }

        if (alvo.getCasa() == personagem.getCasa()) {
            System.out.println("Ataque falhou. Nao eh possivel atacar um aliado.");
            return;
        }

        // execucao do ataque
        double danoCausado = personagem.calcularDano(alvo);
        System.out.println(personagem.getNome() + " ataca " + alvo.getNome());

        alvo.receberDano(danoCausado);
        System.out.println(alvo.getNome() + " sofreu " + String.format("%.1f", danoCausado) + " de dano.");
        
        if (!alvo.isVivo()) {
            System.out.println(alvo.getNome() + " foi derrotado.");
            tabuleiro.removerPersonagem(alvo);
        } else {
            System.out.println(alvo.getNome() + " ficou com " + alvo.getVidaAtual() + " de vida.");
        }
    }
}