import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JOptionPane;

/**
 * Clase agregado que almacena en un ArrayList la evolución de la matriz según
 * la interacción del usuario con el programa
 * 
 * @author Ignacio Doce Bedoya
 *
 */
public class Matrices {
	private ArrayList<String[][]> matrices;
	private int count;

	/**
	 * Constructor de la clase Matrices
	 */
	public Matrices() {
		this.matrices = new ArrayList<>();
		this.count = 0;

	}

	/**
	 * Método que deshace un cambio hecho en la matriz por petición del usuario
	 * 
	 * @return matriz anterior al cambio
	 */
	public String[][] deshacer(String[][] matriz) {
		if (this.count == this.matrices.size()) {
			if (!(compruebaActual(matriz))) {
				insertLastMatriz(matriz);
				restar();
			}
		}

		restar();

		String[][] deshacer = matrices.get(count);

		return deshacer;
	}

	/**
	 * Comprueaba que la matriz que recibe como parámetro que equivale a la que está
	 * viendo el usuario por pantalla, sea igual a la que esta almacenada en la
	 * última posición del agregado
	 * 
	 * @param matriz que está viendo actualmente el usuario por pantalla
	 * @return true si coinciden las matrices, false si no coinciden
	 */
	public Boolean compruebaActual(String[][] matriz) {
		boolean compruebaActual = true;
		String[][] matrizMatrices = this.matrices.get(this.count - 1);

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				if (!Objects.equals(matriz[i][j], matrizMatrices[i][j])) {
					compruebaActual = false;
					break;
				}
			}
		}

		return compruebaActual;
	}

	/**
	 * Añade la matriz que está viendo el usuario por pantallas a la última posición
	 * del agregado.
	 * 
	 * @param matriz que está viendo el usuario por pantalla
	 */
	public void insertLastMatriz(String[][] matriz) {
		int filas = matriz.length;
		int columnas = matriz[0].length;

		String[][] lastMatriz = new String[filas][columnas];

		for (int i = 0; i < filas; i++) {
			for (int j = 0; j < columnas; j++) {
				lastMatriz[i][j] = matriz[i][j];
			}
		}

		this.matrices.add(lastMatriz);
		count++;
	}

	/**
	 * Método que recupera un cambio hecho en la matriz por petición del usuario
	 * 
	 * @return matriz con el cambio hecho previamente
	 */
	public String[][] rehacer() {
		if (checkRehacer()) {
			return matrices.get(count);

		} else {
			return null;

		}
	}

	/**
	 * Comprueba que es posible rehacer el cambio hecho en la matriz, es decir si
	 * existe, una matriz en un índice del ArrayList matrices uno mayor que count
	 * 
	 * @return true si se puede rehacer, false si no se puede rehacer
	 */
	public Boolean checkRehacer() {
		int max = this.matrices.size() - 1;

		if (this.count < max) {
			this.count++;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Añade una nueva matriz al agregado y incrementa en un valor el count
	 * 
	 * @param matriz a anyadir al ArrayList
	 */
	public void sumar(String[][] matriz) {
		if (this.count != this.matrices.size()) { // En caso de hacer un cambio despues de haber deshecho, elimina las
													// matrices de las posiciones siguientes, imposibilitando la función
													// de rehacer
			for (int i = count + 1; i < this.matrices.size(); i++) {
				this.matrices.remove(i);
			}
		}

		this.matrices.add(matriz);
		this.count++;

	}

	/**
	 * Decrementa en un valor el count con el fin de deshacer un cambio, y mostrar
	 * la matriz anterior a la que se encontraba en el window
	 * 
	 */
	public void restar() {
		if (this.count > 0) {
			this.count--;

		} else {
			JOptionPane.showMessageDialog(null, "NO SE PUEDE DESHACER");

		}
	}
}
