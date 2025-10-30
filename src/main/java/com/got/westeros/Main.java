package com.got.westeros;

import com.got.westeros.game.GameManager;
import com.got.westeros.utils.ColorUtil;

public class Main {
    public static void main(String[] args) {
        exibirBannerInicial();
        
        try {
            GameManager game = new GameManager();
            game.iniciarJogo();
        } catch (Exception e) {
            System.out.println(ColorUtil.error("\n❌ Erro ao iniciar o jogo: " + e.getMessage()));
            e.printStackTrace();
        }
    }
    
    /**
     * Exibe banner ASCII art colorido no início do jogo
     */
    private static void exibirBannerInicial() {
        ColorUtil.printSeparator(ColorUtil.BOLD_PURPLE);
        System.out.println(ColorUtil.BOLD_PURPLE + 
            "   ____   ___ _____     ____    _    __  __ _____ \n" +
            "  / ___| / _ \\_   _|   / ___|  / \\  |  \\/  | ____|\n" +
            " | |  _ | | | || |    | |  _  / _ \\ | |\\/| |  _|  \n" +
            " | |_| || |_| || |    | |_| |/ ___ \\| |  | | |___ \n" +
            "  \\____| \\___/ |_|     \\____/_/   \\_\\_|  |_|_____|" + 
            ColorUtil.RESET);
        System.out.println(ColorUtil.info("              🐺 Stark  •  🦁 Lannister  •  🐉 Targaryen"));
        ColorUtil.printSeparator(ColorUtil.BOLD_PURPLE);
        System.out.println();
    }
}
