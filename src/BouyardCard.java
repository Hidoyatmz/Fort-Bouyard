// EN COURS
// FAIRE SYSTEME DE PIOCHE ET METTRE DANS SES CARTES 
// VOIR SI la pioche ne devrait pas etre Card[][]


class BouyardCard extends EpreuvesCreator {
    
    Epreuve initBouyardCard() {
        return newEpreuve(10, "Les cartes Bouyard", -1, "Toi et le maître doivent piocher chacun leur tour une carte dans l'un de ces tas afin de reconstituer le mot BOUYARD écrit devant vous.\n" +
        "Il est possible de tomber sur un joker qui peut remplacer n'importe quelle lettre\n" +
        "Le premier bien sûr qui recompose le mot BOUYARD remporte ce défi.", GameState.CONSEIL);
    }

    boolean startBouyardCard(Epreuve epreuve) {
        char[] cards = new char[]{'B', 'O', 'U', 'Y', 'A', 'R', 'D', 'J'};
        char[][] pioches = initPioches(cards);
        Card[][] players = new Card[][]{initCardsPlayer(cards), initCardsPlayer(cards)};
        boolean win;

        int indexCurrentPlayer = randInt(0, 1);
        int pioche;

        while(!wordComplete(players)) {
            printInfos(pioches, players, indexCurrentPlayer);
            pioche = askPioche(pioches, indexCurrentPlayer);

        }

        return true;
    }

    void discoverCard(char[][] pioches, int pioche, int currentPlayer) {
        int iCard = indiceLastCard(pioches, pioche);

    }

    

    int indiceLastCard(char[][] pioches, int pioche) {
        int res = length(pioches, 2)-1;
        while(res > 0 && pioches[pioche][res] == ' ') {
            res--;
        }
        return res;
    }

    int askPioche(char[][] pioches, int currentPlayer) {
        int res;
        if(currentPlayer == 0) {
            do {
                res = enterNumber()-1;
            } while(!isBetween(0, length(pioches, 1)-1) && !piocheEmpty(pioches, res));
        }
        else {
            do {
                res = (int) (random()*length(pioches));
            } while(!piocheEmpty(pioches, res));
            
        }
        return res;
    }

    boolean piocheEmpty(char[][] pioches, int pioche) {
        return pioches[pioche][0] != ' ';
    }

    void printInfos(char[][] pioches, Card[][] players, int currentPlayer) {
        // 🂠
        if(currentPlayer == 0) println(ANSI_YELLOW + "C'est à toi de jouer !\n" + ANSI_RESET);
        else println(ANSI_YELLOW + "C'est au maître de jouer !\n" + ANSI_RESET);
        for(int i=0; i<length(pioches, 1); i++) {
            println("Pioche " + (i+1) + " : " + ANSI_YELLOW + "🂠 " + countPioche(pioches, i) + " cartes restantes" + ANSI_RESET);
        }
        printPlayerCard(players, 0);
        println("");
        printPlayerCard(players, 1);
    }

    void printPlayerCard(Card[][] players, int player) {
        String desc = player == 0 ? "Tes cartes" : "Les cartes du maître";
        println(desc);
        String color;
        for(int j=0; j<length(players, 2); j++) {
            color = players[player][j].found ? ANSI_YELLOW : ANSI_CYAN;
            print(color + players[player][j].c + ANSI_RESET + " ");
        }
    }

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

    boolean wordComplete(Card[][] players) {
        boolean complete = true;
        int j;
        for(int i=0; i<length(players, 1); i++) {
            complete = true;
            j = 0;
            while(j < length(players, 2) && complete) {
                if(!players[i][j].found) complete = false;
                j++;
            }
        }
        return complete;
    }

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

    Card newCard(char c) {
        Card card = new Card();
        card.c = c;
        card.found = false;
        return card;
    }

    Card[] initCardsPlayer(char[] cards) {
        Card[] cardsPlayer = new Card[length(cards)-1];
        for(int i=0; i<length(cardsPlayer); i++) {
            cardsPlayer[i] = newCard(cards[i]);
        }
        return cardsPlayer;
    }
}
