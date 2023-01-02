import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Matrices {
	private ArrayList<String[][]> matrices;
	private int count;

	public Matrices() {
		this.matrices = new ArrayList<>();
		this.count = 0;

	}

	public String[][] deshacer() {
		restar();
		
		String[][] deshacer = matrices.get(count);
		
		return deshacer;
	}
	
	public String[][] rehacer() {
		if(checkRehacer()) {
			return matrices.get(count);
			
		} else {
			return null;
			
		}
	}
	
	public Boolean checkRehacer() {
		int max = this.matrices.size() - 1;
		
		if(this.count < max) {
			this.count++;
			return true;
			
		} else if (this.count == max){
			return true;
			
		} else {
			return false;
		}
	}

	public void sumar(String[][] matriz) {
		this.matrices.add(matriz);
		this.count++;

	}

	public void restar() {
		if (this.count > 0) {
			this.count--;

		} else {
			JOptionPane.showMessageDialog(null, "NO SE PUEDE DESHACER");
		
		}
	}
}
