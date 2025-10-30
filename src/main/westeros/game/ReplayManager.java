package westeros.game;

import com.got.westeros.utils.ColorUtil;
import com.got.westeros.utils.InputValidator;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Gerenciador de replay do jogo.
 * Registra todas as ações importantes durante a partida e permite visualização posterior.
 */
public class ReplayManager {
    private List<String> acoes;
    private DateTimeFormatter formatoHora;
    private boolean registrarTimestamp;
    
    public ReplayManager() {
        this.acoes = new ArrayList<>();
        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.registrarTimestamp = false; // Desabilitado por padrão para não poluir
    }
    
    /**
     * Habilita/desabilita timestamps nas ações
     */
    public void setRegistrarTimestamp(boolean registrar) {
        this.registrarTimestamp = registrar;
    }
    
    /**
     * Registra uma ação no histórico
     * @param acao Descrição da ação realizada
     */
    public void registrarAcao(String acao) {
        if (registrarTimestamp) {
            String timestamp = LocalTime.now().format(formatoHora);
            acoes.add("[" + timestamp + "] " + acao);
        } else {
            acoes.add(acao);
        }
    }
    
    /**
     * Exibe todas as ações do replay de uma vez
     */
    public void mostrarReplay() {
        if (acoes.isEmpty()) {
            System.out.println(ColorUtil.warning("⚠ Nenhuma ação foi registrada ainda."));
            return;
        }
        
        ColorUtil.printTitle("📜 REPLAY COMPLETO DO JOGO", ColorUtil.BOLD_CYAN);
        System.out.println(ColorUtil.info("Total de ações: " + acoes.size()));
        System.out.println();
        
        for (int i = 0; i < acoes.size(); i++) {
            // Numera as ações começando de 1
            System.out.printf("%3d. %s\n", (i + 1), acoes.get(i));
        }
        
        ColorUtil.printSeparator(ColorUtil.CYAN);
    }
    
    /**
     * Exibe o replay passo a passo, aguardando input do usuário
     * @param scanner Scanner para input do usuário
     */
    public void mostrarReplayPasso(Scanner scanner) {
        if (acoes.isEmpty()) {
            System.out.println(ColorUtil.warning("⚠ Nenhuma ação foi registrada ainda."));
            return;
        }
        
        ColorUtil.printTitle("📜 REPLAY PASSO A PASSO", ColorUtil.BOLD_CYAN);
        System.out.println(ColorUtil.info("Total de ações: " + acoes.size()));
        System.out.println(ColorUtil.warning("Pressione ENTER para avançar ou digite 'S' para sair"));
        ColorUtil.printSeparator(ColorUtil.CYAN);
        
        for (int i = 0; i < acoes.size(); i++) {
            System.out.printf("\n%s Ação %d/%d:%s\n", 
                ColorUtil.BOLD_YELLOW, 
                (i + 1), 
                acoes.size(), 
                ColorUtil.RESET);
            System.out.println(acoes.get(i));
            
            // Aguarda input do usuário
            System.out.print(ColorUtil.info("\n[Enter] Próxima | [S] Sair: "));
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("s") || input.equals("sair")) {
                System.out.println(ColorUtil.warning("Replay interrompido."));
                return;
            }
        }
        
        System.out.println(ColorUtil.success("\n✓ Fim do replay!"));
        ColorUtil.printSeparator(ColorUtil.CYAN);
    }
    
    /**
     * Exibe menu de replay e permite escolher o modo de visualização
     * @param scanner Scanner para input do usuário
     */
    public void menuReplay(Scanner scanner) {
        if (acoes.isEmpty()) {
            System.out.println(ColorUtil.warning("⚠ Nenhuma ação foi registrada ainda."));
            return;
        }
        
        ColorUtil.printTitle("📜 MENU DE REPLAY", ColorUtil.BOLD_CYAN);
        System.out.println("1. Ver replay completo (todas as ações)");
        System.out.println("2. Ver replay passo a passo");
        System.out.println("3. Ver últimas 10 ações");
        System.out.println("4. Voltar");
        
        int opcao = InputValidator.validarInteiro(scanner, "\nEscolha uma opção: ", 1, 4);
        
        switch (opcao) {
            case 1:
                mostrarReplay();
                InputValidator.aguardarEnter(scanner);
                break;
            case 2:
                mostrarReplayPasso(scanner);
                break;
            case 3:
                mostrarUltimasAcoes(10);
                InputValidator.aguardarEnter(scanner);
                break;
            case 4:
                return;
        }
    }
    
    /**
     * Exibe as últimas N ações do replay
     * @param quantidade Número de ações a exibir
     */
    public void mostrarUltimasAcoes(int quantidade) {
        if (acoes.isEmpty()) {
            System.out.println(ColorUtil.warning("⚠ Nenhuma ação foi registrada ainda."));
            return;
        }
        
        int inicio = Math.max(0, acoes.size() - quantidade);
        
        ColorUtil.printTitle("📜 ÚLTIMAS " + quantidade + " AÇÕES", ColorUtil.BOLD_CYAN);
        
        for (int i = inicio; i < acoes.size(); i++) {
            System.out.printf("%3d. %s\n", (i + 1), acoes.get(i));
        }
        
        ColorUtil.printSeparator(ColorUtil.CYAN);
    }
    
    /**
     * Retorna o número total de ações registradas
     */
    public int getTotalAcoes() {
        return acoes.size();
    }
    
    /**
     * Limpa todo o histórico de ações
     */
    public void limparHistorico() {
        acoes.clear();
    }
    
    /**
     * Verifica se há ações registradas
     */
    public boolean temAcoes() {
        return !acoes.isEmpty();
    }
}
