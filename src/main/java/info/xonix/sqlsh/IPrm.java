package info.xonix.sqlsh;

/**
* User: xonix
* Date: 4/25/14
* Time: 11:37 PM
*/
public interface IPrm {
    public CommandParam getParam();

    Class getParamType();

    boolean isValid(String value);

    void set(String value);
}

abstract class PrmAbstract implements IPrm {
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
