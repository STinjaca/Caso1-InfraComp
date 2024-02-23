import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class Casilla extends Thread{
	
	private int fila;
	private int columna;
	private int totalAdy;
	private int adyVivas;
	private int adyRecibido;
	private boolean estado;
	private Stack<int[]> stack = new Stack<int[]>();
	private static Cartero cartero;
	private static int tamañoMatriz;
	private static int generaciones;
	
	
	
	
	public Casilla(int fila, int columna, boolean estado) {
		this.fila = fila;
		this.columna = columna;
		this.estado = estado;
		this.totalAdy = 0;
		this.adyVivas = 0;
		this.adyRecibido = 0;
	}

	//Run
	@Override
	public void run() {
		calcularAdyacentes();
		while(stack.size() > 0 || totalAdy > adyRecibido) {
			
			if(stack.size() > 0) {
				int[] pos = stack.peek();
				boolean result = cartero.consultarEnvio(fila, columna, pos[0], pos[1], estado);
				if(result) {
					stack.pop();
				}

			}
			if (totalAdy > adyRecibido) {
				int[] datos = cartero.consultarPropio(fila, columna);
				adyRecibido+=datos[0];
				adyVivas+=datos[1];
				//System.err.println("hola");
			}

		}
		
		if (adyVivas == 0 || adyVivas > 3) {
			estado = false;
		} else if(adyVivas == 3) {
			estado = true;
		}
		
	}
	
	public boolean getEstado() {
		return estado;
	}

	public void calcularAdyacentes() {
	    
	    // Verificar la casilla superior
	    if (fila - 1 >= 0) {
	        totalAdy++;
	        int[] pos = new int[2];
	        pos[0] = fila-1;
	        pos[1] = columna;
	        stack.add(pos);
	    }

	    // Verificar la casilla inferior
	    if (fila + 1 < tamañoMatriz) {
	        totalAdy++;
	        int[] pos = new int[2];
	        pos[0] = fila +1;
	        pos[1] = columna;
	        stack.add(pos);
	    }

	    // Verificar la casilla izquierda
	    if (columna - 1 >= 0) {
	        totalAdy++;
	        
	        int[] pos = new int[2];
	        pos[0] = fila;
	        pos[1] = columna - 1;
	        stack.add(pos);
	    }

	    // Verificar la casilla derecha
	    if (columna + 1 < tamañoMatriz) {
	        totalAdy++;
	        
	        int[] pos = new int[2];
	        pos[0] = fila;
	        pos[1] = columna + 1;
	        stack.add(pos);
	    }

	    // Verificar la casilla superior izquierda
	    if (fila - 1 >= 0 && columna - 1 >= 0) {
	        totalAdy++;
	        
	        int[] pos = new int[2];
	        pos[0] = fila - 1;
	        pos[1] = columna - 1;
	        stack.add(pos);
	    }

	    // Verificar la casilla superior derecha
	    if (fila - 1 >= 0 && columna + 1 < tamañoMatriz) {
	        totalAdy++;
	        
	        int[] pos = new int[2];
	        pos[0] = fila - 1;
	        pos[1] = columna + 1;
	        stack.add(pos);
	    }

	    // Verificar la casilla inferior izquierda
	    if (fila + 1 < tamañoMatriz && columna - 1 >= 0) {
	        totalAdy++;
	        
	        int[] pos = new int[2];
	        pos[0] = fila + 1;
	        pos[1] = columna - 1;
	        stack.add(pos);
	    }

	    // Verificar la casilla inferior derecha
	    if (fila + 1 < tamañoMatriz && columna + 1 < tamañoMatriz) {
	        totalAdy++;
	        
	        int[] pos = new int[2];
	        pos[0] = fila + 1;
	        pos[1] = columna + 1;
	        stack.add(pos);
	    }
	}

	
	
	//Main
	public static void main(String[] args) throws IOException {
		// Cargar Matriz
		
		Casilla[][] casillas  = cargarArchivo();

		//Iniciar cartero
  		Casilla.cartero = new Cartero(tamañoMatriz);
  		
		//iniciar buzones
		for(int fila = 0; fila < tamañoMatriz; fila++) {
			for(int columna = 0; columna < tamañoMatriz; columna++) {
				Casilla.cartero.iniciarBuzon(fila, columna);
			}
		}
        
		for (int i = 0; i < 1; i ++) {
			
		
	        //Lanzar Hilos
	     		for(int fila = 0; fila < tamañoMatriz; fila++) {
	     			for(int columna = 0; columna < tamañoMatriz; columna++) {
	     				casillas[fila][columna] = new Casilla(fila, columna, casillas[fila][columna].getEstado());
	    				casillas[fila][columna].start();
	    				
	     			}
	     		}
			
			// Esperar Hilos
			for(int fila = 0; fila < tamañoMatriz; fila++) {
				for(int columna = 0; columna < tamañoMatriz; columna++) {
					try {
						casillas[fila][columna].join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		imprimiMatriz(casillas);
		
	}
	
	
	//Imprimir matriz
	public static void imprimiMatriz(Casilla[][] casillas) {
		for(int fila = 0; fila < tamañoMatriz; fila++) {
			for(int columna = 0; columna < tamañoMatriz; columna++) {
					System.out.print((casillas[fila][columna].getEstado()?"■":".") + "  ");
			}
			System.out.print("\n");

		}
	}
	
	
	//Imprimir matriz
		public static Casilla[][] cargarArchivo()  throws IOException {

			//Leer archivo
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        //System.out.println("Ingrese el nombre del archivo: ");
	        //String nombreArchivo = br.readLine();
	        br.close();

			//String path = "files/"+nombreArchivo;
	        String path = "files/caso1.txt";
	        BufferedReader archivo = new BufferedReader(new FileReader(path));
	        tamañoMatriz = Integer.parseInt(archivo.readLine());
			generaciones = Integer.parseInt(archivo.readLine());
			
			Casilla[][] result = new Casilla[tamañoMatriz][tamañoMatriz];

			System.out.println("Generaciones: "+ Integer.toString(generaciones));
			
			
	        for (int i = 0; i < tamañoMatriz; i++){
	            String[] valores = archivo.readLine().split(",");

	            for (int j = 0; j < tamañoMatriz; j++) {
	                boolean estadoInicial = Boolean.parseBoolean(valores[j]);
	                //Cargar Estado
	                result[i][j] = new Casilla(i,j,estadoInicial);
	            }
	        }
	        
	        archivo.close();
	        
			return result;
	        
	        
	        
		}
	
	
}
