package com.got.westeros.players;

import com.got.westeros.entities.GameCharacter;
import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.utils.Position;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class BotPlayer extends Player {
    private Random random;
    private Tabuleiro tabuleiro;
    
    public BotPlayer(String nome, List<GameCharacter> personagens) {
        super(nome, personagens);
        this.random = new Random();
    }
    
    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    @Override
    public void realizarTurno() {
        System.out.println(" BotPlayer.realizarTurno() foi chamado mas nao deve ser usado.");
        System.out.println("Use o TurnManager para gerenciar os turnos do bot.");
    }
    
    public int[] decidirMovimento(GameCharacter personagem, List<GameCharacter> todosPersonagens) {
        if (tabuleiro == null) {
            System.out.println("Tabuleiro nao definido para o bot!");
            return null;
        }
        
        GameCharacter inimigoMaisProximo = encontrarInimigoMaisProximo(personagem, todosPersonagens);
        
        if (inimigoMaisProximo == null) {
            return moverAleatoriamente(personagem);
        }
        
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        Position posInimigo = new Position(inimigoMaisProximo.getLinha(), inimigoMaisProximo.getColuna());
        
        int distancia = Position.calcularDistancia(posAtual, posInimigo);
        int alcance = personagem.getAlcance();
        
        if (distancia <= alcance) {
            System.out.println("Bot: " + personagem.getNome() + " ja esta no alcance, nao precisa mover.");
            return null;
        }

        Position novaPosicao = Position.moverEmDirecao(posAtual, posInimigo);
        
        if (!tabuleiro.verificaPosicao(novaPosicao.getLinha(), novaPosicao.getColuna())) {
            return moverAleatoriamente(personagem);
        }
        
        if (tabuleiro.getPersonagem(novaPosicao.getLinha(), novaPosicao.getColuna()) != null) {
            return encontrarPosicaoAlternativa(personagem, posInimigo);
        }
        
        System.out.println("Bot: " + personagem.getNome() + 
            " move em direcao a " + inimigoMaisProximo.getNome());
        
        return new int[]{novaPosicao.getLinha(), novaPosicao.getColuna()};
    }

    public int[] decidirAtaque(GameCharacter personagem, List<GameCharacter> todosPersonagens) {
        List<GameCharacter> inimigosNoAlcance = encontrarInimigosNoAlcance(personagem, todosPersonagens);
        
        if (inimigosNoAlcance.isEmpty()) {
            System.out.println("Bot: " + personagem.getNome() + " nao tem alvos no alcance.");
            return null;
        }
        
        GameCharacter alvo = inimigosNoAlcance.get(0);
        for (GameCharacter p : inimigosNoAlcance) {
            if (p.getVidaAtual() < alvo.getVidaAtual()) {
                alvo = p;
            }
        }
        
        System.out.println("Bot: " + personagem.getNome() + 
            " vai atacar " + alvo.getNome() + " (vida: " + alvo.getVidaAtual() + ")");
        
        return new int[]{alvo.getLinha(), alvo.getColuna()};
    }

    private GameCharacter encontrarInimigoMaisProximo(GameCharacter personagem, List<GameCharacter> todosPersonagens) {
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        
        GameCharacter maisProximo = null;
        int menorDistancia = 9999;
        
        for (GameCharacter p : todosPersonagens) {
            if (p.isVivo() && p.getCasa() != personagem.getCasa()) {
                Position posInimigo = new Position(p.getLinha(), p.getColuna());
                int distancia = Position.calcularDistancia(posAtual, posInimigo);
                
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    maisProximo = p;
                }
            }
        }
        
        return maisProximo;
    }
    
    private List<GameCharacter> encontrarInimigosNoAlcance(GameCharacter personagem, List<GameCharacter> todosPersonagens) {
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        int alcance = personagem.getAlcance();
        
        List<GameCharacter> inimigosNoAlcance = new ArrayList<>();
        
        for (GameCharacter p : todosPersonagens) {
            if (p.isVivo() && p.getCasa() != personagem.getCasa()) {
                Position posInimigo = new Position(p.getLinha(), p.getColuna());
                if (Position.estaNoAlcance(posAtual, posInimigo, alcance)) {
                    inimigosNoAlcance.add(p);
                }
            }
        }
        
        return inimigosNoAlcance;
    }

    private int[] moverAleatoriamente(GameCharacter personagem) {
        int linhaAtual = personagem.getLinha();
        int colunaAtual = personagem.getColuna();
        
        for (int tentativa = 0; tentativa < 8; tentativa++) {
            int deltaLinha = random.nextInt(3) - 1;
            int deltaColuna = random.nextInt(3) - 1;
            
            if (deltaLinha == 0 && deltaColuna == 0) {
                continue;
            }
            
            int novaLinha = linhaAtual + deltaLinha;
            int novaColuna = colunaAtual + deltaColuna;
            
            if (tabuleiro.verificaPosicao(novaLinha, novaColuna) &&
                tabuleiro.getPersonagem(novaLinha, novaColuna) == null) {
                
                System.out.println("Bot: " + personagem.getNome() + " move aleatoriamente.");
                return new int[]{novaLinha, novaColuna};
            }
        }
        
        System.out.println("Bot: " + personagem.getNome() + " nao encontrou movimento valido.");
        return null;
    }

    private int[] encontrarPosicaoAlternativa(GameCharacter personagem, Position alvo) {
        int linhaAtual = personagem.getLinha();
        int colunaAtual = personagem.getColuna();
        
        int[][] direcoes = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };
        
        int melhorDirecao = -1;
        int menorDistancia = 9999;
        
        for (int i = 0; i < direcoes.length; i++) {
            int novaLinha = linhaAtual + direcoes[i][0];
            int novaColuna = colunaAtual + direcoes[i][1];
            
            if (tabuleiro.verificaPosicao(novaLinha, novaColuna) &&
                tabuleiro.getPersonagem(novaLinha, novaColuna) == null) {
                
                Position novaPosicao = new Position(novaLinha, novaColuna);
                int distancia = Position.calcularDistancia(novaPosicao, alvo);
                
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    melhorDirecao = i;
                }
            }
        }
        
        if (melhorDirecao != -1) {
            int novaLinha = linhaAtual + direcoes[melhorDirecao][0];
            int novaColuna = colunaAtual + direcoes[melhorDirecao][1];
            
            System.out.println("Bot: " + personagem.getNome() + " contorna obstaculo.");
            return new int[]{novaLinha, novaColuna};
        }
        
        return null;
    }
}
