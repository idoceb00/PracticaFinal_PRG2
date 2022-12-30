import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

	private String path;

	private String[][] matriz;

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
		this.guardarJMenuItem = new JMenuItem("Guardar");
		this.guardarComoJMenuItem = new JMenuItem("Guardar Como");
		this.jugarJMenuItem = new JMenuItem("Jugar");
		this.deshacerJMenuItem = new JMenuItem("Deshacer");
		this.rehacerJMenuItem = new JMenuItem("Rehacer");
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
	public void compruebaMatriz(int n, int m) {
		if (n < 1 || n > 9 || m < 1 || m > 9) {
			JOptionPane.showMessageDialog(null, "ERROR. ENTRADA INCORRECTA");
		} else {

			this.matriz = new String[n][m];

			muestraMatriz(this.matriz);
		}
	}

	/**
	 * Añade al la matriz vacía para que la rellene el usuario
	 * 
	 * @param matriz Matriz inicializada con el tamaño que el usuario paso por
	 *               parámetros.
	 */
	public void muestraMatriz(String[][] matriz) {

		this.jPanel.removeAll();// Vacía el panel para poder añadir la matriz
		this.jPanel.setLayout(new GridLayout(matriz.length, matriz[0].length));// Ajusta la el panel en base a las
																				// dimensiones de la matriz

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++) {
				JTextField txt = new JTextField("");
				txt.setHorizontalAlignment(JTextField.CENTER);// Coloca los espacios en blanco a rellenar en en el
																// centro de la posición de la matriz
				this.compruebaCelda(txt, i, j);
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
	public void compruebaCelda(JTextField text, int filas, int columnas) {
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
	}

	/**
	 * Almacena la matriz en un archivo que no se a ha guardado previamente o que se
	 * quiere cambiar el nombre o sobreescribir una ya creada
	 * 
	 */
	public void guardarComo() {
		try {
			JFileChooser jFileChooser = new JFileChooser();

			int jchoose = jFileChooser.showSaveDialog(this);

			if (jchoose == 0) {
				File file = jFileChooser.getSelectedFile();

				this.path = file.getAbsolutePath();

				if (!(path.endsWith(".txt"))) {
					path += ".txt";

				}

				File checkFile = new File(path);

				if (checkFile.exists()) {
					overWrite();

				} else {
					guardar();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "ERROR EN LECTURA");
		}
	}

	/**
	 * Método que pregunta al usuario si desea sobreescribir un archivo ya existente
	 */
	public void overWrite() {

		if (0 == JOptionPane.showConfirmDialog(null, "¿QUIERES SOBRESCRIBIR EL ARCHIVO?")) {
			guardar();
		}
	}

    ///////FIN ITEMS GUARDAR Y GUARDAR COMO ///////////////////////
	
	
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

				compruebaMatriz(n, m);

			} catch (Exception e) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(null, "ERROR. ENTRADA INCORRECTA");
			}

		}

	}

}
