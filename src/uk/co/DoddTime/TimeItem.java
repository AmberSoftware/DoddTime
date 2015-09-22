/*
 * This class defines a time entry item and associated methods
 */

package uk.co.DoddTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 *
 * @author Mike
 */
public class TimeItem {

    //class variables
    private static int nextItemNo = 0;
    private static int activeItemNo = 0;

    // instance variables
    private int itemNo = 0;
    private int jobNumber = 0;
    private String description = "<Enter Description>";
    private Calendar startTime;
    private Calendar endTime;
    private int totalSeconds = 0;
    private boolean active = false;
    
    public TimeItem() {};
    // constructor for use by UI (sets as active)
    public TimeItem(String jobNumber, String description, String time){
        this.setJobNumber(Integer.parseInt(jobNumber));
        this.setDescription(description);
        if (time != null && time.length() > 0) this.setTotalSeconds(convertTimeToSecs(time));
        this.setStartTime(Calendar.getInstance());
        this.active=true;
        this.setItemNo(nextItemNo++);
        setActiveItemNo(this.getItemNo());
    }
    

    // constructor for use by file IO (does not set as active)
    public TimeItem(int jobNumber, String description, int totalSeconds){
        this.setJobNumber(jobNumber);
        this.setDescription(description);
        this.setStartTime(Calendar.getInstance());
        this.active=false;
        this.setItemNo(nextItemNo++);
        this.setTotalSeconds(totalSeconds);
    }
    
    // update item when total seconds passed
    public void updateItem(String jobNumber, String description, int totalSeconds, boolean active){
        this.setJobNumber(Integer.parseInt(jobNumber));
        this.setDescription(description);
        // if updating inactive to active set the start time and activate
        if (this.active != active && active) {
            this.setStartTime(Calendar.getInstance());
            this.active=true;
            setActiveItemNo(this.getItemNo());
        
        // if updating from active to inactive update total time
        } else if (this.active != active && !active)   {
            this.stop();
        // if not switching between active/inactive, and currently inactive, then update time    
        } else if (!this.active) {
            this.setTotalSeconds(totalSeconds);
        }
    }
    
    // update item from table when separate hours/mins/seconds passed
    public void updateItem(String jobNumber, String description, int totalHours, int totalMinutes, int totalSecs, boolean active){
        this.updateItem(jobNumber, description, (totalHours*3600 + totalMinutes*60 + totalSecs), active);
    }
    
//    public TimeItem(){
//    }    
    /**
     * @return the jobNumber
     */
    public int getJobNumber() {
        return jobNumber;
    }

    /**
     * @param jobNumber the jobNumber to set
     */
    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }
    
    // return jobno as string
    public String getJobNoStr(){
        
        // return zero
        if (this.getJobNumber()==0){
            return "00000000";
        }
        // for job numbers less than 6 digits pad
        else if (this.getJobNumber() < 100000){
            return "000" + Integer.toString(this.getJobNumber());   
        }
        // assume 7 digit job number
        else {
            return "00" + Integer.toString(this.getJobNumber());   
        }

    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the startTime
     */
    public Calendar getStartTime() {
        return startTime;
    }
    
    public String getStartTimeStr() {
        return (new SimpleDateFormat("HH:mm:SS").format(startTime.getTime()));
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Calendar getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the totalSeconds
     */
    public int getTotalSeconds() {
        return totalSeconds;
    }
    
    // return total hours part of total time
    public int getHours(){
        return (int) totalSeconds/3600;
    }
    
    // return minutes part of total time
    public int getMinutes(){
        return (int) ((totalSeconds%3600)/60);
    }
    
    // return seconds part of total time
    public int getSeconds(){
        return (int) (totalSeconds%60);
    }
    
    // return minutes as string
    public String getMinutesStr(){
        
        // return zero
        if (this.getMinutes()==0){
            return "00";
        }
        // pad single digit with zero
        else if (this.getMinutes() < 10){
            return '0' + Integer.toString(this.getMinutes());   
        }
        // return double digit
        else {
            return Integer.toString(this.getMinutes());
        }

    }

  
    // calculate total seconds on job
    public void setTotalSeconds() {
        //this.totalSeconds = (int)((endTime.compareTo(startTime)*1000)/60);
        //long diff = endTime.compareTo(startTime);
        long diffMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        // for testing only use no. secs
        this.totalSeconds += (int)((diffMillis/1000));
        // !!Reinstate this line: this.totalSeconds += (int)((diffMillis/1000)/60);

    }
    
    // method to allow manual update of total seconds
    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }
    
    // stop working on this time item
    public void stop(){
        if (this.isActive()) {
            this.setEndTime(Calendar.getInstance());
            this.setTotalSeconds();
            this.setActive(false);
        }
        
    }
    
    public void resume(){
        this.setStartTime(Calendar.getInstance());
        this.setActive(true);
        setActiveItemNo(this.getItemNo());
    }

    
    public void refresh(){
        if (this.isActive()) {
            this.setEndTime(Calendar.getInstance());
            this.setTotalSeconds();
            this.setStartTime(Calendar.getInstance());
        }
    }
    /**
     * @return the isActive
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    /**
     * @return the nextItemNo
     */
    public static int getNextItemNo() {
        return nextItemNo;
    }

    /**
     * @param aNextItemNo the nextItemNo to set
     */
    public static void setNextItemNo(int aNextItemNo) {
        nextItemNo = aNextItemNo;
    }

    /**
     * @return the activeItemNo
     */
    public static int getActiveItemNo() {
        return activeItemNo;
    }

    /**
     * @param aActiveItemNo the activeItemNo to set
     */
    public static void setActiveItemNo(int aActiveItemNo) {
        activeItemNo = aActiveItemNo;
    }    

    /**
     * @return the itemNo
     */
    public int getItemNo() {
        return itemNo;
    }

    /**
     * @param itemNo the itemNo to set
     */
    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }
    
    // build formatted text for time item to allow copy to legacy time system
    public String getCopyText(){
        // build text string (don't start with a blank as cursor is on
        // first date field
        StringBuilder sb = new StringBuilder();
        
        // add date
        sb.append(new SimpleDateFormat("dd MM yy").format(this.getStartTime().getTime()));
        
        // 4 spaces between date and hours
        sb.append("    ");
        
        // hours
        sb.append(Integer.toString(this.getHours()));
        
        // 3 spaces between hours and mins
        sb.append("   ");
        
        // mins
        sb.append(this.getMinutesStr());

        // 2 spaces between hours and mins
        sb.append("  ");        
        
        // add job number
        sb.append(this.getJobNoStr());
        
        // end of line
        sb.append("\n");
        
        // 15 spaces at start of second line
        sb.append("               ");
        
        // description
        sb.append(this.getDescription());
        
        // end of line
        sb.append("\n");
        
        return sb.toString();
    }
    
    // convert HH:MM format time to seconds
    private static int convertTimeToSecs(String time){
        if (time == null) return 0;
        String hours = time.substring(0, 2);
        String mins = time.substring(3,5);
        return Integer.parseInt(hours)*3600 + Integer.parseInt(mins)*60;
    }
}
