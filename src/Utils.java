import extensions.*;

class Utils extends Program {
    final boolean DEBUG = false;

    void testIsBetween() {
        assertTrue(isBetween(5, 1, 10));
        assertFalse(isBetween(15, 1, 10));
    }

    boolean isBetween(int entry, int min, int max) {
        return ((entry >= min) && (entry <= max));
    }

    void testIsDigit(){
        assertTrue(isDigit('9'));
        assertFalse(isDigit('a'));
    }

    boolean isDigit(char letter){
        return ((letter >= '0') && (letter <= '9'));
    }

    void testRandInt(){
        assertEquals(true, isBetween(randInt(1,100),1,100));
        assertEquals(false, isBetween(randInt(101,150),1,100));
    }

    int randInt(int min, int max){
        return (int) (random()*((max+1)-min)+min);
    }

    void debug(String s){
        println(ANSI_GREEN + "[Fort Bouyard 1.0 DEBUGGER] : " + ANSI_RESET + s);
    }

    void info(String s){
        println(ANSI_CYAN + s + ANSI_RESET);
    }

    void erreur(String s){
        println(ANSI_RED + s + ANSI_RESET);
    }

    String readTxt(String path) {
        File txt = newFile(path);
        String res = "";

        while(ready(txt)) {
            res += readLine(txt) + "\n";
        }

        return res;
    }


    void printTxt(String path) {
        println(readTxt(path));
    }

    void myClearScreen(){
        if(!DEBUG){
            clearScreen();
            cursor(0,0);
        }
    }

    void csvMissingError(String filename){
        println(ANSI_RED + "Couldnt start the game.. (" + filename + " is missing)" + ANSI_RESET);
    }

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

    CSVFile myLoadCSV(String filename){
        return loadCSV("../ressources/csv/" + filename);
    }

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

    boolean onlyChars(String text){
        boolean res = true;
        int i = 0;
        while(i < length(text) && res){
            if(!isBetween(charToInt(charAt(text, i)), charToInt('A'), charToInt('Z'))){
                res = false;
            }
            i = i +1;
        }
        return res;
    }

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

    void println(String[] a){
        for(int i = 0; i < length(a); i++){
            println(a[i]);
        }
    }

    void testInArray() {
        assertTrue(inArray(new int[]{1, 2, 3}, 3));
        assertFalse(inArray(new int[]{1, 2, 3}, 5));
    }

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

}