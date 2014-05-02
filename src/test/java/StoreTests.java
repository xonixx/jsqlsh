import info.xonix.sqlsh.store.XmlStore;
import org.joox.Match;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import static org.joox.JOOX.$;

/**
 * User: xonix
 * Date: 5/1/14
 * Time: 11:20 PM
 */
public class StoreTests {
    @Test
    public void test1() {
        XmlStore xmlStore = new XmlStore(new File("aaa.xml"));
        xmlStore.put("aaaa/bbb", "strKey", "Hello world");
        xmlStore.put("aaaa/bbb", "intKey", 123);
        HashMap<String, Integer> m = new HashMap<>();
        m.put("aaa", 111);
        m.put("bbb", 222);
        xmlStore.put("aaaa/ccc", "mapKey", m);
        xmlStore.put("aaaa/ccc", "listKey", Arrays.asList("AAA",222,"BBB",333));
        xmlStore.pprint();
    }
}
