// 6515765 zy15765 Rui Jin
package unnc.cs.zy15765;
import java.io.*;
import java.util.*;
import java.text.*;

class TagsRecord
{
  private ArrayList<ArrayList<String>> tagsList = new ArrayList<ArrayList<String>>();
  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat yearday = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat time = new SimpleDateFormat("HH:mm");
  
  public void TagsRecorder(Task t) throws ParseException
  {
    for (int i = 0; i<t.getTheLengthOfTaskList(); i++)
    {
      ArrayList<String> al = t.getTaskList().get(i);
      
      ArrayList<String> recordTags = new ArrayList<String>(); //if a tag has been found, record it into this arrraylist
      
      String taskContent = al.get(0);//store the content of the task
      String tag = "";
      //use the for loop to search '#' in a task, if it has been found, get the tag
      for (int j = 0; j<taskContent.length(); j++)
      {
        if (taskContent.charAt(j) == '#')
        {
          for(int k = 1; k + j < taskContent.length(); k++)
          {
            char ch = taskContent.charAt(j+k);
            if ( (j+k+1) == taskContent.length() )//if j+k reach the end of the task content
            {
              tag = taskContent.substring(j,j+k+1); //use method substring to record the tag
              if ( !recordTags.contains(tag) ) //if the tag has not been found in the recordTags, record it in the recordTags 
              {
                ReadAndRecordTag(tag,al);
                recordTags.add(tag); //record it 
                break;
              }
              else
              {
                break;
              }
            }
            else if ( ((ch == ' ') || ( ch == '\n') || (ch == '#') || (ch == '\t')) && (k != 1) )//read the tag until reach a white space
            {
              tag = taskContent.substring(j,j+k);
              if ( !recordTags.contains(tag) )
              {
                ReadAndRecordTag(tag,al);
                recordTags.add(tag);
                break;
              }
              else
              {
                break;
              }
            }
          }
        } 
      }
    }
  }
  //this method is used to calculate how many days between two dates
  public long calTheDayNumberBetweenTwoDate(Date Time1, Date Time2) throws ParseException
  {
    String nowDay = yearday.format(Time1);
    String beginDay = yearday.format(Time2);

    Date nowDate = yearday.parse(nowDay);
    Date beginDate = yearday.parse(beginDay);
    
    long index =7 - ( nowDate.getTime() - beginDate.getTime() ) /( 1000*24*60*60 );
    return index;
  }
  //the method is used to read and record the tag, and add tag with its timeinterval in last 7 days into the tagsList
  public void ReadAndRecordTag(String Tag, ArrayList<String> Al) throws ParseException
  {
    //set the boolean value
    boolean canTagBeFoundInList = false;
    
    if(tagsList.size() != 0)
    {
      for(int i = 0; i<tagsList.size(); i++)
      {
        ArrayList<String> temp = tagsList.get(i);
        //if Tag can Be Found In tagsList, read and record it
        if (temp.get(0).equals(Tag))
        {
          canTagBeFoundInList = true;//set the boolean value to ture
          Date beginTime = df.parse(Al.get(1));
          
          Date now = new Date();
          Date endTime;
          if(!Al.get(2).equals("now"))
          {
            endTime = df.parse(Al.get(2));
          }
          else
          {
            endTime = now;
          }
          long index = calTheDayNumberBetweenTwoDate( now, beginTime );
          long interval = endTime.getTime() - beginTime.getTime();
          if ( index > 0 )
          {
            if ( temp.get((int)index).equals("") )
            {
              temp.set((int)index, Long.toString(interval));
            }
            else
            {
              temp.set((int)index, addTimeInterval(temp.get((int)index),Al));
            }
          }
          break;
        }
      }
      //if the tag cannot be found in tagelist
      if(!canTagBeFoundInList)
      {
        ArrayList<String> als = new ArrayList<String>();  //create a new arraylist to store the tag and related information
        als.add(Tag);//add the tag into the arraylist
        //add the timeinterval; at first the timeinterval is set to ""
        for(int i = 0; i<7; i++)
        {
          als.add("");
        }
        Date beginTime = df.parse(Al.get(1));
        Date endTime;
        Date now = new Date();
        if(!Al.get(2).equals("now"))
        {
          endTime = df.parse(Al.get(2));
        }
        else
        {
          endTime = now;
        }
        //get the number of days between the begin day and the date the program is running, use it as a index to 
        //fill the timeinterval in arraylist 
        long index = calTheDayNumberBetweenTwoDate( now, beginTime );
        long interval = endTime.getTime() - beginTime.getTime();
        if ( index > 0 )
        {
          als.set((int)index, Long.toString(interval));
        }
        tagsList.add(als);
      }
    }
    else
    {
      //if the taglist is null, set it by adding tag into it
      ArrayList<String> als = new ArrayList<String>();
      als.add(Tag);
      for(int i = 0; i<7; i++)
      {
        als.add("");
      }
      Date beginTime = df.parse(Al.get(1));
      Date now = new Date();
      Date endTime;

      if(!Al.get(2).equals("now"))
      {
        endTime = df.parse(Al.get(2));
      }
      else
      {
        endTime = now;
      }
      long index = calTheDayNumberBetweenTwoDate( now, beginTime );
      long interval = endTime.getTime() - beginTime.getTime();
      if ( index > 0 )
      {
        als.set((int)index, Long.toString(interval));
      }
      tagsList.add(als);
    }
  }
  //add the time to get time interval
  public String addTimeInterval(String timeToBeAdded, ArrayList<String> al) throws ParseException
  {
    Date beginDate = df.parse(al.get(1));
    Date endDate;
    if (al.get(2).equals("now"))
    {
      endDate = new Date();
    }
    else
    {
      endDate = df.parse(al.get(2));
    }
    long timeToAdd = endDate.getTime() - beginDate.getTime();
    long interval = Long.parseLong(timeToBeAdded) + timeToAdd;
    return Long.toString(interval);
  }
  //this method is used to get tagslist
  public ArrayList<ArrayList<String>> getTagsList()
  {
    return tagsList;
  }
}
