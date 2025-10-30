package com.got.westeros.game;

import com.got.westeros.entities.*;
import com.got.westeros.players.*;
import com.got.westeros.enums.House;
import com.got.westeros.utils.ColorUtil;
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
        // Banner colorido do jogo
        ColorUtil.printTitle("⚔️  BATALHA DAS CASAS DE WESTEROS  ⚔️", ColorUtil.BOLD_PURPLE);
        System.out.println(ColorUtil.info("        Um jogo tático inspirado em Game of Thrones"));
        ColorUtil.printSeparator(ColorUtil.PURPLE);
        System.out.println();
        
        configurarJogo();
        executarPartida();
        
        System.out.println();
        if (InputValidator.validarSimNao(scanner, ColorUtil.info("\nDeseja ver o replay?"))) {
            replayManager.menuReplay(scanner);
        }
        
        if (InputValidator.validarSimNao(scanner, ColorUtil.info("\nDeseja jogar novamente?"))) {
            jogadores.clear();
            replayManager.limparHistorico();
            iniciarJogo();
        } else {
            ColorUtil.printTitle("OBRIGADO POR JOGAR!", ColorUtil.BOLD_GREEN);
        }
        
        scanner.close();
    }
    
    private void configurarJogo() {
        ColorUtil.printTitle("CONFIGURAÇÃO DO JOGO", ColorUtil.BOLD_CYAN);
        
        // Perguntar se quer ver tutorial
        if (InputValidator.validarSimNao(scanner, ColorUtil.info("Deseja ver o tutorial sobre as casas?"))) {
            exibirTutorialCasas();
        }
        
        System.out.println();
        System.out.println(ColorUtil.info("Escolha o modo de jogo:"));
        System.out.println("  1 - Humano vs Humano");
        System.out.println("  2 - Humano vs Bot");
        System.out.println();
        
        int modo = InputValidator.validarInteiro(scanner, "  Escolha: ", 1, 2);
        
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
        
        ColorUtil.printSeparator(ColorUtil.CYAN);
        System.out.println(ColorUtil.info("  " + nomeJogador + ", crie seus 3 personagens:"));
        ColorUtil.printSeparator(ColorUtil.CYAN);
        
        for (int i = 1; i <= 3; i++) {
            System.out.println(ColorUtil.warning("\n▶ Personagem " + i + ":"));
            
            // Validar nome (não vazio, mínimo 2 caracteres)
            String nome = InputValidator.validarString(scanner, "  Nome: ", 2);
            
            System.out.println("  Casa: ");
            System.out.println("    " + ColorUtil.houseColor("1", "STARK") + " - STARK (Defesa, Alcance: 1)");
            System.out.println("    " + ColorUtil.houseColor("2", "LANNISTER") + " - LANNISTER (Ataque, Alcance: 3)");
            System.out.println("    " + ColorUtil.houseColor("3", "TARGARYEN") + " - TARGARYEN (Poder, Alcance: 5)");
            
            // Validar escolha de casa (1-3)
            int casaEscolha = InputValidator.validarInteiro(scanner, "  Escolha: ", 1, 3);
            
            House casa;
            switch (casaEscolha) {
                case 1: casa = House.STARK; break;
                case 2: casa = House.LANNISTER; break;
                case 3: casa = House.TARGARYEN; break;
                default: 
                    System.out.println(ColorUtil.warning("  ⚠️ Opção inválida. Usando STARK."));
                    casa = House.STARK;
            }
            
            GameCharacter personagem = criarPersonagem(nome, casa);
            time.add(personagem);
            
            String nomeColorido = ColorUtil.houseColor(personagem.getNome(), casa.name());
            System.out.println(ColorUtil.success("  ✓ Personagem criado: " + nomeColorido + 
                " (" + casa.name() + ") - Vida: " + casa.getVidaMaxima()));
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
        System.out.println();
        ColorUtil.printTitle("⚔️ INÍCIO DA PARTIDA ⚔️", ColorUtil.BOLD_RED);
        
        System.out.println(ColorUtil.info("Posicionando personagens no tabuleiro..."));
        System.out.println();
        
        // Coletar todos os personagens
        List<GameCharacter> todosPersonagens = new ArrayList<>();
        for (Player jogador : jogadores) {
            todosPersonagens.addAll(jogador.getPersonagens());
        }
        
        // Criar TurnManager
        TurnManager turnManager = new TurnManager(todosPersonagens, tabuleiro, scanner);
        
        // Registrar equipes e bots no TurnManager
        for (Player jogador : jogadores) {
            List<GameCharacter> equipe = jogador.getPersonagens();
            
            // Registra a equipe para cada personagem deste jogador
            for (GameCharacter personagem : equipe) {
                turnManager.registrarEquipe(personagem, equipe);
            }
            
            // Se for bot, registra também no mapa de bots
            if (jogador instanceof BotPlayer) {
                BotPlayer bot = (BotPlayer) jogador;
                System.out.println(ColorUtil.warning("🤖 Jogador Bot detectado - IA ativada!"));
                // Registra cada personagem do bot
                for (GameCharacter personagem : bot.getPersonagens()) {
                    turnManager.registrarBot(personagem, bot);
                }
            }
        }
        
        // Usar o ReplayManager do TurnManager (não o duplicado do GameManager)
        this.replayManager = turnManager.getReplayManager();
        
        System.out.println();
        ColorUtil.printSeparator(ColorUtil.GREEN);
        System.out.println(ColorUtil.success("✓ Tudo pronto! A batalha começa agora!"));
        ColorUtil.printSeparator(ColorUtil.GREEN);
        
        // Executar o jogo
        turnManager.loopTurnos();
        
        declararVencedor();
    }
    
    private void declararVencedor() {
        Player vencedor = jogadores.stream()
            .filter(Player::temPersonagensVivos)
            .findFirst()
            .orElse(null);
            
        if (vencedor != null) {
            System.out.println();
            ColorUtil.printTitle("🎉 " + vencedor.getNome() + " VENCEU! 🎉", ColorUtil.BOLD_GREEN);
            
            // Mostra personagens sobreviventes
            System.out.println(ColorUtil.success("\n  Personagens sobreviventes:"));
            for (GameCharacter p : vencedor.getPersonagens()) {
                if (p.isVivo()) {
                    String nomeColorido = ColorUtil.houseColor(p.getNome(), p.getCasa().name());
                    System.out.println("    • " + nomeColorido + 
                        " - Vida: " + p.getVidaAtual() + "/" + p.getCasa().getVidaMaxima());
                }
            }
            
            replayManager.registrarAcao("🏆 VITÓRIA: " + vencedor.getNome());
        } else {
            ColorUtil.printTitle("⚔️ EMPATE ⚔️", ColorUtil.BOLD_YELLOW);
            System.out.println(ColorUtil.warning("  Todos os personagens foram derrotados!"));
        }
        
        ColorUtil.printSeparator(ColorUtil.GREEN);
    }
    
    /**
     * Exibe tutorial explicando as características de cada casa
     */
    private void exibirTutorialCasas() {
        System.out.println();
        ColorUtil.printTitle("📚 TUTORIAL DAS CASAS DE WESTEROS", ColorUtil.BOLD_PURPLE);
        
        // Casa STARK
        System.out.println(ColorUtil.houseColor("\n🐺 CASA STARK - \"O Inverno Está Chegando\"", "STARK"));
        System.out.println("  • Especialidade: " + ColorUtil.info("DEFESA"));
        System.out.println("  • Vida: 60 HP (a mais alta!)");
        System.out.println("  • Alcance: 1 célula (corpo a corpo)");
        System.out.println("  • Habilidade: Reduz 20% do dano recebido");
        System.out.println("  • Estilo: Tanque defensivo, aguenta muito dano");
        System.out.println("  • Estratégia: Aproxime-se do inimigo e resista aos ataques");
        
        // Casa LANNISTER
        System.out.println(ColorUtil.houseColor("\n🦁 CASA LANNISTER - \"Ouça-me Rugir\"", "LANNISTER"));
        System.out.println("  • Especialidade: " + ColorUtil.info("ATAQUE"));
        System.out.println("  • Vida: 50 HP (equilibrada)");
        System.out.println("  • Alcance: 3 células (médio alcance)");
        System.out.println("  • Habilidade: +15% de dano em todos os ataques");
        System.out.println("  • Estilo: Lutador balanceado e versátil");
        System.out.println("  • Estratégia: Mantenha distância média e cause muito dano");
        
        // Casa TARGARYEN
        System.out.println(ColorUtil.houseColor("\n🐉 CASA TARGARYEN - \"Fogo e Sangue\"", "TARGARYEN"));
        System.out.println("  • Especialidade: " + ColorUtil.info("ALCANCE"));
        System.out.println("  • Vida: 45 HP (a mais baixa)");
        System.out.println("  • Alcance: 5 células (longo alcance!)");
        System.out.println("  • Habilidade: Ignora toda a defesa do inimigo");
        System.out.println("  • Estilo: Atirador de elite, vidro de canhão");
        System.out.println("  • Estratégia: Ataque de longe, evite combate próximo");
        
        System.out.println();
        ColorUtil.printSeparator(ColorUtil.PURPLE);
        System.out.println(ColorUtil.warning("💡 DICAS IMPORTANTES:"));
        System.out.println("  • O movimento é limitado a 1 célula por turno (8 direções)");
        System.out.println("  • Distância é medida pela maior diferença (Chebyshev)");
        System.out.println("  • Exemplo: Diagonal [0,0] → [1,1] = 1 célula de distância");
        System.out.println("  • Crie um time balanceado com diferentes casas!");
        ColorUtil.printSeparator(ColorUtil.PURPLE);
        
        InputValidator.aguardarEnter(scanner);
    }
}
