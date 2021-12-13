import extensions.*;

class Main extends GameManager {

    final String[] mainMenu = new String[]{"Jouer", "Leaderboard", "Règles", "Crédits", "Quitter"};
    final String WORDSCSV = "words.csv";
    final String LEADERBOARDCSV = "leaderboard.csv";

    void algorithm() {
        myClearScreen();

        if(!csvFileExist(WORDSCSV)){
            csvMissingError(WORDSCSV);
            return;
        }

        // CREER CSV LEADERBOARD SI IL NEXISTE PAS
        if(!csvFileExist(LEADERBOARDCSV)){
            createLeaderboardFile();
        }

        // MENU CHOIX
        boolean play = true;
        boolean startGame = true;
        int choice;
        while(play){
            choice = choiceMenuOption()-1;
            debug("User choiced : " + mainMenu[choice]);
            if(choice == 0) {
                // TODO LANCEMENT DU JEU
                Team team = newTeam();
                Game g = newGame(team);
                
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
        debug("LeaderBoard file doesn't exist ! Creating...");
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

    Player newPlayer(String pseudo) {
        Player player = new Player();
        player.pseudo = pseudo;
        player.jail = false;
        return player;
    }

    Team newTeam() {
        Team team = new Team();
        myClearScreen();
        println("Entrez le nom de votre team :");
        team.name = readString();
        myClearScreen();
        println("Entrez votre cri de guerre :");
        team.cri = readString();
        myClearScreen();
        int choice;
        println("Combien de joueurs comporte votre équipe ?");
        do {
            choice = enterNumber();
        } while(choice < 2);


        team.players = new Player[choice];

        for(int i=0; i<length(team.players); i++) {
            myClearScreen();
            println("Entrez le nom du joueur " + (i+1));
            team.players[i] = newPlayer(readString());
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
        return game;
    }
    
    void println(String[] s){
        for(int i = 0; i < length(s); i++){
            println(s[i]);
        }
    }

    void setIndiceFind(Indice indice){
        indice.found = true;
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

}