public class SudokuProblem {
    private Cell[] cells;
    private Row[] rows;
    private Column[] columns;
    private Quadrant[] quadrants;

    public SudokuProblem(SudokuFileLoader sudokuSet, int problemNumber, int nSize) {
        int nSize2 = (int)Math.pow(nSize, 2);
        int nSize3 = (int)Math.pow(nSize, 3);
        int nSize4 = (int)Math.pow(nSize, 4);
        // TODO: nSize should be a SudokuFileLoader calculated property;
        this.cells = new Cell[nSize4];
        this.rows = new Row[nSize2];
        this.columns = new Column[nSize2];
        this.quadrants = new Quadrant[nSize2];

        // Choosing Sudoku problem:
        String sudokuProblemString = sudokuSet.problems.get(problemNumber);

        // Initializing sudoku cells from sudoku problem. Every row, column and quadrant will point to these cells:
        for(int i = 0; i < nSize4; i++) {
            this.cells[i] = new Cell(i, sudokuProblemString.charAt(i));
        }

        // Building rows and assigning cell counterpart:
        for(int r = 1; r <= nSize2; r++) {
            Cell[] rowCells = new Cell[nSize2];
            Row row = new Row(r, rowCells);
            for (int cl = 0; cl < nSize2; cl++) {
                this.cells[((r-1)*(nSize2))+cl].setRow(row);
                row.addCell(this.cells[((r-1)*nSize)+cl], cl);
            }
            this.rows[r-1] = row;
        }

        // Building columns and assigning cell counterpart:
        for(int c = 1; c <= nSize2; c++) {
            Cell[] colCells = new Cell[nSize2];
            Column col = new Column(c, colCells);
            for (int cl = c-1, i = 0; cl <= (nSize4 + (c-1)-nSize2); cl += nSize2, i++) {
                this.cells[cl].setColumn(col);
                col.addCell(this.cells[cl], i);
            }
            this.columns[c-1] = col;
        }

        // Building quadrants and assigning cell counterpart:
        for(int q = 1; q <= nSize2; q++) {
            Cell[] quadCells = new Cell[nSize2];
            Quadrant quad = new Quadrant(q, quadCells);
            int quadFirstCell = (nSize*((q-1)%nSize))+(int)(Math.pow(nSize, 3)*(Math.floor((q-1)/nSize)));
            for (int cl = 0; cl < nSize2; cl++) {
                quad.addCell(this.cells[(int)(quadFirstCell + (cl%nSize) + nSize*(Math.floor(cl/nSize)))], cl);
                this.cells[(int)(quadFirstCell + (cl%nSize) + nSize*(Math.floor(cl/nSize)))].setQuadrant(quad);
            }
            this.quadrants[q-1] = quad;
        }
    }

    public Column[] getColumns() {
        return this.columns;
    }
    public Row[] getRows() {
        return this.rows;
    }
    public Quadrant[] getQuadrants() {
        return this.quadrants;
    }
}
