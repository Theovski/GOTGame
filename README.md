# GOT Game - Game of Thrones Strategy Game

src/main/java/com/got/westeros/
├── Main.java                   
├── entities/                   
│   ├── GameCharacter.java      
│   ├── Stark.java              
│   ├── Lannister.java         
│   ├── Targaryen.java           
│   └── Tabuleiro.java          
├── enums/                    
│   ├── House.java              
│   └── Direction.java           
├── game/                       
│   ├── GameManager.java        
│   ├── TurnManager.java         
│   ├── Turno.java               
│   ├── TurnoBot.java            
│   ├── ReplayManager.java      
│   └── AbandonoPartidaException.java
├── players/                    
│   ├── Player.java              
│   ├── HumanPlayer.java       
│   └── BotPlayer.java          
└── utils/                     
    ├── Position.java           
    ├── ColorUtil.java          
    └── InputValidator.java     



### Comandos Maven

#### Compilar o projeto
```bash
mvn clean compile
```

#### Executar o jogo
```bash
mvn exec:java
```

#### Empacotar em JAR
```bash
mvn package
```

#### Limpar arquivos compilados
```bash
mvn clean
```


## Nomes e Matrícula

Letícia Miyuki Ferreira Yamashita | 202269030A | Théo Magalhães Dias Almeida | 202365139A |
Elias Jose Fadel Vieira | 202476020 |


- **Branch**: `ui-integração-elias`
