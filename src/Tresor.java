class Tresor extends Quiz {

    TresorType tresor;

    final String player = "⅄";
    final String coin = "•";
    final String sol = "▔";
    final int MAXCOINS = 15;
    final int MAXSPEED = 600;
    final int MAXPIECELINE = 5;

    int startTresor(Game game) {
        char[][] help = initHelp(game.motCode);
        String res;
        Timer timer = newTimer(game.timerTresor);
        // Trouver mot
        do{
            myClearScreen();
            printHelp(help, game.motCode);
            printIndices(game.indices);
            println("Il te reste " + ANSI_GREEN + formatTime(getRemainingTime(timer)) + ANSI_RESET + " secondes");
            println("Trouver le mot code :");
            res = readString();
        } while(inTime(timer) && !equals(toUpperCase(game.motCode), toUpperCase(res)));

        // Ramasser Pieces
        tresor = initTresor(10, 15);
        initPlateau(tresor.grille);
        hide();
        delay(1000);
        long lastTime = getElapsedTimeMs(timer);
        while(inTime(timer)) {
            myClearScreen();
            if(getElapsedTimeMs(timer) - lastTime >= tresor.speed) {
                update(timer);
                lastTime = getElapsedTimeMs(timer);
                if(getElapsedTime(timer) % 30 == 0) {
                    tresor.maxNbPieceLine = (tresor.maxNbPieceLine + 1) < MAXPIECELINE ? tresor.maxNbPieceLine + 1 : MAXPIECELINE;
                    tresor.speed = (tresor.speed-100) > MAXSPEED ? tresor.speed-100 : MAXSPEED;
                }
            }
            enableKeyTypedInConsole(false);
            printGrille(tresor.grille);
            println("Il reste " + ANSI_GREEN + formatTime(getRemainingTime(timer)) + ANSI_RESET + " secondes !");
            println("Tu as " + ANSI_RED + tresor.playerCoins + ANSI_RESET + " pièces dans ton sac !");
            println("Trésor : " + ANSI_GREEN + tresor.tresor + ANSI_RESET + " pièces !");
            enableKeyTypedInConsole(true);
            readString(1000);
        }
        enableKeyTypedInConsole(false);
        show();
        return tresor.tresor;
    }

    // Fonctions trouver le mot-code
    void printHelp(char[][] help, String motCode) {
        println("La grille d'aide :");
        String line = line(length(help, 1));
        String res = line;
        for(int i=0; i<length(help, 1); i++) {
            res = res + "\n|" + " ";
            for(int j=0; j<length(help, 2); j++) {
                res = res + ANSI_CYAN +  help[i][j] + ANSI_RESET + " | ";
            }
            res = res + "\n" + line;
        }
        println(res);
    }

    char[][] initHelp(String motCode) {
        char[][] res = new char[6][6];
        int r, l, c;
        for(int i=0; i<length(motCode); i++) {
            do {
                r = (int) (random()*(length(res, 1)*length(res, 2)));
                l = r/length(res, 1);
                c = r%length(res, 2);
            } while(res[l][c] != '\0');
            res[l][c] = charAt(motCode, i);
        }

        fillTab(res);
        return res;
    }

    void fillTab(char[][] tab) {
        for(int i=0; i<length(tab, 1); i++) {
            for(int j=0; j<length(tab, 2); j++) {
                if(tab[i][j] == '\0') {
                    tab[i][j] = generateLetter();
                }
            }
        }
    }

    char generateLetter() {
        return (char) ((int) (random()*26) + 'A');
    }

    String line(int size) {
        String res = "+";
        for(int i=0; i<size; i++) {
            res = res + " - +";
        }
        return res;
    }

    void printIndices(Indice[] indices) {
        String res = "Indices : ";
        for(int i=0; i<length(indices); i++) {
            if(indices[i].found) res = res + indices[i].indice + " ";
        }
        println(res);
    }

    // Fonctions salle au trésor
    TresorType initTresor(int nbL, int nbC) {
        TresorType tresorType = new TresorType();
        tresorType.grille = new String[nbL][nbC];
        tresorType.playerL = length(tresorType.grille, 1)-2;
        tresorType.playerC = 0;
        tresorType.playerCoins = 0;
        tresorType.tresor = 0;
        tresorType.speed = 1000;
        tresorType.maxNbPieceLine = 2;
        return tresorType;
    }

    void update(Timer timer) {
        updatePlateau(tresor.grille);
        apparitionPiece(tresor.grille);
    }

    void addCoin() {
        if(tresor.playerCoins < MAXCOINS) {
            tresor.playerCoins++;
        }
    }

    void apparitionPiece(String[][] plateau) {
        int r = (int) (random()*(tresor.maxNbPieceLine+1));
        int[] pos = new int[r];
        for(int i=0; i<r; i++) {
            pos[i] = (int)(random()*length(plateau, 2));
        }
        for(int j=0; j<length(plateau, 2); j++) {
            if(contains(pos, j)) {
                plateau[0][j] = coin;
            } else {
                plateau[0][j] = " ";
            }
        }
    }

    boolean contains(int[] nbs, int nb) {
        boolean c = false;
        int i=0;
        while(i<length(nbs) && !c) {
            if(nbs[i] == nb) c = true;
            i++;
        }
        return c;
    }

    void updatePlateau(String[][] plateau) {
        for(int i=tresor.playerL; i>=1; i--) {
            for(int j=0; j<length(plateau, 2); j++) {
                if(!equals(plateau[i][j], player)) {
                    plateau[i][j] = plateau[i-1][j];
                } else {
                    if(equals(plateau[i-1][j], coin)) {
                        addCoin();
                    }
                }
            }
        }
    }

    void initPlateau(String[][] plateau) {
        for(int i=0; i<length(plateau, 1); i++) {
            for(int j=0; j<length(plateau, 2); j++) {
                if(i == tresor.playerL && j == 0) {
                    plateau[i][j] = player;
                }
                else if(i == length(plateau, 1)-1) {
                    plateau[i][j] = sol;
                }
                else {
                    plateau[i][j] = " ";
                }
            }
        }
    }

    void printGrille(String[][] plateau) {
        String res = "";
        for(int i=0; i<length(plateau, 1); i++) {
            for(int j=0; j<length(plateau, 2); j++) {
                if(equals(plateau[i][j], coin)) res = res + ANSI_YELLOW;
                else if(equals(plateau[i][j], player)) res = res + ANSI_CYAN;
                if(i == (length(plateau, 1)-1) && j==0) res = res + ANSI_GREEN;
                res = res + plateau[i][j] + ANSI_RESET + " ";
            }
            res = res + "\n";
        }
        println(res);
    }


    void keyTypedInConsole(char c) {
        if(c == ANSI_LEFT) {
            clearLine();
            moveLeft(tresor.grille);
        } else if(c == ANSI_RIGHT) {
            clearLine();
            moveRight(tresor.grille);
        }
    }

    void moveRight(String[][] plateau) {
        if((tresor.playerC+1) < (length(plateau, 2))) {
            if(equals(plateau[tresor.playerL][tresor.playerC+1], coin)) addCoin();
            plateau[tresor.playerL][tresor.playerC] = " ";
            plateau[tresor.playerL][tresor.playerC+1] = player;
            tresor.playerC++;
        }
    }

    void moveLeft(String[][] plateau) {
        if((tresor.playerC-1) >= 0) {
            if(equals(plateau[tresor.playerL][tresor.playerC-1], coin)) addCoin();
            plateau[tresor.playerL][tresor.playerC] = " ";
            plateau[tresor.playerL][tresor.playerC-1] = player;
            tresor.playerC--;
            if(tresor.playerC == 0) {
                tresor.tresor = tresor.tresor + tresor.playerCoins;
                tresor.playerCoins = 0;
            }
        }
    }


}