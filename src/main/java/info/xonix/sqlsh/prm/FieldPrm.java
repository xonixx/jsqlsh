package info.xonix.sqlsh.prm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * User: xonix
 * Date: 4/25/14
 * Time: 11:38 PM
 */
public class FieldPrm<A extends Annotation> extends PrmAbstract<A> {
    private final Field field;
    private final Object obj;
    private final Class<A> annCls;

    public FieldPrm(Field field, Object obj, Class<A> annCls) {
        this.field = field;
        this.obj = obj;
        this.annCls = annCls;
    }

    @Override
    public A getParam() {
        return field.getAnnotation(annCls);
    }

    @Override
    public Class getParamType() {
        return field.getType();
    }

    @Override
    public void set(String value) {
        try {
            field.setAccessible(true);
            field.set(obj, tryConvert(value, getParamType()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
