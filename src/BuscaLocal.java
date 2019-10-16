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
	 3. Enquanto a funcao de avaliacao do jogo completo nao for zero ou a heuristica nao rodar 
	 um numero maximo de vezes:
	 	3.1. Uma vizinhanca eh definida como uma troca qualquer entre duas celulas nao fixas de um quadrado.
	 	Duas celulas sao escolhidas aleatoriamente e sao trocadas. As funcoes de avaliacao correspondentes sao
	 	recalculadas.
	 */
	public static void heuristica1(SudokuProblem problem, int maxLoops) {
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
		// 3 - Enquanto a funcao de avaliacao nao for zero ou nao rodar um maximo de vezes, faz a troca na vizinhanca.
		while (evaluationAll > 0 && cont < maxLoops) {
			
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
			if (quad.getCell(cell1Id).getRow().id != quad.getCell(cell2Id).getRow().id) {
				evaluationRow = reevaluate(quad.getCell(cell2Id).getRow(), evaluationRow, candidates);
			} 
			
			evaluationColumn = reevaluate(quad.getCell(cell1Id).getColumn(), evaluationColumn, candidates);
			if (quad.getCell(cell1Id).getColumn().id != quad.getCell(cell2Id).getColumn().id) {
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
	 * Soma funcoes de avaliacao (linha, coluna ou quadrante).
	 */
	private static int sumEvaluations(int[] eval1, int[] eval2) {
		int sum = 0;
		for (int r : eval1) {
			sum += r;
		}
		for (int c : eval2) {
			sum += c;
		}
		return sum;
	}
	


	/* Ex2:
	 Usar a busca local descrita aqui, para depois melhorar para simulated annealing:
	 https://link.springer.com/article/10.1007/s10732-007-9012-8
	 
	 Passo a passo:
	 1. Solucao candidata inicial eh formada assim:
	 	1.1. Sudoku eh percorrido uma vez e cada celula vazia recebe um valor aleatorio, 
	 	mas, a cada linha, eh garantido que ela contem os numeros de 1 a n^2 apenas uma vez.
	 2. Sudoku eh percorrido mais uma vez e sao calculadas:
	 	2.1. Funcao de avaliacao por quadrante (avaliar quantos numeros unicos faltam no quadrante).
	 	2.2. Funcao de avaliacao por coluna (avaliar quantos numeros unicos faltam na coluna).
	 	2.3. Funcao de avaliacao do jogo completo (somatorio das funcões de coluna e quadrante).
	 3. Enquanto a funcao de avaliacao do jogo completo nao for zero ou a heuristica nao rodar 
	 um numero maximo de vezes:
	 	3.1. Uma vizinhanca eh definida como uma troca qualquer entre duas celulas nao fixas de uma linha.
	 	Duas celulas sao escolhidas aleatoriamente e sao trocadas. As funcoes de avaliacao correspondentes sao
	 	recalculadas.
	 */
	public static void heuristica2(SudokuProblem problem, int maxLoops) {
		Random seed = new Random();
		
		// 1 - Solucao candidata inicial:
		
		// Guarda um array com candidatos do problema:
		ArrayList<Character> candidates = new ArrayList<Character>();
		for (char c : problem.problemDomain) { 
			candidates.add(c);
		}	
		
		
		for (Row row : problem.getRows()) {		
			// Remocao de candidatos que ja aparecem na linha:
			ArrayList<Character> candidatesRow = (ArrayList<Character>) candidates.clone();

			for (Cell cell : row.cells) {
				if (cell.getValue() != '0') {
					candidatesRow.remove((Character) cell.getValue());
	            }
			}
			
			// Seleciona aleatoriamente dos candidatos restantes pra preencher as celulas vazias:
			for (Cell cell : row.cells) {
				if (cell.getValue() == '0') {
					int candidateId = seed.nextInt(candidatesRow.size());
					cell.setValue(candidatesRow.get(candidateId));
					candidatesRow.remove(candidateId);
	            }
			}
		}
		
		
		// 2 - Calculo das funcoes de avaliacao:
		int n2 = (int) Math.pow(problem.getnSize(), 2);
		int[] evaluationQuad = new int[n2]; 
		int[] evaluationColumn = new int[n2]; 
		int evaluationAll = 0;
		int cont = 0;
		
	 	for (Quadrant quad : problem.getQuadrants()) {
	 		// Candidatos que faltam por quadrante:
			ArrayList<Character> candidatesQuad = (ArrayList<Character>) candidates.clone();
			for (Cell cell : quad.cells) {
				if (cell.getValue() != '0') {
					candidatesQuad.remove((Character) cell.getValue());
	            }
			}
			
			evaluationQuad[cont] = candidatesQuad.size();
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
		
		evaluationAll = sumEvaluations(evaluationQuad, evaluationColumn);
		
		cont = 0;
		// 3 - Enquanto a funcao de avaliacao nao for zero ou nao rodar um maximo de vezes, 
		// faz a troca na vizinhanca.
		while (evaluationAll > 0 && cont < maxLoops) {
			
			// Troca entre duas celulas diferentes e nao fixas de uma linha aleatoria.
			int rowId = seed.nextInt(n2);
			Row row = problem.getRow(rowId);
			
			int cell1Id = 0;
			int cell2Id = 0;
			
			while (
					cell1Id == cell2Id || 
					row.cells[cell1Id].fixed || 
					row.cells[cell2Id].fixed
				) {
				cell1Id = seed.nextInt(n2);
				cell2Id = seed.nextInt(n2);
			}
			
			problem.rowCellSwap(rowId, cell1Id, cell2Id);
					
		
			// Recalcula os quadrantes e colunas trocados.
			evaluationQuad = reevaluate(row.getCell(cell1Id).getQuadrant(), evaluationQuad, candidates);
			if (row.getCell(cell1Id).getQuadrant().id != row.getCell(cell2Id).getQuadrant().id) {
				evaluationQuad = reevaluate(row.getCell(cell2Id).getQuadrant(), evaluationQuad, candidates);
			} 
			
			evaluationColumn = reevaluate(row.getCell(cell1Id).getColumn(), evaluationColumn, candidates);
			if (row.getCell(cell1Id).getColumn().id != row.getCell(cell2Id).getColumn().id) {
				evaluationColumn = reevaluate(row.getCell(cell2Id).getColumn(), evaluationColumn, candidates);
			} 
			
			evaluationAll = sumEvaluations(evaluationQuad, evaluationColumn);
			cont++;
		}
		
	}
	
	
}
