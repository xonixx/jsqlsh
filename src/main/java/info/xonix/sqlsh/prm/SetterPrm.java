package info.xonix.sqlsh.prm;

import info.xonix.sqlsh.ConvertUtil;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * User: xonix
 * Date: 4/25/14
 * Time: 11:38 PM
 */
public class SetterPrm<A extends Annotation> extends PrmAbstract<A> {
    private final Method setterMethod;
    private final Object obj;
    private final Class<A> annCls;

    public SetterPrm(Method setterMethod, Object obj, Class<A> annCls) {
        this.setterMethod = setterMethod;
        this.obj = obj;
        this.annCls = annCls;
    }

    @Override
    public A getParam() {
        return setterMethod.getAnnotation(annCls);
    }

    @Override
    public Class getParamType() {
        return setterMethod.getParameterTypes()[0];
    }

    @Override
    public String getFieldName() {
        return StringUtils.uncapitalize(setterMethod.getName().substring(3));
    }

    @Override
    public void set(String value) {
        try {
            setterMethod.setAccessible(true);
            setterMethod.invoke(obj, ConvertUtil.tryConvert(value, getParamType()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
