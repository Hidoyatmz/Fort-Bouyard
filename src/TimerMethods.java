class TimerMethods extends Utils {

    /**
     * Returns a long that represents the time elasped since creation of the timer in seconds.
     * @param timer The timer to calculate elapsed time.
     * @return      a long that represents the time elapsed.
     */

    long getElapsedTime(Timer timer){
        long initTime = timer.initTime;
        return (getTime() - initTime)/1000;
    }

    /**
     * Returns a long that represents the time elasped since creation of the timer in milliseconds.
     * @param timer The timer to calculate elapsed time.
     * @return      a long that represents the time elapsed.
     */

    long getElapsedTimeMs(Timer timer) {
        return getTime() - timer.initTime;
    }
    
    /**
     * Returns if the timer is expired or not.
     * @param timer The timer to check if timer is expired
     * @return true if timer is expired, false otherwise.
     */

    boolean inTime(Timer timer){
        return getRemainingTime(timer) > 0;
    }

    /**
     * Return a long that represents the time remaining before expiration of the timer.
     * @param timer     The timer to calculate remaining time.
     * @return          a long that represents the time remaining before expiration.
     */

    long getRemainingTime(Timer timer) {
        long remainingTime = (timer.maxTime) - getElapsedTime(timer);
        return remainingTime > 0 ? remainingTime : 0;
    }

    String getFormatRemainingTime(Timer timer){
        String color = ANSI_GREEN;
        if(!inTime(timer)){
            color = ANSI_RED;
        } else if(getRemainingTime(timer) < (timer.maxTime / 2)) {
            color = ANSI_YELLOW;
        }
        return "" + color + getRemainingTime(timer) + ANSI_RESET;
    }

    /**
     * Returns a new Timer with default values and given value for Expiration.
     * @param maxTime   Maximum explased time before timer is expired.
     * @return          The new timer.
     */

    Timer newTimer(int maxTime){
        Timer timer = new Timer();
        timer.initTime = getTime();
        timer.maxTime = maxTime;
        return timer;
    }
}