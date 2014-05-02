package info.xonix.sqlsh.xml;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;

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

    public static void pprint(Node node, OutputStream outputStream) {
        Transformer transformer;

        try {
            transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "2");
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

        StreamResult result = new StreamResult(outputStream);
        DOMSource source = new DOMSource(node);

        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
