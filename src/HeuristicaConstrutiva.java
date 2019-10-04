public class HeuristicaConstrutiva {
    private SudokuProblem sudokuProblem;
	private Column[] orderedColumns;
	private Row[] orderedRows;
	private Quadrant[] orderedQuadrants;

	public HeuristicaConstrutiva(SudokuProblem problem) {
	    this.sudokuProblem = problem;
        this.orderedRows = problem.getRows().clone();
        this.orderStructuresByAvailableCellsCrescent(this.orderedRows);
	    this.orderedColumns = problem.getColumns().clone();
	    this.orderStructuresByAvailableCellsCrescent(this.orderedColumns);
	    this.orderedQuadrants = problem.getQuadrants().clone();
	    this.orderStructuresByAvailableCellsCrescent(this.orderedQuadrants);
    }

	private SudokuStructure[] orderStructuresByAvailableCellsCrescent(SudokuStructure[] structureCollection) {
        boolean sorted = false;
        SudokuStructure temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < structureCollection.length - 1; i++) {
                if (structureCollection[i].getNumberOfEmptyCells() > structureCollection[i+1].getNumberOfEmptyCells()) {
                    temp = structureCollection[i];
                    structureCollection[i] = structureCollection[i+1];
                    structureCollection[i+1] = temp;
                    sorted = false;
                }
            }
        }
        return structureCollection;
    }
	/*
	 Ex1:
	1. Ordenar linha e coluna por quantidade de celulas disponiveis (vazias).
	2. Preencher com o primeiro numero valido para aquela coluna e linha.
	2.2. Se nao tem nenhum numero valido, passa e deixa pro final.
	3. Prossegue ate o fim.
	3.2. Se ainda ha celulas vazias, as preenche de modo que haja a mesma quantidade de
	numeros de 1 a n^2 na solucao (por exemplo, mas pode ser outra regra, como inviabilizar so linha ou so coluna).
	 */


	/*
	 Ex2:
	1. Ordenar linha e coluna por quantidade de numeros unicos que faltam.
	2. Preencher com o primeiro numero valido para aquela coluna, linha e quadrado.
	2.2. Se nao tem nenhum numero valido, passa e deixa pro final.
	3. Prossegue ate o fim.
	3.2. Se ainda ha celulas vazias, as preenche de modo que haja a mesma quantidade de
	numeros de 1 a n^2 na solucao (por exemplo, mas pode ser outra regra, como inviabilizar so linha ou so coluna).

	 */

}
