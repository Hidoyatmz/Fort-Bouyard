import extensions.*;

class Main extends GameManager {

    final String[] mainMenu = new String[]{"Jouer", "Leaderboard", "Règles", "Crédits", "Quitter"};
    final String WORDSCSV = "words.csv";
    final String LEADERBOARDCSV = "leaderboard.csv";
    final String[] DEPENDENCIES = new String[]{WORDSCSV, CHARADECSV, SOUNDGAMECSV};
    final int MAXEPREUVESKEY = 4;
    final int MAXEPREUVESJUGEMENT = 2;
    final int MAXEPREUVESINDICES = 5;
    final int MAXEPREUVESCONSEIL = 3;

    void algorithm() {
        myClearScreen();

        // NE LANCE PAS LE JEU SI IL MANQUE DES DEPENDENCIES
        /* @TODO : Check les sounds */
        if(!checkDependencies()){
            return;
        }

        // CREER CSV LEADERBOARD SI IL NEXISTE PAS
        if(!csvFileExist(LEADERBOARDCSV)){
            createLeaderboardFile();
        }

        // MENU CHOIX
        boolean play = true;
        int choice;
        while(play){
            choice = choiceMenuOption()-1;
            debug("User choiced : " + mainMenu[choice]);
            if(choice == 0) {
                // TODO LANCEMENT DU JEU
                Team team = registerTeam();
                Game g = newGame(team);
                startGame(g);
            } else if(choice == 1) {
                displayLeaderboard();
            } else if(choice == 2) {
                displayRules();
            } else if(choice == 3) {
                displayCredits();
            } else {
                play = false;
            }
        }
        myClearScreen();
        println("A bientôt !");
        delay(1000);
    }

    void createLeaderboardFile(){
        debug("LeaderBoard file doesn't exist ! Creating it...");
        delay(1000);
        saveCSV(new String[][]{{"Teamname", "score"}}, LEADERBOARDCSV);
    }

    void displayLeaderboard() {
        myClearScreen();
        CSVFile csv = loadCSV(LEADERBOARDCSV);
        printLeaderboard(csv, 10);
        pressEnterToContinue();
    }

    void displayRules(){
        myClearScreen();
        printTxt("Regles.txt");
        pressEnterToContinue();
    }

    void displayCredits() {
        myClearScreen();
        printTxt("Credits.txt");
        pressEnterToContinue();
    }

    void printMenu() {
        myClearScreen();
        for(int i = 0; i < length(mainMenu); i++){
            println((i+1) + ". " + mainMenu[i]);
        }
    }

    int choiceMenuOption() {
        int choice;
        printMenu();
        println("Entrez votre choix : ");
        do {
            choice = enterNumber();
        } while(!isBetween(choice, 1, length(mainMenu)));
        return choice;
    }

    void printLeaderboard(CSVFile csv, int rowCount) {
        println("Rank - Name - Score");
        int maxRows = rowCount <= rowCount(csv)-1 ? rowCount : rowCount(csv)-1;
        for(int row = 1; row <= maxRows; row++) {
            println(row + " - " + getCell(csv, row, 0) + " - " + getCell(csv, row, 1));
        }
        println("");
    }
    
    void println(String[] s){
        for(int i = 0; i < length(s); i++){
            println(s[i]);
        }
    }

    Player newPlayer(String pseudo) {
        Player player = new Player();
        player.pseudo = pseudo;
        player.jail = false;
        return player;
    }

    Team registerTeam(){
        /* Init variables */
        String teamName;
        String teamCry;
        int playersNumber;
        String[] playersName;

        /* Ask player informations */
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

        return newTeam(teamName, teamCry, playersNumber, playersName);
    }

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

    Indice newIndice(String indice) {
        Indice i = new Indice();
        i.indice = indice;
        i.found = false;
        return i;
    }

    Game newGame(Team team) {
        Game game = new Game();
        game.team = team;
        game.nbKeys = 0;
        generateCode(game);
        setIndiceFind(game.indices[0]);
        game.gameState = GameState.KEYS;
        initEpreuves(game);
        return game;
    }

    void generateCode(Game game){
        CSVFile words = loadCSV(WORDSCSV);
        int line = randInt(1, rowCount(words)-1);
        game.motCode = getCell(words, line, 0);
        game.indices = new Indice[6];
        for(int i = 1; i < columnCount(words); i++){
            game.indices[i-1] = newIndice(getCell(words, line, i));
        }
    }

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
        info("Votre jeu est corrompu, veuillez le télécharger à nouveau.");
        return res;
    }

    // TODO CREATION ET SELECTION EPREUVES

    void initEpreuves(Game game) {
        Epreuve[] generals = new Epreuve[]{initQuiz(), initSoundGame()};
        Epreuve[] jugements = new Epreuve[]{initPileOuFace(), initFakir(), initShiFuMi()};
        Epreuve[] conseils = new Epreuve[1];
        game.epreuves = new Epreuve[MAXEPREUVESKEY + MAXEPREUVESJUGEMENT + MAXEPREUVESINDICES + MAXEPREUVESCONSEIL];
        /*int i = 0;
        randomEpreuves(game, i, generals, MAXEPREUVESKEY);
        i = i + MAXEPREUVESKEY;
        randomEpreuves(game, i, jugements, MAXEPREUVESJUGEMENT);
        i = i + MAXEPREUVESJUGEMENT;
        randomEpreuves(game, i, generals, MAXEPREUVESINDICES);
        i = i + MAXEPREUVESINDICES;
        randomEpreuves(game, i, conseils, MAXEPREUVESCONSEIL);*/
        game.epreuves[0] = generals[0];
        game.epreuves[1] = generals[1];
        game.epreuves[2] = jugements[0];
        game.epreuves[3] = jugements[1];
        game.epreuves[4] = jugements[2];
    }
    
    // A TESTER QUAND IL Y AURA ASSEZ DEPREUVES
    void randomEpreuves(Game game, int startIndex, Epreuve[] epreuves, int maxEpreuves) {
        int r;
        Epreuve tmp;
        int lastIndex = length(epreuves)-1;
        for(int i=0; i<maxEpreuves; i++) {
            r = (int) (random()*(lastIndex-i));
            tmp = epreuves[r];
            epreuves[r] = epreuves[lastIndex-i];
            epreuves[lastIndex-i] = tmp;
        }
        for(int i=startIndex; i<maxEpreuves; i++) {
            game.epreuves[i] = epreuves[lastIndex-i];
        }
    }
}