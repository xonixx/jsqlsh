package info.xonix.sqlsh.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.LinkedList;
import java.util.List;

/**
 * User: gubarkov
 * Date: 11.07.11
 * Time: 19:56
 */
public class XPathUtil {
    public static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();

    public static NodeList nodeset(Object item, XPathExpression expr) {
        try {
            return (NodeList) expr.evaluate(item, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param item Document or Node
     * @param expr xpath
     * @return result as Node
     */
    public static Node node(Object item, XPathExpression expr) {
        try {
            return (Node) expr.evaluate(item, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param item Document or Node
     * @param expr xpath
     * @return as String
     */
    public static String string(Object item, XPathExpression expr) {
        try {
            return (String) expr.evaluate(item, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> stringList(Object item, XPathExpression expr) {
        final NodeList nodeList = XPathUtil.nodeset(item, expr);

        final List<String> result = new LinkedList<String>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node node = nodeList.item(i);

            result.add(node.getNodeValue());
        }
        return result;
    }

    public static XPathExpression compile(String expr) {
        try {
            return XPATH_FACTORY.newXPath().compile(expr);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
