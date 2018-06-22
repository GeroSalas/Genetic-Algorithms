package utn.ai.ag;

import java.io.Serializable;

public class Inputs  implements Serializable {

	// N reinas --> tablero NxN
	private int n_queens;
	
	// numero de individuos en poblacion inicial
	private int poblation_count;		
	
	// maximo numero de iteraciones (corte)
	private int max_generations;	
	
	// probabilidad de mutacion
	private int p_mutations;

	public Inputs(int n_queens, int poblation_count, int max_generations, int p_mutations) {
		super();
		this.n_queens = n_queens;
		this.poblation_count = poblation_count;
		this.max_generations = max_generations;
		this.p_mutations = p_mutations;
	}

	public int getN_queens() {
		return n_queens;
	}

	public void setN_queens(int n_queens) {
		this.n_queens = n_queens;
	}

	public int getPoblation_count() {
		return poblation_count;
	}

	public void setPoblation_count(int poblation_count) {
		this.poblation_count = poblation_count;
	}

	public int getMax_generations() {
		return max_generations;
	}

	public void setMax_generations(int max_generations) {
		this.max_generations = max_generations;
	}

	public int getP_mutations() {
		return p_mutations;
	}

	public void setP_mutations(int p_mutations) {
		this.p_mutations = p_mutations;
	}	
	
}
