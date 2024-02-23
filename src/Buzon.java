import java.util.Stack;
import java.util.HashSet;
import java.util.Set;


public class Buzon {
	private Stack<Boolean> stack = new Stack<Boolean>();
	private int capacidad;
	private boolean despierto;
	private String nombre;
	private Set<String> esperando = new HashSet<String>();
	
	public Buzon(int capacidad, String nombre) {
		this.capacidad = capacidad;
		this.despierto = true;
		this.nombre = nombre;
	}
	
	public synchronized boolean isDespierto() {
		return despierto;
	}

	public synchronized void setDespierto(boolean despierto) {
		this.despierto = despierto;
	}

	public synchronized int size() {
		return stack.size();
	}
	
	public synchronized int capcidad() {
		return capacidad;
	}
	
	public synchronized boolean apilar(Buzon buzon, int filaOrigen, int columnaOrigen, int fila, int columna, boolean estado) {
		
		synchronized(esperando) {
			if (esperando.contains(""+ filaOrigen +"" +columnaOrigen)) {
				return false;
			}
		}
		
		while (stack.size()>= capacidad) {
			try {
				synchronized(buzon.esperando) {
					buzon.esperando.add(nombre);
				}
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		synchronized(buzon.esperando) {
			buzon.esperando.remove(nombre);
		}
		
		stack.add(estado);
		return true;
	}
	
	/*Desapilar
	 * retorna una lista primitiva
	 * [0] Total de elementos
	 * [1] Elementos en True
	 */
	public synchronized int[] desapilar() {
		int[] result = new int[2];
		if (stack.size() == 0) {
			Thread.yield();
		}
		result[0] = stack.size();
		while(stack.size()>0) {
			if(stack.pop()) {
				result[1]++;
			}
		}
		this.notifyAll();
		return result;
	}
}
