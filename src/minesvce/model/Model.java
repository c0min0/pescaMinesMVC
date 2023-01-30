package minesvce.model;

import minesvce.vista.Vista;

/**
 * Classe que conté tots els processos que ha de realitzar el joc per avançar.
 */
public class Model {
    static int nRow;
    static int nCol;
    static int nBomb;

    static boolean gameFinished = false;

    static char[][] mfSolution;
    static char[][] mfPlayer;

    /**
     * Mètode que inicialitza el joc.
     * Crea el camp de mines a resoldre i el del jugador segons els paràmetres indicats.
     * També mostra a l'usuari el seu camp de mines inicial.
     * @param rows nombre de files que ha de tenir el camp de mines
     * @param columns nombre de columnes que ha de tenir el camp de mines
     * @param bombs nombre de bombes que ha de tenir el camp de mines
     */
    public static void initGame(int rows, int columns, int bombs){
        nRow = rows;
        nCol = columns;
        nBomb = bombs;

        mfSolution = new char[nRow + 2][nCol + 2];
        mfPlayer = new char[nRow + 2][nCol + 2];

        // Inicialitzem camp de mines de solució i de joc
        initMinefield(mfSolution, ' ');
        initMinefield(mfPlayer, '·');

        //Generem bombes al camp de mines de solució
        generateBombs(mfSolution);

        //Vescanviem espais per quantitat de bombes a la solució
        countBombs(mfSolution);

        //Mostrem camps de mines
        Vista.printMinefield(mfPlayer);
    }

