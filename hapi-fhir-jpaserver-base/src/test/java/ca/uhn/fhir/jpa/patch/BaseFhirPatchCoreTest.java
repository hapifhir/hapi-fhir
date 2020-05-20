package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.XmlUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public abstract class BaseFhirPatchCoreTest extends BaseTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseFhirPatchCoreTest.class);
	private final String myName;
	private final String myMode;
	private final IBaseResource myInput;
	private final IBaseResource myPatch;
	private final IBaseResource myOutput;

	public BaseFhirPatchCoreTest(String theName, String theMode, IBaseResource theInput, IBaseResource thePatch, IBaseResource theOutput) {
		myName = theName;
		myMode = theMode;
		myInput = theInput;
		myPatch = thePatch;
		myOutput = theOutput;
	}

	@Test
	public void testApply() {
		ourLog.info("Testing diff in {} mode: {}", myMode, myName);

		if (myMode.equals("both") || myMode.equals("forwards")) {

			FhirPatch patch = new FhirPatch(getContext());
			patch.apply(myInput, myPatch);

			String expected = getContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(myOutput);
			String actual = getContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(myInput);
			assertEquals(expected, actual);

		} else {
			fail("Unknown mode: " + myMode);
		}

	}

	protected abstract FhirContext getContext();


	@Nonnull
	public static Collection<Object[]> loadTestSpec(FhirContext theContext, String theTestSpec) throws IOException, SAXException, TransformerException {
		List<Object[]> retVal = new ArrayList<>();

		String testsString = ClasspathUtil.loadResource(theTestSpec);
		Document doc = XmlUtil.parseDocument(testsString);
		Element tests = (Element) doc.getElementsByTagName("tests").item(0);
		NodeList cases = tests.getElementsByTagName("case");

		for (int i = 0; i < cases.getLength(); i++) {
			Element next = (Element) cases.item(i);

			String name = next.getAttribute("name");
			String mode = next.getAttribute("mode");

			Element diffElement = (Element) next.getElementsByTagName("diff").item(0);
			Element diffParametersElement = getFirstChildElement(diffElement);
			String encoded = XmlUtil.encodeDocument(diffParametersElement);
			IBaseResource diff = theContext.newXmlParser().parseResource(encoded);

			Element inputElement = (Element) next.getElementsByTagName("input").item(0);
			Element inputResourceElement = getFirstChildElement(inputElement);
			String inputEncoded = XmlUtil.encodeDocument(inputResourceElement);
			IBaseResource input = theContext.newXmlParser().parseResource(inputEncoded);

			Element outputElement = (Element) next.getElementsByTagName("output").item(0);
			Element outputResourceElement = getFirstChildElement(outputElement);
			String outputEncoded = XmlUtil.encodeDocument(outputResourceElement);
			IBaseResource output = theContext.newXmlParser().parseResource(outputEncoded);

			retVal.add(new Object[]{name, mode, input, diff, output});

		}

		return retVal;
	}

	private static Element getFirstChildElement(Element theInput) {
		for (int i = 0; i < theInput.getChildNodes().getLength(); i++) {
			if (theInput.getChildNodes().item(i) instanceof Element) {
				return (Element) theInput.getChildNodes().item(i);
			}
		}
		fail("No child of type Element");
		throw new Error();
	}
}
