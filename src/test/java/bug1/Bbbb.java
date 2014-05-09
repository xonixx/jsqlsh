package bug1;

import info.xonix.sqlsh.annotations.Command;

import java.util.ArrayList;

/**
 * User: xonix
 * Date: 4/26/14
 * Time: 1:01 AM
 */
@Command(name = "",description = "")
public class Bbbb {
    void m() {
        new ArrayList<>().sort((a,b)->0);
    }
}
