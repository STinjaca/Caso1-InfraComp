import java.util.Stack;

public class Buzon {
	private Stack<Boolean> stack = new Stack<Boolean>();
	private int capacidad;
	private boolean despierto;
	
	public Buzon(int capacidad) {
		this.capacidad = capacidad;
		this.despierto = true;
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
	
	public synchronized boolean apilar(Buzon origen, int filaOrigen, int columnaOrigen, int fila, int columna, boolean estado) {
		System.out.println("Entró en apilar Origen["+ filaOrigen + "]["+ columnaOrigen + "] Destino ["+ fila +"][" + columna+"]");
		if (false) {
			System.out.println("dueño dormido Origen["+ filaOrigen + "]["+ columnaOrigen + "] Destino ["+ fila +"][" + columna+"]");
			return false;
		}
		

		while (stack.size()>= capacidad) {
			System.out.println("me voy a dormir Origen["+ filaOrigen + "]["+ columnaOrigen + "] Destino ["+ fila +"][" + columna+"]");
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		

		stack.add(estado);
		System.out.println("Enviado Origen["+ filaOrigen + "]["+ columnaOrigen + "] Destino ["+ fila +"][" + columna+"]");
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
