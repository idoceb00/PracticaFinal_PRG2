import java.util.ArrayList;

/**
 * Clase correspondiento a los objetos de tipo Solucion
 * 
 * @author Ignacio Doce Bedoya
 *
 */
public class Solucion {

	private int[][] matriz;
	private int n;
	private int m;
	private ArrayList<String[][]> solucione; // ArrayList que almacena cada una de los tableros solucionados
	private int numSoluciones; // Número de soluciones que tiene la matriz introducida por teclado

	/**
	 * Constructor de la clase Solucion del programa. Esta clase representa a los
	 * objetos de tipo Solucion, que trabajan sobre la matriz y almacenan las
	 * soluciones y el número de soluciones en forma de tableros solucionados de la
	 * patriz que tiene como atributo.
	 * 
	 * @param matriz matriz a solucionar por la clase
	 * @param n      número de filas de la matriz
	 * @param m      numero de columnas de la matriz
	 */
	public Solucion(int[][] matriz, int n, int m) {
		this.matriz = matriz;
		this.n = n;
		this.m = m;
		this.numSoluciones = 0;
		this.solucione = new ArrayList<String[][]>();

	}

	/**
	 * Devuelve el número de soluciones
	 * 
	 * @return numSOluciones
	 */
	public int getNumSoluciones() {
		return this.numSoluciones;
	}

	/**
	 * Modifica el valor del atributo numSoluciones
	 * 
	 * @param numSol nuevo número de soluciones.
	 */
	public void setNumSoluciones(int numSol) {
		this.numSoluciones = numSol;
	}

	/**
	 * Busca las soluciones posibles y las almacena en un arraylist
	 * 
	 */
	public void searchSolution(String[][] tablero, int fila, int columna) {
		if (tablero.length == (fila + 1) && tablero[0].length == (columna + 1)) {// estar en la última posición de la
																					// matrizSOlucion
			if (this.isSolution(tablero)) {
				// lo añado a las soluciones el tablero

				// Poner a positivos los tableros;
				int n = (this.n * 2) - 1;
				int m = (this.m * 2) - 1;

				for (int i = 0; i < n; i += 2) {
					for (int j = 0; j < m; j += 2) {
						int num = Integer.parseInt(tablero[i][j]);

						if (num < 0) {
							num = num * -1;
							tablero[i][j] = "" + num;
						}
					}
				}

				solucione.add(tablero);
			}
		} else {

			for (Movimientos movimiento : Movimientos.values()) { // for each que recorre el enum de los movimientos
				if (isValidPosMov(movimiento, fila, columna)) {
					if (isValidMovMov(tablero, movimiento, fila, columna)) {
						String[][] tableroNew = copy(tablero);

						int num = Integer.parseInt(tablero[fila][columna]) * -1;// Paso a negativo el número para tener
																				// una referencia de que esa casilla ya
																				// esta pisada
						tableroNew[fila][columna] = "" + num;

						tableroNew[fila + movimiento.getFila()][columna + movimiento.getCol()] = movimiento
								.getSimbolo();
						searchSolution(tableroNew, fila + movimiento.getFila() * 2, columna + movimiento.getCol() * 2);

					}
				}

			}
		}

	}

