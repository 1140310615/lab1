package lab1;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.regex.Pattern;

public class Control {
  sentence sent = null;
  String input = null;
  public static String print(sentence sent){
    String str = "";
    for (int i :sent.sect.keySet()){
      if(sent.sect.get(i).a<0 && str.length()>=1){
        str = str.substring(0,str.length()-1);
      }
      if (sent.sect.get(i).a != 1 || sent.sect.get(i).a != -1){str += sent.sect.get(i).a+"*";}
      boolean l = false;
      for (Map.Entry<String, Integer> entry : sent.sect.get(i).eles.entrySet()){
        String key = entry.getKey();
        int value = entry.getValue();
        if (value == 1){str += key;}else {str +=key; str+="^";str+=value;}
        str+="*";
        l = true;
      }
      if (!l && (sent.sect.get(i).a == 1 || sent.sect.get(i).a == -1)) {str += sent.sect.get(i).a+"*";}
      if (str.length()>=1){
          str = str.substring(0,str.length()-1);
          str = str+"+";
      }
    }
    if (str.length()>=1){
      str = str.substring(0,str.length()-1);
    }
    return str;
  }
  public sentence ReadExpression(String input){
    Pattern p1 = Pattern.compile("[+-]");
    Matcher M1 = p1.matcher(input);
    Pattern pp = Pattern.compile(
        "\\w+([\\^]\\d+)?(([*]\\w+)|([*]\\w+[\\^]\\d+))*([+-]\\w+([\\^]\\d+)?(([*]\\w+)|([*]\\w+[\\^]\\d+))*)*");
    Matcher MM = pp.matcher(input);
    if (MM.matches()) {
      return new sentence(input.split("[+-]"), M1);// ---------------------
    }else 
      return null;
  }
  public String run(){
    sent = ReadExpression(input);
    if(sent == null) return null;
    return print(sent);
  }//run
  public String opeder(String varname){
    if (!sent.derivation(varname)) {
      return "var doesn't exsit in derivation";
    }
    return print(sent);
  }//opeder
  public String opesim(String varname, int var){
    if (!sent.simplify(varname, var)) {
      return "var doesn't exsit in simplify";
    }
    return print(sent);
  }//opesim
}//class
