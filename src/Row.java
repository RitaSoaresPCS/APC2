/*
    A linha vai da célula (L-1)*(n^2) à célula ((n^2)*L)-1 em sucessão (incrementos de 1).
    Ou seja, a primeira linha em Sudoku n = 3 vai de 0 a 8;
    A segunda vai de 9 a 17;
    A terceira vai de 18 a 26 e assim sucessivamente.
    L mínimo é 1 e máximo é n^2.

    // Algoritmo de construção de Linhas:
    Cell[] sudokuArray = int[n^4];
    // alimentar sudokuArray aqui com SudokuFileLoader.problems[1];
    for(int r = 1; r <= n^2; r++) {
        Cell[] cells = new int[n^2];
        Row row = new Row(r, cells);
        for (int cl = 0; cl < n^2; cl++) {
            row.addCell(sudokuArray[((r-1)*(n^2))+cl])
        }
    }
 */

public class Row {
    private int id;
    private Cell[] cells;

    public Row(int id, Cell[] cells) {
        this.id = id;
        this.cells = cells;
    }

    public Row addCell(Cell cell, int position) {
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

    public int getId() {
        return this.id;
    }
}
