public class Main {
    public static void main(String[] args) {
        SudokuFileLoader loader =
                new SudokuFileLoader("..\\APC2\\files\\sudokuN3.csv", false);
        
        
        // Busca local.
        for (int i = 0; i < loader.problems.size(); i++) {
        	SudokuGame sudokuProblem = new SudokuGame(loader.problems, i);
            SudokuGame sudokuSolution = new SudokuGame(loader.solutions, i);
        	BuscaLocal.evaluatePerfomance(sudokuProblem, sudokuSolution, 1, 10000);
        }
        
        
        /*
        for (int i = 0; i < 19; i++) {
            SudokuProblem sudokuProblem = new SudokuProblem(loader, i, 3);
            // Problem:
            // 040  100  050
            // 107  003  960
            // 520  008  000

            // 000  000  017
            // 000  906  800
            // 803  050  620

            // 090  060  543
            // 600  080  700
            // 250  097  100

            // Solution:
            // 346  179  258
            // 187  523  964
            // 529  648  371
            // 965  832  417
            // 472  916  835
            // 813  754  629
            // 798  261  543
            // 631  485  792
            // 254  397  186

            HeuristicaConstrutiva heuristicaUm = new HeuristicaConstrutiva(sudokuProblem);
            heuristicaUm.gameLoopCell();
            /*
            Row[] rows = sudokuProblem.getRows();
            Column[] columns = sudokuProblem.getColumns();
            Quadrant[] quadrants = sudokuProblem.getQuadrants();

            for (int i = 0; i < rows.length; i++) {
                System.out.println("Row: " + rows[i]);
            }
            for (int i = 0; i < columns.length; i++) {
                System.out.println("Columns: " + columns[i]);
            }
            for (int i = 0; i < quadrants.length; i++) {
                System.out.println("Quadrants: " + quadrants[i]);
            }
            System.out.println("Row free cells: ");
            for (int i = 0; i < sudokuProblem.getRows().length; i++) {
                System.out.println("Row " + i + ": " + sudokuProblem.getRows()[i].getNumberOfEmptyCells());
            }
            System.out.println("Column free cells: ");
            for (int i = 0; i < sudokuProblem.getColumns().length; i++) {
                System.out.println("Column " + i + ": " + sudokuProblem.getColumns()[i].getNumberOfEmptyCells());
            }

            System.out.println("Quadrant free cells: ");
            for (int i = 0; i < sudokuProblem.getQuadrants().length; i++) {
                System.out.println("Quadrant " + i + ": " + sudokuProblem.getQuadrants()[i].getNumberOfEmptyCells());
            }
            */
            /*

            System.out.println("HeuCons Row free cells: ");
            for (int i = 0; i < heuCons.getOrderedRows().length; i++) {
                System.out.println("Row " + i + ":" + heuCons.getOrderedRows()[i].getNumberOfEmptyCells());
            }
            System.out.println("HeuCons Column free cells: ");
            for (int i = 0; i < heuCons.getOrderedColumns().length; i++) {
                System.out.println("Column " + i + ": " + heuCons.getOrderedColumns()[i].getNumberOfEmptyCells());
            }

            System.out.println("HeuCons Quadrant free cells: ");
            for (int i = 0; i < heuCons.getOrderedQuadrants().length; i++) {
                System.out.println("Quadrant " + i + ": " + heuCons.getOrderedQuadrants()[i].getNumberOfEmptyCells());
            }
            
        
        }
    	*/
    }

}

