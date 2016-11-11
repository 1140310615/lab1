package lab1;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/*
 * problem
 * 1. -
 * 2. ! simplify x > 0
 * 3. illegal
 */

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
public class sentence{//+
		public Map<Integer,section> sect = new HashMap<Integer,section>();
		public static Map<Integer,Vector<Integer>> totaleles = new HashMap<Integer, Vector<Integer>>();
		sentence(String[] input, Matcher M){//+false -true
			merge(input, M);
		}
		
		public void merge(String[] input, Matcher M){
			for(int i = 0; i < input.length; i++){
				boolean B = true;
				if (i!=0) {//正负号
					M.find();
					if (M.group(0).equals("-")){B=false;}
				}
				section sectnow = new section(input[i].split("[*]"),B);
				//sect.add( sectnow );
				Set<String> temp = sectnow.eles.keySet();
				if (totaleles.containsKey(temp.size())){
					boolean flag = false;
					Vector<Integer> vect = totaleles.get(temp.size());//specific size to get the Vector
					for (int index : vect ){//get the index
						if ( sect.get(index) == null) {
							break;
						}
						Set<String> S = sect.get(index).eles.keySet();//get the set
						if (SetEqual(S,temp)) {//[x y z]=[x y z] 
							boolean tflag = true;
							for (String stemp : S) //[x^2 y z^3] = [x y z]
							{
								if ( sectnow.eles.get(stemp) != sect.get(index).eles.get(stemp) ){
									tflag = false;
									break;
								}
							}
							if (tflag){
								//equal -> merge
								flag = true;
								sect.get(index).a += sectnow.a;
								//sect.remove(sectnow);
								break;
							}
						}//end set equal
					}
					if (flag == false) {//not equal
						sect.put( i, sectnow );
						totaleles.get(temp.size()).add(i);
					}
				}else{//new element
					totaleles.put(temp.size(), new Vector<Integer>() );
					totaleles.get(temp.size()).add(i);
					sect.put( i, sectnow );
				}//end if
			}//end input loop	
		}
		
		public static boolean SetEqual(Set a, Set b){
			return a.containsAll(b) && b.containsAll(a);
		}
		
		public static String elimispace(String input) throws PatternSyntaxException{
			String elimi = "[ \\t]";
			Pattern p = Pattern.compile(elimi);
			Matcher M = p.matcher(input);
			return M.replaceAll("").trim();
		}
		
		public sentence simplify(String varname, int var){
			boolean f = false;
			for (int i :sect.keySet()){
				if (sect.get(i).eles.containsKey(varname)){
					int t = sect.get(i).eles.get(varname);
					sect.get(i).a *= (int)Math.pow(var,t);
					sect.get(i).eles.remove(varname);
					sentence.totaleles.clear();
					f = true;
				}
			}
			if (!f) {
				System.out.println("var doesn't exsit in simplify");
			}
			//System.out.println(print(this));
			String inputnew = print(this);
			Pattern p = Pattern.compile("[+-]");
			Matcher M = p.matcher(inputnew);
			sentence sentnew = new sentence(inputnew.split("[+-]"),M);
			System.out.println(print(sentnew));
			return sentnew;
		}
		
		public sentence derivation(String varname){
			boolean f = false;
			boolean fremove = false;
			int tremove = 0;
			for (int i :sect.keySet()){
				if (sect.get(i).eles.isEmpty()){
					fremove = true;
					tremove = i;
				}
				if (sect.get(i).eles.containsKey(varname)){
					int t = sect.get(i).eles.get(varname);
					sect.get(i).a *= t;
					if (t == 1) sect.get(i).eles.remove(varname);
					else sect.get(i).eles.put(varname, t-1);
					sentence.totaleles.clear();
					f = true;
				}
			}
			if (fremove) {
				sect.remove(tremove);
				sentence.totaleles.clear();
			}
			if (!f) {
				System.out.println("var doesn't exsit in derivation");
			}
			//System.out.println(print(this));
			String inputnew = print(this);
			Pattern p = Pattern.compile("[+-]");
			Matcher M = p.matcher(inputnew);
			sentence sentnew = new sentence(inputnew.split("[+-]"),M);
			System.out.println(print(sentnew));
			return sentnew;
		}
		
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
		
	public static sentence ReadExpression(String input){
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
	
	public static void main(String[] args) {
		while (true) {
			System.out.println("input the expression:");
			String input = null;
			Scanner scan = new Scanner(System.in);
			input = scan.nextLine();

			long startMili = System.currentTimeMillis();
			System.out.println("执行开始时间：" + startMili);

			input = elimispace(input);
			sentence sent = ReadExpression(input);// ---------------------
			
			if (sent != null) {	
				System.out.println(print(sent));
				// System.out.println("input the instruction:");
				input = scan.nextLine();
				if (input.length() > 9 && input.substring(0, 9).equals("!simplify")) {// simplify
					input = input.substring(10, input.length());
					Pattern p = Pattern.compile("(\\w+)=(\\d+)");
					Matcher M = p.matcher(input);
					while (M.find()) {
						String varname = M.group(1);
						int var = Integer.parseInt(M.group(2));
						sent = sent.simplify(varname, var);// return new sent
					} // end while
				} else if (input.length() > 4 && input.substring(0, 4).equals("!d/d")) {// derivation
					input = input.substring(5, input.length());
					sent.derivation(input);
				} else {
					System.out.println("invilid input");
				}
			} // illegal expression
			else {
				System.out.println("illegal expression");
			}
			long endMili = System.currentTimeMillis();
			System.out.println("结束时间：" + endMili);
			System.out.println("执行总时间：" + (endMili - startMili) + "毫秒");
		} // end while
	}// main
}//class sentense

/*input instance
x+2*x+5+6+y+4*y+d+3*d
!simplify y=3

x+2*x+5+6+y+4*y+d+3*d
!d/d d

-----------------------
x*y*2+35*z 
!simplify x=2 y=3 z=4

x*y*2+35*z 
!d/d y

-----------------------
x*w*s+3+3
!simplify y=2

x*w*s+3+3
!d/d y

-----------------------
x*g*s%@!^#!%&

-----------------------
1+1+2
!simplify y=2

1+1+2
!d/d y

-----------------------

=-.=-==.=.=..=-.===-.=.-.....-=.-=-.-=.-=.=-.=-.-.=
x+2x+x^4+x*x*x*x
!simplify x=2

x+2x+x^4+x*x*x*x
!d/d x

-----------------------
xyz+x^2*y*z^3+6xyz+df+3
!simplify x=2

xyz+x^2*y*z^3+6xyz+df+3
!d/d xyz

-----------------------
2+x+2x+2x^4+sdf+sdf*gf^5*2x+x*x*x*x+x
!simplify x=1 sdf=2

2+x+2x+2x^4+sdf+sdf*gf^5*2x+x*x*x*x+x
!d/d x

-----------------------
2+x+2 x ^ 3 +x 	yz+e
!simplify x=1

2+x+2 x ^ 3 +x 	yz+e
!d/d x

-----------------------

4-8x-2x-3y
!simplify x=4

4-8x-2x-3y
!d/d y

*/



