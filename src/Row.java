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

public class Row extends SudokuStructure {
    public Row(int id, Cell[] cells) {
    	super(id, cells);
    }
}
