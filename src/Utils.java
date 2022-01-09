import extensions.*;

class Utils extends Program {
    /* ANSI Light colors */
    final String ANSI_LIGHT_RED = "\u001B[91m";
    final String ANSI_LIGHT_GREEN = "\u001B[92m";
    final String ANSI_LIGHT_YELLOW = "\u001B[93m";
    final String ANSI_LIGHT_BLUE = "\u001B[94m";
    final String ANSI_LIGHT_PINK = "\u001B[95m";
    final String ANSI_LIGHT_CYAN = "\u001B[96m";
    final String ANSI_BLINK = "\u001B[05m";

    /* SOME SOUNDS */
    final String SOUND_CORRECT_ANSWER = "../ressources/sounds/ambiance/correct-sound.mp3";
    final String SOUND_CORRECT_ANSWER_2 = "../ressources/sounds/ambiance/correct-sound-2.mp3";
    final String SOUND_WRONG_ANSWER = "../ressources/sounds/ambiance/wrong-sound.mp3";
    final String SOUND_THEME = "../ressources/sounds/ambiance/theme.mp3";

    /* Enter "debugme" on the first screen to activate debug mode */
    boolean debug = false;

    void testIsBetween() {
        assertTrue(isBetween(5, 1, 10));
        assertFalse(isBetween(15, 1, 10));
    }
    
    /**
    * Returns if the integerer is in the interval, min and max values are include.
    *
    * @param entry integer to test
    * @param min minimum value of entry to be in the interval
    * @param max maximum value of entry to be in the interval
    * @return true if value is inside interval and false otherwise.
    */

    boolean isBetween(int entry, int min, int max) {
        return ((entry >= min) && (entry <= max));
    }

    void testIsDigit(){
        assertTrue(isDigit('9'));
        assertFalse(isDigit('a'));
    }

    /**
    * Returns if the letter is a digit or not. 
    *
    * @param letter the char to test
    * @return true if the param is a digit, false otherwise.
    */

    boolean isDigit(char letter){
        return ((letter >= '0') && (letter <= '9'));
    }

    void testRandInt(){
        assertEquals(true, isBetween(randInt(1,100),1,100));
        assertEquals(false, isBetween(randInt(101,150),1,100));
    }

    /**
    * Returns a random integer in the interval of the two params.Boundarys are includes.
    *
    * @param min minimum value possible
    * @param max maximum value possible
    * @return the random integer.
    */

    int randInt(int min, int max){
        return (int) (random()*((max+1)-min)+min);
    }

    /**
    * Display the param formated with debug prefix
    *
    * @param s string to display
    */

    void debug(String s){
        println(ANSI_GREEN + "[Fort Bouyard 1.0 DEBUGGER] : " + ANSI_RESET + s);
    }

    /**
    * Display the param formated in cyan color.
    *
    * @param s string to display
    */

    void info(String s){
        println(ANSI_CYAN + s + ANSI_RESET);
    }

    /**
    * Display the param formated in red color.
    *
    * @param s string to display
    */

    void erreur(String s){
        println(ANSI_RED + s + ANSI_RESET);
    }

    /**
    * Returns a String with all the content of a txt file after opening it.
    *
    * @param path   relative path to the txt file
    * @return       the string with all the content including breaklines.
    * */

    String readTxt(String path) {
        File txt = newFile(path);
        String res = "";

        while(ready(txt)) {
            res += readLine(txt) + "\n";
        }

        return res;
    }

    /**
    * Display a txt file in the terminal.
    *
    * @param path   relative path to the txt file
    */

    void printTxt(String path) {
        println(readTxt(path));
    }

    /**
    * Clear the screen and place cursor on the top of the terminal.
    */
    void myClearScreen(){
        /*if(!debug){*/
            clearScreen();
            cursor(0,0);
        /*}*/
    }

    /**
    * Used to display message when a CSV File is missing
    * 
    * @param filename   Filename used to print.
    */
    void csvMissingError(String filename){
        println(ANSI_RED + "Couldnt start the game.. (" + filename + " is missing)" + ANSI_RESET);
    }

    /**
    * Returns if a csv file exist by checking it with filename.
    *
    * @param filename   filename of the csvfile without .csv extension.
    * @return           true if the file has been found, false otherwise. 
    */

    boolean csvFileExist(String filename){
        String[] files = getAllFilesFromDirectory("../ressources/csv");
        boolean exist = false;
        int i = 0;
        while(i < length(files) && !exist){
            if(equals(files[i], filename)){
                exist = true;
            }
            i++;
        }
        return exist;
    }

