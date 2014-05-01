package info.xonix.sqlsh.store;

import info.xonix.sqlsh.xml.XmlUtil;
import org.joox.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * User: xonix
 * Date: 5/1/14
 * Time: 10:16 PM
 */
public class XmlStore implements IStore {
    public static final String SLASH = "/";
    private final Document document;

    public XmlStore(File file) {
        if (file.exists()) {
            try {
                document = XmlUtil.getBuilder().parse(file);
            } catch (SAXException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            document = XmlUtil.getBuilder().newDocument();
        }
    }

    private static String fixSlashes(String path, boolean atStart, boolean atEnd) {
        if (atStart) {
            if (!path.startsWith(SLASH)) {
                path = SLASH + path;
            }
        } else {
            if (path.startsWith(SLASH)) {
                path = path.substring(1);
            }
        }

        if (atEnd) {
            if (!path.endsWith(SLASH)) {
                path += SLASH;
            }
        } else {
            if (path.endsWith(SLASH)) {
                path = path.substring(0, path.length() - 1);
            }
        }

        return path;
    }

    private Element getOrCreatePath(Element element, String path) {
        return getOrCreatePath(element, path.split(SLASH), 0);
    }

    private Element getOrCreatePath(Element element, String[] pathParts, int partIdx) {
        if (partIdx + 1 == pathParts.length) {
            return element;
        }

        String part = pathParts[partIdx];

        NodeList childNodes = element.getChildNodes();
        for (int i = 0, l = childNodes.getLength(); i < l; i++) {
            Element elt = (Element) childNodes.item(i);
            if (elt.getTagName().equals(part)) {
                return getOrCreatePath(elt, pathParts, partIdx + 1);
            }
        }
        // no path elt -> create
        Element elt = document.createElement(part);
        element.appendChild(elt);
        return getOrCreatePath(elt, pathParts, partIdx + 1);
    }

    @Override
    public void put(String path, String key, Object value) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        path = fixSlashes(path, false, false);
        key = fixSlashes(key, false, false);

//        if (exists(path, key)) {
//            delete(path, key);
//        }

        Element keyElt = getOrCreatePath(document.getDocumentElement(), path + SLASH + key);
        XmlUtil.removeChilds(keyElt);
        keyElt.appendChild(valueToElt(value));
    }

    private Node valueToElt(Object value) {
        return null;
    }

    @Override
    public void delete(String path, String key) {
    }

    @Override
    public Object get(String path, String key) {
        return null;
    }

    @Override
    public void flush() {
    }

    @Override
    public boolean exists(String path, String key) {
        return false;
    }

    @Override
    public List<String> listKeys(String path) {
        return null;
    }

    @Override
    public List<StoreElement> list(String path) {
        return null;
    }
}
