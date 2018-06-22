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
public class App {
	// N reinas --> tablero NxN
	public static final int N_QUEENS = 8;
	
	// numero de individuos en poblacion inicial
	public static final int POBLATION_COUNT = 100;		
	
	// maximo numero de iteraciones (corte)
	public static final int MAX_GENERATIONS = 1000;
	
	// probabilidad de mutacion
	public static final double P_MUTATION = 0.05;
	
	// funcion fitness (valor objetivo) --> N*(N-1)/2
	public static final int FITNESS = (N_QUEENS*(N_QUEENS-1)) / 2;
	
	// un posible vector solucion en tablero 8x8
	public static final Integer[] V_SOLUTION = new Integer[] { 2,4,6,8,3,1,7,5 };
	
    
	public static void main(String[] args) {
    	// timer start
    	long startTime = System.currentTimeMillis();    
    	
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
            
            while(padreA.equals(padreB)) {
            	int tops = (int) poblacion.stream().filter(c -> c.getFitness() == padreA.getFitness()).count();
            	padreB = poblacion.get(randomNumber(tops)); // azar
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
            for(int i=0; i<POBLATION_COUNT-2; i++) {
            	int a = (Math.random() > 0.5) ? 1 : 0;
            	int b = (a == 1) ? 0 : 1;
            	int punto = randomNumber(N_QUEENS); // one-point aleatorio
            	List<Gen> subgenesA = nuevos.get(a).getGenes().stream().limit(punto).collect(Collectors.toList());
            	List<Gen> subgenesB = nuevos.get(b).getGenes().stream().skip(punto).collect(Collectors.toList());
            	
            	List<Gen> mezcla = new ArrayList<>(subgenesA);
            	mezcla.addAll(subgenesB);
            	
            	Integer[] values = new Integer[N_QUEENS];
            	for(int v=0; v<N_QUEENS; v++) {
            		values[v] = mezcla.get(v).getRow();
            	}
            	
            	Chromosome cromosoma = new Chromosome(values);
            	nuevos.add(cromosoma);
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
        }
        
        // RESULTADO
        if(optimo == null) {
        	optimo = poblacion.get(0);
        	Utils.log("SOLUCION MAS OPTIMA ENCONTRADA");
            optimo.printInfo();     
        }   
        
        // timer stop
    	long stopTime = System.currentTimeMillis();
    	long timing = stopTime - startTime;
    	Utils.log("Tiempo de ejecucion: " + timing + "ms");
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
