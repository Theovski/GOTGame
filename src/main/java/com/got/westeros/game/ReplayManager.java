package com.got.westeros.game;

import com.got.westeros.utils.InputValidator;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReplayManager {
    private List<String> acoes;
    private DateTimeFormatter formatoHora;
    private boolean registrarTimestamp;
    
    public ReplayManager() {
        this.acoes = new ArrayList<>();
        this.formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.registrarTimestamp = false;
    }
    
    public void setRegistrarTimestamp(boolean registrar) {
        this.registrarTimestamp = registrar;
    }
    
    public void registrarAcao(String acao) {
        if (registrarTimestamp) {
            String timestamp = LocalTime.now().format(formatoHora);
            acoes.add("[" + timestamp + "] " + acao);
        } else {
            acoes.add(acao);
        }
    }
    
    public void mostrarReplay() {
        if (acoes.isEmpty()) {
            System.out.println("Nenhuma ação foi registrada ainda.");
            return;
        }
        
        System.out.println("REPLAY COMPLETO DO JOGO");
        System.out.println("Total de ações: " + acoes.size());
        System.out.println();
        
        for (int i = 0; i < acoes.size(); i++) {
            System.out.printf("%3d. %s\n", (i + 1), acoes.get(i));
        }
    }
    
    public void mostrarReplayPasso(Scanner scanner) {
        if (acoes.isEmpty()) {
            System.out.println("Nenhuma ação foi registrada ainda.");
            return;
        }
        
        System.out.println("REPLAY PASSO A PASSO");
        System.out.println("Total de ações: " + acoes.size());
        System.out.println("Pressione ENTER para avançar ou digite 'S' para sair");
        
        
        for (int i = 0; i < acoes.size(); i++) {
            System.out.printf("\n%s Ação %d/%d:%s\n", (i + 1), acoes.size());
            System.out.println(acoes.get(i));
            
            System.out.print("\n[Enter] Próxima | [S] Sair: ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("s") || input.equals("sair")) {
                System.out.println("Replay interrompido.");
                return;
            }
        }
        
        System.out.println("\nFim do replay!");
    }
    
    public void menuReplay(Scanner scanner) {
        if (acoes.isEmpty()) {
            System.out.println("Nenhuma ação foi registrada ainda.");
            return;
        }

        System.out.println("MENU DE REPLAY");
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

    public void mostrarUltimasAcoes(int quantidade) {
        if (acoes.isEmpty()) {
            System.out.println("Nenhuma ação foi registrada ainda.");
            return;
        }
        
        int inicio = Math.max(0, acoes.size() - quantidade);
        
        System.out.println("ÚLTIMAS " + quantidade + " AÇÕES");
        
        for (int i = inicio; i < acoes.size(); i++) {
            System.out.printf("%3d. %s\n", (i + 1), acoes.get(i));
        }
    }
    
    public int getTotalAcoes() {
        return acoes.size();
    }
    
    public void limparHistorico() {
        acoes.clear();
    }

    public boolean temAcoes() {
        return !acoes.isEmpty();
    }
}
