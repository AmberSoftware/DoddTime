/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.DoddTime;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
/**
 *
 * @author Mike
 */
public class TimeFile implements Closeable {
    
    private static final int RECNO_LEN = 4;
    private static final int JOBNO_LEN = 4;
    private static final int TIME_LEN = 4;
    private static final int DESC_LEN = 60;

    private static final int RECORD_LEN = 
            RECNO_LEN + JOBNO_LEN + 2*DESC_LEN + TIME_LEN;
    private RandomAccessFile raf;
    
    // Constructor- creates file from path
    public TimeFile(String path) throws IOException{
        raf = new RandomAccessFile(path,"rw");
    }
    
    // close method from CLoseable interface
    public void close() throws IOException{
        raf.close();
    }
    
    // add new record to end of file
    private void append(TimeItem addTimeItem)
            throws IOException{
        raf.seek(raf.length());
        this.write(addTimeItem);
    }
    
    // return number of records
    private int getTotalRecs() throws IOException{
        return (int) (raf.length()/RECORD_LEN);
    }

    // read from file
    public TimeItem read() throws IOException
    {
        StringBuffer sb = new StringBuffer();
        int itemNo = raf.readInt();
        int jobNumber = raf.readInt();
        int totalSecs = raf.readInt();        
        for (int i = 0; i < DESC_LEN; i++)
            sb.append(raf.readChar());
        String description = sb.toString().trim();

        return new TimeItem(jobNumber, description, totalSecs);
    }
    
    // update a record
    private void update(TimeItem updateItem)
       throws IOException
    {
       if (updateItem.getItemNo() >= getTotalRecs())
          throw new IllegalArgumentException(updateItem.getItemNo()+" out of range");
       raf.seek(updateItem.getItemNo()*RECORD_LEN);
       this.write(updateItem);
    }    
    
    //write to file
    private void write(TimeItem writeItem)
        throws IOException
    {   
        // Write item number and job number
        raf.writeInt(writeItem.getItemNo());
        raf.writeInt(writeItem.getJobNumber());
        // Write total time
        raf.writeInt(writeItem.getTotalSeconds());        
        
        // Description (pad with blanks if less than description length)
        StringBuffer sb = new StringBuffer(writeItem.getDescription());
        if (sb.length() > DESC_LEN)
            sb.setLength(DESC_LEN);
        else
        if (sb.length() < DESC_LEN)
        {
            int len = DESC_LEN-sb.length();
            for (int i = 0; i < len; i++)
            sb.append(' ');
        }
        raf.writeChars(sb.toString());
        

        
        
    }
    // Update file from array
    public void updateAddAll(TimeItem[] timeItems)
        throws IOException{
        if (this.getTotalRecs() > 0){
            for (TimeItem ti: timeItems) {
                if (ti == null) break;
                try {
                    this.update(ti);
                }
                // if update fails try to append
                catch (IllegalArgumentException ex) {
                    this.append(ti);
                }
            }
        }
        else {
            for (TimeItem ti: timeItems) {
                if (ti == null) break;
                try {
                    this.write(ti);
                }
                catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
    }    
}
