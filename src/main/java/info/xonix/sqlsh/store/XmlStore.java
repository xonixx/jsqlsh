package info.xonix.sqlsh.store;

import info.xonix.sqlsh.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.*;

/**
 * User: xonix
 * Date: 5/1/14
 * Time: 10:16 PM
 */
public class XmlStore implements IStore {
    public static final String SLASH = "/";
    public static final String TAG_STRING = "string";
    public static final String TAG_INT = "int";
    public static final String TAG_MAP = "map";
    public static final String TAG_ENTRY = "entry";
    public static final String TAG_LIST = "list";
    public static final String TAG_NULL = "null";

    private final Document document;
    private final File file;

    public XmlStore(File file) {
        this.file = file;
        if (this.file.exists()) {
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
//        document.setXmlStandalone(true);
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

    private Element getOrCreateElement(String path, String key) {
        path = checkAndBuildFullPath(path, key);
        return getEltByPath(document.getDocumentElement(), path.split(SLASH), 0, true);
    }

    private Element getElement(String path, String key) {
        path = checkAndBuildFullPath(path, key);
        return getEltByPath(document.getDocumentElement(), path.split(SLASH), 0, false);
    }

    private Element getPathElement(String path) {
        Objects.requireNonNull(path);
        path = fixSlashes(path, false, false);

        return getEltByPath(document.getDocumentElement(), path.split(SLASH), 0, false);
    }

    private Element getEltByPath(Element element, String[] pathParts, int partIdx, boolean create) {
        if (partIdx == pathParts.length) {
            return element;
        }

        String part = pathParts[partIdx];

        NodeList childNodes = element.getChildNodes();
        for (int i = 0, l = childNodes.getLength(); i < l; i++) {
            Element elt = (Element) childNodes.item(i);
            if (elt.getTagName().equals(part)) {
                return getEltByPath(elt, pathParts, partIdx + 1, create);
            }
        }
        if (create) {
            // no path elt -> create
            Element elt = document.createElement(part);
            element.appendChild(elt);
            return getEltByPath(elt, pathParts, partIdx + 1, create);
        } else {
            return null;
        }
    }

    @Override
    public void put(String path, String key, Object value) {
        Element keyElt = getOrCreateElement(path, key);
        XmlUtil.removeChilds(keyElt);
        keyElt.appendChild(valueToElt(value));
    }

    private String checkAndBuildFullPath(String path, String key) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(key);

        if (key.contains(SLASH)) {
            throw new IllegalArgumentException("key must not contain '" + SLASH + "'");
        }

        path = fixSlashes(path, false, false);
        return path + SLASH + key;
    }

    private Node valueToElt(Object value) {
        Element result;
        if (value instanceof String) {
            String s = (String) value;
            result = document.createElement(TAG_STRING);
            result.appendChild(document.createTextNode(s));
            return result;
        } else if (value instanceof Integer) {
            Integer iVal = (Integer) value;
            result = document.createElement(TAG_INT);
            result.appendChild(document.createTextNode(String.valueOf(iVal)));
            return result;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            result = document.createElement(TAG_MAP);
            for (Object o : map.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Element entryElt = document.createElement(TAG_ENTRY);
                result.appendChild(entryElt);
                entryElt.appendChild(valueToElt(entry.getKey()));
                entryElt.appendChild(valueToElt(entry.getValue()));
            }
        } else if (value instanceof Collection) {
            Collection coll = (Collection) value;
            result = document.createElement(TAG_LIST);
            for (Object o : coll) {
                result.appendChild(valueToElt(o));
            }
        } else if (value == null) {
            result = document.createElement(TAG_NULL);
        } else {
            throw new IllegalArgumentException("Value type not supported: " + value);
        }
        return result;
    }

    private Object eltToValue(Element element) {
        Objects.requireNonNull(element);

        String tagName = element.getTagName();

        Object res;

        if (TAG_STRING.equals(tagName)) {
            res = element.getTextContent();
        } else if (TAG_INT.equals(tagName)) {
            res = Integer.valueOf(element.getTextContent());
        } else if (TAG_NULL.equals(tagName)) {
            res = null;
        } else if (TAG_MAP.equals(tagName)) {
            Map<Object, Object> m = new LinkedHashMap<>();
            NodeList childNodes = element.getChildNodes();
            for (int i = 0, l = childNodes.getLength(); i < l; i++) {
                Element entry = (Element) childNodes.item(i);
                if (!TAG_ENTRY.equals(entry.getTagName())) {
                    throw new IllegalArgumentException("not a map entry: " + entry.getTagName());
                }
                NodeList keyVal = entry.getChildNodes();
                m.put(eltToValue((Element) keyVal.item(0)), eltToValue((Element) keyVal.item(1)));
            }
            res = m;
        } else if (TAG_LIST.equals(tagName)) {
            List<Object> list = new ArrayList<>();

            NodeList childNodes = element.getChildNodes();
            for (int i = 0, l = childNodes.getLength(); i < l; i++) {
                Element item = (Element) childNodes.item(i);
                list.add(eltToValue(item));
            }

            res = list;
        } else {
            throw new IllegalArgumentException("Unknown value type: " + tagName);
        }

        return res;
    }

    @Override
    public boolean delete(String path, String key) {
        Element elt = getElement(path, key);
        if (elt != null) {
            elt.getParentNode().removeChild(elt);
            return true;
        }
        return false;
    }

    @Override
    public Object get(String path, String key) {
        Element elt = getElement(path, key);
        if (elt != null) {
            return eltToValue((Element) elt.getFirstChild());
        }
        return null;
    }

    @Override
    public void flush() {
        try(OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            XmlUtil.pprint(document, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exists(String path, String key) {
        Element elt = getElement(path, key);
        return elt != null;
    }

    @Override
    public List<String> listKeys(String path) {
        Element elt = getPathElement(path);
        if (elt == null) {
            return null;
        }

        List<String> res = new ArrayList<>();
        NodeList childNodes = elt.getChildNodes();
        for (int i = 0, l = childNodes.getLength(); i < l; i++) {
            Element item = (Element) childNodes.item(i);
            res.add(item.getTagName());
        }

        return res;
    }

    @Override
    public List<StoreElement> list(String path) {
        Element elt = getPathElement(path);
        if (elt == null) {
            return null;
        }

        List<StoreElement> res = new ArrayList<>();
        NodeList childNodes = elt.getChildNodes();
        for (int i = 0, l = childNodes.getLength(); i < l; i++) {
            Element item = (Element) childNodes.item(i);
            res.add(new StoreElement(
                    item.getTagName(),
                    eltToValue((Element) item.getFirstChild())));
        }

        return res;
    }

    /**
     * for debug
     */
    public void pprint() {
        XmlUtil.pprint(document, System.out);
    }
}
