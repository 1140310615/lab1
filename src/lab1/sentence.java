package lab1;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

/*
 * problem
 * 1. -
 * 2. ! simplify x > 0
 * 3. illegal
 */
class sentence{//+
		public Map<Integer,section> sect = new HashMap<Integer,section>();
		public static Map<Integer,Vector<Integer>> totaleles = new HashMap<Integer, Vector<Integer>>();
		sentence(String[] input, Matcher M){//+false -true
			merge(input, M);
		}
		
		public void merge(String[] input, Matcher M){
			for(int i = 0; i < input.length; i++){
				boolean B = true;
				if (i!=0) {//Õý¸ººÅ
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
		
		
		public boolean simplify(String varname, int var){
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
			return f;
		}
	 
		public boolean derivation(String varname){
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
			if (fremove & f) {
				sect.remove(tremove);
				sentence.totaleles.clear();
			}
			return f;
		}
		

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



