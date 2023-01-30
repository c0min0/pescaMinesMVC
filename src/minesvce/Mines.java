package minesvce;

import minesvce.controlador.Controlador;

/**
 * Classe principal des d'on arrenca el programa
 */
public class Mines {

    /**
     * Funció main que executa el joc
     * @param args per introduïr dades d'inici (no s'utilitza)
     */
    public static void main(String[] args) {
        Controlador.play();
    }
}
