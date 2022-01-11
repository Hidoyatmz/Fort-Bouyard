// EN COURS
// FAIRE SYSTEME DE PIOCHE ET METTRE DANS SES CARTES 
// VOIR SI la pioche ne devrait pas etre Card[][]


class BouyardCard extends EpreuvesCreator {
    
    // init
    Epreuve initBouyardCard() {
        return newEpreuve(10, "Les cartes Bouyard", -1, "Toi et le maître doivent piocher chacun leur tour une carte dans l'un de ces tas afin de reconstituer le mot BOUYARD écrit devant vous.\n" +
        "Il est possible de tomber sur un joker qui peut remplacer n'importe quelle lettre\n" +
        "Le premier bien sûr qui recompose le mot BOUYARD remporte ce défi.", GameState.CONSEIL);
    }

    // start epreuve
    boolean startBouyardCard(Epreuve epreuve) {
        char[] cards = new char[]{'B', 'O', 'U', 'Y', 'A', 'R', 'D', 'J'};
        char[][] pioches = initPioches(cards);
        Card[][] players = new Card[][]{initCardsPlayer(cards), initCardsPlayer(cards)};
        boolean win;

        int indexCurrentPlayer = randInt(0, 1);
        int pioche;
        char discover;

        while(!wordComplete(players)) {
            myClearScreen();
            if(indexCurrentPlayer == 0) println(ANSI_YELLOW + "C'est à toi de jouer !\n" + ANSI_RESET);
            else println(ANSI_YELLOW + "C'est au maître de jouer !\n" + ANSI_RESET);
            printInfos(pioches, players);
            delay(1000);
            pioche = askPioche(pioches, indexCurrentPlayer);
            discover = discoverCard(players, indexCurrentPlayer, pioches, pioche, cards);
            putCard(players, indexCurrentPlayer, discover);
            indexCurrentPlayer = (indexCurrentPlayer+1) % 2;
        }
        myClearScreen();
        printInfos(pioches, players);
        win = wordComplete(players, 0);
        if(win) println("Bien joué tu as gagné !");
        else println("Le maître a gagné");
        delay(2000);
        return win;
    }

    
    // Met la carte de char c dans le deck du joueur
    void putCard(Card[][] players, int currentPlayer, char c) {
        int iCard = indexCard(players, currentPlayer, c);
        players[currentPlayer][iCard].found = true;
    }

    // Découvre la carte d'une pioche et retourne son char correspondant | Si c'est un joker le joueur choisi la carte qu'il veut
    char discoverCard(Card[][] cardsPlayers, int player, char[][] pioches, int pioche, char[] cards) {
        char c = pioches[pioche][countPioche(pioches, pioche)-1];
        pioches[pioche][countPioche(pioches, pioche)-1] = ' ';
        if(c == 'J') {
            if(player == 0) {
                do {
                    println("Tu as piocher un joker ! Quelle lettre veux tu remplacer ?");
                    c = charAt(toUpperCase(enterText()), 0);
                } while(!inArray(cards, c));
            }
            else {
                c = jPiocheMaitre(cardsPlayers);
            }
        }
        return c;
    }

    // Carte choisi par le maitre si il a pioché un joker
    char jPiocheMaitre(Card[][] cards) {
        char res = cards[1][0].c;
        int j = 0;
        boolean found = false;
        while(j < length(cards, 2) && !found) {
            if(!cards[1][j].found) {
                found = true;
                res = cards[1][j].c;
            }
            j++;
        }
        return res;
    }

    // Indice de la carte de char c dans le deck du joueur
    int indexCard(Card[][] cards, int currentPlayer, char c) {
        int i = 0;
        boolean trouver = false;
        while(i<length(cards, 2)-1 && !trouver) {
            if(cards[currentPlayer][i].c == c) trouver = true;
            else i++;
        }
        return i;
    }

    // Demande au joueur la pioche dans laquelle il veut piocher | Si c'est le mettre il prend une pioche au hasard
    int askPioche(char[][] pioches, int currentPlayer) {
        int res;
        if(currentPlayer == 0) {
            do {
                println("Dans quelle pioche veux tu piocher ?");
                res = enterNumber();
                res = res - 1;
            } while(!(isBetween(res, 0, (length(pioches, 1)-1)) && !piocheEmpty(pioches, res)));
        }
        else {
            do {
                res = (int) (random()*length(pioches));
            } while(piocheEmpty(pioches, res));
            
        }
        return res;
    }

    // Renvoie si une pioche est vide
    boolean piocheEmpty(char[][] pioches, int pioche) {
        return pioches[pioche][0] == ' ';
    }

    // Affichage des infos du jeu
    void printInfos(char[][] pioches, Card[][] players) {
        // 🂠
        for(int i=0; i<length(pioches, 1); i++) {
            println("Pioche " + (i+1) + " : " + ANSI_YELLOW + "🂠 " + countPioche(pioches, i) + " cartes restantes" + ANSI_RESET);
        }
        printPlayerCard(players, 0);
        println("");
        printPlayerCard(players, 1);
    }

    // Affiche le deck d'un joueur
    void printPlayerCard(Card[][] players, int player) {
        String desc = player == 0 ? "Tes cartes" : "Les cartes du maître";
        println(desc);
        String color;
        String res = "";
        for(int j=0; j<length(players, 2); j++) {
            color = players[player][j].found ? ANSI_YELLOW : ANSI_CYAN;
            res = res + color + players[player][j].c + ANSI_RESET + " ";
        }
        println(res);
    }

    // Retourne le nombre de pioches
    int countPioche(char[][] pioches, int pioche) {
        int somme = 0;
        boolean end = false;
        int j = 0;
        while(j < length(pioches, 2) && !end) {
            if(pioches[pioche][j] != ' ') {
                somme++;
                j++;
            }
            else {
                end = true;
            }
        }
        return somme;
    }

    // Renvoie si un des deck est rempli
    boolean wordComplete(Card[][] players) {
        boolean complete = false;
        int i = 0;
        int j;
        while(i<length(players, 1) && !complete) {
            complete = wordComplete(players, i);
            i++;
        }
        return complete;
    }

    // Renvoie si le deck d'un joueur est rempli
    boolean wordComplete(Card[][] players, int player) {
        boolean complete = true;
        int j = 0;
        while(j < length(players, 2) && complete) {
            if(!players[player][j].found) complete = false;
            j++;
        }
        return complete;
    }

    // init les pioches
    char[][] initPioches(char[] cards) {
        char[][] pioches = new char[4][length(cards)];
        for(int i=0; i<length(pioches, 1); i++) {
            for(int j=0; j<length(pioches, 2); j++) {
                pioches[i][j] = cards[j];
            }
            shufflePioche(pioches, i);
        }
        return pioches;
    }

    // mélange une pioche
    void shufflePioche(char[][] pioches, int pioche) {
        int r;
        char temp;
        for(int i=0; i<10; i++) {
            for(int j=0; j<length(pioches, 2); j++) {
                r = (int) (random()*length(pioches, 2));
                temp = pioches[pioche][j];
                pioches[pioche][j] = pioches[pioche][r];
                pioches[pioche][r] = temp;
            }
        }
    }

    // Constructeur d'une carte
    Card newCard(char c) {
        Card card = new Card();
        card.c = c;
        card.found = false;
        return card;
    }

    // init les cartes d'un joueur
    Card[] initCardsPlayer(char[] cards) {
        Card[] cardsPlayer = new Card[length(cards)-1];
        for(int i=0; i<length(cardsPlayer); i++) {
            cardsPlayer[i] = newCard(cards[i]);
        }
        return cardsPlayer;
    }
}
