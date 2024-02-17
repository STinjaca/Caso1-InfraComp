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
	private static int n;
	
	
	
	
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
				int[] pos = stack.pop();
				cartero.consultarEnvio(pos[0], pos[1], estado);
			}
			if (totalAdy > adyRecibido) {
				int[] datos = cartero.consultarPropio(fila, columna);
				adyRecibido+=datos[0];
				adyVivas+=datos[1];
			}
		}
		
		if (adyVivas == 0 || adyVivas > 3) {
			estado = false;
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
	    if (fila + 1 < n) {
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
	    if (columna + 1 < n) {
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
	    if (fila - 1 >= 0 && columna + 1 < n) {
	        totalAdy++;
	        
	        int[] pos = new int[2];
	        pos[0] = fila - 1;
	        pos[1] = columna + 1;
	        stack.add(pos);
	    }

	    // Verificar la casilla inferior izquierda
	    if (fila + 1 < n && columna - 1 >= 0) {
	        totalAdy++;
	        
	        int[] pos = new int[2];
	        pos[0] = fila + 1;
	        pos[1] = columna - 1;
	        stack.add(pos);
	    }

	    // Verificar la casilla inferior derecha
	    if (fila + 1 < n && columna + 1 < n) {
	        totalAdy++;
	        
	        int[] pos = new int[2];
	        pos[0] = fila + 1;
	        pos[1] = columna + 1;
	        stack.add(pos);
	    }
	}

	
	
	//Main
	public static void main(String[] args) throws IOException {
		// tamaño de la matriz
		
		//Casilla.n = 10;
		//Casilla.cartero = new Cartero(n);
		//Casilla[][] casillas = new Casilla[n][n];
		
		//Leer archivo
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese el nombre del archivo: ");
        String nombreArchivo = br.readLine();
        br.close();

		String path = "files/"+nombreArchivo;
        BufferedReader archivo = new BufferedReader(new FileReader(path));
        int tamañoMatriz = Integer.parseInt(archivo.readLine());

		Casilla[][] casillas = new Casilla[tamañoMatriz][tamañoMatriz];

        for (int i = 0; i < tamañoMatriz; i++){
            String[] valores = archivo.readLine().split(",");

            for (int j = 0; j < tamañoMatriz; j++) {
                Boolean estadoInicial = Boolean.parseBoolean(valores[j]);
                Casilla cas = new Casilla(i, j, estadoInicial);
				System.out.println("Fila:" + Integer.toString(i)+ " " + 
									"Columna: " + Integer.toString(j) + " " +
									"Estado: "+ cas.getEstado());
            }
        }
        archivo.close();
		
		//iniciar buzones
		for(int fila = 0; fila < n; fila++) {
			for(int columna = 0; columna < n; columna++) {
				Casilla.cartero.iniciarBuzon(fila, columna);
			}
		}
	
		//Lanzar Hilos
		for(int fila = 0; fila < n; fila++) {
			for(int columna = 0; columna < n; columna++) {
				casillas[fila][columna] = new Casilla(fila, columna, true);
				casillas[fila][columna].start();;
			}
		}
		
		// Esperar Hilos
		for(int fila = 0; fila < n; fila++) {
			for(int columna = 0; columna < n; columna++) {
				try {
					casillas[fila][columna].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for(int fila = 0; fila < n; fila++) {
			for(int columna = 0; columna < n; columna++) {
					System.out.print((casillas[fila][columna].getEstado()?"■":".") + "  ");
			}
			System.out.print("\n");

		}
	}
	
	
	//Imprimir matriz
	public static void imprimiMatriz(Casilla[] casillas) {
		
	}
	
	
}
