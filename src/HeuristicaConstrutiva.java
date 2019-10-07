import java.util.Queue;
import java.util.Random;

public class HeuristicaConstrutiva {
    private SudokuProblem sudokuProblem;
    private Cell[] solutionCells;
    private Row[] solutionRows;
    private Column[] solutionColumns;
    private Quadrant[] solutionQuadrants;
    private boolean successfulRun;

    public HeuristicaConstrutiva(SudokuProblem problem) {
	    this.sudokuProblem = problem;
	    this.solutionCells = this.sudokuProblem.getCells().clone();
        this.solutionRows = problem.getRows().clone();
        this.orderStructuresByAvailableCellsCrescent(this.solutionRows);
	    this.solutionColumns = problem.getColumns().clone();
	    this.orderStructuresByAvailableCellsCrescent(this.solutionColumns);
	    this.solutionQuadrants = problem.getQuadrants().clone();
	    this.orderStructuresByAvailableCellsCrescent(this.solutionQuadrants);
	    this.successfulRun = false; // Set true if gameLoop successfully sets all cell values without ignoring any of them.
    }

    public Cell[] getSolutionCells() {
        return solutionCells;
    }

    public Row[] getSolutionRows() {
        return solutionRows;
    }

    public Column[] getSolutionColumns() {
        return solutionColumns;
    }

    public Quadrant[] getSolutionQuadrants() {
        return solutionQuadrants;
    }

    public SudokuProblem getSudokuProblem() {
        return sudokuProblem;
    }

