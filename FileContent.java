// 6515765 zy15765 Rui Jin
package unnc.cs.zy15765;
import java.io.*;
import java.util.*;
import java.text.*;

class FileContent
{
  private String filename = "timetracker.data";
  private String newTask = "";
  private String Line = "";
  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat yearday = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat time = new SimpleDateFormat("HH:mm");
  
  public Task readTaskFromFile()
  {
    Task t = new Task();
    try
    {
      //read the content of the file
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line;
      ArrayList<ArrayList<String>> tsl = new ArrayList<ArrayList<String>>();
      ArrayList<String> temp = new ArrayList<String>();
      //set the initial content of object Task
      while( (line = br.readLine()) != null && !line.trim().isEmpty() )
      {
        temp = new ArrayList<String>();
        temp.add(line.replace("\\n","\n"));
        temp.add(line = br.readLine());
        temp.add(line = br.readLine());
        temp.add(line = br.readLine());
        line = br.readLine();
        tsl.add(temp);
      }
      t.setTaskList(tsl);
      t.setTask(temp.get(0));
      t.setBeginTime(temp.get(1));
      t.setEndTime(temp.get(2));
      
    }
    catch(IOException exc)
    {
      exc.printStackTrace();
    }
    catch(ParseException pec)
    {
      pec.printStackTrace();
    }
    return t;//return the object Task
  }
  //when user input option 1, write to the file
  public void WriteToFile(Task t)
  {
    try
    {
      //user can input a NEW TASK 
      System.out.println("Enter new task (blank line ends input, no text ends the current task):");
      BufferedReader  br = new BufferedReader (new InputStreamReader( System.in ) ) ; 
      newTask = "";
      Line = br.readLine();
      while ( ! Line.trim().isEmpty() )
      {
        newTask = newTask + Line;
        if ( ! (( Line = br.readLine() ).trim().isEmpty()) )
        {
          newTask = newTask + "\n";
        }
      }
    }
    catch (FileNotFoundException fnf)
    {
      System.out.println(fnf.getMessage());
    }
    catch (IOException  ioe)
    {
      System.err.println( "Problem reading" + filename + " ! " ) ;
      System.err.println(ioe);
      ioe.printStackTrace();
      System.exit(-2);
    }
    
    if(t.isLastTaskEnd())//if the last task is end
    {
      if ( ! (newTask.trim().isEmpty()) ) //if user has not innput blankline
      { 
        try
        {
          Date dateNow = new Date();
          String beginTime = df.format(dateNow);
          t.setTask(newTask);
          t.setBeginTime(beginTime);
          t.setEndTime("now");
          t.compeleteCurTask(); 
          //set the current task and add it into tasklist
        }
        catch(ParseException exc)
        {
          System.out.println("I/O Error: " + exc);
        }
      }
    }
    else
    {
      //if last task is not end 
      try
      {
        Date dateNow = new Date();
        Date dateBegin = df.parse(t.getBeginTime());
        Calendar calendar = Calendar.getInstance();
        if(!t.isTwoTimeInOneDay(dateNow,dateBegin)) //judge whether the date in list is the date the program is running
        {
          t.setEndTime( yearday.format(dateBegin) + " 23:59:59");
          t.compeleteLastTask();//update the tasklist
          calendar.setTime(dateBegin);
          calendar.add(Calendar.DAY_OF_MONTH, 1);
          dateBegin = calendar.getTime();
          t.setBeginTime( yearday.format(dateBegin) + " 00:00:00");
          dateBegin = df.parse(t.getBeginTime());
          //use the while to record the days between the begin date and today
          //until the today is reached
          while(!t.isTwoTimeInOneDay(dateNow,dateBegin))  
          {
            t.setEndTime( yearday.format(dateBegin) + " 23:59:59");
            t.compeleteCurTask();//add the task to the tasklist
            calendar.setTime(dateBegin);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateBegin = calendar.getTime();
            t.setBeginTime( yearday.format(dateBegin) + " 00:00:00");
            dateBegin = df.parse(t.getBeginTime());
          }
          String endTime = t.df(dateNow);
          t.setEndTime(endTime);
          //add task to the array list    
          t.compeleteCurTask();   
        }
        else
        {
          String endTime = t.df(dateNow);
          t.setEndTime(endTime);
          //change the last task in the array list    
          t.compeleteLastTask();   
        }
        //if user haas not input blank line, add the new task into the task list
        if(! (newTask.trim().isEmpty()) )
        {
          String beginTime = t.df(dateNow);
          t.setTask(newTask);
          t.setBeginTime(beginTime);
          t.setEndTime("now");
          t.compeleteCurTask();
        }
      }
      catch(ParseException exc)
      {
        System.out.println("I/O Error: " + exc);
      }
    }
    writeTaskToFile(t);    
  }
  //write the task list into the file(update the file) after every time user use option1
  public void writeTaskToFile(Task t)
  {
    try
    {
      PrintWriter pw = new PrintWriter(filename);
      for (int i = 0; i < t.getTheLengthOfTaskList(); i++)
      {
        ArrayList<String> al = t.getTaskList().get(i);
        for (int j = 0; j < 4; j++)
        {
          pw.println(al.get(j).replace("\n","\\n"));
        }
        pw.println("-------------------");
      }
      pw.close();
    }
    catch(IOException exc)
    {
      exc.printStackTrace();
    }
  }
}