    /**
     * Inicialitza l'array bidimensional que serà el camp de mines,
     * segons la mida indicada per les variables nRow i nCol (L'array serà d'una unitat més per cada vora).
     * Després l'omplena amb el caràcter indicat per paràmetres.
     * @param minefield array bidimensional que volem inicialitzar
     * @param with caràcter amb què volem omplir l'array
     */
    static void initMinefield(char[][] minefield, char with) {
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[i].length; j++) {
                minefield[i][j] = with;
            }
        }
    }

    /**
     * Genera el nombre de bombes indicat a la variable nBomb en posicions aleatòria
     * sobre el camp de mines indicat per paràmetre.
     * @param minefield camp de mines al que volem introduïr les bombes en posicions aleatòries
     */
    static void generateBombs(char[][] minefield) {
        for (int i = 0; i < nBomb; i++) {
            int f = (int) (Math.random() * nRow + 1);
            int c = (int) (Math.random() * nCol + 1);

            if (minefield[f][c] == 'B') i--;   // Si la posició ja té una bomba no serveix: repetim volta
            else minefield[f][c] = 'B';
        }
    }

    /**
     * Mètode que compta el nombre de bombes que té al voltant cada casella
     * i defineix aquest nombre a la propia casella si aquesta no és una bomba.
     * @param minefield camp de mines sobre el que es vol aplicar el procés
     */
    static void countBombs(char[][] minefield) {
        // Recorrem matriu
        for (int i = 1; i < minefield.length - 1; i++) {
            for (int j = 1; j < minefield[i].length - 1; j++) {

                if (minefield[i][j] != 'B') {   // Si no estem a la posició de la bomba
                    // Contem les bombes que hi ha al voltant
                    int count = 0;
                    // Analitzem posicions del voltant
                    for (int m = i - 1; m < i + 2; m++) {
                        for ( int n = j - 1; n < j + 2; n++) {

                            if (m == i && n == j) n++;  // Ens saltem la posició que estem tractant
                            if (minefield[m][n] == 'B') count++;
                        }
                    }
                    // Mostrem a cada posició quantes bombes té al voltant
                    if (count != 0) minefield[i][j] = (char)('0' + count);
                }
            }
        }
    }

    /**
     * Verifica si les coordenades introduïdes per paràmetres entren dintre del camp de mines.
     * @param row (Integer) coordenada de les files que volem verificar
     * @param column (Integer) coordenada de les columnes que volem verificar
     * @return Retorna "True" si les coordenades són correctes i "False" si no ho són
     */
    public static boolean verifyRowColumn(int row, int column) {
        int min = 0;
        int maxR = mfSolution.length - 1;
        int maxC = mfSolution[0].length -1;

        if (row > min && column > min && row < maxR && column < maxC) return true;
        else return false;
    }

    /**
     * Mètode que destapa la casella del camp de mines de l'usuari de les coordenades introduïdes pels paràmetres
     * i d'aquesta i totes les contigües que no tinguin bombes al voltant.
     * Si la casella té una bandera o ja s'ha destapat la casella, informa del succés;
     * si la casella té una bomba, s'acaba la partida;
     * sinó, es destapa la casella i es comproba si l'usuari ha guanyat.
     * @param Row coordenada de les files de la casella que es vol destapar
     * @param Col coordenada de les columnes de la casella que es vol destapar
     */
    public static void stepOn(int Row, int Col) {
        if (mfPlayer[Row][Col] == 'F') Vista.printMessage("Aquesta casella conté una bandera!");
        else if (mfPlayer[Row][Col] != '·') Vista.printMessage("Aquesta casella ja s'ha trepitjat!");
        else if (mfSolution[Row][Col] == 'B') {
            Vista.printMessage("");
            Vista.printMessage(" ======= GAME OVER =======");
            showSolution();
            gameFinished = true;
        }
        else {
            stepOnRecursive(Row, Col);
            Vista.printMinefield(mfPlayer);
            if(playerWins()) {
                Vista.printMessage("");
                Vista.printMessage("HAS GUANYAT!!");
                gameFinished = true;
            }
        }
    }

    /**
     * Mètode que serveix per posar o treure una bandera a les coordenades indicades per paràmetres.
     * Si té una bandera la treu; si no hi ha cap, la posa;
     * i si la casella ja està destapada, avisa del fet.
     * Al final de l'acció comproba si el jugador ha guanyat i mostra com va el camp de mines del jugador.
     * @param Row coordenada de les files on es vol posar o treure la bandera
     * @param Col coordenada de les columnes on es vol posar o treure la bandera
     */
    public static void putFlag(int Row, int Col) {
        if (mfPlayer[Row][Col] == 'F') mfPlayer[Row][Col] = '·';
        else if (mfPlayer[Row][Col] == '·') mfPlayer[Row][Col] = 'F';
        else {
            Vista.printMessage("Aquesta casella ja s'ha trepitjat!");
            return;
        }

        if (playerWins()) {
            Vista.printMessage("Has guanyat!");
            gameFinished = true;
        }
        Vista.printMinefield(mfPlayer);
    }

    /**
     * Mètode que destapa la casella introduïda per paràmetre i extén l'acció recursivament
     * a tota casella adjacent que no tingui bombes al voltant (que tingui un valor buit).
     * @param Row coordenada de les files de la casella sobre la que volem extendre l'acció
     * @param Col coordenada de les columnes de la casella sobre la que volem extendre l'acció
     */
    static void stepOnRecursive(int Row, int Col) {
        if(verifyRowColumn(Row, Col)) {     // Si les coordenades estan dins dels límits
            if (mfPlayer[Row][Col] == '·') {    // i si la casella no s'ha destapat
                // Destàpala
                mfPlayer[Row][Col] = mfSolution[Row][Col];

                if (mfPlayer[Row][Col] == ' ') {    // // i si no té bombes al voltant
                    // fes el mateix per les caselles del voltant
                    stepOnRecursive(Row - 1, Col - 1);
                    stepOnRecursive(Row - 1, Col);
                    stepOnRecursive(Row - 1, Col + 1);
                    stepOnRecursive(Row, Col - 1);
                    stepOnRecursive(Row, Col + 1);
                    stepOnRecursive(Row + 1, Col - 1);
                    stepOnRecursive(Row + 1, Col);
                    stepOnRecursive(Row + 1, Col + 1);
                }
            }
        }

    }

    /**
     * Mètode per comprobar si l'usuari ha guanyat.
     * Aixó passa quan el camp de mines de l'usuari és igual al camp de mines
     * de la sol·lució però amb banderes a les coordenades de les bombes.
     * @return "True" si ha guanyat i "False" si quan s'executa la funció no es compleixen les condicions
     */
    static boolean playerWins() {
        for (int i = 1; i < mfSolution.length - 1; i++) {
            for (int j = 1; j < mfSolution[0].length - 1; j++) {
                // Si la casella de la sol·lució i la del jugador no són iguals
                if (mfSolution[i][j] != mfPlayer[i][j]) {
                    // i la de la solucio no és 'B' i la del jugador no és 'F'
                    if (!(mfSolution[i][j] == 'B' && mfPlayer[i][j] == 'F')) return false;
                }
            }
        }
        return true;
    }

    /**
     * Mostra per pantalla el camp de mines de l'usuari tal com està en el moment de l'execució,
     * afegint les bombes que no tenen bandera,
     * i una 'X' on les banderes estiguin mal posades.
     */
    static void showSolution() {
        for (int i = 1; i < mfSolution.length - 1; i++) {
            for (int j = 1; j < mfSolution[0].length - 1; j++) {
                // Mostra bombes que no estan marcades
                if (mfSolution[i][j] == 'B' && mfPlayer[i][j] != 'F') mfPlayer[i][j] = 'B';
                // Mostra les baderes que no estan ben posades
                if (mfSolution[i][j] !='B' && mfPlayer[i][j] == 'F') mfPlayer[i][j] = 'X';
            }
        }
        Vista.printMinefield(mfPlayer);
    }

    /**
     * Mètode que indica si el joc ha finalitzat
     * @return "True" si el joc ha finalitzat i "False" si no ha finalitzat
     */
    public static boolean getGameFinished() {
        return gameFinished;
    }
}
