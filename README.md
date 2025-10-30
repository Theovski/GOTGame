# GOT Game - Game of Thrones Strategy Game

## 📋 Descrição do Projeto
Jogo de estratégia tático em turnos baseado no universo de Game of Thrones, implementado em Java com programação orientada a objetos e interface colorida no terminal.

## ✨ Funcionalidades

### Gameplay
- ⚔️ Sistema de combate tático em tabuleiro 10x10
- 🎮 Dois modos de jogo: Humano vs Humano e Humano vs Bot
- 🤖 Inteligência Artificial estratégica para o Bot
- 👥 Suporte para 2 jogadores com 3 personagens cada
- 🎯 Sistema de alcance de ataque baseado na casa escolhida
- 💪 Cálculo de dano com atributos únicos por casa
- 🏆 Detecção automática de vitória/derrota

### Interface e UX
- 🎨 Interface colorida com códigos ANSI
- 🗺️ Tabuleiro visual com símbolos Unicode
- 🟢 Identificação visual de personagem atual (fundo verde)
- 🔵 Identificação visual de aliados (fundo ciano)
- 📊 Indicadores de vida (●/◐/○)
- 📚 Sistema de tutorial integrado
- ✅ Validação de entrada com retry automático
- 🚪 Comando para abandonar partida (-99)

### Sistema de Replay
- 📹 Gravação automática de todas as ações
- ⏯️ Replay completo da partida
- ⏭️ Replay passo a passo
- 📋 Visualização das últimas N ações

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java 11
- **Build Tool**: Apache Maven 3.x
- **Paradigma**: Programação Orientada a Objetos (POO)
- **Padrões de Projeto**: Strategy, Observer
- **Terminal**: ANSI Colors (Windows Terminal/PowerShell)
- **Controle de Versão**: Git

## 🏗️ Arquitetura

```
src/main/java/com/got/westeros/
├── Main.java                    # Ponto de entrada
├── entities/                    # Entidades do domínio
│   ├── GameCharacter.java       # Classe abstrata base
│   ├── Stark.java               # Casa Stark (Defesa)
│   ├── Lannister.java           # Casa Lannister (Ataque)
│   ├── Targaryen.java           # Casa Targaryen (Alcance)
│   └── Tabuleiro.java           # Gerenciamento do tabuleiro
├── enums/                       # Enumerações
│   ├── House.java               # Atributos das casas
│   └── Direction.java           # Direções de movimento
├── game/                        # Lógica do jogo
│   ├── GameManager.java         # Fluxo principal
│   ├── TurnManager.java         # Gerenciamento de turnos
│   ├── Turno.java               # Turno de jogador humano
│   ├── TurnoBot.java            # Turno automatizado do bot
│   ├── ReplayManager.java       # Sistema de replay
│   └── AbandonoPartidaException.java
├── players/                     # Tipos de jogadores
│   ├── Player.java              # Interface base
│   ├── HumanPlayer.java         # Jogador humano
│   └── BotPlayer.java           # IA estratégica
└── utils/                       # Utilitários
    ├── Position.java            # Posicionamento e distância
    ├── ColorUtil.java           # Cores ANSI
    └── InputValidator.java      # Validação de entrada
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

| Casa | Símbolo | Vida | Alcance | Habilidade Especial |
|------|---------|------|---------|---------------------|
| **🐺 STARK** | S● | 60 HP | 1 célula | Reduz 20% do dano recebido |
| **🦁 LANNISTER** | L● | 50 HP | 3 células | +15% de dano nos ataques |
| **🐉 TARGARYEN** | T● | 45 HP | 5 células | Ignora toda a defesa inimiga |

**Cálculo de Distância**: Distância de Chebyshev (movimentação em 8 direções)

## 👥 Equipe de Desenvolvimento

| Nome | Matrícula |
|------|-----------|
| [Nome 1] | [Matrícula 1] |
| [Nome 2] | [Matrícula 2] |
| Elias Jose Fadel Vieira | 202476020 |

## 📝 Informações Técnicas

- **Pacote base**: `com.got.westeros`
- **Classe principal**: `com.got.westeros.Main`
- **Versão**: 1.0.0
- **Java**: 11+
- **Maven**: 3.6+
- **Branch**: `ui-integração-elias`
