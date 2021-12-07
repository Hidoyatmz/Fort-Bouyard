import extensions.*;

class Main extends Program {
    final String[] mainMenu = new String[]{"Jouer", "Leaderboard", "Règles", "Crédits", "Quitter"};
    Utils utils;

    void algorithm() {
        utils = new Utils();
        // MENU CHOIX
        choiceMenuOption();
        // TODO
    }

    void printMenu() {
        for(int i = 0; i < length(mainMenu); i++){
            println((i+1) + ". " + mainMenu[i]);
        }
    }

    int choiceMenuOption() {
        char choice;
        do {
            clearScreen();
            printMenu();
            println("Entrez votre choix : ");
            choice = readChar();
        } while(!utils.isDigit(choice) || !utils.isBetween(charToInt(choice), 1, length(mainMenu)));
        println("[DEBUG] User choiced : " + mainMenu[charToInt(choice)-1]);
        return choice;
    }

    /*int choiceMenuOption() {
        String choice;
        do {
            clearScreen();
            printMenu();
            println("Entrez votre choix : ");
            choice = readString();
            println("t" + choice + "t");
        } while(!utils.isDigit(choice) || !utils.isBetween(stringToInt(choice), 1, length(mainMenu)));
        println("[DEBUG] User choiced : " + mainMenu[stringToInt(choice)-1]);
        return stringToInt(choice);
    }*/

}