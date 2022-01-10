import extensions.*;

class GameManager extends Tresor {

    final String LEADERBOARDCSV = "leaderboard.csv";

    final int MAXEPREUVESKEY = 3; // AUSSI LE NOMBRE DE CLES NECESSAIRES
    final int MAXEPREUVESJUGEMENT = 2;
    final int MAXEPREUVESINDICES = 4;
    final int MAXEPREUVESCONSEIL = 2;
    boolean musicRunning = false;
    Timer musicTimer;

    int epreuveIndex;

    // BOUCLE DU JEU
    void startGame(Game game) {
        epreuveIndex = 0;

        // PARTIE DES CLES
        printInfo(game);
        setGameState(game, GameState.KEYS);
        startKeysStage(game);

        // SI LE JUGEMENT NE PEUT PAS RATTRAPPER LES CLES MANQUANTES, LA PARTIE S'ARRETE
        if((game.nbKeys + MAXEPREUVESJUGEMENT) < MAXEPREUVESKEY || allPlayersInJail(game.team)) {
            stopGame();
        }
        // ON REGARDE SI IL Y A BESOIN D'UN JUGEMENT
        else if(needJugement(game)) {
            game.jugementDone = true;
            setGameState(game, GameState.JUGEMENT);
            printInfo(game);
            startJugementStage(game);
        }
        // Si le jugement n'a pas permis de récupérer les clés manquantes
        if(needJugement(game)){
            stopGame();
        }

        // Partie indices
        epreuveIndex = MAXEPREUVESKEY + MAXEPREUVESJUGEMENT;
        setGameState(game, GameState.INDICES);
        printInfo(game);
        startIndicesStage(game);

        // Partie Conseil
        setGameState(game, GameState.CONSEIL);
        printInfo(game);
        startConseilStage(game);

        // Tresor
        setGameState(game, GameState.TRESOR);
        printInfo(game);
        int pieces = startTresor(game);
        setFinalTime(game);

        // CALCULER SCORE
        double mult_prison = 1.5 - 0.25 * getNbJails(game) > 0 ? 1.5 - 0.25 * getNbJails(game) : 0;
        double mult_answers = 1+((getGoodAnswers(game) - getBadAnswers(game))) > 0 ? 1+((getGoodAnswers(game) - getBadAnswers(game))) : 0;
        double mult_jugement = game.jugementDone ? 1 : 1.5;
        double mult_conseil = 1+((getConseilTimeWon(game)/30)/10);
        int base = 10;
        int score = (int) ((base + pieces) * mult_prison * mult_answers * mult_jugement * mult_conseil);
        debug(""+score);
        saveInLeaderBoard(game, score);
        pressEnterToContinue();
        /* NB_PIECE x (((1.5-(0.25xNB_PRISON))>0) x ((1+(NB_BONNES_REPONSES - NB_FAUSSES_REPONSES))>0) 
        x (1+((1/TEMPS_MIS_MINUTES)x3))) x (1.5 SI NON JUGEMENTS) x (1+((TEMPS_GAGNE_CONSEIL_SECONDES/30)/10))*/
        // METTRE DANS LE LEADERBOARD
    }

    void startKeysStage(Game game) {
        boolean success;
        while(!allPlayersInJail(game.team) && epreuveIndex < MAXEPREUVESKEY) {
            success = startEpreuve(game, game.epreuves[epreuveIndex]);
            updateKeys(game, game.epreuves[epreuveIndex], success);
            epreuveIndex++;
        }
    }

    void startJugementStage(Game game) {
        boolean success;
        int iJugement = epreuveIndex;
        while(iJugement < MAXEPREUVESJUGEMENT+MAXEPREUVESKEY && needJugement(game)){
            success = startEpreuve(game, game.epreuves[epreuveIndex]);
            if(success) {
                game.epreuves[epreuveIndex].player.jail = false;
                game.nbKeys = game.nbKeys + 1;
                info(game.epreuves[epreuveIndex].player.pseudo + " a été libérer ! Vous gagnez donc une clé !");
                delay(3000);
            }
            epreuveIndex++;
            iJugement++;
        }
    }

