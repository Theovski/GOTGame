package com.got.westeros.utils;

/**
 * Utilitário para adicionar cores ANSI ao terminal.
 * Cores funcionam em: Windows Terminal, PowerShell Core, terminais Unix/Linux.
 * Não funciona em: CMD antigo do Windows.
 */
public class ColorUtil {
    
    // Códigos ANSI para cores
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    
    // Cores em negrito (mais intensas)
    public static final String BOLD_RED = "\u001B[1;31m";
    public static final String BOLD_GREEN = "\u001B[1;32m";
    public static final String BOLD_YELLOW = "\u001B[1;33m";
    public static final String BOLD_BLUE = "\u001B[1;34m";
    public static final String BOLD_PURPLE = "\u001B[1;35m";
    public static final String BOLD_CYAN = "\u001B[1;36m";
    
    // Background colors
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_CYAN = "\u001B[46m";     // Azul claro para aliados
    public static final String BG_WHITE = "\u001B[47m";    // Branco
    
    // Flag para detectar se o terminal suporta cores
    private static boolean colorSupported = true;
    
    /**
     * Desabilita cores (útil se o terminal não suportar ANSI)
     */
    public static void disableColors() {
        colorSupported = false;
    }
    
    /**
     * Habilita cores novamente
     */
    public static void enableColors() {
        colorSupported = true;
    }
    
    /**
     * Retorna texto colorido
     * @param text Texto a ser colorido
     * @param color Código de cor ANSI
     * @return Texto formatado com cor
     */
    public static String colorize(String text, String color) {
        if (!colorSupported) {
            return text;
        }
        return color + text + RESET;
    }
    
    /**
     * Retorna texto em vermelho (erros)
     */
    public static String error(String text) {
        return colorize(text, BOLD_RED);
    }
    
    /**
     * Retorna texto em verde (sucesso)
     */
    public static String success(String text) {
        return colorize(text, BOLD_GREEN);
    }
    
    /**
     * Retorna texto em amarelo (avisos/informações)
     */
    public static String warning(String text) {
        return colorize(text, BOLD_YELLOW);
    }
    
    /**
     * Retorna texto em azul (informações)
     */
    public static String info(String text) {
        return colorize(text, BOLD_CYAN);
    }
    
    /**
     * Retorna texto colorido pela casa de Game of Thrones
     */
    public static String houseColor(String text, String houseName) {
        switch (houseName.toUpperCase()) {
            case "STARK":
                return colorize(text, BOLD_BLUE);
            case "LANNISTER":
                return colorize(text, BOLD_RED);
            case "TARGARYEN":
                return colorize(text, BOLD_PURPLE);
            default:
                return text;
        }
    }
    
    /**
     * Retorna cor da casa sem texto
     */
    public static String getHouseColor(String houseName) {
        if (!colorSupported) return "";
        
        switch (houseName.toUpperCase()) {
            case "STARK":
                return BOLD_BLUE;
            case "LANNISTER":
                return BOLD_RED;
            case "TARGARYEN":
                return BOLD_PURPLE;
            default:
                return RESET;
        }
    }
    
    /**
     * Imprime linha separadora colorida
     */
    public static void printSeparator(String color) {
        System.out.println(colorize("═══════════════════════════════════════════════════════", color));
    }
    
    /**
     * Imprime título centralizado com cor
     */
    public static void printTitle(String title, String color) {
        printSeparator(color);
        System.out.println(colorize("    " + title, color));
        printSeparator(color);
    }
}