    /**
    * Returns a CSVFile by his filename by searching in the directory with all csvs.
    *
    * @param filename   filename of the csv without .csv extension.
    * @return           CSVFile after loading it.
    */

    CSVFile myLoadCSV(String filename){
        return loadCSV("../ressources/csv/" + filename);
    }

    /**
     * Returns an integer asked to user while it is not a valid digit between 0 and 9.
     * @return integer from user after validation.
     */

    int enterNumber() {
        String s;
        cusp();
        do {
            curp();
            clearLine();
            s = readString();
        } while(!(length(s) > 0) || !isDigit(charAt(s, 0)));
        return stringToInt(s);
    }

    /**
     * Returns a string asked to user while it is not a valid string.
     * @return string from user after validation.
     */

    String enterText(){
        String s;
        cusp();
        do {
            curp();
            clearLine();
            s = readString();
        } while(!(length(s)>0));
        return s;
    }

    void setIndiceFind(Indice indice){
        indice.found = true;
    }

    void pressEnterToContinue() {
        info("Appuyez sur entrée pour continuer.");
        readString();
    }

    boolean customPressEnterToContinue() {
        info("Appuyez sur entrée pour continuer.");
        if(equals(readString(), "debugme")){
            return true;
        }
        return false;
    }

    /**
     * Display an array of string on screen.
     */

    void println(String[] a){
        for(int i = 0; i < length(a); i++){
            println(a[i]);
        }
    }

    void testInArray() {
        assertTrue(inArray(new int[]{1, 2, 3}, 3));
        assertFalse(inArray(new int[]{1, 2, 3}, 5));
    }

    /**
    * Returns if an integer is inside an array of integers.
    *
    * @param a  array to check
    * @param x  integer to be inside array
    * @return   true if x is inside a 
    */

    boolean inArray(int[] a, int x){
        boolean found = false;
        int i = 0;
        while(!found && i < length(a)) {
            if(a[i] == x){
                found = true;
            }
            i++;
        }
        return found;
    }

    /**
    * Returns if a char is inside an array of chars.
    *
    * @param a  array to check
    * @param x  char to be inside array
    * @return   true if x is inside a 
    */

    boolean inArray(char[] a, char x){
        boolean found = false;
        int i = 0;
        do {
            if(a[i] == x){
                found = true;
            }
            i++;
        } while(!found && i < length(a));
        return found;
    }

    /**
    * Shuffle the given array.
    */

    void shuffleArray(String[] a) {
        String temp;
        int r;
        for(int i = 0; i < length(a); i++) {
            r = randInt(0, length(a)-1);
            temp = a[r];
            a[r] = a[i];
            a[i] = temp;
        }
    }

    void testCountChar() {
        assertEquals(2, countChar("Bleu,Jaune,Rouge", ','));
    }

    int countChar(String str, char car) {
        int res = 0;
        for(int i=0; i<length(str);i++) {
            if(charAt(str, i) == car) {
                res = res + 1;
            }
        }
        return res;
    }

    void testIndicesChar() {
        assertArrayEquals(new int[]{4, 10}, indicesChar("Bleu,Jaune,Rouge", ','));
    }

    int[] indicesChar(String str, char car) {
        int countChar = countChar(str, car);
        int[] res = new int[countChar];
        int i = 0;
        int ires = 0;
        while(i < length(str) && ires < countChar) {
            if(charAt(str, i) == car) {
                res[ires] = i;
                ires++;
            }
            i++;
        }
        return res;
    }

    void testSplit() {
        assertArrayEquals(new String[]{"Bleu", "Jaune", "Rouge"}, split("Bleu,Jaune,Rouge", ','));
        assertArrayEquals(new String[]{"Bleu,Jaune,Rouge"}, split("Bleu,Jaune,Rouge", '*'));
        assertArrayEquals(new String[]{""}, split("", 'n'));
        assertArrayEquals(new String[]{"Bleu"}, split(":Bleu:", ':'));
        assertArrayEquals(new String[]{"Bleu", "Jaune", "Rouge"}, split("Bleu Jaune Rouge", ' '));
    }

    String[] split(String str, char car) {
        String[] res = new String[countChar(str, car)+1];
        int[] indices = indicesChar(str, car);
        
        if(length(res) > 1) {
            res[0] = substring(str, 0, indices[0]);
            for(int i=1; i<length(indices); i++) {
                res[i] = substring(str, indices[i-1]+1, indices[i]);
            }
            res[length(res)-1] = substring(str, indices[length(indices)-1]+1, length(str));
        } else {
            res[0] = str;
            return res;
        }
        return removeVoid(res);
    }

