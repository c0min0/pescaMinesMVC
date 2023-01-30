package minesvce.controlador;

import java.util.Scanner;

import minesvce.model.Model;
import minesvce.vista.Vista;

/**
 * Classe que conté aquells mètodes que fan que l'usuari interactui amb el joc.
 */
public class Controlador {

    static Scanner scan = new Scanner(System.in);
    static int rows, cols, bombs;

    /**
     * Mètode que executa tot el que és necessari a cada etapa del joc perquè aquest funcioni correctament.
     */
    public static void play() {
        Vista.printMessage("=====PESCA-MINES=====");

        // Fem que l'usuari trïi el nivell
        setLevel(scan);

        // Inicialitzem el joc
        Model.initGame(rows, cols, bombs);

        do {

            char opcio;
            int fila, columna;

            // Demanem acció a realitzar
        Vista.printMessage("");
        Vista.printMessage("Vols trepitjar (T), posar una bandera (F), o acabar (A)? ");
            opcio = scan.nextLine().toUpperCase().charAt(0);

            // L'opció triada és incorrecta
            if (opcio != 'T' && opcio != 'F' && opcio != 'A') {
                Vista.printMessage("");
                Vista.printMessage("No has triat una opció correcte! (T, F o A)");

            // L'usuari vol acabar
            } else if (opcio == 'A') {
                Vista.printMessage("");
                Vista.printMessage("Fins la propera!");
                return;

            } else {

                boolean errorCoordenada;
                do {
                    errorCoordenada = false;

                    // Demanem coordenades on actuar
                    Vista.printMessage("");
                    Vista.printMessage("Sobre quina casella? (Fila, Columna): ");
                    String fc = scan.nextLine().trim();    // Agafem tot i treiem espais davant i darrere
                    fila = (fc.toUpperCase().charAt(0) - 64); // Passem caràcter a enter
                    fc = fc.substring(1).trim(); // Treiem el primer valor i treiem espais davant i darrere
                    columna = Integer.parseInt(fc); // Passem caracter a enter

                    if (Model.verifyRowColumn(fila, columna)) { // Si les coordenades són correctes
                        switch (opcio) {
                            case 'T':   // Trepitjem casella indicada
                                Model.stepOn(fila, columna);
                                if(Model.getGameFinished()) return; // Si el joc ha acabat, tornem
                                break;

                            case 'F':   // Posem una bandera
                                Model.putFlag(fila, columna);
                                if(Model.getGameFinished()) return;
                        }

                    } else {    // Si les coordenades no són correctes
                        Vista.printMessage("Les coordenades són incorrectes!");
                        errorCoordenada = true;
                    }
                } while (errorCoordenada);  // Repetim fins que les coordenades siguin correctes
            }
        } while (!Model.getGameFinished());    // Repetim fins que el joc hagi acabat
    }

    /**
     * Mostra un menú amb les dificultats del joc i li demana a l'usuari que trïi quina desitja.
     * @param scan Dificultat que vol jugar l'usuari (introduït per teclat per aquest)
     */
    static void setLevel(Scanner scan) {
        boolean error;
        do {
            error = false;
            Vista.printMessage("1 - 8x8 (10 bombes)");
            Vista.printMessage("2 - 16x16 (40 bombes)");
            Vista.printMessage("3 - 16x30 (99 bombes)");
            Vista.printMessage("S - Sortir del programa");
            System.out.print("Tria el nivell: ");

            char option = scan.nextLine().toUpperCase().charAt(0);

            switch (option) {
                case '1':
                    rows = 8;
                    cols = 8;
                    bombs = 10;
                    break;
                case '2':
                    rows = 16;
                    cols = 16;
                    bombs = 40;
                    break;
                case '3':
                    rows = 16;
                    cols = 30;
                    bombs = 99;
                    break;
                case 'S':
                    // Finalitzem el programa
                    System.exit(0);
                default:
                    Vista.printMessage("");
                    Vista.printMessage("No has triat una opció correcte!");
                    error = true;
            }
        } while (error);
    }
}
