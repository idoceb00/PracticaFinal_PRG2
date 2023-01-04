import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.text.TableView.TableRow;

/**
 * Clase que implementa los objetos de tipo Window
 * 
 * @author Ignacio Doce Bedoya
 *
 */
public class Window extends JFrame implements ActionListener {

	private JPanel jPanel; // JPanel principal que varia sus componentes dependiendo del frame que se
							// accione

	private JMenuItem abrirJMenuItem; // Item correspondiente a la acción de abrir
	private JMenuItem guardarJMenuItem; // Item correspondiente a la acción de guardar
	private JMenuItem guardarComoJMenuItem; // Item correspondiente a la acción de guardar como
	private JMenuItem jugarJMenuItem; // Item correspondiente a la acción de jugar
	private JMenuItem deshacerJMenuItem; // Item correspondiente a la acción de deshacer
	private JMenuItem rehacerJMenuItem; // Item correspondiente a la acción de rehacer
	private JMenuItem ayudaJMenuItem; // Item correspondiente a la acción de ayuda
	private JMenuItem crearJMenuItem; // Item correspondiente a la acción de crear

	private String path; // Ruta de directorios en la que se almacena el fichero.

	private String[][] matriz;

	private String estado;

	private Matrices matrices;

	private boolean edit;

	private ArrayList<String[][]> almacenJuego;

	private JuegoCellTablero cellTabOrigin;

	private JuegoCellTablero cellTabDest;

	/**
	 * Constructor de los objetos de tipo Window
	 */
	public Window() {
		this.setLocationRelativeTo(null);
		this.setTitle("Puzzle ZigZag");
		this.setSize(700, 700);

		BorderLayout borderLayout = new BorderLayout();
		this.setLayout(borderLayout);

		this.jPanel = new JPanel();

		this.add(this.jPanel, BorderLayout.CENTER);

		this.matrices = new Matrices();

		this.edit = true;

		this.almacenJuego = new ArrayList<String[][]>();

		this.cellTabDest = null;

		this.cellTabOrigin = null;

		menuBarMethod();

		this.setVisible(true);
	}

	/**
	 * Método que crea la barra de herramientas con los menus e items
	 * correspondientes
	 * 
	 */
	public void menuBarMethod() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileJMenu = new JMenu("Archivo");// Menu archivo
		JMenu editJMenu = new JMenu("Editar");// Menu editar

		// Inicializa el item "abrir" y le da una acción
		this.abrirJMenuItem = new JMenuItem("Abrir");
		this.abrirJMenuItem.addActionListener(this);

		// Da función al item "guardar" y al "guardar como" y los inicializa
		this.guardarJMenuItem = new JMenuItem("Guardar");
		this.guardarJMenuItem.addActionListener(this);
		this.guardarComoJMenuItem = new JMenuItem("Guardar Como");
		this.guardarComoJMenuItem.addActionListener(this);

		// Inicializa el item Jugar y le de acción
		this.jugarJMenuItem = new JMenuItem("Jugar");
		this.jugarJMenuItem.addActionListener(this);

		// Inicializa los items "rehacer" y "deshacer" del menu editar y les da una
		// acción
		this.deshacerJMenuItem = new JMenuItem("Deshacer");
		this.deshacerJMenuItem.addActionListener(this);
		this.rehacerJMenuItem = new JMenuItem("Rehacer");
		this.rehacerJMenuItem.addActionListener(this);

		this.ayudaJMenuItem = new JMenuItem("Ayuda");
		this.ayudaJMenuItem.addActionListener(this);

		// Da función a la item "crear" y lo inicializa
		this.crearJMenuItem = new JMenuItem("Crear");
		this.crearJMenuItem.addActionListener(this);

		menuBar.add(fileJMenu);
		menuBar.add(editJMenu);
		menuBar.add(jugarJMenuItem);

