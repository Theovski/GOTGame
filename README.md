# GOT Game - Game of Thrones Strategy Game

## 📋 Descrição do Projeto
Jogo de estratégia em turnos baseado no universo de Game of Thrones, implementado em Java usando orientação a objetos.

## 🏗️ Estrutura do Projeto (Maven)

Este projeto utiliza **Apache Maven** como ferramenta de build e gerenciamento de dependências.

```
GOTGame/
├── pom.xml                          # Arquivo de configuração do Maven
├── .gitignore                       # Arquivos a serem ignorados pelo Git
├── README.md                        # Este arquivo
└── src/
    └── main/
        └── java/
            └── com/got/westeros/
                ├── Main.java              # Classe principal
                ├── entities/              # Entidades do jogo
                │   ├── GameCharacter.java
                │   ├── Lannister.java
                │   ├── Stark.java
                │   ├── Targaryen.java
                │   └── Tabuleiro.java
                ├── enums/                 # Enumerações
                │   ├── Direction.java
                │   └── House.java
                ├── game/                  # Lógica do jogo
                │   ├── GameManager.java
                │   ├── ReplayManager.java
                │   ├── TurnManager.java
                │   └── Turno.java
                ├── players/               # Tipos de jogadores
                │   ├── Player.java
                │   ├── HumanPlayer.java
                │   └── BotPlayer.java
                └── utils/                 # Classes utilitárias
                    └── Position.java
```

## 🚀 Como Executar

### Pré-requisitos
- Java 11 ou superior
- Maven 3.6 ou superior

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

## 🎮 Como Jogar

1. Execute o projeto usando `mvn exec:java`
2. Escolha o modo de jogo:
   - **1 - Humano vs Humano**: Dois jogadores humanos
   - **2 - Humano vs Bot**: Jogador contra IA
3. Crie seus 3 personagens escolhendo:
   - Nome do personagem
   - Casa: STARK, LANNISTER ou TARGARYEN
4. Durante o turno:
   - **Movimento**: Mova seu personagem uma casa (ortogonal ou diagonal)
   - **Ataque**: Ataque personagens inimigos dentro do alcance

## 🏰 Casas e Características

| Casa | Vida | Ataque | Defesa | Alcance | Especial |
|------|------|--------|--------|---------|----------|
| **STARK** | 60 | 20 | 10 | 1 | Redução de dano: -20% |
| **LANNISTER** | 50 | 20 | 10 | 2 | Bônus de ataque: +15% |
| **TARGARYEN** | 45 | 20 | 10 | 3 | Ataque ignora defesa |

## 👥 Membros do Grupo
Inserir: Nome/Matrícula/E-mail

## 📝 Informações Adicionais

- **Pacote base**: `com.got.westeros`
- **Classe principal**: `com.got.westeros.Main`
- **Versão**: 1.0.0
- **Java**: 11
