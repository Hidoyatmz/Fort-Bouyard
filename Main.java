import extensions.*;

class Main extends GameManager {

    final String[] mainMenu = new String[]{"Jouer", "Leaderboard", "Règles", "Crédits", "Quitter"};
    final String WORDSCSV = "words.csv";
    final String LEADERBOARDCSV = "leaderboard.csv";
    final String[] DEPENDENCIES = new String[]{WORDSCSV, CHARADECSV, SOUNDGAMECSV};

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
                startQuiz(g.epreuves[0], g);
                startSoundGame(g.epreuves[1], g);
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
        info("\nAppuyez sur entrée pour continuer.");
        readString();
    }

    void displayRules(){
        myClearScreen();
        printTxt("Regles.txt");
        info("Appuyez sur entrée pour continuer.");
        readString();
    }

    void displayCredits() {
        myClearScreen();
        printTxt("Credits.txt");
        info("Appuyez sur entrée pour continuer.");
        readString();
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
        int line = randInt(1, rowCount(words));
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
        Epreuve[] jugements = new Epreuves[1];
        Epreuve[] conseils = new Epreuve[1];
        game.epreuves = new Epreuve[4];
        game.epreuves[0] = initQuiz();
        game.epreuves[1] = initSoundGame();
    }
    

}