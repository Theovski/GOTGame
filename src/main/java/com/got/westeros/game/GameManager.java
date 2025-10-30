package com.got.westeros.game;

import com.got.westeros.entities.*;
import com.got.westeros.players.*;
import com.got.westeros.enums.House;
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
        System.out.println("=== GAME OF THRONES É EXTREMAMENTE MID FORA DOS LIVROS ===");
        
        configurarJogo();
        executarPartida();
        
        System.out.println("\nDeseja ver o replay? (S/N)");
        if (scanner.next().equalsIgnoreCase("S")) {
            replayManager.exibirReplay();
        }
        
        System.out.println("\nDeseja jogar novamente? (S/N)");
        if (scanner.next().equalsIgnoreCase("S")) {
            replayManager.limparHistorico();
            iniciarJogo();
        }
        
        scanner.close();
    }
    
    private void configurarJogo() {
        System.out.println("Escolha o modo de jogo:");
        System.out.println("1 - Humano vs Humano");
        System.out.println("2 - Humano vs Bot");
        
        int modo = scanner.nextInt();
        scanner.nextLine();
        
        // Configurar jogador 1
        List<GameCharacter> time1 = criarTime("Jogador 1");
        jogadores.add(new HumanPlayer("Jogador 1", time1, scanner));
        
        // Configurar jogador 2
        List<GameCharacter> time2;
        if (modo == 1) {
            time2 = criarTime("Jogador 2");
            jogadores.add(new HumanPlayer("Jogador 2", time2, scanner));
        } else {
            time2 = criarTimeBot();
            jogadores.add(new BotPlayer("Bot", time2));
        }
        
        // Posicionar no tabuleiro
        tabuleiro.posicionaPerson(time1.toArray(new GameCharacter[0]), "esquerdo");
        tabuleiro.posicionaPerson(time2.toArray(new GameCharacter[0]), "direito");
    }
    
    private List<GameCharacter> criarTime(String nomeJogador) {
        List<GameCharacter> time = new ArrayList<>();
        System.out.println("\n" + nomeJogador + ", crie seus 3 personagens:");
        
        for (int i = 1; i <= 3; i++) {
            System.out.println("\nPersonagem " + i + ":");
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            
            System.out.println("Casa: (1) STARK, (2) LANNISTER, (3) TARGARYEN");
            int casaEscolha = scanner.nextInt();
            scanner.nextLine();
            
            House casa;
            switch (casaEscolha) {
                case 1: casa = House.STARK; break;
                case 2: casa = House.LANNISTER; break;
                case 3: casa = House.TARGARYEN; break;
                default: 
                    System.out.println("Opção inválida. Usando STARK.");
                    casa = House.STARK;
            }
            
            GameCharacter personagem = criarPersonagem(nome, casa);
            time.add(personagem);
            System.out.println("Personagem criado: " + personagem);
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
        switch (casa) {
            case STARK: return new Stark(nome);
            case LANNISTER: return new Lannister(nome);
            case TARGARYEN: return new Targaryen(nome);
            default: return new Stark(nome);
        }
    }
    
    private void executarPartida() {
        System.out.println("\n=== INÍCIO DA PARTIDA ===");
        
        // Usar o TurnManager existente
        List<GameCharacter> todosPersonagens = new ArrayList<>();
        for (Player jogador : jogadores) {
            todosPersonagens.addAll(jogador.getPersonagens());
        }
        
        TurnManager turnManager = new TurnManager(todosPersonagens, tabuleiro, scanner);
        turnManager.loopTurnos();
        
        declararVencedor();
    }
    
    private boolean jogoContinua() {
        return jogadores.stream().filter(Player::temPersonagensVivos).count() > 1;
    }
    
    private void declararVencedor() {
        Player vencedor = jogadores.stream()
            .filter(Player::temPersonagensVivos)
            .findFirst()
            .orElse(null);
            
        if (vencedor != null) {
            System.out.println("\n🎉 " + vencedor.getNome() + " VENCEU O JOGO! 🎉");
            replayManager.registrarAcao("VITORIA: " + vencedor.getNome());
        } else {
            System.out.println("\nEMPATE!");
        }
    }
}
