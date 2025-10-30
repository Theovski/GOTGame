package com.got.westeros.players;

import com.got.westeros.entities.GameCharacter;
import com.got.westeros.entities.Tabuleiro;
import com.got.westeros.utils.Position;
import com.got.westeros.utils.ColorUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Jogador controlado por IA com estratégia básica.
 * Estratégia:
 * 1. Identifica o inimigo mais próximo
 * 2. Se está no alcance → ataca
 * 3. Se não está no alcance → move em direção ao inimigo
 * 4. Se não há inimigos → move aleatoriamente
 */
public class BotPlayer extends Player {
    private Random random;
    private Tabuleiro tabuleiro;
    
    public BotPlayer(String nome, List<GameCharacter> personagens) {
        super(nome, personagens);
        this.random = new Random();
    }
    
    /**
     * Define o tabuleiro para o bot poder tomar decisões
     */
    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    @Override
    public void realizarTurno() {
        // O Bot não executa turno diretamente
        // Isso é gerenciado pelo TurnManager que chama Turno para cada personagem
        // Este método está aqui por compatibilidade com a classe abstrata Player
        System.out.println(ColorUtil.warning("⚠️ BotPlayer.realizarTurno() foi chamado mas não deve ser usado."));
        System.out.println(ColorUtil.info("Use o TurnManager para gerenciar os turnos do bot."));
    }
    
    /**
     * Decide o movimento do bot para um personagem específico.
     * Retorna a nova posição desejada ou null se não quiser mover.
     * 
     * @param personagem Personagem do bot que vai mover
     * @param todosPersonagens Lista de todos os personagens no jogo
     * @return Array [linha, coluna] da nova posição ou null para não mover
     */
    public int[] decidirMovimento(GameCharacter personagem, List<GameCharacter> todosPersonagens) {
        if (tabuleiro == null) {
            System.out.println(ColorUtil.error("❌ Tabuleiro não definido para o bot!"));
            return null;
        }
        
        // Encontra o inimigo mais próximo
        GameCharacter inimigoMaisProximo = encontrarInimigoMaisProximo(personagem, todosPersonagens);
        
        if (inimigoMaisProximo == null) {
            // Sem inimigos, move aleatoriamente
            return moverAleatoriamente(personagem);
        }
        
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        Position posInimigo = new Position(inimigoMaisProximo.getLinha(), inimigoMaisProximo.getColuna());
        
        int distancia = Position.calcularDistancia(posAtual, posInimigo);
        int alcance = personagem.getAlcance();
        
        // Se já está no alcance, não precisa mover (vai atacar depois)
        if (distancia <= alcance) {
            System.out.println(ColorUtil.info("🤖 Bot: " + personagem.getNome() + " já está no alcance, não precisa mover."));
            return null; // Não move
        }
        
        // Move em direção ao inimigo
        Position novaPosicao = Position.moverEmDirecao(posAtual, posInimigo);
        
        // Verifica se a nova posição é válida
        if (!tabuleiro.verificaPosicao(novaPosicao.getLinha(), novaPosicao.getColuna())) {
            // Posição inválida, tenta movimento alternativo
            return moverAleatoriamente(personagem);
        }
        
        // Verifica se a posição está ocupada
        if (tabuleiro.getPersonagem(novaPosicao.getLinha(), novaPosicao.getColuna()) != null) {
            // Posição ocupada, tenta movimento alternativo ao redor do inimigo
            return encontrarPosicaoAlternativa(personagem, posInimigo);
        }
        
        System.out.println(ColorUtil.info("🤖 Bot: " + personagem.getNome() + 
            " move em direção a " + inimigoMaisProximo.getNome()));
        
        return new int[]{novaPosicao.getLinha(), novaPosicao.getColuna()};
    }
    
