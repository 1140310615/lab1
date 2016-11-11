package lab1;
import static org.junit.Assert.*;
import org.junit.Test;
public class sentenceTest {
  @Test
  public void testReadexpression() {
    String exp = "7";
    String result = null;
    exp = sentence.elimispace(exp);
    sentence sent = sentence.ReadExpression(exp);
    if (sent != null){
      result = sentence.print(sent.derivation("x"));
    }else
    {
      result = "illegal expression";
    }
    assertEquals("0",result);
  }
}
