package lab1;
import static org.junit.Assert.*;
import org.junit.Test;
public class sentenceTest {
  @Test
  public void testReadexpression() {
    String exp = "x+   y+4   +e ^ 4";
    String result = null;
    exp = sentence.elimispace(exp);
    sentence sent = sentence.ReadExpression(exp);
    if (sent != null){
      result = sentence.print(sent);
    }else
    {
      result = "illegal expression";
    }
    assertEquals("1*x+1*y+4+1*e^4",result); 
  }
}
