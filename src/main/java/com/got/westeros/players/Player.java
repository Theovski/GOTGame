package com.got.westeros.players;

import com.got.westeros.entities.GameCharacter;
import java.util.List;

public abstract class Player {
    protected String nome;
    protected List<GameCharacter> personagens;
    
    public Player(String nome, List<GameCharacter> personagens) {
        this.nome = nome;
        this.personagens = personagens;
    }
    
    public abstract void realizarTurno();
    
    public boolean temPersonagensVivos() {
        return personagens.stream().anyMatch(GameCharacter::isVivo);
    }
    
    public List<GameCharacter> getPersonagens() { return personagens; }
    public String getNome() { return nome; }
}
