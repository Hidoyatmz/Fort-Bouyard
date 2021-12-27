class Fakir extends PileOuFace {
    
    Epreuve initFakir() {
        return newEpreuve(3, "Le Fakir", -1, "Choisi la couleur où la bille va tomber et met là où tu as envie qu'elle tombe.", GameState.JUGEMENT);
    }

    boolean startFakir(Epreuve epreuve, Game game) {
        FakirItems[][] grille = new FakirItems[11][9];
        initFakirGrille(grille);
        afficherFakir(grille);
        // CHOIX COULEUR OU LA BILLE VA ATTERIR
        // CHOIX COLONNE DE LA BILLE PAR LE JOUEUR
        // LANCER LA BILLE ET LA FAIRE DESCENDRE SUIVANT DES REGLES ET PROBAS
        // RECUPERER COULEUR OU LA BILLE EST ARRIVER ET LA COMPARER AVEC LA COULEUR CHOISI PAR LE JOUEUR (LE VIOLET COMPTE COMME NIMPORTE QUEL COULEUR)
        delay(20*1000);
        return true;
    }

    enum FakirItems {
        VIDE, BILLE, BARRE, TROU_BLANC, TROU_BLEU, TROU_ALL;
    }

    void afficherFakir(FakirItems[][] grille) {
        String affichage = "";
        for(int i=0; i<length(grille, 1); i++) {
            for(int j=0; j<length(grille, 2); j++) {
                if(grille[i][j] == FakirItems.BILLE) affichage = affichage + " O ";
                else if(grille[i][j] == FakirItems.BARRE) affichage = affichage + " * ";
                else if(grille[i][j] == FakirItems.TROU_BLANC) affichage = affichage + ANSI_WHITE + " _ " + ANSI_RESET;
                else if(grille[i][j] == FakirItems.TROU_BLEU) affichage = affichage + ANSI_CYAN + " _ " + ANSI_RESET;
                else if(grille[i][j] == FakirItems.TROU_ALL) affichage = affichage + ANSI_PURPLE + " _ " + ANSI_RESET;
                else affichage = affichage + "   ";
            }
            affichage = affichage + "\n";
        }
        println(affichage);
    }

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