		fileJMenu.add(crearJMenuItem);
		fileJMenu.add(abrirJMenuItem);
		fileJMenu.add(guardarJMenuItem);
		fileJMenu.add(guardarComoJMenuItem);

		editJMenu.add(rehacerJMenuItem);
		editJMenu.add(deshacerJMenuItem);

		menuBar.add(ayudaJMenuItem);

		this.setJMenuBar(menuBar);

	}

	///////// INICIO MÉTODOS ITEM CREAR//////////////////////////////////////

	/**
	 * Comprueba que las dimensiones que recibe como parámetro son válidas para
	 * crear una matriz dentro de los criterios establecidos en el juego
	 * 
	 * @param n número de filas
	 * @param m número de columnas
	 */
	public void checkMatrizCrear(int n, int m) {
		if (n < 1 || n > 10 || m < 1 || m > 10) {
			JOptionPane.showMessageDialog(null, "ERROR. ENTRADA INCORRECTA");
		} else {

			this.matriz = new String[n][m];

			showMatriz(this.matriz);
		}
	}

	/**
	 * Añade al la matriz vacía para que la rellene el usuario
	 * 
	 * @param matriz Matriz inicializada con el tamaño que el usuario paso por
	 *               parámetros.
	 */
	public void showMatriz(String[][] matriz) {

		this.jPanel.removeAll();// Vacía el panel para poder añadir la matriz
		this.jPanel.setLayout(new GridLayout(matriz.length, matriz[0].length));// Ajusta la el panel en base a las
																				// dimensiones de la matriz

		for (int i = 0; i < this.matriz.length; i++) {
			for (int j = 0; j < this.matriz[0].length; j++) {
				JTextField txt = new JTextField("");
				txt.setHorizontalAlignment(JTextField.CENTER);// Coloca los espacios en blanco a rellenar en el
																// centro de la posición de la matriz
				if (matriz[i][j] != null) {
					txt.setText(matriz[i][j]);
				}
				this.checkCell(txt, i, j);
				this.jPanel.add(txt);
			}
		}
		this.setVisible(true);
	}

	/**
	 * Almacena el valor que el usario introduce en la celda creada por el item
	 * "crear"
	 * 
	 * @param text     JTextField de la celda
	 * @param filas    fila de la celda
	 * @param columnas columna de la celda
	 */
	public void checkCell(JTextField text, int filas, int columnas) {
		text.addFocusListener(new FocusAdapter() { // Subclase focus adapter

			/**
			 * Guarda el valor de la celda cuando el foco se va de esta en la matriz globla
			 */
			public void focusLost(FocusEvent event) {

				try {
					int n = Integer.parseInt(text.getText());
					if (n >= 1 && n <= 9) {
						matriz[filas][columnas] = text.getText();

					} else {
						JOptionPane.showMessageDialog(null, "ERROR. EL NÚMERO DEBE SER MAYOR QUE 0 Y MENOR QUE 10");
						text.setText("");

						matriz[filas][columnas] = text.getText();
					}

				} catch (Exception e) {
					// TODO: handle exception en caso de que el contenido de la celda sea distinto
					// de un número o un espacio vacío
					if (!(text.getText().equals(""))) {
						JOptionPane.showMessageDialog(null, "ERROR. NO SE PUEDE INTRODUCIR TEXTO");
						text.setText("");
					}
				}

				// EN caso de que la matriz halla cambiado llama al método copy que hace una
				// copia de esta y la almacena
				if (!estado.equals(text.getText())) {
					copy();

				}
			}

			/**
			 * Guarda el contenido de la celda en la variable global estado cuando el foco
			 * entra en la celda
			 */
			public void focusGained(FocusEvent event) {
				estado = text.getText();
			}

			/**
			 * Crea una copia de la matriz que queda almacenada en el atributo matrices
			 */
			private void copy() {
				String[][] copy = new String[matriz.length][matriz[0].length];

				for (int i = 0; i < copy.length; i++) {
					for (int j = 0; j < copy[0].length; j++) {
						if (i == filas && j == columnas) {
							copy[i][j] = estado;
						} else {
							copy[i][j] = matriz[i][j];
						}
					}
				}

				matrices.sumar(copy);
			}
		});
	}

	/////// FIN MÉTODOS ITEM
	/////// CREAR//////////////////////////////////////////////////////

	/////// INICIO ITEMS GUARDAR Y GUARDAR COMO ///////////////////////

	/**
	 * Guarda cambios de un archivo
	 * 
	 */
	public void guardar() {
		if (this.path == null) {
			guardarComo();

		}

		try {
			FileWriter fWriter = new FileWriter(this.path);// Archivo en el que escribir

			PrintWriter pWriter = new PrintWriter(fWriter);// Como el boli que escribe en el papel

			for (int i = 0; i < matriz.length; i++) {
				for (int j = 0; j < matriz[0].length; j++) {
					if (this.matriz[i][j] == null) {
						pWriter.print("0"); // Los espacios vacíos guarda un string "0" en lugar de un null

					} else {
						pWriter.print(matriz[i][j]);

					}

					if (j != matriz[0].length - 1) { // EN caso de que no se encuentre en la última celda añade un
														// espacio
						pWriter.print(" ");

					}
				}
				pWriter.println();

			}

			fWriter.close();// Es necesario cerrar el archivo, se abre al inicializarlo

		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "ERROR DE ESCRITURA");

		}
	}

	/**
	 * Guarda la ruta en la que se quiere guardar el fichero, y el nombre del
	 * fichero junto con la terminación de este.
	 * 
	 */
	public void guardarComo() {
		try {
			JFileChooser jFileChooser = new JFileChooser();// Frame que permite seleccionar al usuario un fichero del
															// sistema

			int jchoose = jFileChooser.showSaveDialog(this);// almacena el valor de la seleccion del usuario al frame, 0
															// si clicka en aceptar

			if (jchoose == 0) {
				File file = jFileChooser.getSelectedFile();

				this.path = file.getAbsolutePath();

				// Si el arcchivo no termina en ".txt" lo añade
				if (!(path.endsWith(".txt"))) {
					path += ".txt";

				}

				// Renueva el fichero por si iba sin ".txt"
				File checkFile = new File(path);

				if (checkFile.exists()) { // Encaso de que el fichero con esa ruta ya exista, mediante el método exists
											// de la clase File
					overWrite();

				} else {
					guardar();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "ERROR DE LECTURA");
		}
	}

	/**
	 * Método que pregunta al usuario si desea sobreescribir un archivo ya existente
	 * 
	 */
	public void overWrite() {

		if (0 == JOptionPane.showConfirmDialog(null, "¿QUIERES SOBRESCRIBIR EL ARCHIVO?")) {
			guardar();
		}
	}

	/////// FIN ITEMS GUARDAR Y GUARDAR COMO ///////////////////////

	////// INICIO DE ITEM ABRIR/////////////////////

	/**
	 * Método que relaliza la funcionalidad del item "abrir"
	 */
	public void open() {
		try {
			JFileChooser fileChooser = new JFileChooser(); // Frame que permite al usuario seleccionar un fichero del
															// sistema

			int num = fileChooser.showOpenDialog(this); // Almacena la respuesta del usuario

			if (num == 0) {
				File file = fileChooser.getSelectedFile(); // Almacena el fichero seleccionado en la ruta del objeto
															// JFileChooser

				FileReader fileReader = new FileReader(file); // Crea un objeto FileReader, que es un objeto copia del
																// fichero con permisos de lectura para el ficheros
																// seleccionado por el usuario

				BufferedReader bufferedReader = new BufferedReader(fileReader);// Lector del fichero de tipo FileReader

				ArrayList<String> lineas = new ArrayList<String>();

				String linea = bufferedReader.readLine();

				for (int i = 0; linea != null; i++) {
					lineas.add(linea);
					linea = bufferedReader.readLine();

				}

				int filas = lineas.size();

				String[] calculaColumnas = lineas.get(0).split(" ");

				int columnas = calculaColumnas.length;
				// Falta comprobar que todas lineas tengan las mismas columnas

				checkMatrizAbrir(filas, columnas, lineas);
				showMatriz(this.matriz);

			}
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "ERROR EN EL ARCHIVO");
		}
	}

	/**
	 * Rellena la matriz del programa copiando el archivo leído
	 * 
	 * @param n      filas de la matriz
	 * @param m      columnas de la matriz
	 * @param lineas filas de la matriz léida en el archivo
	 */
	public void checkMatrizAbrir(int n, int m, ArrayList<String> lineas) {
		if (n < 1 || n > 10 || m < 1 || m > 10) {
			JOptionPane.showMessageDialog(null, "ERROR EN EL ARCHIVO. MATRIZ INCORRECTA");

		} else {

			String matriz[][] = new String[n][m];

			for (int i = 0; i < n; i++) {
				String[] cells = lineas.get(i).split(" ");

				for (int j = 0; j < m; j++) {
					if (cells.length == m) {
						if (checkNumberAbrir(cells[j])) {
							matriz[i][j] = cells[j];

						} else if (!cells[j].equals("0")) {

							// En caso de que no sea valido no lo añadimos a la matriz y paramos
							JOptionPane.showMessageDialog(null, "ERROR EN EL ARCHIVO. MATRIZ INCORRECTA");
							return;
						}
					} else {
						JOptionPane.showMessageDialog(null, "ERROR EN EL ARCHIVO. MATRIZ INCORRECTA");
						return;
					}
				}
			}
			this.matriz = matriz;
		}

	}

	/**
	 * Comprueba que el número de la celda de la matriz es válido, implementado para
	 * el item abrir
	 * 
	 * @param cell celda que contiene el número
	 * @return true si es válido, dalse si no lo es.
	 */
	public boolean checkNumberAbrir(String cell) {
		try {
			int num = Integer.parseInt(cell);

			if (num < 1 || num > 9) {
				return false;

			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			if (!cell.equals("0")) {
				JOptionPane.showMessageDialog(null, "ERROR EN EL ARCHIVO. NO SE PUEDE INTRODUCIR TEXTO");
			}
			return false;
		}
	}

	////// FIN DE ITEM ABRIR/////////////////////

	////// INICIO DE LOS ITEMS HACER Y DESHACER/////////////
	/**
	 * Método que realiza la función del item "deshacer" el cual que vuelve atrás
	 * después de un cambio hecho en la matriz
	 */
	public void deshacer() {
		if (!edit) {
			this.matriz = matrices.deshacer(this.matriz);
			loadPanelJuego();
		} else {
			this.matriz = matrices.deshacer(this.matriz);
			showMatriz(this.matriz);
		}
	}

	/**
	 * Método que realiza la función del item "rehacer", que recupera un cambio
	 * deshecho previamente
	 */
	public void rehacer() {
		String[][] rehacer = matrices.rehacer();

		if (rehacer != null) {
			this.matriz = rehacer;
			if (!edit) {
				loadPanelJuego();
			} else {
				showMatriz(this.matriz);
			}
		} else {
			JOptionPane.showMessageDialog(null, "NO SE PUEDE REHACER");

		}
	}
	////// FIN DE LOS ITEMS HACER Y DESHACER/////////////

	///// INICIO ITEM JUGAR /////////////

	public void jugar() {

		edit = false;
		this.matrices = new Matrices();
		String[][] copy = copyJugar();
		int filasTablero = copy.length * 2 - 1;
		int colTablero = copy[0].length * 2 - 1;
		this.matriz = new String[filasTablero][colTablero]; // Reinicializo la matriz global con las dimensiones del
															// tablero de juego

		for (int i = 0; i < filasTablero; i++) {
			for (int j = 0; j < colTablero; j++) {
				if (i % 2 == 0 && j % 2 == 0) {
					this.matriz[i][j] = copy[i / 2][j / 2];
				}
			}
		}

		loadPanelJuego();

	}

	/**
	 * Carga el panel en de juego en la ventana, con las herramientas de juego
	 * también
	 */
	public void loadPanelJuego() {
		this.jPanel.removeAll();// Vacía el panel para poder añadir la matriz
		this.jPanel.setLayout(new GridLayout(matriz.length, matriz[0].length));// Ajusta la el panel en base a las
																				// dimensiones de la matriz
		for (int i = 0; i < this.matriz.length; i++) {
			for (int j = 0; j < this.matriz[0].length; j++) {
				if (i % 2 == 0 && j % 2 == 0) { // Si me encuentro en una de las celdas que contiene números, añado un
												// objeto de tipo JuegoCellTablero
					JuegoCellTablero button = new JuegoCellTablero(i, j);

					this.jPanel.add(button);
				} else {
					JTextField txt = new JTextField();
					txt.setEditable(false); // Impide al usuario editar las celdas impares
					txt.setHorizontalAlignment(JTextField.CENTER);
					if (this.matriz[i][j] != null) {
						txt.setFont(new Font("Arial", Font.BOLD, 14));
						txt.setText(this.matriz[i][j]);

					}

					this.jPanel.add(txt);
				}
			}
		}

		this.setVisible(true);

	}

	/**
	 * Comprueba que la matriz con la que el usuario va a jugar este completa
	 * 
	 * @return true si esta completa la matriz, false si faltan casillas por
	 *         completar
	 */
	public Boolean checkJugar() {
		boolean check = true;

		for (int i = 0; i < this.matriz.length; i++) {
			for (int j = 0; j < this.matriz[0].length; j++) {
				if (this.matriz[i][j] == null) {
					check = false;
				}
			}
		}

		return check;
	}

	/**
	 * Crea una copia de la matriz para el método jugar
	 * 
	 * @return copia de la matriz
	 */
	private String[][] copyJugar() {
		String[][] copy = new String[this.matriz.length][this.matriz[0].length];
		int filas = this.matriz.length;
		int columnas = this.matriz[0].length;

		for (int i = 0; i < copy.length; i++) {
			for (int j = 0; j < copy[0].length; j++) {
				if (i == filas && j == columnas) {
					copy[i][j] = estado;
				} else {
					copy[i][j] = matriz[i][j];
				}
			}
		}

		return copy;
	}

	/**
	 * Asigna la acción a relaizar segun el JFrame accionando.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		if (arg0.getSource() == this.crearJMenuItem) { // ITEM CREAR
			try {
				int n = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el número de filas(n): "));
				int m = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el número de columnas(m): "));

				checkMatrizCrear(n, m);

			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, "ERROR. ENTRADA INCORRECTA");
			}

		} else if (arg0.getSource() == this.guardarJMenuItem) { // ITEM GUARDAR
			this.guardar();

		} else if (arg0.getSource() == this.guardarComoJMenuItem) { // ITEM GUARDARCOMO
			this.guardarComo();

		} else if (arg0.getSource() == this.abrirJMenuItem) { // ITEM ABRIR
			this.open();

		} else if (arg0.getSource() == this.deshacerJMenuItem) { // ITEM DESHACER
			this.deshacer();

		} else if (arg0.getSource() == this.rehacerJMenuItem) { // ITEM REHACER
			this.rehacer();

		} else if (arg0.getSource() == this.jugarJMenuItem) {
			this.jugar();

		} else if (arg0.getSource() == this.ayudaJMenuItem) {
			if (edit) {
				JOptionPane.showMessageDialog(null, "SOLO DISPONIBLE EN JUEGO");
			} else {
				this.ayuda();
			}
		}
	}

	private void ayuda() {
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

			@Override
			protected String doInBackground() throws Exception {
				// TODO Auto-generated method stub
				int[][] m = new int[matriz.length / 2 + 1][matriz.length / 2 + 1];
				for (int i = 0; i < m.length; i++) {
					for (int j = 0; j < m[0].length; j++) {
						m[i][j] = Integer.parseInt(matriz[i * 2][j * 2]);
					}
				}
				Solucion solucion = new Solucion(m, m.length, m[0].length);

				solucion.searchSolution(solucion.matrizSolution(), 0, 0);
				String[][] firstSol = solucion.getSolution(0);
				if (firstSol == null) {
					JOptionPane.showMessageDialog(null, "No hay solución");
				} else {
					matriz = firstSol;
					loadPanelJuego();
				}
				return null;
			}

		};
		worker.execute();

	}

	/**
	 * 
	 * @author ignacio
	 *
	 */
	public class JuegoCellTablero extends JButton implements ActionListener {

		private int i;
		private int j;

		public JuegoCellTablero(int fila, int columna) {
			this.i = fila;
			this.j = columna;
			this.addActionListener(this);
			this.setText(matriz[i][j]);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (cellTabOrigin == null) {
				cellTabOrigin = this;
			} else {
				cellTabDest = this;

				for (Movimientos movimiento : Movimientos.values()) {
					if ((cellTabOrigin.i + movimiento.getFila() * 2 == cellTabDest.i)
							&& (cellTabOrigin.j + movimiento.getCol() * 2 == cellTabDest.j)) {

						if (matriz[cellTabOrigin.i + movimiento.getFila()][cellTabOrigin.j
								+ movimiento.getCol()] == null) {

							if (Integer.parseInt(matriz[cellTabOrigin.i][cellTabOrigin.j]) + 1 == Integer
									.parseInt(matriz[cellTabDest.i][cellTabDest.j])) {
								String[][] copia = copyJugar();
								matrices.sumar(copia);
								matriz[cellTabOrigin.i + movimiento.getFila()][cellTabOrigin.j
										+ movimiento.getCol()] = movimiento.getSimbolo();
								cellTabDest = null;
								cellTabOrigin = null;
								loadPanelJuego();
								return;
							} else if ((Integer.parseInt(matriz[cellTabOrigin.i][cellTabOrigin.j]) == maxNum())
									&& (Integer.parseInt(matriz[cellTabDest.i][cellTabDest.j]) == minNum())) {
								String[][] copia = copyJugar();
								matrices.sumar(copia);
								matriz[cellTabOrigin.i + movimiento.getFila()][cellTabOrigin.j
										+ movimiento.getCol()] = movimiento.getSimbolo();
								cellTabDest = null;
								cellTabOrigin = null;
								loadPanelJuego();
								return;
							}
						}
					}
				}
				JOptionPane.showMessageDialog(null, "NO SE PUEDE MOVER");
				cellTabDest = null;
				cellTabOrigin = null;
			}
		}

		/**
		 * Devuelve el número mayor de la matriz
		 * 
		 * @return max entero que representa al número mayor de la matriz
		 */
		public int maxNum() {
			int max = Integer.parseInt(matriz[0][0]);

			for (int i = 0; i < matriz.length; i += 2) {
				for (int j = 0; j < matriz[0].length; j += 2) {
					if (Integer.parseInt(matriz[i][j]) > max) {
						max = Integer.parseInt(matriz[i][j]);
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
			int min = Integer.parseInt(matriz[0][0]);

			for (int i = 0; i < matriz.length; i += 2) {
				for (int j = 0; j < matriz[0].length; j += 2) {
					if (Integer.parseInt(matriz[i][j]) < min) {
						min = Integer.parseInt(matriz[i][j]);

					}
				}
			}

			return min;
		}
	}

}
