package com.got.westeros.game;

import com.got.westeros.entities.*;
import com.got.westeros.players.*;
import com.got.westeros.enums.House;
import com.got.westeros.utils.InputValidator;
import java.util.*;

public class GameManager {
    private Scanner scanner;
    private Tabuleiro tabuleiro;
    private List<Player> jogadores;
    private ReplayManager replayManager;
    
    public GameManager() {
        this.scanner = new Scanner(System.in);
        this.tabuleiro = new Tabuleiro();
        this.jogadores = new ArrayList<>();
        this.replayManager = new ReplayManager();
    }
    
    public void iniciarJogo() {
        System.out.println("BATALHA DAS CASAS DE WESTEROS");
        System.out.println("        Um jogo tatico inspirado em Game of Thrones");
    
        System.out.println();
        
        configurarJogo();
        executarPartida();
        
        System.out.println();
        if (InputValidator.validarSimNao(scanner,"\nDeseja ver o replay?")) {
            replayManager.menuReplay(scanner);
        }
        
        if (InputValidator.validarSimNao(scanner,"\nDeseja jogar novamente?")) {
            jogadores.clear();
            replayManager.limparHistorico();
            iniciarJogo();
        } else {
            System.out.println("OBRIGADO POR JOGAR!");
        }
        
        scanner.close();
    }
    
    private void configurarJogo() {
        System.out.println("CONFIGURAÇÃO DO JOGO");
        
        if (InputValidator.validarSimNao(scanner, "Deseja ver o tutorial sobre as casas?")) {
            exibirTutorialCasas();
        }
        
        System.out.println();
        System.out.println("Escolha o modo de jogo:");
        System.out.println("  1 - Humano vs Humano");
        System.out.println("  2 - Humano vs Bot");
        System.out.println();
        
        int modo = InputValidator.validarInteiro(scanner, "  Escolha: ", 1, 2);
        
        List<GameCharacter> time1 = criarTime("Jogador 1");
        jogadores.add(new HumanPlayer("Jogador 1", time1, scanner));
        
        List<GameCharacter> time2;
        if (modo == 1) {
            time2 = criarTime("Jogador 2");
            jogadores.add(new HumanPlayer("Jogador 2", time2, scanner));
        } else {
            time2 = criarTimeBot();
            jogadores.add(new BotPlayer("Bot", time2));
        }
        
        tabuleiro.posicionaPerson(time1.toArray(new GameCharacter[0]), "esquerdo");
        tabuleiro.posicionaPerson(time2.toArray(new GameCharacter[0]), "direito");
    }
    
    private List<GameCharacter> criarTime(String nomeJogador) {
        List<GameCharacter> time = new ArrayList<>();
        
        System.out.println("  " + nomeJogador + ", crie seus 3 personagens:");
        
        for (int i = 1; i <= 3; i++) {
            System.out.println("\n Personagem " + i + ":");
            
            String nome = InputValidator.validarString(scanner, "  Nome: ", 2);
            
            System.out.println("  Casa: ");
            System.out.println("    1 STARK (Defesa, Alcance: 1)");
            System.out.println("    2 LANNISTER (Ataque, Alcance: 3)");
            System.out.println("    3 TARGARYEN (Poder, Alcance: 5)");
            
            int casaEscolha = InputValidator.validarInteiro(scanner, "  Escolha: ", 1, 3);
            
            House casa;
            if (casaEscolha == 1) {
                casa = House.STARK;
            } else if (casaEscolha == 2) {
                casa = House.LANNISTER;
            } else if (casaEscolha == 3) {
                casa = House.TARGARYEN;
            } else {
                System.out.println("  Opcao invalida. Usando STARK.");
                casa = House.STARK;
            }
            
            GameCharacter personagem = criarPersonagem(nome, casa);
            time.add(personagem);
            
            String charName = personagem.getNome();
            System.out.println("  " + charName + 
                " (" + casa.name() + ") - Vida: " + casa.getVidaMaxima());
        }
        
        return time;
    }
    
    private List<GameCharacter> criarTimeBot() {
        List<GameCharacter> time = new ArrayList<>();
        String[] nomesBot = {"Bot-Stark", "Bot-Lannister", "Bot-Targaryen"};
        House[] casas = {House.STARK, House.LANNISTER, House.TARGARYEN};
        
        for (int i = 0; i < 3; i++) {
            time.add(criarPersonagem(nomesBot[i], casas[i]));
        }
        
        return time;
    }
    
