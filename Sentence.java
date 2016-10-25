package lab1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/*
 * problem
 * 1. -
 * 2. ! simplify x > 0
 * 3. illegal
 */
// modify is here
class Section {
	public int a = 1;
	public Map<String, Integer> eles = new HashMap<String, Integer>();

	Section(final String[] input, boolean b) {// * B + true - false
		Pattern pnum = Pattern.compile("\\d+");
		Pattern pword = Pattern.compile("\\w+");
		Matcher m;
		Matcher n;
		for (int i = 0; i < input.length; i++) {
			m = pnum.matcher(input[i]);
			if (m.matches()) {
				if (b) {
					a = a * Integer.parseInt(input[i]);
				} else {
					a = -1 * a * Integer.parseInt(input[i]);
				}
			} // num alone
			else {
				m = pnum.matcher(input[i]);
				if (m.find()) { // 2x^4 2x
					if (m.start() == 0) {
						if (b) {
							a = a * Integer.parseInt(m.group());
						} else {
							a = -1 * a * Integer.parseInt(m.group());
						}
						input[i] = input[i].substring(m.end(), input[i].length());
						
					}
				}
				// x^4 x
				m = pnum.matcher(input[i]);
				n = pword.matcher(input[i]);
				n.find();
				int times = 1;
				if (m.find()) { // x^4
					times = Integer.parseInt(m.group(0));
				}
				// x
				// element = input[i]
				try {
					if (eles.containsKey(n.group(0))) { // x*x*x x^4*x*x
						eles.put(n.group(0), eles.get(n.group(0)) + times);
					} else {
						eles.put(n.group(0), times);
					}
				} catch (IllegalStateException e) {
					break;
				}
			}
		}
	}
}

public class Sentence { // +
	public Map<Integer, Section> sect = new HashMap<Integer, Section>();
	public static Map<Integer, Vector<Integer>> totaleles = new HashMap<Integer, Vector<Integer>>();
	

	Sentence(String[] input, Matcher m) { // +false -true
		merge(input, m);
	}

	public final void merge(final String[] input, Matcher m) {
		for (int i = 0; i < input.length; i++) {
			boolean b = true;
			if (i != 0) { // 姝ｈ礋鍙�?
				m.find();
				if (m.group(0).equals("-")) {
					b = false;
				}
			}
			Section sectnow = new Section(input[i].split("[*]"), b);
			// sect.add( sectnow );
			Set<String> temp = sectnow.eles.keySet();
			if (totaleles.containsKey(temp.size())) {
				boolean flag = false;
				Vector<Integer> vect = totaleles.get(temp.size());// specific
																	// size to
																	// get the
																	// Vector
				for (int index : vect) { // get the index
					if (sect.get(index) == null) {
						break;
					}
					Set<String> s = sect.get(index).eles.keySet();// get the set
					if (setEqual(s, temp)) {// [x y z]=[x y z]
						boolean tflag = true;
						for (String stemp : s) // [x^2 y z^3] = [x y z]
						{
							if (!sectnow.eles.get(stemp).equals(sect.get(index).eles.get(stemp))) {
								tflag = false;
								break;
							}
						}
						if (tflag) {
							// equal -> merge
							flag = true;
							sect.get(index).a += sectnow.a;
							// sect.remove(sectnow);
							break;
						}
					} // end set equal
				}
				if (flag == false) {// not equal
					sect.put(i, sectnow);
					totaleles.get(temp.size()).add(i);
				}
			} else {// new element
				totaleles.put(temp.size(), new Vector<Integer>());
				totaleles.get(temp.size()).add(i);
				sect.put(i, sectnow);
			} // end if
		} // end input loop
	}

	public static boolean setEqual(Set a, Set b) {
		return a.containsAll(b) && b.containsAll(a);
	}

	public static String elimispace(String input) throws PatternSyntaxException {
		String elimi = "[ \\t]";
		Pattern p = Pattern.compile(elimi);
		Matcher m = p.matcher(input);
		return m.replaceAll("").trim();
	}

	public Sentence simplify(String varname, int var) {
		boolean f = false;
		for (int i : sect.keySet()) {
			if (sect.get(i).eles.containsKey(varname)) {
				int t = sect.get(i).eles.get(varname);
				sect.get(i).a *= (int) Math.pow(var, t);
				sect.get(i).eles.remove(varname);
				Sentence.totaleles.clear();
				f = true;
			}
		}
		if (!f) {
			System.out.println("var doesn't exsit in simplify");
		}
		// System.out.println(print(this));
		String inputnew = print(this);
		Pattern p = Pattern.compile("[+-]");
		Matcher m = p.matcher(inputnew);
		Sentence sentnew = new Sentence(inputnew.split("[+-]"), m);
		System.out.println(print(sentnew));
		return sentnew;
	}

