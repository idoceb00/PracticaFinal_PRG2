import java.awt.BorderLayout;
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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultEditorKit.CopyAction;

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

		menuBarMethod();
		this.setVisible(true);
	}

	/**
	 * Método que crea la barra de herramientas con los menus e items
	 * correspondientes
	 */
	public void menuBarMethod() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileJMenu = new JMenu("Archivo");// Menu archivo
		JMenu editJMenu = new JMenu("Editar");// Menu herramientas

		this.abrirJMenuItem = new JMenuItem("Abrir");
		this.abrirJMenuItem.addActionListener(this);

		// Dando función al item "guardar" y al "guardar como"
		this.guardarJMenuItem = new JMenuItem("Guardar");
		this.guardarJMenuItem.addActionListener(this);
		this.guardarComoJMenuItem = new JMenuItem("Guardar Como");
		this.guardarComoJMenuItem.addActionListener(this);

		this.jugarJMenuItem = new JMenuItem("Jugar");

		this.deshacerJMenuItem = new JMenuItem("Deshacer");
		this.deshacerJMenuItem.addActionListener(this);
		this.rehacerJMenuItem = new JMenuItem("Rehacer");
		this.rehacerJMenuItem.addActionListener(this);

		this.ayudaJMenuItem = new JMenuItem("Ayuda");

		// Dando función a la item "crear"
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

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				JTextField txt = new JTextField("");
				txt.setHorizontalAlignment(JTextField.CENTER);// Coloca los espacios en blanco a rellenar en en el
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
		text.addFocusListener(new FocusAdapter() {

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
					// TODO: handle exception
					if (!(text.getText().equals(""))) {
						JOptionPane.showMessageDialog(null, "ERROR. NO SE PUEDE INTRODUCIR TEXTO");
						text.setText("");
					}
				}
				if (!estado.equals(text.getText())) {
					copy();

				}
			}

			public void focusGained(FocusEvent event) {
				estado = text.getText();
			}

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
						pWriter.print("0");

					} else {
						pWriter.print(matriz[i][j]);

					}

					if (j != matriz[0].length - 1) {
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
			JFileChooser jFileChooser = new JFileChooser();// ¿PREGUNTAR COMO FUNCIONABA ESTO?

			int jchoose = jFileChooser.showSaveDialog(this);

			if (jchoose == 0) {
				File file = jFileChooser.getSelectedFile();

				this.path = file.getAbsolutePath();

				// Si el arcchivo no termina en ".txt" lo añade
				if (!(path.endsWith(".txt"))) {
					path += ".txt";

				}

				// Renueva el fichero por si iba sin ".txt"
				File checkFile = new File(path);

				if (checkFile.exists()) {
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
			JFileChooser fileChooser = new JFileChooser();

			int num = fileChooser.showOpenDialog(this);

			if (num == 0) {
				File file = fileChooser.getSelectedFile();

				FileReader fileReader = new FileReader(file);

				BufferedReader bufferedReader = new BufferedReader(fileReader);

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

	public void deshacer() {
		this.matriz = matrices.deshacer();

		showMatriz(this.matriz);
	}

	public void rehacer() {
		String[][] rehacer = matrices.rehacer();
		
		if (rehacer != null) {
			this.matriz = rehacer;
			showMatriz(this.matriz);
			
		}else {
			JOptionPane.showMessageDialog(null, "NO SE PUEDE REHACER");
		
		}
	}

	/**
	 * Asigna la acción a relaizar segun el JFrame accionando.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == this.crearJMenuItem) {
			try {
				int n = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el número de filas(n): "));
				int m = Integer.parseInt(JOptionPane.showInputDialog(null, "Introduzca el número de columnas(m): "));

				checkMatrizCrear(n, m);

			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, "ERROR. ENTRADA INCORRECTA");
			}

		} else if (arg0.getSource() == this.guardarJMenuItem) {
			this.guardar();

		} else if (arg0.getSource() == this.guardarComoJMenuItem) {
			this.guardarComo();

		} else if (arg0.getSource() == this.abrirJMenuItem) {
			this.open();

		} else if (arg0.getSource() == this.deshacerJMenuItem) {
			this.deshacer();
		} else if (arg0.getSource() == this.rehacerJMenuItem) {
			this.rehacer();
		}
	}

}
