import java.util.Stack;

public class Buzon {
	private Stack<Boolean> stack = new Stack<Boolean>();
	private int capacidad;
	
	public Buzon(int capacidad) {
		this.capacidad = capacidad;
	}
	
	public synchronized int size() {
		return stack.size();
	}
	
	public synchronized void apilar(boolean estado) {
		while (stack.size()>= capacidad) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		stack.add(estado);
	}
	
	/*Desapilar
	 * retorna una lista primitiva
	 * [0] Total de elementos
	 * [1] Elementos en True
	 */
	public synchronized int[] desapilar() {
		int[] result = new int[2];
		result[0] = stack.size();
		while(stack.size()>0) {
			if(stack.pop()) {
				result[1]++;
			}
			
			this.notify();
		}
		
		return result;
	}
}
