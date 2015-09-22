/*
 * This class defines a time entry item and associated methods
 */

package uk.co.DoddTime;

//import java.text.SimpleDateFormat;
//import java.util.Calendar;


/**
 *
 * @author Mike
 */
public class RoundedTimeItem extends TimeItem {

    //class variables
    private static int roundMins = 1;
    private static int roundSecs = 60;
    private static int roundDownMax = 30;

    // instance variables

    // methods
    
    /**
     * @return the roundMins
     */
    public static int getRoundMins() {
        return roundMins;
    }

    /**
     * @param aRoundMins the roundMins to set
     */
    public static void setRoundMins(int aRoundMins) {
        if (aRoundMins == 0) aRoundMins = 1;
        roundMins = aRoundMins;
        roundSecs = roundMins*60;
        roundDownMax = roundSecs/2;
    }

    
    // constructor
    public RoundedTimeItem(TimeItem ti){
        this.setItemNo(ti.getItemNo());
        this.setJobNumber(ti.getJobNumber());
        this.setDescription(ti.getDescription());
        this.setStartTime(ti.getStartTime());
        this.setEndTime(ti.getEndTime());
                
        // If total seconds exactly divisible by rounding seconds, no need to round
        int remainder = ti.getTotalSeconds()%roundSecs;
        if (remainder == 0) {
            this.setTotalSeconds(ti.getTotalSeconds());
        // round up or down based on remainder
        } else if(remainder>roundDownMax) {
            this.setTotalSeconds(ti.getTotalSeconds()+(roundSecs-remainder));
        } else {
            this.setTotalSeconds(ti.getTotalSeconds()-remainder);
        }
        // Finally if zero seconds calculated, set to minimum round amount
        if (this.getTotalSeconds() == 0) this.setTotalSeconds(roundSecs);

    }
}
    
