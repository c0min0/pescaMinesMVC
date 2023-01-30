package minesvce.vista;

/**
 * Classe que conté totes les funcions que mostren informació per pantalla
 */
public class Vista {

    /**
     * Mètode per mostrar per pantalla el camp de mines introduït per paràmetres amb les seves coordenades
     * @param minefield Camp de mines que volem mostrar com està
     */
    public static void printMinefield(char[][] minefield) {
        int lCol = minefield.length;
        int lRow = minefield[0].length;

        // Imprimim coordenades de columnes
        System.out.println();
        System.out.print("  ");
        for (int i = 1; i < lRow - 1; i++) {
            System.out.printf(" %2s", i);
        }

        System.out.println();
        for (int i = 1; i < lCol - 1; i++) {
            System.out.print((char)(64 + i) + " "); // Imprimim coordenada de fila

            for (int j = 1; j < lRow - 1; j++) { // Imprimim fila
                System.out.printf(" %2s", minefield[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Mètode per mostrar per pantalla el text desitjat acabant amb un salt de línia
     * @param message Missatge que es vol mostrar
     */
    public static void printMessage(String message) {
        System.out.println(message);
    }
}
