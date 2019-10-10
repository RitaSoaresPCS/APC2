package heuristica;

public class BuscaLocal {
	
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

}
