import org.joox.Match;
import org.junit.Test;

import static org.joox.JOOX.$;

/**
 * User: xonix
 * Date: 5/1/14
 * Time: 11:20 PM
 */
public class JooxTests {
    @Test
    public void test1() {
        Match doc = $();
//        doc = doc.add($("a", $("b", "111")));
        doc = doc.add($("a/b/c", $("d", "111")));
        System.out.println(doc);
    }
}
