package com.got.westeros.utils;

import java.util.Scanner;

public class InputValidator {
    
    public static int validarInteiro(Scanner scanner, String mensagem, int min, int max) {
        int valor;
        
        while (true) {
            System.out.print(mensagem);
            
            if (scanner.hasNextInt()) {
                valor = scanner.nextInt();
                scanner.nextLine();
                
                if (valor >= min && valor <= max) {
                    return valor;
                } else {
                    System.out.println(" Valor invalido! Digite um numero entre " + min + " e " + max + ".");
                }
            } else {
                scanner.nextLine();
                System.out.println("Entrada invalida! Por favor, digite apenas numeros.");
            }
        }
    }

    public static int validarInteiro(Scanner scanner, String mensagem) {
        int valor;
        
        while (true) {
            System.out.print(mensagem);
            if (scanner.hasNextInt()) {
                valor = scanner.nextInt();
                scanner.nextLine();
                return valor;
            } else {
                scanner.nextLine();
                System.out.println("Entrada invalida! Por favor, digite apenas numeros.");
            }
        }
    }

    public static String validarString(Scanner scanner, String mensagem) {
        String valor;
        
        while (true) {
            System.out.print(mensagem);
            valor = scanner.nextLine().trim();
            
            if (!valor.isEmpty()) {
                return valor;
            } else {
                System.out.println("Campo obrigatorio! Por favor, digite algo.");
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
                System.out.println("O texto deve ter no minimo " + minLength + " caracteres!");
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
            } else if (valor.equals("n") || valor.equals("nao") || valor.equals("nao")) {
                return false;
            } else {
                System.out.println("Opcao invalida! Digite 'S' para Sim ou 'N' para Nao.");
            }
        }
    }

    public static int validarIndice(Scanner scanner, String mensagem, int tamanhoArray) {
        if (tamanhoArray <= 0) {
            System.out.println("Tamanho do array deve ser maior que 0");
            return -1;
        }
        
        return validarInteiro(scanner, mensagem, 0, tamanhoArray - 1);
    }
    
    public static void aguardarEnter(Scanner scanner) {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
    
    public static void limparTela() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}
