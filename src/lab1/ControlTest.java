package lab1;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControlTest {

  @Test
  public void testRun() {
    Control con = new Control();
    con.input = "x+   y+4   +e ^ 4";
    assertEquals("",con.run());
  }

}
