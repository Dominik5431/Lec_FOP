package h1;

public class Fibonacci {
	
	public static void main (String[] args) {
		/** int[] i1 = {1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3};
		int[] i2 = {3, 9, 3, 9};
		int[] i3 = {1, 7, 3, 1, 9, 4, 1, 7, 4};
		int[] i4 = {2, 2};
		int[] i5 = {1, 2, 5, 9, 1, 2, 5, 1, 2, 5};
		int[] i6 = {25, 1, 2, 1, 2, 1, 2};
		int[] i7 = {1, 2, 1, 2, 1, 2, 25}; **/
		
		System.out.println(fibRek(5));
	}
	
	
	/**
	 * Diese Methode berechnet rekursiv das n-te Glied der Fibonacci-Folge.
	 * 
	 * @param n: Stelle der Fibonacci-Folge, die zurückgegeben wird.
	 * @return n-tes Glied der Fibonacci-Folge
	 */
	
	public static int fibRek (int n) {
		if (n==0) {
			return 0;
		} else if (n==1) {
			return 1;
		} else {
			return fibRek(n-1)+fibRek(n-2);
		}
		
	}
	
	/**
	 * Diese Methode berechne iterativ die Folgenglieder der Fibonacci-Folge.
	 * 
	 * @param fibonacci: Array mit Länge n, in das die Fibonacci-Folge bis zum n-1-ten Glied geschrieben wird. 
	 */
	
	public static void fibIt (int[] fibonacci) {
		fibonacci[0]=0;
		fibonacci[1]=1;
		for (int i=2; i<fibonacci.length; i++) {
			fibonacci[i]=fibonacci[i-1]+fibonacci[i-2];
		}
	}
	
	/**
	 * Diese Methode berechnet die in Gleichung (2) angegebene Matrix.
	 * 
	 * @param n: Stelle der Fibonacci-Folge, die zurückgegeben werden soll.
	 * @return n-tes Glied der Fibonacci-Folge.
	 */
	
	public static int fibMat (int n) {
		int a[][] = {{1,1},{1,0}};
		int b[][] = a;
		for (int i=0; i<n-1; i++) {
			b = Util.matrixMultiplication(a, b);
		}
		return b[0][1];
	}
	/**
	 * Diese Methode berechnet mithilfe von Gleichung (3) das n-te Glied der Fibonacci-Folge.
	 * 
	 * @param n: Stelle der Fibonacci-Folge, die zurückgegeben wird.
	 * @return n-tes Glied der Fibonacci-Folge.
	 */
	
	public static int fibSum (int n) {
		int back=0;
		if (n%2 == 0) {
			for (int i=1; i<=n/2; i++) {
				back += Util.binomialCoefficient(n-i, i-1);
			}
			return back;
		} else {
			for (int i=1; i<= (n+1)/2; i++) {
				back += Util.binomialCoefficient(n-i, i-1);
			}
			return back;			
		}
	}
	
	/**
	 * Diese Methode berechnet, wie viele Elemente die Collatz-Folge mit übergebenem Startwert
	 * hat, bis sie in den Zyklus 4,2,1 gerät.
	 * 
	 * @param initial: Anfangswert der Collatz-Folge.
	 * @return Elemente der Collatz-Folge, bis sie in den Zyklus 4,2,1 gerät.
	 */
	
	public static int collatz(int initial) {
		int zaehler=0;
		while (initial != 4) {
			if (initial%2 == 0) {
				initial /=2;
			} else {
				initial *=3;
				initial++;
			}
			zaehler++;
		}
		return zaehler+3;
	}
	
	/**
	 * Diese Methode überprüft eine vorgegebene Zahlenfolge auf das kleinste, ununterbrochen immer wiederkehrende Muster.
	 * 
	 * @param set: Zu überprüfende Zahlenfolge
	 * @return Länge des kleinsten, ununterbrochen immer wiederkehrenden Musters oder -1, falls kein solches Muster existiert.
	 */
	
	public static int cycle(int[] set) {
		int zaehler=1;
		while (true) {
			if (set.length == zaehler) {
				return -1;
			} else if (set.length%zaehler == 0) {
				int z=0;
				boolean bool=true;
				while (z+zaehler < set.length) {
					if (set[z]!=set[z+zaehler]) {
						bool=false;
					}
					z++;
				}
				if (bool) {
					return zaehler;
				} else {
					zaehler++;
				}
				
			} else if (set.length%zaehler != 0) {
				zaehler++;
			}
		}
	}
}
