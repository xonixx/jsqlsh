package info.xonix.sqlsh;

import info.xonix.sqlsh.store.IStore;

/**
 * User: xonix
 * Date: 5/2/14
 * Time: 8:12 PM
 */
public interface IContext {
    IStore getStore();

    ISession getSession();

    IConsole getConsole();
}
