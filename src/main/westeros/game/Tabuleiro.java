package westeros.entities;

import java.util.Random;

public class Tabuleiro  {
    // private Character personagem;


    private final int linhas = 10;
    private final int colunas = 10;
    
    private Character[][] tabuleiro;
    private Random aleatorio = new Random();
    
    public Tabuleiro(){
       tabuleiro = new Character[linhas][colunas];
    }


    public void posicionaPerson(Character[] persona, String lado){
        int colunaMin, colunaMax;

        if(lado.equalsIgnoreCase("esquerdo")){
            colunaMin = 0;
            colunaMax = 4;
        } else {
            colunaMin = 5;
            colunaMax = 9;
        }

        for (Character p : persona){
            
            int lin, col;

            do {
                lin = aleatorio.nextInt(linhas);
                col = aleatorio.nextInt(colunaMax - colunaMin + 1) + colunaMin;
            } while (tabuleiro[lin][col] != null);

            tabuleiro[lin][col] = p;

            p.setLinha(lin);  
            p.setColuna(col);

            System.out.printf("%s (%s) posicionado em [%d, %d]%n",
                    p.getNome(), p.getCasa(), lin, col);
        }

    }

    public boolean verificaPosicao(int l, int c){
        return l >= 0 && l < this.linhas && c >= 0 && c < this.colunas;
    }

    public Character getPersonagem(int linha, int coluna){
        if(verificaPosicao(linha, coluna)){
            return tabuleiro[linha][coluna];
        }
        return null;
    }

    public void exibirTabuleiro(){
        for(int i = 0; i < linhas; i++){
            for(int j = 0; j < colunas; j++){
                if(tabuleiro[i][j] == null){
                    System.out.print(" [ ]");
                } else { 
                    // exibe posicao e nome
                    System.out.printf("[%s]", tabuleiro[i][j].getCasa().name().substring(0, 3));
                }
            }
            System.out.println();
        }

    }

    public boolean moverPersonagem(Character personagem, int novaLinha, int novaColuna) {
        int lAtual = personagem.getLinha();
        int cAtual = personagem.getColuna();

        if (!verificaPosicao(novaLinha, novaColuna)) {
            return false;
        }

        int diffL = Math.abs(novaLinha - lAtual);
        int diffC = Math.abs(novaColuna - cAtual);

        if (diffL > 1 || diffC > 1 || (diffL == 0 && diffC == 0)) {
            System.out.println("Movimento inválido: Só é permitido mover 1 casa (ortogonal ou diagonal).");
            return false;
        }

        if (getPersonagem(novaLinha, novaColuna) != null) {
            System.out.println("Movimento inválido: A casa de destino já está ocupada.");
            return false;
        }

        tabuleiro[lAtual][cAtual] = null;
        tabuleiro[novaLinha][novaColuna] = personagem;
        personagem.setLinha(novaLinha);
        personagem.setColuna(novaColuna);
        
        System.out.printf("%s moveu para [%d, %d]\n", personagem.getNome(), novaLinha, novaColuna);
        return true;
    }

    public void removerPersonagem(Character personagem) {
        if (personagem != null && verificaPosicao(personagem.getLinha(), personagem.getColuna())) {
            tabuleiro[personagem.getLinha()][personagem.getColuna()] = null;
        }
    }

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }


    public Character[][] getTabuleiro() {
        return tabuleiro;
    }


}
