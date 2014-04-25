package info.xonix.sqlsh;

import java.lang.reflect.Method;

/**
 * User: xonix
 * Date: 4/25/14
 * Time: 11:38 PM
 */
class SetterPrm extends PrmAbstract {
    private final Method setterMethod;
    private final Object obj;

    SetterPrm(Method setterMethod, Object obj) {
        this.setterMethod = setterMethod;
        this.obj = obj;
    }

    @Override
    public CommandParam getParam() {
        return setterMethod.getAnnotation(CommandParam.class);
    }

    @Override
    public Class getParamType() {
        return setterMethod.getParameterTypes()[0];
    }

    @Override
    public void set(String value) {
        try {
            setterMethod.setAccessible(true);
            setterMethod.invoke(obj, tryConvert(value,getParamType()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
