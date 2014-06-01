package info.xonix.sqlsh;

/**
 * User: xonix
 * Date: 6/1/14
 * Time: 8:02 PM
 */
public class ConvertUtil {
    public static class ConvertExc extends Exception {}

    public static Object tryConvert(String value, Class toType) throws ConvertExc {
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
            if (value == null || "".equals(value) || "1".equals(value) || "true".equals(value)) {
                return true;
            } else if ("0".equals(value) || "false".equals(value)) {
                return false;
            } else {
                throw new ConvertExc();
            }
        }

        throw new ConvertExc();
    }

    private ConvertUtil() {
    }
}
