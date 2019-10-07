public class SudokuProblem {
    // This universal domain supports up to nSize = 6;
    public static char[] universalDomain = new char[]{'1', '2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V', 'W', 'X', 'Y', 'Z', '+'};
    public char[] problemDomain;
    private final int nSize;
    private Cell[] cells;
    private Row[] rows;
    private Column[] columns;
    private Quadrant[] quadrants;

    public SudokuProblem(SudokuFileLoader sudokuSet, int problemNumber, int nSize) {
        this.nSize = nSize;
        int nSize2 = (int)Math.pow(nSize, 2);
        int nSize4 = (int)Math.pow(nSize, 4);
        this.problemDomain = new char[nSize2];
        // TODO: nSize should be a SudokuFileLoader calculated property;
        this.cells = new Cell[nSize4];
        this.rows = new Row[nSize2];
        this.columns = new Column[nSize2];
        this.quadrants = new Quadrant[nSize2];

        // Choosing Sudoku problem:
        String sudokuProblemString = sudokuSet.problems.get(problemNumber);

        // Initializing problem's domain from universal Domain:
        // TODO: During the sudoku cells initialization, an error should be returned in case a cell value is found outside problemDomain's boundaries;
        for(int v = 0; v < this.problemDomain.length; v++) {
            this.problemDomain[v] = SudokuProblem.universalDomain[v];
        }

        // Initializing sudoku cells from sudoku problem. Every row, column and quadrant will point to these cells:
        for(int i = 0; i < nSize4; i++) {
            this.cells[i] = new Cell(i, sudokuProblemString.charAt(i));
        }

        // Building structures that point to previous cells:
        this.buildRows();
        this.buildColumns();
        this.buildQuadrants();
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
    public Cell[] getCells() {
        return this.cells;
    }
    public int getnSize() {
        return this.nSize;
    }

    private SudokuProblem buildRows() {
        int nSize2 = (int)Math.pow(this.nSize, 2);
        for(int r = 1; r <= nSize2; r++) {
            Cell[] rowCells = new Cell[nSize2];
            Row row = new Row(r, rowCells);
            for (int cl = 0; cl < nSize2; cl++) {
                this.cells[((r-1)*(nSize2))+cl].setRow(row);
                row.setCell(this.cells[((r-1)*(int)Math.pow(nSize,2)+cl)], cl);
            }
            this.rows[r-1] = row;
        }
        return this;
    }
    private SudokuProblem buildColumns() {
        int nSize2 = (int)Math.pow(this.nSize, 2);
        int nSize4 = (int)Math.pow(this.nSize, 4);
        for(int c = 1; c <= nSize2; c++) {
            Cell[] colCells = new Cell[nSize2];
            Column col = new Column(c, colCells);
            for (int cl = c-1, i = 0; cl <= (nSize4 + (c-1)-nSize2); cl += nSize2, i++) {
                this.cells[cl].setColumn(col);
                col.setCell(this.cells[cl], i);
            }
            this.columns[c-1] = col;
        }
        return this;
    }
    private SudokuProblem buildQuadrants() {
        int nSize2 = (int)Math.pow(this.nSize, 2);
        for(int q = 1; q <= nSize2; q++) {
            Cell[] quadCells = new Cell[nSize2];
            Quadrant quad = new Quadrant(q, quadCells);
            int quadFirstCell = (nSize*((q-1)%nSize))+(int)(Math.pow(nSize, 3)*(Math.floor((q-1)/nSize)));
            for (int cl = 0; cl < nSize2; cl++) {
                quad.setCell(this.cells[(int)(quadFirstCell + (cl%nSize) + Math.pow(nSize,2)*(Math.floor(cl/nSize)))], cl);
                this.cells[(int)(quadFirstCell + (cl%nSize) + Math.pow(nSize,2)*(Math.floor(cl/nSize)))].setQuadrant(quad);
            }
            this.quadrants[q-1] = quad;
        }
        return this;
    }
}
