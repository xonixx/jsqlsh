package info.xonix.sqlsh.prm;

import info.xonix.sqlsh.ConvertUtil;
import info.xonix.sqlsh.annotations.CommandArgument;
import info.xonix.sqlsh.annotations.CommandParam;
import org.apache.commons.lang.StringUtils;

import java.lang.annotation.Annotation;

/**
 * User: xonix
 * Date: 5/10/14
 * Time: 1:57 AM
 */
abstract class PrmAbstract<A extends Annotation> implements IPrm<A> {
    @Override
    public String getName() {
        // TODO: this is dirty, need to enhance
        A param = getParam();
        String name;
        if (param instanceof CommandParam) {
            CommandParam commandParam = (CommandParam) param;
            name = commandParam.name();
        } else if (param instanceof CommandArgument) {
            CommandArgument commandArgument = (CommandArgument) param;
            name = commandArgument.name();
        } else {
            throw new IllegalStateException("not implemented");
        }
        return StringUtils.defaultIfEmpty(name, getFieldName());
    }

    @Override
    public boolean isValid(String value) {
        try {
            ConvertUtil.tryConvert(value, getParamType());
            return true;
        } catch (ConvertUtil.ConvertExc e) {
            return false;
        }
    }
}