    void startIndicesStage(Game game) {
        boolean success;
        for(int i = epreuveIndex; i < MAXEPREUVESJUGEMENT+MAXEPREUVESKEY+MAXEPREUVESINDICES; i++){
            success = startEpreuve(game, game.epreuves[epreuveIndex]);
            if(success){
                setNextIndiceFind(game);
                info("Vous avez gagné un indice ! Il sera révélé au début de la salle au Trésor.");
                delay(3000);
            }
            epreuveIndex++;
        }
    }

    void startConseilStage(Game game) {
        boolean success;
        for(int i = epreuveIndex; i < MAXEPREUVESJUGEMENT+MAXEPREUVESKEY+MAXEPREUVESINDICES+MAXEPREUVESCONSEIL; i++){
            success = startEpreuve(game, game.epreuves[epreuveIndex]);
            if(success){
                game.timerTresor += 10;
                info("Vous avez gagné 10 secondes dans la salle au Trésor !");
                delay(3000);
            }
            epreuveIndex++;
        }
    }

    // ARRET DE LA PARTIE
    void stopGame() {
        myClearScreen();
        println(ANSI_RED + "Vous n'avez pas réussi à réunir assez de clés pour continuer la partie !\nTu réussieras la prochaine fois !" + ANSI_RESET);
        pressEnterToContinue();
        return;
    }

    void setGameState(Game game, GameState gameState) {
        game.gameState = gameState;
    }

    void setNextIndiceFind(Game game) {
        int i=0;
        boolean found = false;
        while(i<length(game.indices) && !found) {
            if(!game.indices[i].found) {
                game.indices[i].found = true;
                found = true;
            }
            i++;
        }
    }

    boolean needJugement(Game game) {
        return game.nbKeys < MAXEPREUVESKEY || nbPlayersInJail(game.team) > 0;
    }

    void printInfo(Game game) {
        myClearScreen();
        GameState gameState = game.gameState;
        if(gameState == GameState.KEYS) {
            println("Epreuves des clés !");
            println("Ton but est de " + ANSI_LIGHT_GREEN + "ramasser un maximum de clés !" + ANSI_RESET);
            println("Pour se faire, remportez la victoire à une épreuve pour en récuperer sa clef.");
            println(ANSI_RED + "ATTENTION si tous les joueurs sont en prison, la partie s'arrête !" + ANSI_RESET);
            pressEnterToContinue();
        }
        else if(gameState == GameState.JUGEMENT) {
            println("Epreuves du jugement !");
            println("Si tu es ici, c'est que tu as échoué aux épreuves des clefs !");
            if(nbPlayersInJail(game.team) > 0){
                println(ANSI_PURPLE + nbPlayersInJail(game.team) + ANSI_RESET + " joueur(s) de votre équipe, est/sont en prison ! C'est donc à lui/eux de jouer en priorité(s).\n"+ ANSI_GREEN +"Si le joueur gagne le défi, alors il est libéré."+ANSI_RESET);
                String[] playersNameInJail = getPlayersNameInJail(game.team);

                // TODO : while
                for(int i = 0; i < length(playersNameInJail) && i < 3; i++){
                    println(playersNameInJail[i] + " prépares toi à jouer un défi !");
                }
            }
            info("De plus, chaque victoire à un défi vous rapporte une des clefs manquante.");
            pressEnterToContinue();
        }
        else if(gameState == GameState.INDICES) {
            println("Epreuves des indices !");
            println("Ton but est de " + ANSI_LIGHT_GREEN + "trouver un maximum d'indices!" + ANSI_RESET);
            println("Pour se faire, remportez la victoire à une épreuve pour afficher son indice.");
            pressEnterToContinue();
        }
        else if(gameState == GameState.CONSEIL) {
            println("Epreuves du conseil !");
            println("Vous êtes ici dans le but de " + ANSI_LIGHT_GREEN + " gagner du temps dans la salle du trésor" + ANSI_RESET);
            println("Pour se faire, remportez le duel contre le maître du conseil afin de gagner " + ANSI_LIGHT_PINK +"10 secondes" + ANSI_RESET + " par duel.");
            info("Une défaite ne vous fait pas perdre de temps.");
            pressEnterToContinue();
        }
        else if(gameState == GameState.TRESOR) {
            println("Et tout de suite ! L'épreuve FINALE ! La salle du trésor !!!");
            println("Trouvez le mot code à l'aide des indices puis ramassez un maximum de pièce afin d'augmenter considérablement votre score final.");
            info("Vous ne pouvez pas portez plus de 15 pièces à la fois dans ton sac ! Vous devez déposer vos pièces sur la case verte en bas à gauche pour les mettre dans le trésor.");
            pressEnterToContinue();
        }
    }

