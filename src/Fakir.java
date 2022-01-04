/**
* The Fakir game is a guessing game : You've to find the color of the hole where the ball will fall
* 
*/
class Fakir extends PileOuFace {


    /**
     *  Return an Epreuve, containing an Id, a Name, a Timer, The rules of the games, and a GameState
     *  
     *  @see Epreuve
     *  @return Epreuve Object
     */
    Epreuve initFakir() {
        return newEpreuve(3, "Le Fakir", -1, "Choisi la couleur où la bille va tomber et met là où tu as envie qu'elle tombe.", GameState.JUGEMENT);
    }

    /**
     *  Start the Fakir's game
     *
     *  @param epreuve 
     *  @return true, if the player won the game, false if he lose
     */
    boolean startFakir(Epreuve epreuve) {
        FakirItems[][] grille = new FakirItems[12][9];
        initFakirGrille(grille);
        afficherFakir(grille);
        FakirItems couleur = choixBilleCouleur();
        int columnStart = choixBilleStart(grille);
        placerBille(grille, columnStart);
        afficherFakir(grille);
        delay(700);
        int[] pos = new int[]{0, columnStart};
        while(pos[0] < length(grille, 1)-1) {
            descendreBille(grille, pos);
            afficherFakir(grille);
            delay(700);
        }
        boolean rebondi = rebond(grille, pos);
        if(rebondi) {
            afficherFakir(grille);
            println("La bille a rebondi !");
            delay(700);
        }
        FakirItems atteri = getHole(grille, pos);
        boolean win = fakirWin(couleur, atteri);
        if(win) {
            println("Bravo tu as gagné !");
        } else {
            println("Tu as perdu !");
        }
        return win;
    }

    /**
     *  Check if the player predict is a correct guess or not
     *
     *  @param predict is the player's choice
     *  @param land is where the ball fall 
     *  @return true, if the player won the game, false if he loose
     */
    boolean fakirWin(FakirItems predict, FakirItems land) {
        boolean res = false;
        if(land == FakirItems.TROU_ALL || predict == land) {
            res = true;
        }
        return res;
    }

    /**
     *  Get the color of the hole, where the ball falled in
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     *  @param pos 
     *  @return the color of the hole, where the ball falled in
     */
    FakirItems getHole(FakirItems[][] grille, int[] pos) {
        FakirItems res;
        if(pos[1] == (length(grille, 2)/2)) {
            res = FakirItems.TROU_ALL;
        }
        else if(pos[1] % 2 == 0) {
            res = FakirItems.TROU_BLANC;
        }
        else {
            res = FakirItems.TROU_BLEU;
        }
        return res;
    }

    /**
     *  Check if the ball bounced
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     *  @param pos
     *  @return true, if the ball bounced
     */
    boolean rebond(FakirItems[][] grille, int[] pos) {
        int r = randInt(0, 2);
        // 1 chance sur 3 de rebondir a la fin
        if(r == 0) {
            int[] nextPos1 = new int[]{pos[0], pos[1]-1};
            int[] nextPos2 = new int[]{pos[0], pos[1]+1};
            aleaNextPosBille(grille, pos, nextPos1, nextPos2);
            return true;
        }
        return false;
    }
    
    /**
     *  Make the ball fall off one part of the grid
     *
     *  @param grille
     *  @param pos
     *  @return true, if the ball bounced
     */
    void descendreBille(FakirItems[][] grille, int[] pos) {
        FakirItems underBille = grille[pos[0]+1][pos[1]];
        if(underBille == FakirItems.BARRE) {
            int[] nextPos1 = new int[]{pos[0]+1, pos[1]-1};
            int[] nextPos2 = new int[]{pos[0]+1, pos[1]+1};
            aleaNextPosBille(grille, pos, nextPos1, nextPos2);
        }
        else {
            moveBille(grille, pos, new int[]{pos[0]+1, pos[1]});
            pos[0] = pos[0] + 1;
        }
    }

    /**
     *  Function is choosing where the ball is going
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     *  @param pos
     *  @param nextPos1 is one choice
     *  @param nextPos2 is one choice
     */
    void aleaNextPosBille(FakirItems[][] grille, int[] pos, int[] nextPos1, int[] nextPos2) {
        if(nextPos1[1] >= 0 && nextPos2[1] < length(grille, 2)) {
            int r = randInt(0, 1);
            if(r == 0) {
                moveBille(grille, pos, nextPos1);
                pos[0] = nextPos1[0];
                pos[1] = nextPos1[1];
            }
            else {
                moveBille(grille, pos, nextPos2);
                pos[0] = nextPos2[0];
                pos[1] = nextPos2[1];
            }
        }
        else if(nextPos1[1] >= 0) { // LA BILLE NE PEUT PAS ALLER A DROITE
            moveBille(grille, pos, nextPos1);
            pos[0] = nextPos1[0];
            pos[1] = nextPos1[1];
        }
        else { // LA BILLE NE PEUT ALLER QU'A DROITE
            moveBille(grille, pos, nextPos2);
            pos[0] = nextPos2[0];
            pos[1] = nextPos2[1];
        }
    }

