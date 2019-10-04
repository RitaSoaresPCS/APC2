public class Main {
    public static void main(String[] args) {
        SudokuFileLoader loader =
                new SudokuFileLoader("..\\APC2\\files\\sudoku.csv", true);
        System.out.println(loader.problems.get(1));

        SudokuProblem sudokuProblem = new SudokuProblem(loader, 1, 3);
        Column[] columns = sudokuProblem.getColumns();

        for (int i = 0; i < columns.length; i++) {
            System.out.println("Column: " + columns[i]);
        }

        // Exemplo: problema e solucao da linha 3 arquivo .csv:
        // loader.problems.get(3);
        // loader.solutions.get(3);
    }
}

