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
            System.out.println("Nenhuma acao foi registrada ainda.");
            return;
        }
        
        System.out.println("REPLAY COMPLETO DO JOGO");
        System.out.println("Total de acoes: " + acoes.size());
        System.out.println();
        
        for (int i = 0; i < acoes.size(); i++) {
            System.out.println((i + 1) + ". " + acoes.get(i));
        }
    }
    
    public void mostrarReplayPasso(Scanner scanner) {
        if (acoes.isEmpty()) {
            System.out.println("Nenhuma acao foi registrada ainda.");
            return;
        }
        
        System.out.println("REPLAY PASSO A PASSO");
        System.out.println("Total de acoes: " + acoes.size());
        System.out.println("Pressione ENTER para avancar ou digite 'S' para sair");
        
        
        for (int i = 0; i < acoes.size(); i++) {
            System.out.println("\nAcao " + (i + 1) + "/" + acoes.size() + ":");
            System.out.println(acoes.get(i));
            
            System.out.print("\n[Enter] Proxima | [S] Sair: ");
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
            System.out.println("Nenhuma acao foi registrada ainda.");
            return;
        }

        System.out.println("MENU DE REPLAY");
        System.out.println("1. Ver replay completo (todas as acoes)");
        System.out.println("2. Ver replay passo a passo");
        System.out.println("3. Ver ultimas 10 acoes");
        System.out.println("4. Voltar");
        
        int opcao = InputValidator.validarInteiro(scanner, "\nEscolha uma opcao: ", 1, 4);
        
        if (opcao == 1) {
            mostrarReplay();
            InputValidator.aguardarEnter(scanner);
        } else if (opcao == 2) {
            mostrarReplayPasso(scanner);
        } else if (opcao == 3) {
            mostrarUltimasAcoes(10);
            InputValidator.aguardarEnter(scanner);
        }
    }

    public void mostrarUltimasAcoes(int quantidade) {
        if (acoes.isEmpty()) {
            System.out.println("Nenhuma acao foi registrada ainda.");
            return;
        }
        
        int inicio = acoes.size() - quantidade;
        if (inicio < 0) {
            inicio = 0;
        }
        
        System.out.println("ULTIMAS " + quantidade + " ACOES");
        
        for (int i = inicio; i < acoes.size(); i++) {
            System.out.println((i + 1) + ". " + acoes.get(i));
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
