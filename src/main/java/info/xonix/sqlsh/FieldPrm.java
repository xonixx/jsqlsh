package info.xonix.sqlsh;

import java.lang.reflect.Field;

/**
 * User: xonix
 * Date: 4/25/14
 * Time: 11:38 PM
 */
class FieldPrm extends PrmAbstract {
    private final Field field;
    private Object obj;

    public FieldPrm(Field field, Object obj) {
        this.field = field;
        this.obj = obj;
    }

    @Override
    public CommandParam getParam() {
        return field.getAnnotation(CommandParam.class);
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
