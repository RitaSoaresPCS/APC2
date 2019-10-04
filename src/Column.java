/*
    A coluna é formada pelas células de C-1 a (n^4 + ((C-1)-n²) em incrementos de n^2.
    Ou seja, a primeira coluna em Sudoku n = 3 é formada pelas células 0, 9, 18, ..., 72;
    A segunda coluna por 1, 10, 19, ..., 73;
    A última por 8, 17, 26, ..., 80.
    C mínimo é 1 e máximo é n^2.

    // Algoritmo de construção de Colunas:
    Cell[] sudokuArray = int[n^4];
    // alimentar sudokuArray aqui com SudokuFileLoader.problems[1];
    for(int c = 1; c <= n^2; c++) {
        Cell[] cells = new int[n^2];
        Column col = new Column(c, cells);
        for (int cl = 0; cl < n^2; cl++) {
            col.addCell(sudokuArray[(c-1)+(cl*(n^2))])
        }
    }
 */

public class Column {
    private int id;
    private Cell[] cells;

    public Column(int id, Cell[] cells) {
        this.id = id;
        this.cells = cells;
    }

    public int getId() {
        return this.id;
    }

    public Column addCell(Cell cell, int position) {
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

    public Cell getCell(int index) {
        return this.cells[index];
    }

    public int getLenght() {
        return this.cells.length;
    }
}