	public Sentence derivation(String varname) {
		boolean f = false;
		boolean fremove = false;
		int tremove = 0;
		for (int i : sect.keySet()) {
			if (sect.get(i).eles.isEmpty()) {
				fremove = true;
				tremove = i;
			}
			if (sect.get(i).eles.containsKey(varname)) {
				int t = sect.get(i).eles.get(varname);
				sect.get(i).a *= t;
				if (t == 1)
					sect.get(i).eles.remove(varname);
				else
					sect.get(i).eles.put(varname, t - 1);
				Sentence.totaleles.clear();
				f = true;
			}
		}
		if (fremove) {
			sect.remove(tremove);
			Sentence.totaleles.clear();
		}
		if (!f) {
			System.out.println("var doesn't exsit in derivation");
		}
		// System.out.println(print(this));
		String inputnew = print(this);
		Pattern p = Pattern.compile("[+-]");
		Matcher m = p.matcher(inputnew);
		Sentence sentnew = new Sentence(inputnew.split("[+-]"), m);
		System.out.println(print(sentnew));
		return sentnew;
	}

	public static String print(Sentence sent) {
		String str = "";
		for (int i : sent.sect.keySet()) {
			if (sent.sect.get(i).a < 0 && str.length() >= 1) {
				str = str.substring(0, str.length() - 1);
			}
			if (sent.sect.get(i).a != 1 || sent.sect.get(i).a != -1) {
				str += sent.sect.get(i).a + "*";
			}
			boolean l = false;
			for (Map.Entry<String, Integer> entry : sent.sect.get(i).eles.entrySet()) {
				String key = entry.getKey();
				int value = entry.getValue();
				if (value == 1) {
					str += key;
				} else {
					str += key;
					str += "^";
					str += value;
				}
				str += "*";
				l = true;
			}
			if (!l && (sent.sect.get(i).a == 1 || sent.sect.get(i).a == -1)) {
				str += sent.sect.get(i).a + "*";
			}
			if (str.length() >= 1) {
				str = str.substring(0, str.length() - 1);
				str = str + "+";
			}
		}
		if (str.length() >= 1) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public static void main(String[] args) {
		while (true) {
			System.out.println("input the expression:");
			String input = null;
			Scanner scan = new Scanner(System.in);
			input = scan.nextLine();

			long startMili = System.currentTimeMillis();
			System.out.println("鎵ц寮�濮嬫椂闂达細" + startMili);

			input = elimispace(input);
			Pattern pp = Pattern.compile(
					"\\w+([\\^]\\d+)?(([*]\\w+)|([*]\\w+[\\^]\\d+))*([+-]\\w+([\\^]\\d+)?(([*]\\w+)|([*]\\w+[\\^]\\d+))*)*");
			Matcher mm = pp.matcher(input);
			if (mm.matches()) {
				Pattern p1 = Pattern.compile("[+-]");
				Matcher m1 = p1.matcher(input);
				Sentence sent = new Sentence(input.split("[+-]"), m1);// ---------------------
				System.out.println(print(sent));

				// System.out.println("input the instruction:");
				input = scan.nextLine();
				if (input.length() > 9 && input.substring(0, 9).equals("!simplify")) { // simplify
					
					input = input.substring(10, input.length());
					Pattern p = Pattern.compile("(\\w+)=(\\d+)");
					Matcher m = p.matcher(input);
					while (m.find()) {
						String varname = m.group(1);
						int var = Integer.parseInt(m.group(2));
						sent = sent.simplify(varname, var);// return new sent
					} // end while
				} else if (input.length() > 4 && input.substring(0, 4).equals("!d/d")) { // derivation
					
					input = input.substring(4, input.length());
					sent.derivation(input);
				} else {
					System.out.println("invilid input");
				}
			} // illegal expression
			else {
				System.out.println("illegal expression");
			}
			long endMili = System.currentTimeMillis();
			System.out.println("缁撴潫鏃堕棿锛�" + endMili);
			System.out.println("鎵ц鎬绘椂闂达細" + (endMili - startMili) + "姣�?");
			
		} // end while
	} // main
} // class sentense

/*
 * input instance x+2*x+5+6+y+4*y+d+3*d !simplify y=3
 * 
 * x+2*x+5+6+y+4*y+d+3*d !d/dd
 * 
 * ----------------------- x*y*2+35*z !simplify x=2 y=3 z=4
 * 
 * x*y*2+35*z !d/dy
 * 
 * ----------------------- x*w*s+3+3 !simplify y=2
 * 
 * x*w*s+3+3 !d/dy
 * 
 * ----------------------- x*g*s%@!^#!%&
 * 
 * ----------------------- 1+1+2 !simplify y=2
 * 
 * 1+1+2 !d/dy
 * 
 * -----------------------
 * 
 * =-.=-==.=.=..=-.===-.=.-.....-=.-=-.-=.-=.=-.=-.-.= x+2x+x^4+x*x*x*x
 * !simplify x=2
 * 
 * x+2x+x^4+x*x*x*x !d/dx
 * 
 * ----------------------- xyz+x^2*y*z^3+6xyz+df+3 !simplify x=2
 * 
 * xyz+x^2*y*z^3+6xyz+df+3 !d/dxyz
 * 
 * ----------------------- 2+x+2x+2x^4+sdf+sdf*gf^5*2x+x*x*x*x+x !simplify x=1
 * sdf=2
 * 
 * 2+x+2x+2x^4+sdf+sdf*gf^5*2x+x*x*x*x+x !d/dx
 * 
 * ----------------------- 2+x+2 x ^ 3 +x yz+e !simplify x=1
 * 
 * 2+x+2 x ^ 3 +x yz+e !d/dx
 * 
 * -----------------------
 * 
 * 4-8x-2x-3y !simplify x=4
 * 
 * 4-8x-2x-3y !d/dy
 * 
 */