    // UPDATE LE NOMBRE DE CLES ET LE JOUEUR DE L'EPREUVE
    // UTILISER DANS LES PHASES KEYS ET JUGEMENT
    void updateKeys(Game game, Epreuve epreuve, boolean success) {
        myClearScreen();
        if(success) {
            game.nbKeys = game.nbKeys + 1;
            playSound(SOUND_CORRECT_ANSWER, true);
            println(ANSI_GREEN + "Félicitation jeune padawan, tu as gagné une clef pour ton équipe !\nVous avez maintenant " + game.nbKeys + "/" + MAXEPREUVESKEY + " clefs !" + ANSI_RESET);
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

    String[] getPlayersNameInJail(Team team){
        String[] res = new String[nbPlayersInJail(team)];
        int cpt = 0;
        for(int i=0; i<length(team.players); i++) {
            if(team.players[i].jail){
                res[cpt] = team.players[i].pseudo;
                cpt++;
            }
        }
        return res;
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
        // TODO : Améliorer ça
        if(game.gameState == GameState.JUGEMENT){
            epreuve.player = haulPrisonPlayer(game.team);
        }
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

    Player haulPrisonPlayer(Team team){
        boolean res = false;
        Player pRes = team.players[0];
        int i = 0;
        while(i < length(team.players) && !res){
            if(team.players[i].jail){
                pRes = team.players[i];
                res = true;
            }
            i++;
        }
        return pRes;
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
    
    void saveInLeaderBoard(Game game, int score) {
        String[][] cells = getLeaderBoardNewLine();
        cells[length(cells, 1)-1] = new String[]{game.team.name, game.team.cry, ""+score, formatTime(getElapsedTime(game.timer))};
        sortNewScore(cells);
        saveCSV(cells, "../ressources/csv/" + LEADERBOARDCSV, ';');
    }

    String[][] getLeaderBoardNewLine() {
        CSVFile csv = myLoadCSV(LEADERBOARDCSV, ';');
        String[][] res = new String[rowCount(csv)+1][columnCount(csv)];
        for(int i=0; i<rowCount(csv); i++) {
            for(int j=0; j<columnCount(csv); j++) {
                res[i][j] = getCell(csv, i, j);
            }
        }
        return res;
    }

    void sortNewScore(String[][] cells) {
        String[] temp = new String[length(cells, 2)];
        boolean placer = false;
        int i = length(cells, 1)-1;
        while(i >= 2 && !placer) {
            if(stringToInt(cells[i][2]) > stringToInt(cells[i-1][2])) {
                copyTab(temp, cells[i-1]);
                copyTab(cells[i-1], cells[i]);
                copyTab(cells[i], temp);
            }
            else {
                placer = true;
            }
            i--;
        }
    }

    void copyTab(String[] receive, String[] toCopy) {
        if(length(receive) == length(toCopy)) {
            for(int j=0; j<length(receive); j++) {
                receive[j] = toCopy[j];
            }
        }
    }
}