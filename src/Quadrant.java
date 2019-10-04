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

public class Quadrant extends SudokuStructure {
    public Quadrant(int id, Cell[] cells) {
    	super(id, cells);
    }
}
