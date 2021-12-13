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
        return (int) (random()*(max-min)+min);
    }

    void debug(String s){
        println(ANSI_GREEN + "[Fort Bouyard 1.0 DEBUGGER] : " + ANSI_RESET + s);
    }

    void info(String s){
        println(ANSI_CYAN + s + ANSI_RESET);
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
        String[] files = getAllFilesFromCurrentDirectory();
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

}