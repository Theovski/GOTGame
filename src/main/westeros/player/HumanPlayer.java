package westeros.players;

import westeros.entities.GameCharacter;
import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;
    
    public HumanPlayer(String nome, List<GameCharacter> personagens, Scanner scanner) {
        super(nome, personagens);
        this.scanner = scanner;
    }
    
    @Override
    public void realizarTurno() {
        // Implementação será feita no TurnManager
    }
}