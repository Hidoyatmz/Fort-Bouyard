class GameManager extends Tresor {

    final int MAXEPREUVESKEY = 3; // AUSSI LE NOMBRE DE CLES NECESSAIRES // -1
    final int MAXEPREUVESJUGEMENT = 2; // -1
    final int MAXEPREUVESINDICES = 4;
    final int MAXEPREUVESCONSEIL = 3; // -1
    boolean musicRunning = false;
    Timer musicTimer;

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
            game.jugementDone = true;
            // TO DO BOUCLE EPREUVES JUGEMENTS
            for(int i=epreuveIndex; i<MAXEPREUVESJUGEMENT; i++) {
                success = startEpreuve(game, game.epreuves[epreuveIndex]);
                epreuveIndex++;
            }
        }

        
        /*
            TODO
            - LA BOUCLE DE JEU -- EN COURS
            - ASSIGNER UN JOUEUR A L'EPREUVE (TIRER ALEATOIREMENT PARMIS LES JOUEURS QUI NE SONT PAS EN PRISON) -- FAIT
            - L'EPREUVE RETOURNE TRUE SI LE JOUEUR A REUSSI SINON FALSE ET L'ENVOIE EN PRISON -- FAIT
        */
        startTresor(game);
        setFinalTime(game);
    }

    boolean needJugement(Game game) {
        return game.nbKeys < MAXEPREUVESKEY || nbPlayersInJail(game.team) > 0;
    }

    // UPDATE LE NOMBRE DE CLES ET LE JOUEUR DE L'EPREUVE
    // UTILISER DANS LES PHASES KEYS ET JUGEMENT
    void updateKeys(Game game, Epreuve epreuve, boolean success) {
        myClearScreen();
        if(success) {
            game.nbKeys = game.nbKeys + 1;
            playSound(SOUND_CORRECT_ANSWER, true);
            println(ANSI_GREEN + "Félicitation jeune padawan, tu as gagné une clef pour ton équipe !\nVous avez maintenant " + game.nbKeys + "/4 clefs !" + ANSI_RESET);
        }
        else {
            println(ANSI_RED + "C'est perdu.. Quel dommage ahah !\nNe perdez pas le rythme ! Il vous faut encore " + (MAXEPREUVESKEY-game.nbKeys) + " clefs" + ANSI_RESET);
            playSound(SOUND_WRONG_ANSWER, true);
            if(epreuve.gameState == GameState.KEYS) {
                println("Le joueur " + ANSI_YELLOW + epreuve.player.pseudo + ANSI_RESET + " est envoyé en prison !");
                increaseNbJails(game);
                epreuve.player.jail = true;
            }
        }
        pressEnterToContinue();
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
            res = startQuiz(epreuve, game); // GENERAL
        }
        else if(epreuve.id == 1) {
            res = startPipeGame(epreuve); // GENERAL
        }
        else if(epreuve.id == 2) {
            res = startSoundGame(epreuve, game); // GENERAL
        }
        else if(epreuve.id == 3) {
            res = startFakir(epreuve); // DEFI
        }
        else if(epreuve.id == 4) {
            res = startPileOuFace(epreuve); // DEFI
        }
        else if(epreuve.id == 5) {
            res = startShiFuMi(epreuve); // CONSEIL
        }
        else if(epreuve.id == 6) {
            res = startMathematix(epreuve, game); // GENERAL
        }
        else if(epreuve.id == 7) {
            res = startMemoGame(epreuve); // CONSEIL
        }
        else if(epreuve.id == 8) {
            res = startRoulette(game, epreuve); // DEFI
        }
        else if(epreuve.id == 9) {
            res = startMastermind(epreuve); // DEFI
        }
        else if(epreuve.id == 10) {
            res = startBouyardCard(epreuve); // CONSEIL
        }
        else if(epreuve.id == 11){
            res = startEnglishGame(epreuve, game); // GENERAL
        }
        else if(epreuve.id == 12) {
            res = startGeographyGame(epreuve, game); // GENERAL
        }
        return res;
    }

    // INITIALISE LE JOUEUR A L'EPREUVE EN L'ASSIGNANT A L'EPREUVE ET EN LUI EXPLIQUANT L'EPREUVE
    void initEpreuve(Game game, Epreuve epreuve) {
        myClearScreen();
        epreuve.player = haulPlayer(game.team);
        long elapsedTime = getElapsedTime(game.timer);
        checkIfResetMusicBool(elapsedTime);
        println("Vous avez passez : " + ANSI_CYAN + formatTime(elapsedTime) + ANSI_RESET + " en jeu.");
        println("La prochaine épreuve sera : " + ANSI_YELLOW + epreuve.name + ANSI_RESET + " !");
        println("C'est à " + ANSI_YELLOW + epreuve.player.pseudo + ANSI_RESET + " de jouer !\n");
        pressEnterToContinue();
        myClearScreen();
        println("Voici " + ANSI_PURPLE + "les règles " + ANSI_RESET + "de l'épreuve " + ANSI_YELLOW + epreuve.name + ANSI_RESET);
        println("");
        println(epreuve.rules + "\n");
        pressEnterToContinue();
    }

    void checkIfResetMusicBool(long elapsedTime) {
        if(musicRunning && !inTime(musicTimer)){
            playSound(SOUND_THEME, true);
            musicTimer = newTimer(220);
        }
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