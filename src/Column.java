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
            col.setCell(sudokuArray[(c-1)+(cl*(n^2))])
        }
    }
 */

public class Column extends SudokuStructure {
    public Column(int id, Cell[] cells) {
    	super(id, cells);
    }
}
