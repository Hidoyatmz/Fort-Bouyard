import extensions.*;

class Main extends Utils {

    final String[] mainMenu = new String[]{"Jouer", "Leaderboard", "Règles", "Crédits", "Quitter"};

    void algorithm() {
        // CREER CSV LEADERBOARD SI IL NEXISTE PAS
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
                // TODO LEADERBOARD
            } else if(choice == 2) {
                // TODO REGLES
                displayRules();
            } else if(choice == 3) {
                // TODO CREDITS
            } else {
                play = false;
            }
        }
        println("A bientôt !");
    }

    void displayRules(){
        myClearScreen();
        printTxt("Regles.txt");
        info("Appuyez sur entrée pour continuer.");
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

}