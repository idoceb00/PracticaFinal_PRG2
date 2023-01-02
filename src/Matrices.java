import java.util.ArrayList;

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
	public String[][] deshacer() {
		restar();

		String[][] deshacer = matrices.get(count);

		return deshacer;
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

		} else if (this.count == max) {
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
