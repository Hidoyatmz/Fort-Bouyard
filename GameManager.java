class GameManager extends Quiz {

    final int MAXEPREUVESKEY = 4;
    final int MAXEPREUVESJUGEMENT = 2;
    final int MAXEPREUVESINDICES = 5;
    final int MAXEPREUVESCONSEIL = 3;

    void startGame(Game game) {
        myClearScreen();
        println("Epreuves des clés !");
        println("Ton but est de " + ANSI_RED + "ramasser un maximum de clés !" + ANSI_RESET);
        delay(5000);
        /*
            TODO
            - LA BOUCLE DE JEU -- PAS FAIT
            - ASSIGNER UN JOUEUR A L'EPREUVE (TIRER ALEATOIREMENT PARMIS LES JOUEURS QUI NE SONT PAS EN PRISON) -- FAIT
            - L'EPREUVE RETOURNE TRUE SI LE JOUEUR A REUSSI SINON FALSE ET L'ENVOIE EN PRISON -- EN COURS
        */
        //startEpreuve(game, game.epreuves[0]);
        //startEpreuve(game, game.epreuves[1]);
        //startEpreuve(game, game.epreuves[2]);
        startEpreuve(game, game.epreuves[3]);
        //startEpreuve(game, game.epreuves[4]);
    }

    boolean startEpreuve(Game game, Epreuve epreuve) {
        boolean res = false;
        initEpreuve(game, epreuve);
        if(epreuve.id == 0) {
            res = startQuiz(epreuve, game);
        }
        else if(epreuve.id == 1) {
            res = startSoundGame(epreuve, game);
        }
        else if(epreuve.id == 2) {
            res = startPileOuFace(epreuve, game);
        }
        else if(epreuve.id == 3) {
            res = startFakir(epreuve, game);
        }
        else if(epreuve.id == 4) {
            res = startShiFuMi(epreuve, game);
        }
        return res;
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