import extensions.*;

class Main extends GameManager {

    final String[] mainMenu = new String[]{"Jouer", "Leaderboard", "Règles", "Crédits", "Activer la musique", "Quitter"};
    final String WORDSCSV = "words.csv";
    
    final String[] DEPENDENCIES = new String[]{WORDSCSV, CHARADECSV, SOUNDGAMECSV, ENGLISHGAMECSV};
    
    void algorithm() {
        myClearScreen();
        enableKeyTypedInConsole(false);

        // NE LANCE PAS LE JEU SI IL MANQUE DES DEPENDENCIES
        /* @TODO : Check les sounds */
        if(!checkDependencies()){
            return;
        }

        // CREER CSV LEADERBOARD SI IL NEXISTE PAS
        if(!csvFileExist(LEADERBOARDCSV)){
            createLeaderboardFile();
        }
        
        displayIntroGame();
        myClearScreen();
        // MENU CHOIX
        boolean play = true;
        int choice;
        while(play){
            choice = choiceMenuOption()-1;
            if(debug){
                String uChoice = choice == 6 ? "Test a minigame" :  mainMenu[choice];
                debug("User choiced : " + uChoice);
                delay(500);
            }
            if(choice == 0) {
                Team team = registerTeam();
                Game g = newGame(team);
                startGame(g);
            } else if(choice == 1) {
                displayLeaderboard();
            } else if(choice == 2) {
                displayRules();
            } else if(choice == 3) {
                displayCredits();
            } else if(choice == 4) {
                myClearScreen();
                if(!musicRunning){
                    musicTimer = newTimer(220);
                    musicRunning = true;
                    playSound(SOUND_THEME, true);
                    printTxt("../ressources/music.txt");
                } else {
                    info("La musique est déjà en cours..");
                }
                delay(1000);
            } else if (debug && choice == 6) {
                myClearScreen();
                Team team = registerTeam();
                Game g = newGame(team);
                for(int i = 0; i < length(g.epreuves); i++) {
                    println(i + " " + g.epreuves[i].name);
                }
                info("Entrez l'id du jeu à tester : ");
                int debugGameId = enterNumber();
                startEpreuve(g, g.epreuves[debugGameId]);
            } else {
                play = false;
            }
        }
        myClearScreen();
        println("A bientôt !");
        delay(1000);
    }

    // Display 
    void displayIntroGame() {
        println(ANSI_CURSOR_HIDE);
        for(int i = 0; i <= 10; i++){
            myClearScreen();
            print(readTxt("../ressources/welcome/welcome"+i+".txt"));
            delay(250);
        }
        println(ANSI_CURSOR_SHOW);
        if(customPressEnterToContinue()){
            debug = true;
        }
    }

    // créer le leaderboard
    void createLeaderboardFile(){
        debug("LeaderBoard file doesn't exist ! Creating it...");
        delay(1000);
        saveCSV(new String[][]{{"Teamname", "Cri", "Score", "Temps"}}, LEADERBOARDCSV);
    }

    // afficher le leaderboard
    void displayLeaderboard() {
        myClearScreen();
        CSVFile csv = myLoadCSV(LEADERBOARDCSV, ';');
        printLeaderboard(csv, 10);
        pressEnterToContinue();
    }

    // afficher les règles
    void displayRules(){
        myClearScreen();
        printTxt("../ressources/rules.txt");
        pressEnterToContinue();
    }

    // afficher les crédits
    void displayCredits() {
        myClearScreen();
        printTxt("../ressources/credits.txt");
        pressEnterToContinue();
    }

    // afficher le menu
    void printMenu() {
        myClearScreen();
        for(int i = 0; i < length(mainMenu); i++){
            println((i+1) + ". " + mainMenu[i]);
        }
        if(debug){
            println("7. Test a minigame");
        }
    }

    // récupère le choix du joueur
    int choiceMenuOption() {
        int choice;
        int lenoptions = debug ? length(mainMenu) + 1 : length(mainMenu);
        printMenu();
        println("Entrez votre choix : ");
        do {
            choice = enterNumber();
        } while(!isBetween(choice, 1, lenoptions));
        return choice;
    }

    // afficher le leaderboard
    void printLeaderboard(CSVFile csv, int rowCount) {
        println("Classement - Nom - Cri - Score - Temps");
        int maxRows = rowCount <= rowCount(csv)-1 ? rowCount : rowCount(csv)-1;
        for(int row = 1; row <= maxRows; row++) {
            print(row);
            for(int col = 0; col<columnCount(csv); col++) {
                print(" - " + getCell(csv, row, col));
            }
            print("\n");
        }
        println("");
    }
    
    // afficher un tableau de String
    void println(String[] s){
        for(int i = 0; i < length(s); i++){
            println(s[i]);
        }
    }

    // Constructeur Player
    Player newPlayer(String pseudo) {
        Player player = new Player();
        player.pseudo = pseudo;
        player.jail = false;
        return player;
    }

