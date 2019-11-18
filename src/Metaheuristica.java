import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Metaheuristica {

	
	private static double currentTemp;
	private static double temp0;


	/* Ex1:
	 Usa a primeira busca local e implementa simulated annealing pra decidir se vai piorar a solucao.
	 https://link.springer.com/article/10.1007/s10732-007-9012-8
	 
	 Markov chains, usadas no artigo, foram ignoradas como fora de escopo.
	*/
	
	public static void metaheuristica1(SudokuGame problem, int maxLoops, double coolingRate, int runsWithoutTemp) {
		// coolingRate eh um parametro que define o quao rapido a temperatura da metaheuristica cai.
		// Se muito perto de 1, a temperatura cai muito devagar, fica muito aleatorio.
		// Se muito perto de 0, cai muito rapido e fica similar a busca local 1.
		// 0 < coolingRate (alfa) < 1
		if (coolingRate >= 1) {
			coolingRate = 0.999;
		} else if (coolingRate <= 0) {
			coolingRate = 0.001;
		}
		
		// runsWithoutTemp define quantas vezes roda sem a metaheuristica.
		// Os deltas das funcoes de avaliacao (new - old) sao salvos nesse array.
		double[] deltas = new double[runsWithoutTemp]; 
		
		
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
		
		// Contador auxiliar para guardar deltas e iniciar metaheuristica:
		int contAux = runsWithoutTemp - 1;
		
		// 3 - Enquanto a funcao de avaliacao nao for zero ou nao rodar um maximo de vezes, faz a troca na vizinhanca.
		while (evaluationAll > 0 && cont < maxLoops) {
			
			// Seleciona um quadrante aleatoriamente para avaliar.
			int quadrantId = seed.nextInt(n2);
			Quadrant quad = problem.getQuadrant(quadrantId);
			
			// -------------
			// Troca entre celulas - first improvement.
			int evaluationNew = evaluationAll;
			
			// Tenta por n^2 vezes trocar aleatoriamente um par de celulas.
			for (int i = 0; i < n2; i++) {
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
				
				int[] evaluationRowOld = evaluationRow.clone();
				int[] evaluationColumnOld = evaluationColumn.clone();
				
				// Troca e avalia.
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
				
	
				// Se nao for melhor do que a avaliacao anterior ou nao atingir a probabilidade necessaria, desfaz.
				evaluationNew = sumEvaluations(evaluationRow, evaluationColumn);
				boolean undoMove = false;
				
				if (contAux < 0) {
					
					if (contAux == -1) {
						temp0 = sd(deltas);
						contAux--;
					}
					
					// Roda metaheuristica.
					// Gera um numero aleatorio entre 0 e 1.
					double prob = seed.nextDouble();
					
					// Metaheuristica.
					int delta = evaluationNew - evaluationAll;
					double temperature = getTempRun(cont, runsWithoutTemp, coolingRate);
					double probChange = Math.exp(-delta/temperature);
					
					if (prob > probChange) {
						undoMove = true;
					}
				
				} else {
					// Guarda deltas.
					deltas[contAux] = evaluationNew - evaluationAll;
	
					// Busca local normal.
					if (evaluationNew >= evaluationAll) {
						undoMove = true;
					} 
					contAux--;
				}
				
				
				if (undoMove) {
					// Desfaz.
					evaluationNew = evaluationAll;
					problem.quadrantCellSwap(quadrantId, cell2Id, cell1Id);
					
					evaluationRow = evaluationRowOld;
					evaluationColumn = evaluationColumnOld;
				} else {
					// Aceitou o movimento, sai do loop.
					break;
				}
				
			} // n^2 tentativas.
				
			evaluationAll = evaluationNew;
			cont++;
			
		} // fim while
		
	}
	
	
	/**
	 * Calcula o desvio padrao de um array de numeros double.
	 * @param numArray double
	 * @return desvio padrao, um double.
	 */
	private static double sd(double[] numArray) {
		double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;
        
        for (double num : numArray) {
            sum += num;
        }
        
        double mean = sum/length;
        for (double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
	}



	/**
	 * Define a temperatura da rodada atual.
	 * @param run int, numero da rodada atual.
	 * @param runsWithoutTemp int, quantidade de rodadas em que nao houve metaheuristica.
	 * @param coolingRate double, parametro da metaheuristica.
	 * @return double, temperatura para a rodada.
	 */
	private static double getTempRun(int run, double runsWithoutTemp, double coolingRate) {
		double temperature = 0;
		
		if (run == runsWithoutTemp) {
			temperature = temp0;
			currentTemp = temp0;
		} else {
			temperature = coolingRate * currentTemp;
			currentTemp = temperature;
		}
		return temperature;
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
	
	
	/**
	 * Refaz a funcao de avaliacao para uma linha ou coluna.
	 * @param struct Row ou Column.
	 * @param evaluations int[] com funcoes de avaliacao correntes.
	 * @param candidates ArrayList com todos os candidatos possiveis.
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
	
	
	
	
	/* Ex2:
	 Tentar fazer busca tabu?
	 
	 */
	
	
	

	/**
	 * Mede a qualidade da solucao em:
	 * - tempo de execucao.
	 * - precisao das celulas.
	 * - precisao das linhas.
	 * - precisao das colunas.
	 * - precisao dos quadrantes.
	 * @param sudokuProblem
	 * @param sudokuSolution 
	 */
	public static void evaluatePerfomance(
			SudokuGame sudokuProblem, 
			SudokuGame sudokuSolution, 
			int maxLoops, double coolingRate, int runsWithoutTemp) {
		
		// Tempo.
		long startTime = System.nanoTime();
		
		Metaheuristica.metaheuristica1(sudokuProblem, maxLoops, coolingRate, runsWithoutTemp);
		
		long endTime = System.nanoTime();
		double timeElapsed = (endTime - startTime)/1000000;
		System.out.println("Milisegundos: " + timeElapsed);
		
		// Comparacao com a solucao.
		int equalCells = sudokuProblem.compareCells(sudokuSolution);
		double percentCell = (double) equalCells*100/sudokuProblem.getCells().length;
		System.out.println("% celulas: " + percentCell);
		
		int equalRows = sudokuProblem.compareRows(sudokuSolution);
		double percentRow = (double) equalRows*100/sudokuProblem.getRows().length;
		System.out.println("% linhas: " + percentRow);
		
		int equalCols = sudokuProblem.compareColumns(sudokuSolution);
		double percentColumn = (double) equalCols*100/sudokuProblem.getColumns().length;
		System.out.println("% colunas: " + percentColumn);
		
		int equalQuads = sudokuProblem.compareQuadrants(sudokuSolution);
		double percentQuad = equalQuads*100/sudokuProblem.getQuadrants().length;
		System.out.println("% quadrados: " + percentQuad);
		
		System.out.println("---");
		
	}
	
	
	
	
}
