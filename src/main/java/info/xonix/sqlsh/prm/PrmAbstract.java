package info.xonix.sqlsh.prm;

import java.lang.annotation.Annotation;

/**
 * User: xonix
 * Date: 5/10/14
 * Time: 1:57 AM
 */
abstract class PrmAbstract<A extends Annotation> implements IPrm<A> {
    private static class ConvertExc extends Exception {}

    @Override
    public boolean isValid(String value) {
        try {
            tryConvert(value, getParamType());
            return true;
        } catch (ConvertExc e) {
            return false;
        }
    }

    protected static Object tryConvert(String value, Class toType) throws ConvertExc {
        if (String.class.isAssignableFrom(toType)) {
            return value;
        }

        if (Integer.class.isAssignableFrom(toType) || toType == int.class) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException e) {
                throw new ConvertExc();
            }
        }

        if (Boolean.class.isAssignableFrom(toType) || toType == boolean.class) {
            if (value == null || "1".equals(value) || "true".equals(value)) {
                return true;
            } else if ("0".equals(value) || "false".equals(value)) {
                return false;
            } else {
                throw new ConvertExc();
            }
        }

        throw new ConvertExc();
    }
}