    /**
     * Decide qual alvo atacar.
     * Escolhe o inimigo mais próximo que esteja no alcance.
     * 
     * @param personagem Personagem do bot que vai atacar
     * @param todosPersonagens Lista de todos os personagens no jogo
     * @return Posição [linha, coluna] do alvo ou null se não houver alvo no alcance
     */
    public int[] decidirAtaque(GameCharacter personagem, List<GameCharacter> todosPersonagens) {
        List<GameCharacter> inimigosNoAlcance = encontrarInimigosNoAlcance(personagem, todosPersonagens);
        
        if (inimigosNoAlcance.isEmpty()) {
            System.out.println(ColorUtil.warning("🤖 Bot: " + personagem.getNome() + " não tem alvos no alcance."));
            return null; // Não ataca
        }
        
        // Escolhe o inimigo com MENOS vida (estratégia: eliminar o mais fraco primeiro)
        GameCharacter alvo = inimigosNoAlcance.stream()
            .min((a, b) -> Integer.compare(a.getVidaAtual(), b.getVidaAtual()))
            .orElse(inimigosNoAlcance.get(0));
        
        System.out.println(ColorUtil.error("🤖 Bot: " + personagem.getNome() + 
            " vai atacar " + alvo.getNome() + " (vida: " + alvo.getVidaAtual() + ")"));
        
        return new int[]{alvo.getLinha(), alvo.getColuna()};
    }
    
    /**
     * Encontra o inimigo mais próximo do personagem
     */
    private GameCharacter encontrarInimigoMaisProximo(GameCharacter personagem, List<GameCharacter> todosPersonagens) {
        Position posAtual = new Position(personagem.getLinha(), personagem.getColuna());
        
        return todosPersonagens.stream()
            .filter(p -> p.isVivo()) // Apenas vivos
            .filter(p -> p.getCasa() != personagem.getCasa()) // Apenas inimigos
            .min((a, b) -> {
                Position posA = new Position(a.getLinha(), a.getColuna());
                Position posB = new Position(b.getLinha(), b.getColuna());
                int distA = Position.calcularDistancia(posAtual, posA);
                int distB = Position.calcularDistancia(posAtual, posB);
                return Integer.compare(distA, distB);
            })
            .orElse(null);
    }
    
    /**
     * Encontra todos os inimigos que estão no alcance de ataque
     */
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
    
    /**
     * Move o personagem para uma posição aleatória adjacente válida
     */
    private int[] moverAleatoriamente(GameCharacter personagem) {
        int linhaAtual = personagem.getLinha();
        int colunaAtual = personagem.getColuna();
        
        // Tenta até 8 vezes encontrar uma posição válida (8 direções possíveis)
        for (int tentativa = 0; tentativa < 8; tentativa++) {
            int deltaLinha = random.nextInt(3) - 1; // -1, 0, ou 1
            int deltaColuna = random.nextInt(3) - 1; // -1, 0, ou 1
            
            // Evita ficar parado
            if (deltaLinha == 0 && deltaColuna == 0) {
                continue;
            }
            
            int novaLinha = linhaAtual + deltaLinha;
            int novaColuna = colunaAtual + deltaColuna;
            
            // Verifica se é válido e está vazio
            if (tabuleiro.verificaPosicao(novaLinha, novaColuna) &&
                tabuleiro.getPersonagem(novaLinha, novaColuna) == null) {
                
                System.out.println(ColorUtil.info("🤖 Bot: " + personagem.getNome() + " move aleatoriamente."));
                return new int[]{novaLinha, novaColuna};
            }
        }
        
        // Não conseguiu mover, fica parado
        System.out.println(ColorUtil.warning("🤖 Bot: " + personagem.getNome() + " não encontrou movimento válido."));
        return null;
    }
    
    /**
     * Encontra uma posição alternativa ao redor de um alvo quando o caminho direto está bloqueado
     */
    private int[] encontrarPosicaoAlternativa(GameCharacter personagem, Position alvo) {
        int linhaAtual = personagem.getLinha();
        int colunaAtual = personagem.getColuna();
        
        // Tenta as 8 direções ao redor
        int[][] direcoes = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };
        
        int melhorDirecao = -1;
        int menorDistancia = Integer.MAX_VALUE;
        
        for (int i = 0; i < direcoes.length; i++) {
            int novaLinha = linhaAtual + direcoes[i][0];
            int novaColuna = colunaAtual + direcoes[i][1];
            
            // Verifica se é válido e está vazio
            if (tabuleiro.verificaPosicao(novaLinha, novaColuna) &&
                tabuleiro.getPersonagem(novaLinha, novaColuna) == null) {
                
                // Calcula distância até o alvo
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
            
            System.out.println(ColorUtil.info("🤖 Bot: " + personagem.getNome() + " contorna obstáculo."));
            return new int[]{novaLinha, novaColuna};
        }
        
        // Não encontrou alternativa, não move
        return null;
    }
}
