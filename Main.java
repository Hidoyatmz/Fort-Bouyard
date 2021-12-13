import extensions.*;

class Main extends Utils {

    final String[] mainMenu = new String[]{"Jouer", "Leaderboard", "Règles", "Crédits", "Quitter"};

    void algorithm() {
        myClearScreen();

        // CREER CSV LEADERBOARD SI IL NEXISTE PAS
        if(!csvFileExist("leaderboard.csv")){
            debug("LeaderBoard file doesn't exist ! Creating...");
            delay(1000);
            saveCSV(new String[][]{{"Teamname", "score"}}, "leaderboard.csv");
        }
        // TODO

        // MENU CHOIX
        boolean play = true;
        int choice;
        while(play){
            choice = choiceMenuOption()-1;
            debug("User choiced : " + mainMenu[choice]);
            if(choice == 0) {
                // TODO LANCEMENT DU JEU
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

    void displayLeaderboard() {
        myClearScreen();
        CSVFile csv = loadCSV("leaderboard.csv");
        printLeaderboard(csv, 10);
        info("\nAppuyez sur entrée pour continuer.");
        readString();
    }

    void printMenu() {
        for(int i = 0; i < length(mainMenu); i++){
            println((i+1) + ". " + mainMenu[i]);
        }
    }

    int choiceMenuOption() {
        String choice;
        do {
            myClearScreen();
            printMenu();
            println("Entrez votre choix : ");
            choice = readString();
        } while(!(length(choice) > 0) || !isDigit(charAt(choice, 0)) || !isBetween(stringToInt(choice), 1, length(mainMenu)));
        return stringToInt(choice);
    }

    void printLeaderboard(CSVFile csv, int rowCount) {
        println("Rank - Name - Score");
        int maxRows = rowCount <= rowCount(csv)-1 ? rowCount : rowCount(csv)-1;
        for(int row = 1; row <= maxRows; row++) {
            println(row + " - " + getCell(csv, row, 0) + " - " + getCell(csv, row, 1));
        }
    }

    Game newGame() {

    }

    Player newPlayer(String pseudo) {
        Player player = new Player();
        player.pseudo = pseudo;
        player.jail = false;
        return player;
    }

}