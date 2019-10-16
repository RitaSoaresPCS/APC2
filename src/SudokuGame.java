import java.util.ArrayList;

public class SudokuGame {
    // This universal domain supports up to nSize = 6;
    public static char[] universalDomain = new char[]{'1', '2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V', 'W', 'X', 'Y', 'Z', '+'};
    public char[] problemDomain;
    private final int nSize;
    private Cell[] cells;
    private Row[] rows;
    private Column[] columns;
    private Quadrant[] quadrants;

    public SudokuGame(ArrayList<String> game, int gameNumber) {
    	// Jogo vem como n^4, entao tira raiz duas vezes para obter n
        this.nSize = (int) Math.sqrt(Math.sqrt(game.get(gameNumber).length())); 
        int nSize2 = (int) Math.pow(nSize, 2);
        int nSize4 = (int) Math.pow(nSize, 4);
        this.problemDomain = new char[nSize2];
        
        this.cells = new Cell[nSize4];
        this.rows = new Row[nSize2];
        this.columns = new Column[nSize2];
        this.quadrants = new Quadrant[nSize2];
        

        // Choosing Sudoku problem or solution:
        String sudokuProblemString = game.get(gameNumber);

        // Initializing game domain from universal Domain:
        // TODO: During the sudoku cells initialization, an error should be returned in case a cell value is found outside problemDomain's boundaries;
        for(int v = 0; v < this.problemDomain.length; v++) {
            this.problemDomain[v] = SudokuGame.universalDomain[v];
        }

        // Initializing sudoku cells from sudoku problem. Every row, column and quadrant will point to these cells:
        for(int i = 0; i < nSize4; i++) {
            this.cells[i] = new Cell(i, sudokuProblemString.charAt(i));
            if (sudokuProblemString.charAt(i) != '0') {
            	this.cells[i].fixed = true;
            }
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
    public Row getRow(int id) {
    	return this.rows[id];
	}
    
    public Quadrant[] getQuadrants() {
        return this.quadrants;
    }
    
    public Quadrant getQuadrant(int id) {
    	return this.quadrants[id];
    }
    
    public Cell[] getCells() {
        return this.cells;
    }
    public int getnSize() {
        return this.nSize;
    }

    private SudokuGame buildRows() {
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
    private SudokuGame buildColumns() {
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
    private SudokuGame buildQuadrants() {
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
    
    
    
    public String toString() {
    	String content = "";
        for (int i = 0; i < rows.length; i++) {
        	content += rows[i] + "\r\n";
        }
        return content;	
    }

    
    /**
     * Muda duas celulas de um quadrante de lugar de acordo com os ids.
     * @param quadrantId
     * @param cell1Id
     * @param cell2Id
     */
	public void quadrantCellSwap(int quadrantId, int cell1Id, int cell2Id) {
		char cell1 = this.quadrants[quadrantId].getCell(cell1Id).getValue();
		char cell2 = this.quadrants[quadrantId].getCell(cell2Id).getValue();
		this.quadrants[quadrantId].getCell(cell1Id).setValue(cell2);
		this.quadrants[quadrantId].getCell(cell2Id).setValue(cell1);
		
	}
	
	
	
	/**
     * Muda duas celulas de uma linha de lugar de acordo com os ids.
     * @param rowId
     * @param cell1Id
     * @param cell2Id
     */
	public void rowCellSwap(int rowId, int cell1Id, int cell2Id) {
		char cell1 = this.rows[rowId].getCell(cell1Id).getValue();
		char cell2 = this.rows[rowId].getCell(cell2Id).getValue();
		this.rows[rowId].getCell(cell1Id).setValue(cell2);
		this.rows[rowId].getCell(cell2Id).setValue(cell1);
		
	}

	
}