    // Register Team
    Team registerTeam(){
        /* Init variables */
        String teamName;
        String teamCry;
        int playersNumber;
        String[] playersName;

        /* Ask player informations */
        if(!debug){
            myClearScreen();
            println("Entrez le nom de votre team :");
            teamName = readString();
            myClearScreen();
            println("Entrez votre cri de guerre :");
            teamCry = readString();
            myClearScreen();
            println("Combien de joueurs comporte votre équipe ? (minimum 2)");
            do {
                playersNumber = enterNumber();
            } while(playersNumber < 2);

            playersName = new String[playersNumber];
            for(int i = 0; i < length(playersName); i++){
                myClearScreen();
                println("Entrez le nom du joueur n°" + (i+1));
                playersName[i] = readString();
            }
        } else {
            teamName = "DEBUG";
            teamCry = "DEBUG";
            playersNumber = 2;
            playersName = new String[]{"Debug1", "Debug2"};
        }
        return newTeam(teamName, teamCry, playersNumber, playersName);
    }

    // Team constructor
    Team newTeam(String teamName, String teamCry, int playersNumber, String[] playersName) {
        Team team = new Team();
        team.name = teamName;
        team.cry = teamCry;
        team.players = new Player[playersNumber];
        for(int i = 0; i < length(team.players); i++){
            team.players[i] = newPlayer(playersName[i]);
        }
        return team;
    }

    // Indice constructor
    Indice newIndice(String indice) {
        Indice i = new Indice();
        i.indice = indice;
        i.found = false;
        return i;
    }

    // Game constructor
    Game newGame(Team team) {
        Game game = new Game();
        game.team = team;
        game.nbKeys = 0;
        game.timer = newTimer(1800);
        game.timerTresor = 120;
        game.jugementDone = false;
        generateCode(game);
        giveFreeIndice(game);
        game.gameState = GameState.KEYS;
        initStats(game);
        initEpreuves(game);
        return game;
    }

    // donne des indices gratuits en fonction du nombre d'épreuves d'indices
    void giveFreeIndice(Game game) {
        for(int i=0; i<(6-MAXEPREUVESINDICES); i++) {
            setIndiceFind(game.indices[i]);
        }
    }

    // initialise les stats de la partie (pour le calcul du score final)
    void initStats(Game g){
        int nbStats = 5;
        // 0: NB_JAIL_TOTAL 1: NB_GOOD_ANSWERS 2: NB_BAD_ANSWERS 3: FINAL_TIME_SECONDES 5: CONSEIL_TIME_WON_SECONDES
        g.stats = new int[nbStats];
        for(int i = 0; i < length(g.stats); i++){
            g.stats[i] = 0;
        }
    }

    // génére le mot code et les indices d'une partie
    void generateCode(Game game){
        CSVFile words = myLoadCSV(WORDSCSV);
        int line = randInt(1, rowCount(words)-1);
        game.motCode = getCell(words, line, 0);
        game.indices = new Indice[6];
        for(int i = 1; i < columnCount(words); i++){
            game.indices[i-1] = newIndice(getCell(words, line, i));
        }
    }

    // vérifie les dépendances
    boolean checkDependencies(){
        boolean res = true;
        String file;
        for(int i = 0; i < length(DEPENDENCIES); i++){
            file = DEPENDENCIES[i];
            if(!csvFileExist(file)){
                csvMissingError(file);
                res = false;
            }
        }
        if(!res)
            info("Votre jeu est corrompu, veuillez le télécharger à nouveau.");
        return res;
    }

    // Selectionne les épreuves de la game aléatoirement
    void initEpreuves(Game game) {
        Epreuve[] generals = new Epreuve[]{initGeographyGame(), initEnglishGame(), initQuiz(), initPipeGame(), initSoundGame(), initMathematix(), initPipeGame()};
        Epreuve[] jugements = new Epreuve[]{initFakir(), initPileOuFace(), initRoulette(), initMastermind()};
        Epreuve[] conseils = new Epreuve[]{initBouyardCard(), initShiFuMi(), initMemoGame()};
        shuffleEpreuves(generals);
        shuffleEpreuves(jugements);
        shuffleEpreuves(conseils);
        game.epreuves = new Epreuve[MAXEPREUVESKEY + MAXEPREUVESJUGEMENT + MAXEPREUVESINDICES + MAXEPREUVESCONSEIL];
        int indexEpreuve = 0;
        for(int i = 0; i < MAXEPREUVESKEY; i++) {
            game.epreuves[indexEpreuve] = generals[i];
            indexEpreuve++;
        }
        for(int i = 0; i < MAXEPREUVESJUGEMENT; i++) {
            game.epreuves[indexEpreuve] = jugements[i];
            indexEpreuve++;
        }
        for(int i = MAXEPREUVESKEY; i<(MAXEPREUVESINDICES+MAXEPREUVESKEY); i++) {
            game.epreuves[indexEpreuve] = generals[i];
            game.epreuves[indexEpreuve].gameState = GameState.INDICES;
            indexEpreuve++;
        }
        for(int i = 0; i < MAXEPREUVESCONSEIL; i++) {
            game.epreuves[indexEpreuve] = conseils[i];
            indexEpreuve++;
        }
    }

    // Melange un tableau d'épreuves
    void shuffleEpreuves(Epreuve[] epreuves) {
        int r;
        Epreuve tmp;
        for(int j=0; j<10; j++) {
            for(int i=0; i<length(epreuves); i++) {
                r = randInt(0, length(epreuves)-1);
                tmp = epreuves[i];
                epreuves[i] = epreuves[r];
                epreuves[r] = tmp;
            }
        }
    }
}