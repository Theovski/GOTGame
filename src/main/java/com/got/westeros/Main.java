package com.got.westeros;

import com.got.westeros.game.GameManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INCESTO SINISTRO ===");
        
        try {
            GameManager game = new GameManager();
            game.iniciarJogo();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar o jogo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