    private Cell[] orderCellsByAvailableValuesCrescent(char[] domain) {
        Cell[] orderedCells = this.solutionCells.clone();
        boolean sorted = false;
        Cell temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < orderedCells.length - 1; i++) {
                if (orderedCells[i].getAvailableValues(domain).length > orderedCells[i+1].getAvailableValues(domain).length) {
                    temp = orderedCells[i];
                    orderedCells[i] = orderedCells[i+1];
                    orderedCells[i+1] = temp;
                    sorted = false;
                }
            }
        }
        return orderedCells;

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

    private HeuristicaConstrutiva reorderAllStructures() {
        this.orderStructuresByAvailableCellsCrescent(this.solutionRows);
        this.orderStructuresByAvailableCellsCrescent(this.solutionColumns);
        this.orderStructuresByAvailableCellsCrescent(this.solutionQuadrants);
        return this;
    }

    public boolean safeSetProblemCell(Cell cell, char value) {
        Row cellRow = cell.getRow();
        Column cellColumn = cell.getColumn();
        Quadrant cellQuadrant = cell.getQuadrant();
        boolean rowAvail = cellRow.checkIfValueAvailable(value, this.sudokuProblem.problemDomain);
        boolean columnAvail = cellColumn.checkIfValueAvailable(value, this.sudokuProblem.problemDomain);
        boolean quadrantAvail = cellQuadrant.checkIfValueAvailable(value, this.sudokuProblem.problemDomain);
        if (rowAvail && columnAvail && quadrantAvail) {
            cell.setValue(value);
            this.reorderAllStructures();
            return true;
        }
        else {
            return false;
        }
    }

    public Cell forcefulSetProblemCell(Cell cell, char value) {
	    // Same as safeSetProblemCell but without constraint checking;
	    cell.setValue(value);
	    this.reorderAllStructures();
	    return cell;
    }

    private SudokuStructure getFirstAvailableStructure(SudokuStructure[] collection) {
        for(int i = 0; i < collection.length; i++) {
            if(collection[i].getNumberOfEmptyCells() != 0 &&
                                !collection[i].isIgnored() &&
                                !collection[i].isSolved()) {
                return collection[i];
            }
        }
        return null;
    }

    private SudokuStructure getSolutionStructureById(int id, SudokuStructure[] collection) {
        for(SudokuStructure struct : collection) {
            if(struct.getId() == id) {
                return struct;
            }
        }
        return null;
    }

    private void printSolution() {
        System.out.println("Solution: ");
        int n = 0;
        for(int i = 0; i < this.solutionRows.length; i++) {
            Row row = (Row)this.getSolutionStructureById(i+1, this.solutionRows);
            for(Cell cell : row.getCells()) {
                n++;
                System.out.print(cell.getValue() + " ");
                if(n == Math.pow(this.sudokuProblem.getnSize(),2)) {
                    System.out.println(" ");
                    n = 0;
                }

            }
        }
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

    public void gameLoopRow() {
        System.out.println("Starting simplified game loop (Rows only):");
        boolean problemRanWithConstraints = false;

        /* O loop com constraints funciona da seguinte forma:
            1) Encontra a linha que não tenha sido ignorada ou resolvida com menor número de células disponíveis
            (lembrando que 'solutionRows' foi ordenada antes desse loop começar para que isso seja possível);
                1.a) Se nenhuma linha está disponível, é porque já fizemos tudo que podíamos fazer obedecendo os constraints.
            2) Pega a primeira célula dessa linha que não tenha sido ignorada ou resolvida;
                2.a) Se nenhuma célula está disponível, passa pra próxima linha (passo 1) e marca essa linha como resolvida.
            3) Atribui um valor aleatório dentre os valores possíveis para ela.
                3.a) Se não há valores disponíveis, ignora a célula e volta ao passo 1.
         */
        while(!problemRanWithConstraints) {
            Row row = (Row)this.getFirstAvailableStructure(this.solutionRows);
            Cell firstEmptyCell = null;

            if(row != null) {
                System.out.println("Row: " + row);
                firstEmptyCell = row.getFirstEmptyCell();
                if(firstEmptyCell != null) {
                    char[] availableValues = firstEmptyCell.getAvailableValues(this.sudokuProblem.problemDomain);
                    if(availableValues != null) {
                        System.out.print("Available values for first 0 cell: ");
                        for (char value : availableValues) {
                            System.out.print(value + ", ");
                        }
                        System.out.println(" ");
                        Random seed = new Random();
                        row.getFirstEmptyCell().setValue(availableValues[seed.nextInt(availableValues.length)]);
                        System.out.println("Row afterwards: " + row);
                    }
                    else {
                        System.out.println("No available values for this cell. Ignoring cell.");
                        // Having no values for a '0' cell means something is out of place and this run is not sucessful.
                        this.successfulRun = false;
                        firstEmptyCell.setIgnored(true);
                    }
                }
                else {
                    System.out.println("Row has no available cells, marking as solved.");
                    row.setSolved(true);
                }
            }
            else {
                System.out.println("No available structures. Problem has been run through with constraints turned on.");
                problemRanWithConstraints = true;
            }
        }

        if(this.successfulRun) {
            System.out.println("GameLoop successful with constraints.");
        }

        else {
            System.out.println("========================================");
            System.out.println("GameLoop has not found a viable solution. Proceeding with forceful Loop.");
            // No-constraints loop.
            // Resetting structure control parameters:
            for(int i = 0; i < this.solutionRows.length; i++) {
                this.solutionRows[i].setSolved(false);
                this.solutionRows[i].setIgnored(false);
                this.solutionRows[i].setBestSolved(false);
            }
            for(int i = 0; i < this.solutionCells.length; i++) {
                this.solutionCells[i].setIgnored(false);
            }

            boolean problemRanForcefully = false;
            while(!problemRanForcefully) {
                Row row = (Row)this.getFirstAvailableStructure(this.solutionRows);
                Cell firstEmptyCell = null;
                if(row != null) {
                    Cell cell = row.getFirstEmptyCell();
                    if(cell != null) {
                        char[] availableValues = row.getAvailableValues(this.sudokuProblem.problemDomain);
                        if(availableValues != null) {
                            System.out.println("Row before force: " + row);
                            Random seed = new Random();
                            cell.setValue(availableValues[seed.nextInt(availableValues.length)]);
                            System.out.println("Row after force: " + row);
                        }
                        else {
                            // Just to be safe:
                            System.out.println("ERROR: " + row.getId() + " has an unsolvable cell at row level.");
                        }
                    }
                    else {
                        System.out.println("Forceful run done on row: " + row.getId());
                        System.out.println("Marking row as solved.");
                        row.setSolved(true);
                    }
                }
                else {
                    System.out.println("===================");
                    System.out.println("Forceful run done.");
                    problemRanForcefully = true;
                }
            }

        }
        this.printSolution();
    }


	/*
        Ex2:
        1. Ordenar linha e coluna por quantidade de numeros unicos que faltam.
        2. Preencher com o primeiro numero valido para aquela coluna, linha e quadrado.
        2.2. Se nao tem nenhum numero valido, passa e deixa pro final.
        3. Prossegue ate o fim.
        3.2. Se ainda ha celulas vazias, as preenche de modo que haja a mesma quantidade de
        numeros de 1 a n^2 na solucao (por exemplo, mas pode ser outra regra, como inviabilizar so linha ou so coluna).
	 */
    public void gameLoopCell() {
        boolean sucessfulRun = true;
        Random seed = new Random();
        System.out.println("Starting expensive game loop: ");
        this.solutionCells = this.orderCellsByAvailableValuesCrescent(this.sudokuProblem.problemDomain);
        for (Cell cell : this.solutionCells) {
            while (!cell.isIgnored()) {
                char[] availVals = cell.getAvailableValues(this.sudokuProblem.problemDomain);
                if (availVals != null && availVals.length != 0) {
                    cell.setValue(availVals[seed.nextInt(availVals.length)]);
                }
                else {
                    // System.out.println("Cell has no available values. Proceeding to Cell " + (cell.getId() + 1));
                    sucessfulRun = false;
                    cell.setIgnored(true);
                }
            }
        }
        if(sucessfulRun) {
            System.out.println("All cells have been assigned a value obeying all constraints.");
        }
        else {
            System.out.println("Program failed to obey all constraints. Proceeding to forceful run: ");
            for (Cell cell : this.solutionCells) {
                if(cell.getValue() == '0') {
                    cell.setValue(Character.forDigit(seed.nextInt((int)Math.pow(this.sudokuProblem.getnSize(), 2)-1)+1, 10));
                }
            }
        }
        this.printSolution();
    }
}
