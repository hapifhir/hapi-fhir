package ca.uhn.fhir.jpa.dao.r4.corevalidator.utils;

import ca.uhn.fhir.parser.XmlParser;
import org.hl7.fhir.r4.model.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.List;

public class XMLUtils {

    public static void TestMethod(String loadedFile) {
        Document document = null;
        try {
            document = convertStringToXMLDocument(loadedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getBundleEntries(document);

    }

    public static List<Bundle.BundleEntryComponent> getBundleEntries(Document xmlDoc) {

        return null;
    }

    public static Document convertStringToXMLDocument(String path) throws Exception {

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(path));

        db.parse(XMLUtils.class.getClassLoader().getResourceAsStream(path));

        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("entry");

        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doccc = builder.parse(new InputSource(new StringReader(path)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
