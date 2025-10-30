package westeros.game;

import java.util.ArrayList;
import java.util.List;

public class ReplayManager {
    private List<String> historico;
    
    public ReplayManager() {
        this.historico = new ArrayList<>();
    }
    
    public void registrarAcao(String acao) {
        historico.add(acao);
    }
    
    public void exibirReplay() {
        System.out.println("\n=== REPLAY DO JOGO ===");
        for (int i = 0; i < historico.size(); i++) {
            System.out.println((i + 1) + ": " + historico.get(i));
        }
    }
    
    public void limparHistorico() {
        historico.clear();
    }
}