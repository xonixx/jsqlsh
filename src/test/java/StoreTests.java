import info.xonix.sqlsh.store.XmlStore;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

/**
 * User: xonix
 * Date: 5/1/14
 * Time: 11:20 PM
 */
public class StoreTests {
    @Test
    public void test1() {
        XmlStore store = new XmlStore(new File("aaa.xml"));
        store.put("aaaa/bbb", "strKey", "Hello world");
        store.put("aaaa/bbb", "intKey", 123);
        HashMap<String, Integer> m = new HashMap<>();
        m.put("aaa", 111);
        m.put("bbb", 222);
        store.put("aaaa/ccc", "mapKey", m);
        store.put("aaaa/ccc", "listKey", Arrays.asList("AAA", 222, "BBB", 333));
        store.put("aaaa/ccc", "nullKey", null);
        store.pprint();

        System.out.println(store.get("aaaa/bbb", "intKey"));
        System.out.println(store.get("aaaa/ccc", "mapKey"));
        System.out.println(store.get("aaaa/ccc", "listKey"));
        System.out.println(store.get("aaaa/ccc", "nullKey"));

        store.flush();
    }
}
