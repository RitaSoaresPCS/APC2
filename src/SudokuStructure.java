
public abstract class SudokuStructure {
    protected boolean ignored;
    protected boolean bestSolved;
    protected boolean solved;
    protected int id;
	protected Cell[] cells;

    public SudokuStructure(int id, Cell[] cells) {
        this.id = id;
        this.cells = cells;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public void setBestSolved(boolean bestSolved) {
        this.bestSolved = bestSolved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCells(Cell[] cells) {
        this.cells = cells;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public boolean isBestSolved() {
        return bestSolved;
    }

    public boolean isSolved() {
        return solved;
    }

    public SudokuStructure setCell(Cell cell, int position) {
        this.cells[position] = cell;
        return this;
    }
    
    public int countEmptyCells() {
        int emptyCells = 0;
        for(int i = 0; i < this.cells.length; i++) {
            if (this.cells[i].getValue() == '0') {
                emptyCells++;
            }
        }
        return emptyCells;
    }

    public int getId() {
        return this.id;
    }
    public Cell[] getCells() {
        return this.cells;
    }
    public Cell getCell(int index) {
        return this.cells[index];
    }

    // Auxiliary functions:
    public char getCellValue(int index) {
        return this.cells[index].getValue();
    }
    public int getLength() {
        return this.cells.length;
    }
    public boolean checkIfEmptyCell(int index) {
        if(this.getCellValue(index) == '0') {
            return false;
        }
        return true;
    }
    public Cell getFirstEmptyCell() {
        for(int i = 0; i < this.cells.length; i++) {
            if (this.getCellValue(i) == '0' && !this.getCell(i).isIgnored()) {
                return this.getCell(i);
            }
        }
        return null;
    }
    public int getNumberOfEmptyCells() {
        int nEmptyCells = 0;
        for(int i = 0; i < this.getLength(); i++) {
            if(this.checkIfEmptyCell(i)) {
                nEmptyCells++;
            }
        }
        return nEmptyCells;
    }
    public char[] getAvailableValues(char[] valuesDomain) {
        char[] availableValues = valuesDomain.clone();
        for (int i = 0; i < this.cells.length; i++) {
            for (int j = 0; j < valuesDomain.length; j++) {
                if (this.cells[i].getValue() == valuesDomain[j]) {
                    availableValues[j] = '0';
                }
            }
        }
        int nAvailVals = 0;
        for (int i = 0; i < availableValues.length; i++) {
            if (availableValues[i] != '0') {
                nAvailVals++;
            }
        }

        if (nAvailVals == 0) {
            return null;
        } else {
            char[] availValsFiltered = new char[nAvailVals];

            int aux = 0;
            for (int i = 0; i < availableValues.length; i++) {
                if (availableValues[i] != '0') {
                    availValsFiltered[aux] = availableValues[i];
                    aux++;
                }
                if (aux == nAvailVals) {
                    break;
                }
            }

            return availValsFiltered;
        }
    }

    public boolean checkIfValueAvailable(char value, char[] valuesDomain) {
        char[] availableValues = this.getAvailableValues(valuesDomain);
        for(int i = 0; i < availableValues.length; i++) {
            if(value == availableValues[i]) {
                return false;
            }
        }
        return false;
    }
    public boolean isValueValid(char value, char[] valuesDomain) {
        char[] availableValues = this.getAvailableValues(valuesDomain);
        for(int i = 0; i < availableValues.length; i++) {
            if(value == availableValues[i]) {
                return true;
            }
        }
        return false;
    }
    public String toString() {
        String contentString = "";
        for(int i = 0; i < this.getLength(); i++) {
            contentString += this.cells[i].getValue() + " ";
        }
        return contentString;
    }
    
    
    @Override
    /**
     * Estruturas sao iguais se todos os valores das celulas
     * componentes forem iguais.
     */
    public boolean equals(Object obj) { 
        SudokuStructure struct = (SudokuStructure) obj;  
        int cont = 0;
        for (Cell cell : struct.getCells()) {
        	if (cell.getValue() != this.cells[cont].getValue()) {
        		return false;
        	}
        	cont++;
        }
        
	    return true;
    }
    
}
