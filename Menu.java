package unnc.cs.zy;
import java.io.*;
import java.util.*;
import java.text.*;

class Menu
{
  private String curTask;
  private String optionInput;//the String is used to store the user input
  //print the menu
  public void PrintMenu(Task t) throws ParseException,IOException,NullPointerException
  {
    if( t.getEndTime().equals("now") ) 
    {
      t.setEndTime("now");//update the current task 
      curTask = "("+ t.getIntervalTime() + ") " + t.getLastTask(); //store the current task into the String
    }
    else
    {
      curTask = "(None)";
    }
    
    System.out.println("Current task: " + curTask);
    System.out.println("1) Change task");
    System.out.println("2) Show today's tasks");
    System.out.println("3) Show this week's summary");
    System.out.println("4) Search tasks");
    System.out.println("5) Quit");
    System.out.print("Option:");

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    optionInput = br.readLine();
  }
  //return the user input
  public String getOptionInput()
  {
    return optionInput;
  } 
}
