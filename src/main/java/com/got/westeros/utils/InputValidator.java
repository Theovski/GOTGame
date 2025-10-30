package com.got.westeros.utils;

import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Classe para garantir que apenas valores válidos sejam aceitos
 */

public class InputValidator {
    
    public static int validarInteiro(Scanner scanner, String mensagem, int min, int max) {
        int valor;
        
        while (true) {
            try {
                System.out.print(mensagem);
                
                valor = scanner.nextInt();
                scanner.nextLine();
                
                if (valor >= min && valor <= max) {
                    return valor;
                } else {
                    System.out.println(" Valor inválido! Digite um número entre " + min + " e " + max + ".");
                }
                
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Entrada inválida! Por favor, digite apenas números.");
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    public static int validarInteiro(Scanner scanner, String mensagem) {
        int valor;
        
        while (true) {
            try {
                System.out.print(mensagem);
                valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
                
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Entrada inválida! Por favor, digite apenas números.");
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }
    }

    public static String validarString(Scanner scanner, String mensagem) {
        String valor;
        
        while (true) {
            try {
                System.out.print(mensagem);
                valor = scanner.nextLine().trim();
                
                if (!valor.isEmpty()) {
                    return valor;
                } else {
                    System.out.println("Campo obrigatório! Por favor, digite algo.");
                }
                
            } catch (Exception e) {
                System.out.println("Erro ao ler entrada: " + e.getMessage());
            }
        }
    }
    
    public static String validarString(Scanner scanner, String mensagem, int minLength) {
        String valor;
        
        while (true) {
            valor = validarString(scanner, mensagem);
            
            if (valor.length() >= minLength) {
                return valor;
            } else {
                System.out.println("O texto deve ter no mínimo " + minLength + " caracteres!");
            }
        }
    }
    
    public static boolean validarSimNao(Scanner scanner, String mensagem) {
        String valor;
        
        while (true) {
            System.out.print(mensagem + " (S/N): ");
            valor = scanner.nextLine().trim().toLowerCase();
            
            if (valor.equals("s") || valor.equals("sim")) {
                return true;
            } else if (valor.equals("n") || valor.equals("nao") || valor.equals("não")) {
                return false;
            } else {
                System.out.println("Opção inválida! Digite 'S' para Sim ou 'N' para Não.");
            }
        }
    }

    public static int validarIndice(Scanner scanner, String mensagem, int tamanhoArray) {
        if (tamanhoArray <= 0) {
            throw new IllegalArgumentException("Tamanho do array deve ser maior que 0");
        }
        
        return validarInteiro(scanner, mensagem, 0, tamanhoArray - 1);
    }
    
    public static void exibirErro(String mensagem) {
        System.out.println(mensagem);
    }

    public static void exibirSucesso(String mensagem) {
        System.out.println(mensagem);
    }
    
    public static void exibirAviso(String mensagem) {
        System.out.println(mensagem);
    }
    
    public static void aguardarEnter(Scanner scanner) {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    public static void limparTela() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
