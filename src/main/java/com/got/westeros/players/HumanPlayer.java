package com.got.westeros.players;

import com.got.westeros.entities.GameCharacter;
import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends Player {
    
    public HumanPlayer(String nome, List<GameCharacter> personagens, Scanner scanner) {
        super(nome, personagens);
    }
    
    @Override
    public void realizarTurno() {
    }
}