	/**
	 * Crea una copia del tablero que recibe como parámetro
	 * 
	 * @param tablero
	 * @return String[][] tablero copia del parametro.
	 */
	public String[][] copy(String[][] tablero) {

		int n = (this.n * 2) - 1;
		int m = (this.m * 2) - 1;

		String[][] copy = new String[n][m];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				copy[i][j] = tablero[i][j];
			}
		}

		return copy;
	}

	/**
	 * Comprueba que la la posción a la que me quiero mover entra dentro de las
	 * dimensiones de la matrizSolucion
	 * 
	 * @param movimiento a realizar
	 * @param fila       coordenada de la casilla del tablero en la que voy a
	 *                   realizar el movimiento
	 * @param columna    coordenada de la casilla del tablero en la que voy a
	 *                   realizar el movimiento
	 * @return true si la posición a la que lleva el movimiento es válida, false si
	 *         se sale de los límites.
	 */
	public Boolean isValidPosMov(Movimientos movimiento, int fila, int columna) {
		int filaPosMov = movimiento.getFila() * 2 + fila;
		int colPosMov = movimiento.getCol() * 2 + columna;

		if ((filaPosMov >= 0) && (filaPosMov < (this.n * 2) - 1)) {
			if ((colPosMov >= 0) && (colPosMov < (this.m * 2) - 1)) {
				return true;

			}
		}

		return false;
	}

	/**
	 * Comprueba que el movimiento es válido dentro de las normas del puzzle y que
	 * no he hecho el movimiento ese previamente ni que esté pisada la casilla
	 * 
	 * @param tablero    a solucionar
	 * @param movimiento a comprobar
	 * @param fila       coordenada de la casilla del tablero en la que voy a
	 *                   realizar el movimiento
	 * @param columna    coordenada de la casilla del tablero en la que voy a
	 *                   realizar el movimiento
	 * @return true si el movimiento es válido false si no lo es
	 */
	public Boolean isValidMovMov(String[][] tablero, Movimientos movimiento, int fila, int columna) {
		int filaMov = movimiento.getFila() * 2 + fila;
		int colMov = movimiento.getCol() * 2 + columna;

		int numMov = Integer.parseInt(tablero[filaMov][colMov]);
		int num = Integer.parseInt(tablero[fila][columna]);

		if (tablero[fila + movimiento.getFila()][columna + movimiento.getCol()] != null) {
			return false;
		}

		if (num == maxNum() && numMov == minNum()) {
			return true;

		} else if ((num + 1) == numMov) {
			return true;
		}

		return false;
	}

	/**
	 * Devuelve el número mayor de la matriz
	 * 
	 * @return max entero que representa al número mayor de la matriz
	 */
	public int maxNum() {
		int max = matriz[0][0];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (matriz[i][j] > max) {
					max = matriz[i][j];

				}
			}
		}

		return max;
	}

	/**
	 * Devuleve el número menor de la matriz;
	 * 
	 * @return min entero que representa el número menor de la matriz
	 */
	public int minNum() {
		int min = matriz[0][0];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (matriz[i][j] < min) {
					min = matriz[i][j];

				}
			}
		}

		return min;
	}

	/**
	 * Comprueba si el método searchSolution() encontro una solución de la matriz. Y
	 * en caso de que sea solución incrementa el numSoluciones del objeto
	 * 
	 * @return boolean true si el tablero que recibe como parámetro es una solución
	 *         valida, false si no lo es
	 */
	public boolean isSolution(String[][] tablero) {
		int n = (this.n * 2) - 1;
		int m = (this.m * 2) - 1;

		for (int i = 0; i < n; i += 2) {
			for (int j = 0; j < m; j += 2) {
				int num = Integer.parseInt(tablero[i][j]);

				if (num > 0) {
					// FIXED
					if (tablero.length != (i + 1) || (tablero.length == i + 1 && tablero[0].length != (j + 1))) {
						return false;
					}

				}
			}
		}
		numSoluciones = this.getNumSoluciones() + 1;
		setNumSoluciones(numSoluciones);

		return true;
	}

	/**
	 * Crea la matriz en la que se van a escribir las distintas soluciones al
	 * problema. Es decir crea las filas y columnas vacías del medio.
	 * 
	 * 
	 * @return matrizSolucion tablero en el que se van a escribir cada una de las
	 *         soluciones posibles
	 */

	public String[][] matrizSolution() {
		int n = (2 * this.n) - 1;
		int m = (2 * this.m) - 1;

		String[][] matrizSolucion = new String[n][m];

		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.m; j++) {
				matrizSolucion[i * 2][j * 2] = "" + matriz[i][j];
			}
		}

		return matrizSolucion;
	}

	/**
	 * Imprime por pantalla el número de soluciones de la matriz, y los tableros
	 * correspondientes a cada solución
	 * 
	 */
	public void imprimeSoluciones() {
		int n = (this.n * 2) - 1;
		int m = (this.m * 2) - 1;

		System.out.println(this.numSoluciones);

		for (int x = 0; x < this.solucione.size(); x++) {
			String[][] tableroSolucionStrings = solucione.get(x);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (tableroSolucionStrings[i][j] == null) {
						System.out.print(" ");

					} else {
						System.out.print(tableroSolucionStrings[i][j]);

					}
				}
				System.out.println();
			}
			if (x < this.solucione.size() - 1) {
				System.out.println();
			}
		}

	}

	public String[][] getSolution(int i) {
		// TODO Auto-generated method stub
		if(this.solucione.size() ==0) {
			return null;
		}
		return this.solucione.get(0);
	}

}