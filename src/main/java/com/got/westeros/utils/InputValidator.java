package com.got.westeros.utils;

import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Classe utilitária para validação de inputs do usuário.
 * Garante que apenas valores válidos sejam aceitos, com retry automático.
 */
public class InputValidator {
    
    /**
     * Valida e retorna um número inteiro dentro de um intervalo.
     * Continua pedindo até receber um valor válido.
     * 
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem a ser exibida ao usuário
     * @param min Valor mínimo aceito (inclusivo)
     * @param max Valor máximo aceito (inclusivo)
     * @return Inteiro validado dentro do intervalo
     */
    public static int validarInteiro(Scanner scanner, String mensagem, int min, int max) {
        int valor;
        
        while (true) {
            try {
                System.out.print(mensagem);
                
                // Tenta ler um inteiro
                valor = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer
                
                // Verifica se está no intervalo válido
                if (valor >= min && valor <= max) {
                    return valor;
                } else {
                    System.out.println(ColorUtil.error(
                        "❌ Valor inválido! Digite um número entre " + min + " e " + max + "."
                    ));
                }
                
            } catch (InputMismatchException e) {
                // Usuário digitou algo que não é número
                scanner.nextLine(); // Limpa o buffer
                System.out.println(ColorUtil.error(
                    "❌ Entrada inválida! Por favor, digite apenas números."
                ));
            } catch (Exception e) {
                // Erro inesperado
                scanner.nextLine();
                System.out.println(ColorUtil.error(
                    "❌ Erro inesperado: " + e.getMessage()
                ));
            }
        }
    }
    
    /**
     * Valida e retorna um número inteiro sem limite de intervalo.
     * Continua pedindo até receber um número válido.
     * 
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Inteiro validado
     */
    public static int validarInteiro(Scanner scanner, String mensagem) {
        int valor;
        
        while (true) {
            try {
                System.out.print(mensagem);
                valor = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer
                return valor;
                
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println(ColorUtil.error(
                    "❌ Entrada inválida! Por favor, digite apenas números."
                ));
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println(ColorUtil.error(
                    "❌ Erro inesperado: " + e.getMessage()
                ));
            }
        }
    }
    
    /**
     * Valida e retorna uma string não vazia.
     * Continua pedindo até receber um texto válido.
     * 
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return String validada (não vazia)
     */
    public static String validarString(Scanner scanner, String mensagem) {
        String valor;
        
        while (true) {
            try {
                System.out.print(mensagem);
                valor = scanner.nextLine().trim();
                
                // Verifica se a string não está vazia
                if (!valor.isEmpty()) {
                    return valor;
                } else {
                    System.out.println(ColorUtil.error(
                        "❌ Campo obrigatório! Por favor, digite algo."
                    ));
                }
                
            } catch (Exception e) {
                System.out.println(ColorUtil.error(
                    "❌ Erro ao ler entrada: " + e.getMessage()
                ));
            }
        }
    }
    
    /**
     * Valida string com tamanho mínimo
     * 
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem a ser exibida
     * @param minLength Tamanho mínimo da string
     * @return String validada
     */
    public static String validarString(Scanner scanner, String mensagem, int minLength) {
        String valor;
        
        while (true) {
            valor = validarString(scanner, mensagem);
            
            if (valor.length() >= minLength) {
                return valor;
            } else {
                System.out.println(ColorUtil.error(
                    "❌ O texto deve ter no mínimo " + minLength + " caracteres!"
                ));
            }
        }
    }
    
    /**
     * Valida uma opção de menu (sim/não)
     * Aceita: s, S, sim, SIM, n, N, não, NAO
     * 
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem a ser exibida
     * @return true se sim, false se não
     */
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
                System.out.println(ColorUtil.error(
                    "❌ Opção inválida! Digite 'S' para Sim ou 'N' para Não."
                ));
            }
        }
    }
    
    /**
     * Valida um índice de array (0 a tamanho-1)
     * 
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem a ser exibida
     * @param tamanhoArray Tamanho do array
     * @return Índice validado
     */
    public static int validarIndice(Scanner scanner, String mensagem, int tamanhoArray) {
        if (tamanhoArray <= 0) {
            throw new IllegalArgumentException("Tamanho do array deve ser maior que 0");
        }
        
        return validarInteiro(scanner, mensagem, 0, tamanhoArray - 1);
    }
    
    /**
     * Exibe mensagem de erro formatada
     * 
     * @param mensagem Mensagem de erro
     */
    public static void exibirErro(String mensagem) {
        System.out.println(ColorUtil.error("❌ ERRO: " + mensagem));
    }
    
    /**
     * Exibe mensagem de sucesso formatada
     * 
     * @param mensagem Mensagem de sucesso
     */
    public static void exibirSucesso(String mensagem) {
        System.out.println(ColorUtil.success("✓ " + mensagem));
    }
    
    /**
     * Exibe mensagem de aviso formatada
     * 
     * @param mensagem Mensagem de aviso
     */
    public static void exibirAviso(String mensagem) {
        System.out.println(ColorUtil.warning("⚠ " + mensagem));
    }
    
    /**
     * Pausa e aguarda o usuário pressionar Enter
     */
    public static void aguardarEnter(Scanner scanner) {
        System.out.print(ColorUtil.info("\nPressione ENTER para continuar..."));
        scanner.nextLine();
    }
    
    /**
     * Limpa a tela do console (funciona em Windows e Unix)
     */
    public static void limparTela() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                // Windows - envia comando ANSI para limpar
                System.out.print("\033[H\033[2J");
                System.out.flush();
            } else {
                // Unix/Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Se falhar, imprime várias linhas em branco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}
