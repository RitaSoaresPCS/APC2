
public abstract class SudokuStructure {
	protected int id;
	protected Cell[] cells;

    public SudokuStructure(int id, Cell[] cells) {
        this.id = id;
        this.cells = cells;
    }

    public SudokuStructure addCell(Cell cell, int position) {
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

    public Cell getCell(int index) {
        return this.cells[index];
    }

    public int getLength() {
        return this.cells.length;
    }
    
}
