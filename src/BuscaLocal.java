import java.util.ArrayList;
import java.util.Random;

public abstract class BuscaLocal {
	
	/* Ex1:
	 Usar a busca local descrita aqui, para depois melhorar para simulated annealing:
	 https://link.springer.com/article/10.1007/s10732-007-9012-8
	 
	 Passo a passo:
	 1. Solucao candidata inicial eh formada assim:
	 	1.1. Sudoku eh percorrido uma vez e cada celula vazia recebe um valor aleatorio, 
	 	mas, a cada quadrado, eh garantido que ele contem os numeros de 1 a n^2 apenas uma vez.
	 2. Sudoku eh percorrido mais uma vez e sao calculadas:
	 	2.1. Funcao de avaliacao por linha (avaliar quantos numeros unicos faltam na linha).
	 	2.2. Funcao de avaliacao por coluna (avaliar quantos numeros unicos faltam na coluna).
	 	2.3. Funcao de avaliacao do jogo completo (somatorio das funcões de coluna e linha).
	 3. Enquanto a funcao de avaliacao do jogo completo nao for zero:
	 	3.1. Uma vizinhanca eh definida como uma troca qualquer entre duas celulas nao fixas de um quadrado.
	 	Duas celulas sao escolhidas aleatoriamente e sao trocadas. As funcoes de avaliacao correspondentes sao
	 	recalculadas.
	 */
	public static void heuristica1(SudokuProblem problem) {
		Random seed = new Random();
		
		// 1 - Solucao candidata inicial:
		
		// Guarda um array com candidatos do problema:
		ArrayList<Character> candidates = new ArrayList<Character>();
		for (char c : problem.problemDomain) { 
			candidates.add(c);
		}	
		
		
		for (Quadrant quad : problem.getQuadrants()) {		
			// Remocao de candidatos que ja aparecem no quadrante:
			ArrayList<Character> candidatesQuad = (ArrayList<Character>) candidates.clone();

			for (Cell cell : quad.cells) {
				if (cell.getValue() != '0') {
					candidatesQuad.remove((Character) cell.getValue());
	            }
			}
			
			// Seleciona aleatoriamente dos candidatos restantes pra preencher as celulas vazias:
			for (Cell cell : quad.cells) {
				if (cell.getValue() == '0') {
					int candidateId = seed.nextInt(candidatesQuad.size());
					cell.setValue(candidatesQuad.get(candidateId));
					candidatesQuad.remove(candidateId);
	            }
			}
		}
		
		
		// 2 - Calculo das funcoes de avaliacao:
		int n2 = (int) Math.pow(problem.getnSize(), 2);
		int[] evaluationRow = new int[n2]; 
		int[] evaluationColumn = new int[n2]; 
		int evaluationAll = 0;
		int cont = 0;
		
	 	for (Row row : problem.getRows()) {
	 		// Candidatos que faltam por linha:
			ArrayList<Character> candidatesRow = (ArrayList<Character>) candidates.clone();
			for (Cell cell : row.cells) {
				if (cell.getValue() != '0') {
					candidatesRow.remove((Character) cell.getValue());
	            }
			}
			
			evaluationRow[cont] = candidatesRow.size();
			cont++;
	 	}
	 	
	 	cont = 0;
	 	for (Column column : problem.getColumns()) {
	 		// Candidatos que faltam por coluna:
			ArrayList<Character> candidatesColumn = (ArrayList<Character>) candidates.clone();
			for (Cell cell : column.cells) {
				if (cell.getValue() != '0') {
					candidatesColumn.remove((Character) cell.getValue());
	            }
			}
			
			evaluationColumn[cont] = candidatesColumn.size();
			cont++;
	 	}
		
		evaluationAll = sumEvaluations(evaluationRow, evaluationColumn);
		
		cont = 0;
		// Enquanto a funcao de avaliacao nao for zero, faz a troca na vizinhanca.
		while (evaluationAll > 0 && cont < 10000) {
			
			// Troca entre duas celulas diferentes e nao fixas de um quadrado aleatorio.
			int quadrantId = seed.nextInt(n2);
			Quadrant quad = problem.getQuadrant(quadrantId);
			
			int cell1Id = 0;
			int cell2Id = 0;
			
			while (
					cell1Id == cell2Id || 
					quad.cells[cell1Id].fixed || 
					quad.cells[cell2Id].fixed
				) {
				cell1Id = seed.nextInt(n2);
				cell2Id = seed.nextInt(n2);
			}
			
			problem.quadrantCellSwap(quadrantId, cell1Id, cell2Id);
					
		
			// Recalcula as linhas e colunas trocadas.
			evaluationRow = reevaluate(quad.getCell(cell1Id).getRow(), evaluationRow, candidates);
			if (quad.getCell(cell1Id).getRow() != quad.getCell(cell2Id).getRow()) {
				evaluationRow = reevaluate(quad.getCell(cell2Id).getRow(), evaluationRow, candidates);
			} 
			
			evaluationColumn = reevaluate(quad.getCell(cell1Id).getColumn(), evaluationColumn, candidates);
			if (quad.getCell(cell1Id).getColumn() != quad.getCell(cell2Id).getColumn()) {
				evaluationColumn = reevaluate(quad.getCell(cell2Id).getColumn(), evaluationColumn, candidates);
			} 
			
			evaluationAll = sumEvaluations(evaluationRow, evaluationColumn);
			cont++;
		}
	}
	
	
	/**
	 * Refaz a funcao de avaliacao para uma linha ou coluna.
	 * @param struct Row ou Column.
	 * @param evaluations int[] com funcoes de avaliacao correntes.
	 * @param candidates ArrayList com todos os candidaos possiveis.
	 * @return int[] com funcoes de avaliacao atualizadas.
	 */
	private static int[] reevaluate(SudokuStructure struct, int[] evaluations, ArrayList<Character> candidates) {
		ArrayList<Character> candidatesClone = (ArrayList<Character>) candidates.clone();
		for (Cell cell : struct.cells) {
			if (cell.getValue() != '0') {
				candidatesClone.remove((Character) cell.getValue());
            }
		}
		
		evaluations[struct.id - 1] = candidatesClone.size();
		return evaluations;
	}
	
	
	/**
	 * Soma as funcoes de avaliacao pra linha e coluna.
	 */
	private static int sumEvaluations(int[] evalRow, int[] evalColumn) {
		int sum = 0;
		for (int r : evalRow) {
			sum += r;
		}
		for (int c : evalColumn) {
			sum += c;
		}
		return sum;
	}
	


	/* Ex2:
	 Usar a busca local descrita aqui:
	 http://vigusmao.github.io/manuscripts/sudoku.pdf
	 Nesse caso, faz Hill Climbing e escolhe a vizinhanca de melhor custo.
	 O custo eh calculado pela soma da quantidade de digitos que faltam ser inseridos em todo o tabuleiro.
	 
	 Passo a passo:
	 1. Sudoku eh percorrido uma vez e sao calculadas:
	 	1.1. Funcao de avaliacao por linha (avaliar quantos numeros unicos faltam na linha).
	 	1.2. Funcao de avaliacao por coluna (avaliar quantos numeros unicos faltam na coluna).
	 	1.3. Funcao de avaliacao do jogo completo (somatorio das funcões de coluna e linha).
	 2. Enquanto a funcao de avaliacao do jogo completo nao for zero:
	 	2.1. Cada celula vazia eh preenchida com o melhor candidato da vizinhanca.
	 	A vizinhanca nesse caso eh definida como o conjunto de numeros de 1 a n^2, e a funcao de avaliacao de cada
	 	um eh dada pelo numero de repeticoes do candidato numa linha ou coluna em que sera inserido.
	 
	 */
	
	public static void heuristica2() {
		
	}

}
