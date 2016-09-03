package unnc.cs.zy;
import java.io.*;
import java.util.*;
import java.text.*;

class Task
{
  //the arraylist<arraylist<string>> is used to record all the task's information
  private ArrayList<ArrayList<String>> taskList = new ArrayList<ArrayList<String>>();
  //stroe the current task and add it into tasklist later
  private ArrayList<String> task;

  private String bTime = "";
  private String eTime = "";
  private String intervalTime = "";
  private String lastTask = "";

  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat yearday = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat time = new SimpleDateFormat("HH:mm");
   
  //allocate memory for task
  public void createTask()
  {
    task = new ArrayList<String>();
  }
  //set tasklist (used when program is reading the file)
  public void setTaskList(ArrayList<ArrayList<String>> tslist)
  {
    taskList = tslist;
  }
  //set task (used when program is reading the file)
  public void setTask(String curTask)
  {
    lastTask = curTask;
  }
  //add the content (not only the time) into the task
  public void addTime(String Time)
  {
    task.add(Time);
  }
  //return the lasttask
  public String getLastTask()
  {
    return lastTask;
  }

  public void addTaskList(ArrayList<String> curTask)
  {
    taskList.add(curTask);
  }
  //add the last task to tasklist
  public void compeleteCurTask()
  {
    createTask();
    addTime(lastTask);
    addTime(bTime);
    addTime(eTime);
    addTime(intervalTime);
    addTaskList(task);
  }
  //update the tasklist
  public void compeleteLastTask()
  {
    createTask();
    addTime(lastTask);
    addTime(bTime);
    addTime(eTime);
    addTime(intervalTime);
    taskList.set( taskList.size()-1 ,task );
  }
  //return the tasklist
  public ArrayList<ArrayList<String>> getTaskList()
  {
    return taskList;
  }
  //return the length of task list
  public int getTheLengthOfTaskList()
  {
    return taskList.size();
  }
  //judge whether the last task is end
  public boolean isLastTaskEnd()
  {
    if(eTime.equals("now"))
    {
      return false;
    }
    else
    {
      return true;
    }
  }
  //set begin time of the task
  public void setBeginTime(String Time)
  {
    bTime = Time;
  }
  //convert the long time interval into a String
  public String calTimeInterval(long l)
  {
    long hour=(l/(60*60*1000));
    long min=((l/(60*1000))-hour*60);
    long s=((l/1000)-min*60-hour*60*60)+1;  

    if(s != 0)
    {
      min = min+1;
      s = 0;
      if(min == 60) 
      {
        min = 0;
        hour = hour + 1;
      }
    }
    String intv;
    if(hour == 0)
    {
      intv = min + "m";
      return intv;
    }
    else if (min == 0)
    {
      intv = hour + "h";
      return intv;
    }
    else 
    {
      intv = hour + "h" + " " + min + "m";
      return intv;
    }
  }
  //set endtime and calculate the time  interval
  public void setEndTime(String endTime) throws ParseException
  {
    this.eTime = endTime;
    Date beginDate = df.parse(bTime); 
    Date endDate;
    if(!getEndTime().equals("now"))
    {
      endDate = df.parse(getEndTime()); 
    }
    else
    {
      endDate = new Date();
    }

    long l = endDate.getTime() - beginDate.getTime();
    intervalTime = calTimeInterval(l);
  }
  //return the begin time
  public String getBeginTime()
  {
    return bTime;
  }
  //return the end time
  public String getEndTime()
  {
    return eTime;
  }
  //return the time interval
  public String getIntervalTime()
  {
    return intervalTime;
  }
  //the next three method is used to convert date value into string and return it
  public String df(Date date)
  {
    return df.format(date);
  }

  public String yearday(Date date)
  {
    return yearday.format(date);
  }

  public String time(Date date)
  {
    return time.format(date);
  }
  //judge whether two dates are in only one day
  public boolean isTwoTimeInOneDay(Date Time1, Date Time2)
  {
    if( yearday.format(Time1).equals(yearday.format(Time2)) )
    {
      return true;
    }
    else 
    {
      return false;
    }
  }
}
