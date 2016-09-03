package unnc.cs.zy;
import java.io.*;
import java.util.*;
import java.text.*;

class TimeTracker
{
  public static void main(String[] args) throws ParseException,IOException,NullPointerException
  {
    String filename = "timetracker.data";
    boolean isFileNull = false;
    
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat yearday = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm");
    
    File file = new File(filename);
    
    if(! file.exists() )
    {
      file.createNewFile();
      isFileNull = true;
    }
  
    Task t = new Task();
    FileContent fc = new FileContent();
   
    if(!isFileNull) 
    {
      t = (Task)fc.readTaskFromFile();
    }

    TablePrinter tp = new TablePrinter();
    
    // (menu)
    Menu m = new Menu();
    m.PrintMenu(t);

    while( !m.getOptionInput().equals("5") )
    {
      if(m.getOptionInput().equals("1"))
      {
        //(1)
        //opt1.ChangeTask();
        fc.WriteToFile(t);
        t = fc.readTaskFromFile();
      }
      else if (m.getOptionInput().equals("2"))
      {
        //(2)
        //opt2.ShowTask();
        tp.ShowTask(t);
      }
      else if (m.getOptionInput().equals("3")) 
      {
        //(3)
        TagsRecord tr = new TagsRecord();
        tr.TagsRecorder(t);
        tp.ShowSummary(tr,t);  
      }
      else if (m.getOptionInput().equals("4"))
      {
        //(4)
        //opt4.SearchTask();
        tp.SearchTask(t);
      }
      else
      {
        System.out.println("Invalid option!");
      }
      m.PrintMenu(t);
    }
  }
}

