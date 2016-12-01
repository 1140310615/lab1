package lab1;

import java.util.regex.Matcher;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
public class CommandLine {
  public static String elimispace(String input) throws PatternSyntaxException{
    String elimi = "[ \\t]";
    Pattern p = Pattern.compile(elimi);
    Matcher M = p.matcher(input);
    return M.replaceAll("").trim();
  }
  public static void main(String[] args){
    while (true){
      String input = null;
      Scanner scan = new Scanner(System.in);
      Control con = new Control();
      String out = null;
      
      do{
        System.out.println("input the expression:");
        input = scan.nextLine();
        input = elimispace(input);
        con.input = input;
        out = con.run();
        if (out == null)
          System.out.println("illegal expression");
        else {
          System.out.println(out);
          break;
        }
      } while (true);
      
      do{
        input = scan.nextLine();
        if (input.length() > 9 && input.substring(0, 9).equals("!simplify")) {// simplify
          input = input.substring(10, input.length());
          Pattern p = Pattern.compile("(\\w+)=(\\d+)");
          Matcher M = p.matcher(input);
          while (M.find()) {
            String varname = M.group(1);
            int var = Integer.parseInt(M.group(2));
            System.out.println(con.opesim(varname, var));
          } // end while
        } else if (input.length() > 4 && input.substring(0, 4).equals("!d/d")) {// derivation
          input = input.substring(5, input.length());
          System.out.println(con.opeder(input));
        } else {
          System.out.println("invilid input");
        }
      }while (true);
      
    }//end while
  }
}
