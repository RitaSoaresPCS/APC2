public class Main {
    public static void main(String[] args) {
        SudokuFileLoader loader =
                new SudokuFileLoader("..\\APC2\\files\\sudokuN3.csv", false);
        System.out.println(loader.problems.get(1));

        SudokuProblem sudokuProblem = new SudokuProblem(loader, 1, 3);
        // 040  100  050
        // 107  003  960
        // 520  008  000

        // 000  000  017
        // 000  906  800
        // 803  050  620

        // 090  060  543
        // 600  080  700
        // 250  097  100
        Row[] rows = sudokuProblem.getRows();
        Column[] columns = sudokuProblem.getColumns();
        Quadrant[] quadrants = sudokuProblem.getQuadrants();

        for (int i = 0; i < rows.length; i++) {
            System.out.println("Row: " + rows[i]);
        }
        for (int i = 0; i < rows.length; i++) {
            System.out.println("Columns: " + columns[i]);
        }
        for (int i = 0; i < rows.length; i++) {
            System.out.println("Quadrants: " + quadrants[i]);
        }



        // Exemplo: problema e solucao da linha 3 arquivo .csv:
        // loader.problems.get(3);
        // loader.solutions.get(3);
    }
}

