package com.got.westeros.players;

import com.got.westeros.entities.GameCharacter;
import java.util.List;
import java.util.Random;

public class BotPlayer extends Player {
    private Random random;
    
    public BotPlayer(String nome, List<GameCharacter> personagens) {
        super(nome, personagens);
        this.random = new Random();
    }
    
    @Override
    public void realizarTurno() {
        // Lógica simples do bot
    }
}
