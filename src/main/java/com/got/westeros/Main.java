package com.got.westeros;

import com.got.westeros.game.GameManager;

public class Main {
    public static void main(String[] args) {
        exibirBannerInicial();
        
        try {
            GameManager game = new GameManager();
            game.iniciarJogo();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o jogo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Exibe banner ASCII art colorido no início do jogo
     */
    private static void exibirBannerInicial() {
        System.out.println(
            "   ____   ___ _____     ____    _    __  __ _____ \n" +
            "  / ___| / _ \\_   _|   / ___|  / \\  |  \\/  | ____|\n" +
            " | |  _ | | | || |    | |  _  / _ \\ | |\\/| |  _|  \n" +
            " | |_| || |_| || |    | |_| |/ ___ \\| |  | | |___ \n" +
            "  \\____| \\___/ |_|     \\____/_/   \\_\\_|  |_|_____|");
        System.out.println();
        System.out.println("              Stark  •  Lannister  •  Targaryen");
        System.out.println();
    }
}