    private GameCharacter criarPersonagem(String nome, House casa) {
        if (casa == House.STARK) {
            return new Stark(nome);
        } else if (casa == House.LANNISTER) {
            return new Lannister(nome);
        } else if (casa == House.TARGARYEN) {
            return new Targaryen(nome);
        }
        return new Stark(nome);
    }
    
    private void executarPartida() {
        System.out.println();
        System.out.println("INICIO DA PARTIDA");
        
        System.out.println("Posicionando personagens no tabuleiro...");
        System.out.println();
        
        List<GameCharacter> todosPersonagens = new ArrayList<>();
        for (Player jogador : jogadores) {
            todosPersonagens.addAll(jogador.getPersonagens());
        }
        
        TurnManager turnManager = new TurnManager(todosPersonagens, tabuleiro, scanner);
        
        for (Player jogador : jogadores) {
            List<GameCharacter> equipe = jogador.getPersonagens();
            
            for (GameCharacter personagem : equipe) {
                turnManager.registrarEquipe(personagem, equipe);
            }
            
            if (jogador instanceof BotPlayer) {
                BotPlayer bot = (BotPlayer) jogador;
                System.out.println("Jogador Bot detectado - IA ativada!");
                for (GameCharacter personagem : bot.getPersonagens()) {
                    turnManager.registrarBot(personagem, bot);
                }
            }
        }

        this.replayManager = turnManager.getReplayManager();
        
        System.out.println();
        System.out.println("Tudo pronto! A batalha comeca agora!");
        
        turnManager.loopTurnos();
        
        declararVencedor();
    }
    
    private void declararVencedor() {
        Player vencedor = null;
        for (Player p : jogadores) {
            if (p.temPersonagensVivos()) {
                vencedor = p;
                break;
            }
        }
            
        if (vencedor != null) {
            System.out.println();
           System.out.println(vencedor.getNome() + " VENCEU!");
            
            System.out.println("\n  Personagens sobreviventes:");
            for (GameCharacter p : vencedor.getPersonagens()) {
                if (p.isVivo()) {
                    String charName = p.getNome();
            System.out.println("  " + charName + 
                " (" + p.getCasa().name() + ") - Vida: " + p.getVidaAtual() + "/" + p.getCasa().getVidaMaxima());
                }
            }
            
            replayManager.registrarAcao("VITORIA: " + vencedor.getNome());
        } else {
            System.out.println("EMPATE");
            System.out.println("  Todos os personagens foram derrotados!");
        }
    }
    
    private void exibirTutorialCasas() {
        System.out.println();
        System.out.println("TUTORIAL DAS CASAS DE WESTEROS");
        
        System.out.println("\n CASA STARK - O Inverno Esta Chegando");
        System.out.println("  Especialidade: DEFESA");
        System.out.println("  Vida: 60 HP (a mais alta!)");
        System.out.println("  Alcance: 1 celula (corpo a corpo)");
        System.out.println("  Habilidade: Reduz 20% do dano recebido");
        System.out.println("  Estilo: Tanque defensivo, aguenta muito dano");
        System.out.println("  Estrategia: Aproxime-se do inimigo e resista aos ataques");
        
        System.out.println("\n CASA LANNISTER - Ouca-me Rugir");
        System.out.println("  Especialidade: ATAQUE");
        System.out.println("  Vida: 50 HP (equilibrada)");
        System.out.println("  Alcance: 3 celulas (medio alcance)");
        System.out.println("  Habilidade: +15% de dano em todos os ataques");
        System.out.println("  Estilo: Lutador balanceado e versatil");
        System.out.println("  Estrategia: Mantenha distancia media e cause muito dano");
        
        System.out.println("\n CASA TARGARYEN - Fogo e Sangue");
        System.out.println("  Especialidade: ALCANCE");
        System.out.println("  Vida: 45 HP (a mais baixa)");
        System.out.println("  Alcance: 5 celulas (longo alcance!)");
        System.out.println("  Habilidade: Ignora toda a defesa do inimigo");
        System.out.println("  Estilo: Atirador de elite, vidro de canhao");
        System.out.println("  Estrategia: Ataque de longe, evite combate proximo");
        
        System.out.println();
        System.out.println("DICAS IMPORTANTES:");
        System.out.println("  O movimento e limitado a 1 celula por turno (8 direcoes)");
        System.out.println("  Distancia e medida pela maior diferenca (Chebyshev)");
        System.out.println("  Exemplo: Diagonal [0,0] para [1,1] = 1 celula de distancia");
        System.out.println("  Crie um time balanceado com diferentes casas!");
        
        InputValidator.aguardarEnter(scanner);
    }
}
