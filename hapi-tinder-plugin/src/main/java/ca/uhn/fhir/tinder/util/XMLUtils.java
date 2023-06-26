
package ca.uhn.fhir.tinder.util;

import ca.uhn.fhir.i18n.Msg;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSResourceResolver;
import org.w3c.dom.ls.LSSerializer;

public class XMLUtils {

    private static DOMImplementation IMPL;

    @SuppressWarnings("unchecked")
    public synchronized static <T> T getDOMImpl() {
        if (IMPL == null) {
            try {
                DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
                IMPL = registry.getDOMImplementation("LS 3.0");
            } catch (Exception e) {
                throw new RuntimeException(Msg.code(149) + e);
            }
        }
        return (T) IMPL;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getDOMImplUncached() {
        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            return (T) registry.getDOMImplementation("LS 3.0");
        } catch (Exception e) {
            throw new RuntimeException(Msg.code(150) + e);
        }
    }

    public static Document parse(String s) {
        return parse(s, false);
    }

    public static Document parse(String s, boolean validateIfSchema) {
        DOMImplementationLS impl = getDOMImpl();
        LSInput input = impl.createLSInput();
        input.setStringData(s);
        return parse(input, validateIfSchema);
    }

    public static Document parse(InputStream s, boolean validateIfSchema) {
        DOMImplementationLS impl = getDOMImpl();
        LSInput input = impl.createLSInput();
        input.setByteStream(s);
        return parse(input, validateIfSchema);
    }

    private static Document parse(LSInput input, boolean validateIfSchema) {
        DOMImplementationLS impl = getDOMImpl();
        LSParser parser = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
        DOMConfiguration config = parser.getDomConfig();
        config.setParameter("element-content-whitespace", false);
        config.setParameter("namespaces", true);
        config.setParameter("validate-if-schema", validateIfSchema);
        return parser.parse(input);
    }

    public static void validate(Document d, String schema, DOMErrorHandler handler) {
        DOMConfiguration config = d.getDomConfig();
        config.setParameter("schema-type", "http://www.w3.org/2001/XMLSchema");
        config.setParameter("validate", true);
        config.setParameter("schema-location", schema);
        config.setParameter("resource-resolver", new ClasspathResourceResolver());
        config.setParameter("error-handler", handler);
        d.normalizeDocument();
    }

    public static String serialize(Document document, boolean prettyPrint) {
        DOMImplementationLS impl = getDOMImpl();
        LSSerializer serializer = impl.createLSSerializer();
        // document.normalizeDocument();
        DOMConfiguration config = serializer.getDomConfig();
        if (prettyPrint && config.canSetParameter("format-pretty-print", Boolean.TRUE)) {
            config.setParameter("format-pretty-print", true);
        }
        config.setParameter("xml-declaration", true);        
        LSOutput output = impl.createLSOutput();
        output.setEncoding("UTF-8");
        Writer writer = new StringWriter();
        output.setCharacterStream(writer);
        serializer.write(document, output);
        return writer.toString();
    }

    public static Document emptyDocument(String title) {
        DOMImplementation impl = getDOMImpl();
        Document doc = impl.createDocument("urn:hl7-org:v2xml", title, null);
        return doc;
    }

    /**
     * This is an implementation of LSResourceResolver that can resolve XML schemas from the
     * classpath
     */
    private static class ClasspathResourceResolver implements LSResourceResolver {
        private DOMImplementationLS impl;

        ClasspathResourceResolver() {
            impl = getDOMImpl();
        }

        @Override
		  public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId,
													String baseURI) {
            LSInput lsInput = impl.createLSInput();
            InputStream is = getClass().getResourceAsStream("/" + systemId);
            if (is == null)
                return null;
            lsInput.setByteStream(is);
            return lsInput;
        }
    }

}
