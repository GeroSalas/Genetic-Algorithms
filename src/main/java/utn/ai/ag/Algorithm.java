package utn.ai.ag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Main Class - Algoritmo Genetico para resolver Problemas de las N Reinas
 * jGAP Reference: https://karczmarczuk.users.greyc.fr/TEACH/IAD/java/jgap_tutorial.html
 * 
 * @author GeronimoSalas
 *
 */
public class Algorithm {

	// default inputs
	public static int N_QUEENS = 8;
	public static int POBLATION_COUNT = 100;		
	public static int MAX_GENERATIONS = 5000;
	public static double P_MUTATION = 0.02;
	
	public static Solution execute(int queens, int poblation, int iterations, int mutation) {
    	// timer start
    	long startTime = System.currentTimeMillis();    
    	
    	N_QUEENS = queens;
    	POBLATION_COUNT = poblation;
    	MAX_GENERATIONS = iterations;
    	P_MUTATION = mutation;
    	
    	Utils.log("INPUTS:");
    	Utils.log("N_QUEENS = " + N_QUEENS);
    	Utils.log("POBLATION_COUNT = " + POBLATION_COUNT);
    	Utils.log("MAX_GENERATIONS = " + MAX_GENERATIONS);
    	Utils.log("P_MUTATION = " + P_MUTATION);
    	Utils.log("----------------------------");
    	/*************************************************************
    	 * 1)
    	 */
    	Utils.log("Inicializando poblacion...");
    	List<Chromosome> poblacion = generateInitialPoblation();
    	Chromosome optimo =  null;
    	
        for(int generation=1; generation<=MAX_GENERATIONS; generation++) {
        	Utils.log("Generacion #" + generation);
            /*************************************************************
        	 * 2)
        	 */
            Utils.log("Evaluacion de individuos con seleccion elitista...");
            
            // seleccionamos los mejores (top 2) para ser los padres 
            poblacion.sort(Comparator.comparingInt(Chromosome::getFitness));
            Chromosome padreA = poblacion.get(0);
            Chromosome padreB = poblacion.get(1);
            
            // los padres deben ser diferentes para lograr mejores hijos
            int rankingOrder = 1;
            while(padreA.equals(padreB)) {
            	rankingOrder++;
            	padreB = poblacion.get(rankingOrder);
            }
            
            padreA.printInfo();
            padreB.printInfo();
            
            // verificamos si ya tenemos una solucion con aptitud optima para cortar
            if(padreA.getFitness() == 0 || padreB.getFitness() == 0) {
            	optimo = (padreA.getFitness() == 0) ? padreA : padreB;
            	Utils.log("SOLUCION OPTIMA ENCONTRADA!");
                optimo.printInfo(); 
            	break;
            }
            
            /***************************************************************
        	 * 3)
        	 */
            Utils.log("One-Point Crossover...");
            
            List<Chromosome> nuevos = new ArrayList<>();
            nuevos.add(padreA);
            nuevos.add(padreB);
            // reproducimos nuevos hijos manteniendo los padres
            for(int i=0; i<(POBLATION_COUNT-2)/2; i++) {
            	int a = (Math.random() > 0.5) ? 1 : 0;
            	int b = (a == 1) ? 0 : 1;
            	int punto = randomNumber(N_QUEENS); // one-point aleatorio
            	//1
            	List<Gen> subgenesA1 = nuevos.get(a).getGenes().stream().limit(punto).collect(Collectors.toList());
            	List<Gen> subgenesB1 = nuevos.get(b).getGenes().stream().skip(punto).collect(Collectors.toList());
            	List<Gen> mezcla1 = new ArrayList<>(subgenesA1);
            	mezcla1.addAll(subgenesB1);
            	//2
            	List<Gen> subgenesA2 = nuevos.get(a).getGenes().stream().skip(punto).collect(Collectors.toList());
            	List<Gen> subgenesB2 = nuevos.get(b).getGenes().stream().limit(punto).collect(Collectors.toList());
            	List<Gen> mezcla2 = new ArrayList<>(subgenesA2);
            	mezcla2.addAll(subgenesB2);
            	
            	Integer[] values1 = new Integer[N_QUEENS];
            	Integer[] values2 = new Integer[N_QUEENS];
            	for(int v=0; v<N_QUEENS; v++) {
            		values1[v] = mezcla1.get(v).getRow();
            		values2[v] = mezcla2.get(v).getRow();
            	}
            	
            	// nuevos hijos reproducidos
            	Chromosome hijo1 = new Chromosome(values1);
            	Chromosome hijo2 = new Chromosome(values1);
            	nuevos.add(hijo1);
            	nuevos.add(hijo2);
            	
            	//hijo1.printInfo();
            	//hijo2.printInfo();
            }
            
            /***************************************************************
        	 * 4)
        	 */
            Utils.log("Mutacion...");
            int count = 0;
            for(int c=0; c<nuevos.size(); c++) {
            	Chromosome child = nuevos.get(c);
            	if(Math.random() <= P_MUTATION && c > 1) { // no mutan los padres
            		int[] excludeValues = child.getGenes().stream().mapToInt(Gen::getRow).distinct().toArray();
            		// solo mutaremos las reinas en filas repetidas para tratar de lograr un mejor resultado aleatorio 		
            		for(int i=0; i<N_QUEENS; i++) {
            			for(int j=0; j<N_QUEENS; j++) {
                			if(i != j) {
                				int rowI = child.getGenes().get(i).getRow();
                				int rowJ = child.getGenes().get(j).getRow();
                				if(rowI == rowJ) {
                					int mutatedValue = randomNumber(N_QUEENS, excludeValues);
                					child.getGenes().get(j).setRow(mutatedValue);
                					// update puntaje para evaluacion
                					child.setFitness();
                					child.setAptitud();
                					//child.printInfo();
                					count++;
                				}
                			}
                    	}
                	}        		
            	}
            }
            Utils.log("Total de mutaciones: " + count);
            
            // poblacion evolucionada para ser evaluada en la siguiente iteracion
            poblacion = new ArrayList<>(nuevos);
            Collections.shuffle(poblacion);
        }
        
        // RESULTADO
        if(optimo == null) {
        	poblacion.sort(Comparator.comparingInt(Chromosome::getFitness));
        	optimo = poblacion.get(0);
        	Utils.log("SOLUCION MAS OPTIMA ENCONTRADA");
            optimo.printInfo();   
        }   
        
        // timer stop
    	long stopTime = System.currentTimeMillis();
    	long timing = stopTime - startTime;
    	Utils.log("Tiempo de ejecucion: " + timing + "ms");
    	
    	return new Solution(optimo);
    }
    
	
	private static List<Chromosome> generateInitialPoblation() {
		// individuo base
        Integer[] inicio = new Integer[N_QUEENS];
        for(int i=0; i<N_QUEENS; i++) {
        	inicio[i] = i+1; // esto nos permite garantizar en #1 que no generamos amenazas entre filas y columnas
        }
        
        // poblacion inicial de individuos aleatorios
        List<Chromosome> poblacion = new ArrayList<>();
        for(int i=0; i<POBLATION_COUNT; i++) {
        	Integer[] random = shuffleArray(inicio);
        	Chromosome cromosoma = new Chromosome(random);
        	
        	// debug individuos generados
        	//Utils.log(i+") ");
        	//cromosoma.printInfo();
        	
        	poblacion.add(cromosoma);
        }
        return poblacion;
	}
	
    private static Integer[] shuffleArray(Integer[] array) {
		List<Integer> v = Arrays.asList(array);
		Collections.shuffle(v);
		Integer[] tmp = new Integer[v.size()];
		return v.toArray(tmp);
    }
    
    private static int randomNumber(int max, int... exclude) {
    	Random random = new Random();
    	int num = 1 + random.nextInt(max - exclude.length);
    	for (int ex : exclude) {
            if (num < ex) {
                break;
            }
            num++;
        }
    	return num;
    }
    
}
