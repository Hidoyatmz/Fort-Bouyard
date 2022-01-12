/**
* The Fakir game is a guessing game : You've to find the color of the hole where the ball will fall
* 
*/
class Fakir extends Mists {


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
        Coordonnees pos = newCoordonnees(0, columnStart);
        while(pos.l < length(grille, 1)-1) {
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
    FakirItems getHole(FakirItems[][] grille, Coordonnees pos) {
        FakirItems res;
        if(pos.c == (length(grille, 2)/2)) {
            res = FakirItems.TROU_ALL;
        }
        else if(pos.c % 2 == 0) {
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
    boolean rebond(FakirItems[][] grille, Coordonnees pos) {
        int r = randInt(0, 2);
        // 1 chance sur 3 de rebondir a la fin
        if(r == 0) {
            Coordonnees nextPos1 = newCoordonnees(pos.l, pos.c - 1);
            Coordonnees nextPos2 = newCoordonnees(pos.l, pos.c + 1);
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
    void descendreBille(FakirItems[][] grille, Coordonnees pos) {
        FakirItems underBille = grille[pos.l+1][pos.c];
        if(underBille == FakirItems.BARRE) {
            Coordonnees nextPos1 = newCoordonnees(pos.l+1, pos.c-1);
            Coordonnees nextPos2 = newCoordonnees(pos.l+1, pos.c+1);
            aleaNextPosBille(grille, pos, nextPos1, nextPos2);
        }
        else {
            moveBille(grille, pos, newCoordonnees(pos.l+1, pos.c));
            pos.l = pos.l + 1;
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
    void aleaNextPosBille(FakirItems[][] grille, Coordonnees pos, Coordonnees nextPos1, Coordonnees nextPos2) {
        if(nextPos1.c >= 0 && nextPos2.c < length(grille, 2)) {
            int r = randInt(0, 1);
            if(r == 0) {
                moveBille(grille, pos, nextPos1);
                pos.l = nextPos1.l;
                pos.c = nextPos1.c;
            }
            else {
                moveBille(grille, pos, nextPos2);
                pos.l = nextPos2.l;
                pos.c = nextPos2.c;
            }
        }
        else if(nextPos1.c >= 0) { // LA BILLE NE PEUT PAS ALLER A DROITE
            moveBille(grille, pos, nextPos1);
            pos.l = nextPos1.l;
            pos.c = nextPos1.c;
        }
        else { // LA BILLE NE PEUT ALLER QU'A DROITE
            moveBille(grille, pos, nextPos2);
            pos.l = nextPos2.l;
            pos.c = nextPos2.c;
        }
    }

    /**
     *  The function moves the ball
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     *  @param from is the in-comming position
     *  @param to is the next position
     */
    void moveBille(FakirItems[][] grille, Coordonnees from, Coordonnees to) {
        if(from.l < length(grille, 1)-1) {
            grille[from.l][from.c] = FakirItems.VIDE;
        } else {
            if(from.c == (length(grille, 2)/2)) {
                grille[from.l][from.c] = FakirItems.TROU_ALL;
            }
            else if(from.c % 2 == 0) {
                grille[from.l][from.c] = FakirItems.TROU_BLANC;
            }
            else {
                grille[from.l][from.c] = FakirItems.TROU_BLEU;
            }
        }
        grille[to.l][to.c] = FakirItems.BILLE;
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

    /**
     *  Create new coordinate
     *
     *  @param grille is game's board, you can find obstacles and the ball inside it
     */
    Coordonnees newCoordonnees(int line, int column) {
        Coordonnees coordonnees = new Coordonnees();
        coordonnees.l = line;
        coordonnees.c = column;
        return coordonnees;
    }
}