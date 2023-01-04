/**
 * Enum de los movimientos posibles a la hora de realizar el puzzle
 * 
 * @author Ignacio Doce Bedoya
 *
 */
public enum Movimientos {
	DiagIzqArriba(-1, -1, "\\"), Arriba(-1, 0, "|"), DiagDerArriba(-1, 1, "/"), Izquierda(0, -1, "-"),
	Derecha(0, 1, "-"), DiagIzqAbajo(1, -1, "/"), Abajo(1, 0, "|"), DiagDerAbajo(1, 1, "\\");

	private int fila;
	private int columna;
	private String simbolo;

	/**
	 * Constructor de los Movimientos
	 * 
	 * @param fila    movimiento con respecto a la fila
	 * @param columna movimiento con respecto a la columna
	 * @param simbolo simbolo que corresponde al movimiento
	 */
	private Movimientos(int fila, int columna, String simbolo) {
		this.fila = fila;
		this.columna = columna;
		this.simbolo = simbolo;
	}

	/**
	 * Devueleve el movimiento con respecto a la fila
	 * 
	 * @return
	 */
	public int getFila() {
		return this.fila;
	}

	/**
	 * Devueleve el movimiento con respecto a la columna
	 * 
	 * @return
	 */
	public int getCol() {
		return this.columna;
	}

	/**
	 * Devueleve el el símbolo que corresponde al movimiento
	 * 
	 * @return
	 */
	public String getSimbolo() {
		return this.simbolo;

	}
}
