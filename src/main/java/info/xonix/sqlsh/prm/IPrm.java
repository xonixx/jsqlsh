package info.xonix.sqlsh.prm;

import java.lang.annotation.Annotation;

/**
* User: xonix
* Date: 4/25/14
* Time: 11:37 PM
*/
public interface IPrm<A extends Annotation> {
    public A getParam();

    String getName();

    String getFieldName();

    Class getParamType();

    boolean isValid(String value);

    void set(String value);
}

