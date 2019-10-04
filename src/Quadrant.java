/*  A primeira célula de cada quadrante pode ser obtida através da fórmula: (q%n)+((floor(q/n)*n)^3)
    Dessa forma, podemos construir as colunas de cada quadrante, quadrante a quadrante.

    // Algoritmo de construção de Quadrantes:
    for(q = 1; q <= n^2; q++) {
        Cell[] sudokuArray = int[n^4];
        // alimentar sudokuArray aqui com SudokuFileLoader.problems[1];
        for(int q = 1; q <= n^2; q++) {
            Cell[] cells = new int[n^2];
            Quadrant quad = new Quadrant(q, cells);
            for (int cl = 0; cl < n^2; cl++) {
                quad.addCell(sudokuArray[((r-1)*(n^2))+cl])
            }
        }
    }

 */

public class Quadrant {
    private int id;
    private Cell[] cells;

    public Quadrant(int id, Cell[] cells) {
        this.id = id;
        this.cells = cells;
    }

    public Quadrant addCell(Cell cell, int position) {
        this.cells[position] = cell;
        return this;
    }
    
    public int countEmptyCells() {
        int emptyCells = 0;
        for(int i = 0; i < this.cells.length; i++) {
            System.out.println("Reading cell and checking if empty: " + this.cells[i]);
            if (this.cells[i].getValue() == '0') {
                emptyCells++;
            }
        }
        return emptyCells;
    }
}
