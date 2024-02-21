public class Cartero {
	
	private Buzon[][] buzones;
	
	
	public Cartero(int n) {
		this.buzones = new Buzon[n][n];
	}
	
	public void iniciarBuzon(int fila, int columna) {
		buzones[fila][columna] = new Buzon(fila+1);
	}
	
	public boolean consultarEnvio(int filaOrigen, int columnaOrigen, int fila, int columna, boolean estado) {
		return buzones[fila][columna].apilar(buzones[filaOrigen][columnaOrigen], filaOrigen, columnaOrigen, fila, columna, estado);
	}
	
	/*Desapilar
	 * retorna una lista primitiva
	 * [0] Total de elementos
	 * [1] Elementos en True
	 */
	public int[] consultarPropio(int fila, int columna) {
		return buzones[fila][columna].desapilar();
		
	}
}
