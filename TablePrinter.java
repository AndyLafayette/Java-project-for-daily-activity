package unnc.cs.zy;
import java.io.*;
import java.util.*;
import java.text.*;

class TablePrinter
{
  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat time = new SimpleDateFormat("HH:mm");
  SimpleDateFormat yearday = new SimpleDateFormat("yyyy-MM-dd");
  //show the table of today's task
  public void ShowTask(Task t) throws ParseException,IndexOutOfBoundsException
  {
    Date DateNow = new Date();

    System.out.println("Today's tasks (" + yearday.format(DateNow) + ")");
    //the for loop is used to read from start of tasklist to the end
    for ( int i = 0; i <=  t.getTheLengthOfTaskList()-1 ; i++)
    {
      if ( t.getTheLengthOfTaskList() == 0 )  //if there are no  element in the list, just jump out from it
      {
        break;
      }
      //get the task is been read and put it into the arraylist al
      ArrayList<String> al = t.getTaskList().get(i);  
      Date beginDate = df.parse(al.get(1)); //parse the beginTime (String) to beginTime (Date)
      Date endDate;
      if ( yearday.format( DateNow ).equals( yearday.format( beginDate ) ) )     //if year and day of begin date equals to that of today, print it
      {
        if( !al.get(2).equals("now") )
        {
          endDate = df.parse(al.get(2)); 
          if (yearday.format(beginDate).equals(yearday.format(endDate))) //judge whether the date of begintime is equal to endtime
          {
            System.out.println(t.time(beginDate) + "-" + t.time(endDate)+ "\t(" + al.get(3) +")\t" + al.get(0) + "\t");
          }
        }
        else
        {
          //if the endtime of the task in the list shows "now", reset the time interval and endtime in the list 
          endDate = new Date();
          t.setEndTime("now");
          t.compeleteLastTask();
          al = t.getTaskList().get(i); //get the task after reset
          System.out.println(t.time(beginDate) + "-" + al.get(2) + "\t(" + al.get(3) +")\t" + al.get(0) + "\t");
        }
      }
    } 
  }

  public void SearchTask(Task t) throws ParseException
  {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String searchTerm = "";
    boolean isNoTaskFound = true; //the boolean variable is used to clarify whether the last task is end
    try 
    {
      //use the buffered reader to read the input from keyboard
      System.out.print("Enter search term: ");
      BufferedReader instr = new BufferedReader (new InputStreamReader( System.in ) ) ; 
      searchTerm = instr.readLine();//pay attention to NullPointerException
    }
    catch (IOException ioe)
    {
      System.out.println(ioe.getMessage());
    }
    //read the single task from list from start to end
    for ( int i = 0; i <=  t.getTheLengthOfTaskList()-1 ; )
    {
      //if the tasklist contains no element,jump out of for loop
      if ( t.getTheLengthOfTaskList() == 0 || searchTerm.trim().isEmpty() )
      {
        break;
      }
      ArrayList<String> al = t.getTaskList().get(i); //use the arraylist al to store the task in the current position
      String str = (String) al.get(0);  //String str is used to store the task content is been reading 
      if ( str.contains(searchTerm) ) //judge whether the task contains the search term
      {
        Date beginDate = df.parse(al.get(1));
        Date endDate;
        if ( al.get(2).equals("now") ) //if the end time of task is "now", rest the endtime and its timeinterval
        {
          endDate = new Date(); 
          t.setEndTime("now");
          t.compeleteLastTask();  //reset the last element in the tasklist
          al = t.getTaskList().get(i); //store the reset task in the al
          long timeInterval = endDate.getTime() - beginDate.getTime(); //calculate the time interval after reset
          
          System.out.println(yearday.format(beginDate) + "\t" + time.format(beginDate) + "-" + al.get(2) + "\t(" + t.calTimeInterval(timeInterval) +")\t" + al.get(0) + "\t");
        }
        else //if the end time of the task is not "now"
        {
          int j = 0;
          //set the arraylist temp to store the next task in the tasklist, to judge whether a task spans multiple days
          ArrayList<String> temp = t.getTaskList().get(i+j); 
          do
          { 
            al = temp;
            j = j + 1;
            if (i+j != t.getTaskList().size()) //if i+j reach the end of tasklist, jump out of the loop
            {
              temp = t.getTaskList().get(i+j);
            }
            else
            {
              break;
            }
          }while( temp.get(0).equals(al.get(0)) && al.get(2).contains("23:59:59") && temp.get(1).contains("00:00:00"));
          //if the condition is satisfied, show that the task spans multiple days,then continue the while loop
          //the condition is the task content in current day is equal to next day and their end time and begin time is "23:59:59"
          //and "00:00:00" 
          //when the "temp" reach "today" (the time is program is running), jump out of task
          
          if ( al.get(2).equals("now") ) //if the end time is "now",reset it
          {
            endDate = new Date();
            t.setEndTime("now");
            t.compeleteLastTask();
            al = t.getTaskList().get(t.getTheLengthOfTaskList()-1);

            long timeInterval = endDate.getTime() - beginDate.getTime();

            System.out.println(yearday.format(beginDate) + "\t" + time.format(beginDate) + "-" + al.get(2) + "\t(" + t.calTimeInterval(timeInterval) +")\t" + al.get(0) + "\t"); //print it
          }
          else
          {
            endDate = df.parse(al.get(2));
 
            long timeInterval = endDate.getTime() - beginDate.getTime();

          System.out.println(yearday.format(beginDate) + "\t" + time.format(beginDate) + "-" + time.format(endDate) + "\t(" + t.calTimeInterval(timeInterval) +")\t" + al.get(0) + "\t");
          }
          
          if(j != 0)
          {
            i = i+j-1; //update the i
          }
        }
        
        isNoTaskFound = false;
      }
      i = i+1; //at the end of loop update i 
    }
    if(isNoTaskFound) //if no task has been found, print (no matchs)
    {
      System.out.println("(no matches)");
    }
  }
  
  //use the object tags record and time as the parameter, the information of tags and its time interval is recorded in tagslist 
  public void ShowSummary(TagsRecord tr,Task t)
  {
    System.out.print("Tags\t"); 
    Date nowDate = new Date();
    
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(nowDate);
    calendar.add(Calendar.DAY_OF_MONTH, -7);

    for (int i = 0; i < 7; i++)  //print the date of last seven days
    {
      calendar.add(Calendar.DAY_OF_MONTH, 1);
      Date theDayInLastWeek = calendar.getTime();
      System.out.print(  yearday.format(theDayInLastWeek) + "\t" );  //print the date at header
    }
    System.out.println();
    //use the for loop to print the table
    for (int j = 0;j < tr.getTagsList().size(); j++)
    {
      ArrayList<String> temp = tr.getTagsList().get(j);
      System.out.print( temp.get(0) + "\t");
      for (int k = 1; k < temp.size(); k++)
      {
        if (temp.get(k) != "") 
        {
          long tim = Long.parseLong(temp.get(k));
        
          System.out.print(  t.calTimeInterval(tim) + "\t\t"); 
        }
        else
        {
          System.out.print( "\t\t" );
        }
      }
      System.out.println();
    }
  }
}
