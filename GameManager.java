class GameManager extends Quiz {

    void startGame(Game game) {
        myClearScreen();
        println("Et tout de suite : UN NOUVEAU JEU !");
        delay(2000);
        /*
            TODO
            - LA BOUCLE DE JEU
            - ASSIGNER UN JOUEUR A L'EPREUVE (TIRER ALEATOIREMENT PARMIS LES JOUEURS QUI NE SONT PAS EN PRISON) -- FAIT
            - L'EPREUVE RETOURNE TRUE SI LE JOUEUR A REUSSI SINON FALSE ET L'ENVOIE EN PRISON
        */
        //startEpreuve(game, game.epreuves[0]);
        //startEpreuve(game, game.epreuves[1]);
        //startEpreuve(game, game.epreuves[2]);
        startEpreuve(game, game.epreuves[3]);
        //startEpreuve(game, game.epreuves[4]);
    }

    void startEpreuve(Game game, Epreuve epreuve) {
        initEpreuve(game, epreuve);
        if(epreuve.id == 0) {
            startQuiz(epreuve, game);
        }
        else if(epreuve.id == 1) {
            startSoundGame(epreuve, game);
        }
        else if(epreuve.id == 2) {
            startPileOuFace(epreuve, game);
        }
        else if(epreuve.id == 3) {
            startFakir(epreuve, game);
        }
        else if(epreuve.id == 4) {
            startShiFuMi(epreuve, game);
        }
    }

    void initEpreuve(Game game, Epreuve epreuve) {
        myClearScreen();
        epreuve.player = haulPlayer(game.team);
        println("La prochaine épreuve sera : " + ANSI_YELLOW + epreuve.name + ANSI_RESET + " !");
        println("C'est à " + ANSI_YELLOW + epreuve.player.pseudo + ANSI_RESET + " de jouer !\n");
        pressEnterToContinue();
        myClearScreen();
        println("Voici " + ANSI_PURPLE + "les règles " + ANSI_RESET + "de l'épreuve " + ANSI_YELLOW + epreuve.name + ANSI_RESET);
        println("");
        println(epreuve.rules + "\n");
        pressEnterToContinue();
    }

    Player haulPlayer(Team team) {
        Player player;
        int r;
        do {
            r = randInt(0, length(team.players)-1);
            player = team.players[r];
        } while(player.jail);
        return player;
    }
    
}