    /**
     *  The function moves the ball
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     *  @param from is the in-comming position
     *  @param to is the next position
     */
    void moveBille(FakirItems[][] grille, int[] from, int[] to) {
        if(from[0] < length(grille, 1)-1) {
            grille[from[0]][from[1]] = FakirItems.VIDE;
        } else {
            if(from[1] == (length(grille, 2)/2)) {
                grille[from[0]][from[1]] = FakirItems.TROU_ALL;
            }
            else if(from[1] % 2 == 0) {
                grille[from[0]][from[1]] = FakirItems.TROU_BLANC;
            }
            else {
                grille[from[0]][from[1]] = FakirItems.TROU_BLEU;
            }
        }
        grille[to[0]][to[1]] = FakirItems.BILLE;
    }

    /**
     *  Ask to the player the choice of the final hole
     *
     *  @return the choice of the player
     */
    FakirItems choixBilleCouleur() {
        println("Choisis la couleur sur laquelle tu penses que la bille va tomber.");
        println("1 - Blanc");
        println("2 - Bleu");
        int res;
        do {
            res = enterNumber();
        } while(!isBetween(res, 1, 2));
        FakirItems choix = FakirItems.TROU_BLANC;
        if(res == 2) choix = FakirItems.TROU_BLEU;
        return choix;
    }

    /**
     *  Ask to the player the starting position of the ball
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     */
    int choixBilleStart(FakirItems[][] grille) {
        println("Choisis la colonne où la bille sera lachée.");
        println("Entre 1 et " + length(grille, 2));
        int res;
        do {
            res = enterNumber();
        } while(!isBetween(res, 1, length(grille, 2)));
        return res-1;
    }

    /**
     *  Put the ball in the grid
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     *  @param column is the column where the ball is placed
     */
    void placerBille(FakirItems[][] grille, int column) {
        grille[0][column] = FakirItems.BILLE;
    }

    /**
     *  Display the game
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     */
    void afficherFakir(FakirItems[][] grille) {
        myClearScreen();
        String affichage = "";
        for(int i=1; i<=length(grille, 2); i++) {
            affichage = affichage + " " + i + " ";
        }
        for(int i=0; i<length(grille, 1); i++) {
            affichage = affichage + "\n";
            for(int j=0; j<length(grille, 2); j++) {
                if(grille[i][j] == FakirItems.BILLE) affichage = affichage + ANSI_YELLOW + " O " + ANSI_RESET;
                else if(grille[i][j] == FakirItems.BARRE) affichage = affichage + " * ";
                else if(grille[i][j] == FakirItems.TROU_BLANC) affichage = affichage + ANSI_WHITE + " ▁ " + ANSI_RESET;
                else if(grille[i][j] == FakirItems.TROU_BLEU) affichage = affichage + ANSI_CYAN + " ▁ " + ANSI_RESET;
                else if(grille[i][j] == FakirItems.TROU_ALL) affichage = affichage + ANSI_PURPLE + " ▁ " + ANSI_RESET;
                else affichage = affichage + "   ";
            }
        }
        println(affichage);
    }

    /**
     *  Init the fakir's grid
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     */
    void initFakirGrille(FakirItems[][] grille) {
        for(int i=0; i<length(grille, 1); i++) {
            for(int j=0; j<length(grille, 2); j++) {
                if(i==0 || i==1 || i == length(grille, 1)-2) {
                    grille[i][j] = FakirItems.VIDE;
                }
                else if(i == length(grille, 1)-1) {
                    if(j == (length(grille, 2)/2)) {
                        grille[i][j] = FakirItems.TROU_ALL;
                    }
                    else if(j % 2 == 0) {
                        grille[i][j] = FakirItems.TROU_BLANC;
                    }
                    else {
                        grille[i][j] = FakirItems.TROU_BLEU;
                    }
                }
                else {
                    if((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1 && j<length(grille, 2)-1)) {
                        grille[i][j] = FakirItems.BARRE;
                    }
                    else {
                        grille[i][j] = FakirItems.VIDE;
                    }
                }
            }
        }
    }
}