    void testStrip() {
        assertEquals("strip", strip("  strip  "));
        assertEquals("test strip", strip("  test strip  "));
    }

    String strip(String str) {
        String res = str;
        // strip start of str
        int i = 0;
        boolean check = false;
        while(i < length(str) && !check) {
            if(charAt(res, i) != ' ') check = true;
            else i++;
        }
        res = substring(res, i, length(res));
        // strip end of str
        i = length(res)-1;
        check = false;
        while(i >= 0 && !check) {
            if(charAt(res, i) != ' ') check = true;
            else i--;
        }
        res = substring(res, 0, i+1);
        return res;
    }

    /**
    * Returns a string where every occurence of toFind has been replaced by replace.
    *
    * @param s          Given string to replace occurences.
    * @param toFind     Given char to replace occurences.
    * @param replace    Char to replace every occurences.
    * @return           The initial string with occurences replaced by replace.
    */

    String replaceInString(String s, char toFind, char replace){
        char toCheck;
        String res = "";
        for(int i = 0; i < length(s); i++){
            toCheck = charAt(s, i);
            if(toCheck == toFind){
                res += replace;
            } else {
                res += toCheck;
            }
        }
        return res;
    }

    void testRemoveVoid() {
        assertArrayEquals(new String[]{"Bleu"}, removeVoid(new String[]{"", "Bleu", " "}));
    }

    String[] removeVoid(String[] tab) {
        int s = length(tab);
        for(int i=0; i<length(tab); i++) {
            if(equals(strip(tab[i]), "")) {
                s = s - 1;
            }
        }
        String[] res = new String[s];
        int ires = 0;
        for(int i=0; i<length(tab); i++) {
            if(!equals(strip(tab[i]), "")) {
                res[ires] = tab[i];
                ires++;
            }
        }
        return res;
    }

    /**
     * Returns 1 if true, 0 if false.
     * 
     * @version debug
     * @param bool boolean to test
     * @return 1 for true, 0 if false.
     */

    String toString(boolean bool){
        return bool ? "1" : "0";
    }

    void testFormatTime(){
        assertEquals("60s", formatTime(60));
        assertEquals("1m30s", formatTime(90));
    }

    /**
     * Returns the time formated with minutes and seconds.
     * @param time  the time as a long to format
     * @return      a String formated, ready to be displayed.
     */

    String formatTime(long time){
        String res = time+"s";
        if(time > 60){
            String minutes = ""+(time % 3600) / 60;
            String seconds = ""+(int) (time % 60);
            res = minutes + "m" + seconds + "s";
        }
        return res;
    }

    void println(char[][] chars) {
        for(int i=0; i<length(chars, 1); i++) {
            for(int j=0; j<length(chars, 2); j++) {
                print(chars[i][j] + " ");
            }
            print("\n");
        }
    }

    void testAllDigits(){
        assertEquals(false, allDigits("a123"));
        assertEquals(false, allDigits("123a"));
        assertEquals(false, allDigits("1a23"));
        assertEquals(true, allDigits("123"));
    }

    boolean allDigits(String s){
        boolean res = true;
        int i = 0;
        while(i < length(s) && res){
            res = isDigit(charAt(s,i)) ? true: false;
            i++;
        }
        return res;
    }

    void testCutArray(){
        String[] test = new String[]{"a", "b", "c", "d", "e"};
        assertTrue(length(test) > length(cutArray(0,2,test)));
    }

    String[] cutArray(int s, int e, String[] strings) {
        int size = e-s;
        String[] newArray = new String[size+1];
        for(int i = s; i <= e; i++){
            newArray[i] = strings[i];
        }
        return newArray;
    }

    void increaseNbJails(Game g){
        g.stats[0] = g.stats[0] + 1;
    }
    
    void increaseGoodAnswers(Game g){
        g.stats[1] = g.stats[1] + 1;
    }

    void increaseBadAnswers(Game g){
        g.stats[2] = g.stats[2] + 1;
    }

    void setFinalTime(Game g){
        // g.stats[3] = (int) getElapsedTime(g.timer);
        g.stats[3] = 0;
    }

    void increaseConseilTimeWon(Game g){
        g.stats[4] = g.stats[4] + 30;
    }

}