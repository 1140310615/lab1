package lab1;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class section
{
  public int a = 1;
  //element[] eles;
  public Map<String,Integer> eles = new HashMap<String, Integer>();
  section(String[] input, boolean B){//* B + true - false
    Pattern pnum = Pattern.compile("\\d+");
    Pattern pword = Pattern.compile("\\w+");
    Matcher M,N;
    for (int i = 0; i < input.length; i++){
      M = pnum.matcher(input[i]);
      if (M.matches()){
        if(B) {a = a*Integer.parseInt(input[i]);}else{a = -1*a*Integer.parseInt(input[i]);}
      }// num alone
      else//2x^4 2x x^4 x
      {
        M = pnum.matcher(input[i]);
        if (M.find()){//2x^4 2x
          if (M.start() == 0 ){
            if (B){a = a*Integer.parseInt(M.group());}else{a = -1*a*Integer.parseInt(M.group());}
            input[i] = input[i].substring(M.end(), input[i].length());
          }
        }
        //x^4 x 
        M = pnum.matcher(input[i]);
        N = pword.matcher(input[i]);
        N.find();
        int times = 1;
        if (M.find()){//x^4
          times = Integer.parseInt(M.group(0));
        }
        //x
        //element = input[i]
        try{
          if ( eles.containsKey(N.group(0)) ){ //x*x*x x^4*x*x
            eles.put(N.group(0), eles.get(N.group(0))+times ); 
          }else{
            eles.put(N.group(0), times);
          }
        }catch(IllegalStateException e){
          break;
        }
      }
    }
  }
}