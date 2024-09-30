import java.util.ArrayList;
import java.util.Scanner;
import java.util.Hashtable;
import java.io.File;
import java.io.PrintWriter;

public class MakeSQL {

    /** Parse the TSV file into an ArrayList of hashtables.
     * Each hashtable contains a row from the spread sheet, but
     * data is stored as (field name, value) pairs */
    public ArrayList <Hashtable<String,String>>  parseTSVfile(String filename, String delimiter) {
        ArrayList <Hashtable<String,String>> arr = new ArrayList <Hashtable<String,String>> ();
        try {
          File f = new File(filename);
          Scanner scan = new Scanner(f);

          // get first line, an array of the field names
          String s = scan.nextLine();
          System.out.println(s);
          String [] fields = s.split(delimiter); // split on tab

          System.out.println(fields.length + " fields ");
          /*for (String f1: fields) {
            System.out.println(f1);
          }*/
          while( scan.hasNext() ) {
                String t = scan.nextLine();

                // For each line, create a new hashtable, then add
                //it to ArrayList
                String [] vals = t.split(delimiter);
                Hashtable <String,String> h = new Hashtable<String,String> ();
                for (int i = 0; i < fields.length; i++) {
                     h.put(fields[i], vals[i]);
                }
                arr.add(h);


          }
          scan.close();
          System.out.println(arr.size() + " Hashtables in ArrayList");

        } catch (Exception ex) {
            System.out.println("Exception parsing " + filename);
            System.out.println(ex.toString());
            return null;
        }
        return arr;
    }
    
    public void outputTest(String filename, ArrayList<Hashtable<String, String>> arr) throws Exception {
    	PrintWriter outfile = new PrintWriter(filename);
    	outfile.println("drop database if exists cemetery;");
    	outfile.println("create database `cemetery`;");
    	outfile.println("use `cemetery`;");
    	outfile.println("drop table if exists `StoneRecords`;");
    	outfile.println("create table `StoneRecords` (`stoneID` smallint unsigned auto_increment not null, "
    			+ "`ROW#` varchar(4) not null, `STONE#` int(3) not null, `CASE#` int(3) not null, `GENDER` varchar(3), "
    			+ "`LAST` varchar(200), `FIRST` varchar(100), `M.I.` varchar(1), `MAIDEN` varchar(200), "
    			+ "`BIRTH YR` int(4), `BIRTH MO` int(2), `BIRTH DAY` int(2), `DEATH YR` int(4), `DEATH MO` int (2), "
    			+ "`DEATH DAY` int(2), `AGE YR` int(3), `AGE MO` int(2), `AGE DAY` int(2), `MATERIAL` varchar(2), "
    			+ "`STATUS` varchar(100), `SYMBOLS` varchar(300), `COMMENTS` varchar(300), primary key(`stoneID`));");
    	
    	for (int i = 0; i < arr.size(); i++) {
    		String s = "insert into `StoneRecords` values (0, ";
    		s += "'" + arr.get(i).get("ROW#") + "', ";
    		s += "'" + arr.get(i).get("STONE#") + "', ";
    		s += "'" + arr.get(i).get("CASE#") + "', ";
    		s += "'" + arr.get(i).get("GENDER") + "', ";
    		s += "'" + arr.get(i).get("LAST") + "', ";
    		s += "'" + arr.get(i).get("FIRST") + "', ";
    		s += "'" + arr.get(i).get("M.I.") + "', ";
    		s += "'" + arr.get(i).get("MAIDEN") + "', ";
    		s += "'" + arr.get(i).get("BIRTH YR") + "', ";
    		s += "'" + arr.get(i).get("BIRTH MO") + "', ";
    		s += "'" + arr.get(i).get("BIRTH DAY") + "', ";
    		s += "'" + arr.get(i).get("DEATH YR") + "', ";
    		s += "'" + arr.get(i).get("DEATH MO") + "', ";
    		s += "'" + arr.get(i).get("DEATH DAY") + "', ";
    		s += "'" + arr.get(i).get("AGE YR") + "', ";
    		s += "'" + arr.get(i).get("AGE MO") + "', ";
    		s += "'" + arr.get(i).get("AGE DAY") + "', ";
    		s += "'" + arr.get(i).get("MATERIAL") + "', ";
    		s += "'" + arr.get(i).get("STATUS") + "', ";
    		s += "'" + arr.get(i).get("SYMBOLS") + "', ";
    		s += "'" + arr.get(i).get("COMMENTS") + "');";
    		
    		s = s.replaceAll("'null'", "null");
    		outfile.println(s);
    	}
    	outfile.close();
    }

    public static void main(String [] args) {
      MakeSQL p = new MakeSQL();
      ArrayList <Hashtable<String,String>> arr = p.parseTSVfile("cemetery.tsv", "\t");
      System.out.println("First record's last name is " + arr.get(0).get("LAST"));
      System.out.println("First record's comments is " + arr.get(0).get("COMMENTS"));
      
      MakeSQL ft = new MakeSQL();
      try {
    	  ft.outputTest("cemetery_dump.sql", arr);
      } catch (Exception ex) {
    	  System.out.println("Exception throw by outputTest, caught in main.");
    	  System.out.println(ex.toString());
      }
    }
}

