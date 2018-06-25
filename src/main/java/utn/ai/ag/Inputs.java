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
	private double p_mutations;
	
	// metodo de corte
	private int f_method;
	
	// metodo de seleccion
	private int selection_type;
	
	// tipo de crossover
	private int crossover_point;
	
	public Inputs() {}

	public Inputs(int n_queens, int poblation_count, int max_generations, double p_mutations, int f_method, int selection_type, int crossover_point) {
		this.n_queens = n_queens;
		this.poblation_count = poblation_count;
		this.max_generations = max_generations;
		this.p_mutations = p_mutations;
		this.f_method = f_method;
		this.selection_type = selection_type;
		this.crossover_point = crossover_point;
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

	public double getP_mutations() {
		return p_mutations;
	}

	public void setP_mutations(double p_mutations) {
		this.p_mutations = p_mutations;
	}

	public int getF_method() {
		return f_method;
	}

	public void setF_method(int f_method) {
		this.f_method = f_method;
	}

	public int getCrossover_point() {
		return crossover_point;
	}

	public void setCrossover_point(int crossover_point) {
		this.crossover_point = crossover_point;
	}

	public int getSelection_type() {
		return selection_type;
	}

	public void setSelection_type(int selection_type) {
		this.selection_type = selection_type;
	}
	
}
