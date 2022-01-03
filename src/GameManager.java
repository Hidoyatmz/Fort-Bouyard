class GameManager extends Quiz {

    final int MAXEPREUVESKEY = 4; // AUSSI LE NOMBRE DE CLES NECESSAIRES
    final int MAXEPREUVESJUGEMENT = 2;
    final int MAXEPREUVESINDICES = 5;
    final int MAXEPREUVESCONSEIL = 3;

    // BOUCLE DU JEU
    void startGame(Game game) {
        // PARTIE DES CLES
        myClearScreen();
        println("Epreuves des clés !");
        println("Ton but est de " + ANSI_RED + "ramasser un maximum de clés !" + ANSI_RESET);
        println(ANSI_RED + "ATTENTION si tous les joueurs sont en prison, la partie s'arrête !" + ANSI_RESET);
        pressEnterToContinue();
        int epreuveIndex = 0;
        boolean success;
        while(!allPlayersInJail(game.team) && epreuveIndex < MAXEPREUVESKEY) {
            success = startEpreuve(game, game.epreuves[epreuveIndex]);
            updateKeys(game, game.epreuves[epreuveIndex], success);
            epreuveIndex++;
        }

        // SI LE JUGEMENT NE PEUT PAS RATTRAPPER LES CLES MANQUANTES, LA PARTIE S'ARRETE 
        if(game.nbKeys + MAXEPREUVESJUGEMENT < MAXEPREUVESKEY) {
            myClearScreen();
            println(ANSI_RED + "Vous n'avez pas réussi à réunir assez de clés pour continuer la partie !\nTu réussieras la prochaine fois !" + ANSI_RESET);
            // ARRET DE LA PARTIE
            return;
        }
        // ON REGARDE SI IL Y A BESOIN D'UN JUGEMENT
        else if(needJugement(game)) {
            // TO DO BOUCLE EPREUVES JUGEMENTS
        }
        /*
            TODO
            - LA BOUCLE DE JEU -- EN COURS
            - ASSIGNER UN JOUEUR A L'EPREUVE (TIRER ALEATOIREMENT PARMIS LES JOUEURS QUI NE SONT PAS EN PRISON) -- FAIT
            - L'EPREUVE RETOURNE TRUE SI LE JOUEUR A REUSSI SINON FALSE ET L'ENVOIE EN PRISON -- FAIT
        */
    }

    boolean needJugement(Game game) {
        return game.nbKeys < MAXEPREUVESKEY || nbPlayersInJail(game.team) > 0;
    }

    // UPDATE LE NOMBRE DE CLES ET LE JOUEUR DE L'EPREUVE
    // UTILISER DANS LES PHASES KEYS ET JUGEMENT
    void updateKeys(Game game, Epreuve epreuve, boolean success) {
        if(success) {
            game.nbKeys = game.nbKeys + 1;
            println("Félicitation jeune padawan, tu as gagné une clef pour ton équipe !\nVous avez maintenant " + game.nbKeys + "/4 clefs !");
        }
        else {
            println("C'est perdu.. Quel dommage ahah !\nNe perdez pas le rythme ! Il vous faut encore " + (MAXEPREUVESKEY-game.nbKeys) + " clefs");
            if(epreuve.gameState == GameState.KEYS) {
                println("Le joueur " + ANSI_YELLOW + epreuve.player.pseudo + ANSI_RESET + " est envoyer en prsion !");
                epreuve.player.jail = true;
            }
        }
        delay(5000);
    }

    // RENVOIE LE NOMBRE DE PERSONNES EN PRISON
    int nbPlayersInJail(Team team) {
        int somme = 0;
        for(int i=0; i<length(team.players); i++) {
            if(team.players[i].jail) somme = somme + 1;
        }
        return somme;
    }

    // RENVOIE SI TOUT LES JOUEURS SONT EN PRISONS
    boolean allPlayersInJail(Team team) {
        boolean all = true;
        int i = 0;
        while(all && i<length(team.players)) {
            if(!team.players[i].jail) all = false;
            i++;
        }
        return all;
    }

    // DEMARRE UNE EPREUVE
    // ON NE PEUT PAS FAIRE AUTREMENT POUR L'INSTANT
    boolean startEpreuve(Game game, Epreuve epreuve) {
        boolean res = false;
        initEpreuve(game, epreuve);
        if(epreuve.id == 0) {
            res = startQuiz(epreuve);
        }
        else if(epreuve.id == 1) {
            res = startPipeGame(epreuve);
        }
        else if(epreuve.id == 2) {
            res = startSoundGame(epreuve);
        }
        else if(epreuve.id == 3) {
            res = startFakir(epreuve);
        }
        else if(epreuve.id == 4) {
            res = startPileOuFace(epreuve);
        }
        else if(epreuve.id == 5) {
            res = startShiFuMi(epreuve);
        }
        else if(epreuve.id == 6) {
            res = startMathematix(epreuve);
        }
        else if(epreuve.id == 7) {
            res = startMemoGame(epreuve);
        }
        else if(epreuve.id == 8) {
            res = startRoulette(epreuve);
        }
        else if(epreuve.id == 9) {
            res = startMastermind(epreuve);
        }
        else if(epreuve.id == 10) {
            res = startBouyardCard(epreuve);
        }
        return res;
    }

    // INITIALISE LE JOUEUR A L'EPREUVE EN L'ASSIGNANT A L'EPREUVE ET EN LUI EXPLIQUANT L'EPREUVE
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

    // TIRE ALEATOIREMENT UN JOUEUR QUI N'EST PAS EN PRISON
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