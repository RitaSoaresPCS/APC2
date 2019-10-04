import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Classe que carrega as instancias e suas solucoes a partir
 * de um arquivo .csv.
 */
public class SudokuFileLoader {

	public ArrayList<String> problems = new ArrayList<String>();
	public ArrayList<String> solutions = new ArrayList<String>();
	
	public SudokuFileLoader(String filePath) {
		BufferedReader file;
		try {
			file = new BufferedReader(new FileReader(filePath));
			
			String line;
			while ((line = file.readLine()) != null) {
			    String[] data = line.split(",");
	            
	            this.problems.add(data[0]);
	            this.solutions.add(data[1]);
			}
			
			file.close();
		} catch (Exception e) {
			System.out.println("Erro na leitura do arquivo.");
		}
	}
}
