package utn.ai.ag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * VECTOR SOLUCION DE UBICACIONES DE LAS REINAS EN EL TABLERO
 */
public class Chromosome {
	
	private List<Gen> genes = new ArrayList<>();
	private int fitness = 0;
	private double aptitud = 0;

	public Chromosome(Integer[] values) {
		// configurar ubicacion de reinas
		for(int i=0; i<values.length; i++){
			int row = values[i];
			int column = i;
			Gen queen = new Gen(row, column);
			genes.add(queen);
		}
		// puntaje del cromosoma
		setFitness();
		setAptitud();
	}
	
	public void setFitness() {
		int clashes = 0;
		// posibles amenazas
		for(int i=0; i<genes.size(); i++) {
			for(int j=0; j<genes.size(); j++) {
				if(i != j) { // no comparamos mismas reinas
					int rx = genes.get(i).getRow();
					int ry = genes.get(j).getRow();
					if(rx == ry) { // filas
						clashes++;
					}
					
					int dx = Math.abs(rx - ry);
					int dy = Math.abs(genes.get(i).getColumn() - genes.get(j).getColumn());
					if(dx == dy) { // diagonales
						clashes++;
					}
				}
			}
		}
		this.fitness = clashes;
	}
	
	public int getFitness() {
		return this.fitness; // cuanto mas aproximado a 0 (cero) es mejor
	}
	
	public void setAptitud() {
		int nQueens = genes.size();
		int maxClashes = (nQueens * (nQueens-1)) / 2;
		this.aptitud = 100 - ((this.fitness * 100) / maxClashes);
	}
	
	public double getAptitud() {
		return this.aptitud; // porcentaje %
	}
	
	public List<Gen> getGenes() {
		return genes;
	}

	public void printInfo() {
		genes.stream().forEach(q -> System.out.print(q.getRow()+","));
		System.out.println(" --> Aptitud: " + this.aptitud + "% - Fitness: " + this.fitness);
	}
	
	public int[] getValues() {
		return genes.stream().mapToInt(Gen::getRow).toArray();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chromosome other = (Chromosome) obj;
		if (!Arrays.equals(getValues(), other.getValues()))
			return false;
		return true;
	}
	
	
}
