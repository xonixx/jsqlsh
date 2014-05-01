package info.xonix.sqlsh.xml;

import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * User: xonix
 * Date: 5/2/14
 * Time: 1:58 AM
 */
public class XmlUtil {
    private static final DocumentBuilderFactory BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    private static final ThreadLocal<DocumentBuilder> REUSABLE_BUILDER = new ThreadLocal<DocumentBuilder>() {
        @Override
        protected DocumentBuilder initialValue() {
            try {
                return BUILDER_FACTORY.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    };

    /**
     * Fetch a builder from the pool, creating a new one only if necessary.
     */
    public static DocumentBuilder getBuilder() {
        DocumentBuilder builder = REUSABLE_BUILDER.get();
        builder.reset();
        return builder;
    }

    public static void removeChilds(Node node) {
        while (node.hasChildNodes())
            node.removeChild(node.getFirstChild());
    }
}
