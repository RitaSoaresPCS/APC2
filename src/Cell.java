public class Cell {
    private boolean ignored;
    public boolean fixed; // Se celula ja veio preenchida.
    private int id;
    private char value;
    private Row row;
    private Column column;
    private Quadrant quadrant;
    public Cell(int id, char value) {
        this.id = id;
        this.value = value;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public Cell setRow(Row row) {
        // TODO: Set constraints (e.g.: row cannot be larger than n^2 or smaller than 1.);
        this.row = row;
        return this;
    }
    public Cell setColumn(Column col) {
        // TODO: Set constraints (e.g.: column cannot be larger than n^2 or smaller than 1.);
        this.column = col;
        return this;
    }
    public Cell setQuadrant(Quadrant quad) {
        // TODO: Set constraints (e.g.: quadrant cannot be larger than n^2 or smaller than 1.);
        this.quadrant = quad;
        return this;
    }

    public int getId() {
        return id;
    }

    public Row getRow() {
        return row;
    }

    public Column getColumn() {
        return column;
    }

    public Quadrant getQuadrant() {
        return quadrant;
    }

    public Cell setValue(char value) {
        // TODO: Set constraints;
        this.value = value;
        return this;
    }

    public char[] getAvailableValues(char[] problemDomain) {
        if(this.value != '0') {
            return new char[0];
        }
        char[] availVals = problemDomain.clone();
        // Eliminate row used values:
        for(int i = 0; i < this.row.getLength(); i++) {
            for(int j = 0; j < availVals.length; j++) {
                if(this.row.getCellValue(i) == availVals[j]) {
                    availVals[j] = '0';
                }
            }
        }
        for(int i = 0; i < this.column.getLength(); i++) {
            for(int j = 0; j < availVals.length; j++) {
                if(this.column.getCellValue(i) == availVals[j]) {
                    availVals[j] = '0';
                }
            }
        }
        for(int i = 0; i < this.quadrant.getLength(); i++) {
            for(int j = 0; j < availVals.length; j++) {
                if(this.quadrant.getCellValue(i) == availVals[j]) {
                    availVals[j] = '0';
                }
            }
        }

        int nAvailVals = 0;
        for(int i = 0; i < availVals.length; i++) {
            if(availVals[i] != '0') {
                nAvailVals++;
            }
        }

        if(nAvailVals == 0) {
            return null;
        }
        else {
            char[] availValsFiltered = new char[nAvailVals];

            int aux = 0;
            for(int i = 0; i < availVals.length; i++) {
                if(availVals[i] != '0') {
                    availValsFiltered[aux] = availVals[i];
                    aux++;
                }
                if(aux == nAvailVals) {
                    break;
                }
            }

            return availValsFiltered;
        }
    }


    public char getValue() {
        return this.value;
    }
}
