public class Cell {
    private int id;
    private char value;
    private Row row;
    private Column column;
    private Quadrant quadrant;
    public Cell(int id, char value) {
        this.value = value;
    }

    public Cell setRow(Row row) {
        // TODO: Set constraints (e.g.: row cannot be larger than n^2 or smaller than 1.);
        this.row = row;
        return this;
    }
    public Cell setColumn(Column col) {
        // TODO: Set constraints (e.g.: row cannot be larger than n^2 or smaller than 1.);
        this.column = col;
        return this;
    }
    public Cell setQuadrant(Quadrant quad) {
        // TODO: Set constraints (e.g.: row cannot be larger than n^2 or smaller than 1.);
        this.quadrant = quad;
        return this;
    }

    public char getValue() {
        return this.value;
    }
}
