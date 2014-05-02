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
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
            Element rootElt = document.createElement("store");
            document.appendChild(rootElt);
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
        if (partIdx == pathParts.length) {
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

        if (key.contains(SLASH)) {
            throw new IllegalArgumentException("key must not contain '" + SLASH + "'");
        }

        path = fixSlashes(path, false, false);


        Element keyElt = getOrCreatePath(document.getDocumentElement(), path + SLASH + key);
        XmlUtil.removeChilds(keyElt);
        keyElt.appendChild(valueToElt(value));
    }

    private Node valueToElt(Object value) {
        Element result = null;
        if (value instanceof String) {
            String s = (String) value;
            result = document.createElement("string");
            result.appendChild(document.createTextNode(s));
            return result;
        } else if (value instanceof Integer) {
            Integer iVal = (Integer) value;
            result = document.createElement("int");
            result.appendChild(document.createTextNode(String.valueOf(iVal)));
            return result;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            result = document.createElement("map");
            for (Object o : map.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Element entryElt = document.createElement("entry");
                result.appendChild(entryElt);
                entryElt.appendChild(valueToElt(entry.getKey()));
                entryElt.appendChild(valueToElt(entry.getValue()));
            }
        } else if (value instanceof Collection) {
            Collection coll = (Collection) value;
            result = document.createElement("list");
            for (Object o : coll) {
                result.appendChild(valueToElt(o));
            }
        }
        return result;
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

    /**
     * for debug
     */
    public void pprint() {
        XmlUtil.pprint(document, System.out);
    }
}
