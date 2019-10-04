public class Main {
    public static void main(String[] args) {
        SudokuFileLoader loader =
                new SudokuFileLoader("..\\Heuristicas-Sudoku\\files\\sudoku.csv", true);
        System.out.println(loader.problems.get(1));

        SudokuProblem sudokuProblem = new SudokuProblem(loader, 1, 3);
        Column[] columns = sudokuProblem.getColumns();

        for (int i = 0; i < columns.length; i++) {
            System.out.println("Column: " + columns[i].getId());
            for(int j = 0; j < columns[i].getLenght(); j++) {
                System.out.println(columns[i].getCell(j).getValue());
            }
        }

        // Exemplo: problema e solucao da linha 3 arquivo .csv:
        // loader.problems.get(3);
        // loader.solutions.get(3);
    }
}

