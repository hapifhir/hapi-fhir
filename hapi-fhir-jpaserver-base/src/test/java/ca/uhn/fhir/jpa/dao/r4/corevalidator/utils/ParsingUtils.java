package ca.uhn.fhir.jpa.dao.r4.corevalidator.utils;

import ca.uhn.fhir.rest.api.EncodingEnum;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class ParsingUtils {

    public static final String RESOURCE_TYPE = "resourceType";

    /**
     * Parses the passed in {@link String} and returns the name of the associated fhir resource type name.
     *
     * @param encoding One of {@link EncodingEnum#JSON} or {@link EncodingEnum#XML}
     * @param resourceAsString The entire resource as a {@link String}
     *
     * @return {@link String} name of the resource, eg. "Patient", "Bundle", "Group"
     *
     * @throws RuntimeException
     * @throws JsonSyntaxException
     */
    public static String getResourceName(EncodingEnum encoding, String resourceAsString) throws RuntimeException, JsonSyntaxException {
        String resourceName;
        switch (encoding) {
            case XML:
                resourceName = getResourceNameFromXML(resourceAsString);
                break;
            case JSON:
                resourceName = getResourceNameFromJSON(resourceAsString);
                break;
            default:
                throw new IllegalStateException("Passed in test file is not a supported file " +
                        "type for parsing in the test case. (Must be either XML or JSON)");
        }
        return resourceName;
    }

    /**
     * Parses the passed in JSON {@link String} and returns the name of the primitive with the label {@link ParsingUtils#RESOURCE_TYPE}
     * in the resulting {@link JsonObject}, which for our purposes, is the name of the FHIR resource.
     *
     * @param resourceAsString {@link String} resource.
     * @return {@link String} name of the resource, eg. "Patient", "Bundle", "Group"
     *
     * @throws JsonSyntaxException
     */
    protected static String getResourceNameFromJSON(String resourceAsString) throws JsonSyntaxException {
        JsonObject parsedObject = new Gson().fromJson(resourceAsString, JsonObject.class);
        return parsedObject.getAsJsonPrimitive(RESOURCE_TYPE).getAsString();
    }

    /**
     * Parses the passed in XML {@link String} and returns the name of the base element in the {@link Document},
     * which for our purposes, is the name of the FHIR resource.
     *
     * @param resourceAsString {@link String} resource.
     * @return {@link String} name of the resource, eg. "Patient", "Bundle", "Group"
     *
     * @throws RuntimeException
     */
    protected static String getResourceNameFromXML(String resourceAsString) throws RuntimeException {
        Document xmlDocument = ParsingUtils.parseXmlFile(resourceAsString);
        if (xmlDocument != null) {
            return xmlDocument.getDocumentElement().getNodeName();
        } else {
            return null;
        }
    }

    /**
     * This function converts String XML to Document object
     *
     * @param in - XML {@link String}
     * @return {@link Document} object
     */
    protected static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Takes an XML Document object and makes an XML String. A utility function for testing parsing.
     *
     * @param doc - The {@link Document}
     * @return the XML {@link String}
     */
    protected static String makeXMLString(Document doc) {
        String xmlString = "";
        if (doc != null) {
            try {
                TransformerFactory transfac = TransformerFactory.newInstance();
                Transformer trans = transfac.newTransformer();
                trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                trans.setOutputProperty(OutputKeys.INDENT, "yes");
                StringWriter sw = new StringWriter();
                StreamResult result = new StreamResult(sw);
                DOMSource source = new DOMSource(doc);
                trans.transform(source, result);
                xmlString = sw.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return xmlString;
    }
}
