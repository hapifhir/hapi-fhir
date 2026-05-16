package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.XmlUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.hl7.fhir.r5.model.Composition;
import org.junit.jupiter.api.Test;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirPatchCoreTest extends BaseTest {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirPatchCoreTest.class);

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("parameters")
	public void testApply(TestCase theTestCase) {

		ourLog.info("Testing diff {} in {} mode", theTestCase.name(), theTestCase.mode());

		if (theTestCase.mode().equals("both") || theTestCase.mode().equals("forwards")) {

			FhirPatch patch = new FhirPatch(theTestCase.theFhirContext());

			if (theTestCase.error() != null) {
				assertThatThrownBy(() -> patch.apply(theTestCase.input(), theTestCase.diff()))
					.hasMessageContaining(theTestCase.error());
			} else {
				patch.apply(theTestCase.input(), theTestCase.diff());
				String expected = theTestCase.theFhirContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(theTestCase.output());
				String actual = theTestCase.theFhirContext().newJsonParser().setPrettyPrint(true).encodeResourceToString(theTestCase.input());
				assertEquals(expected, actual);
			}

		} else {
			fail("Unknown mode: " + theTestCase.mode());
		}

	}

	public static List<TestCase> parameters() throws TransformerException, SAXException, IOException {
		String testSpecR4 = "/org/hl7/fhir/testcases/r4/patch/fhir-patch-tests.xml";
		// the above file is missing an <output></output> section on line 1413 due to a processing error during file generation
		// as per the error statement found in the file.
		Collection<TestCase> retValR4 = loadTestSpec(FhirContext.forR4Cached(), testSpecR4);

		String testSpecR5 = "/org/hl7/fhir/testcases/r5/patch/fhir-patch-tests.xml";
		// The above file his missing xml closing tag '/>' on line 241 and 245
		Collection<TestCase> retValR5 = loadTestSpec(FhirContext.forR5Cached(), testSpecR5);

		ArrayList<TestCase> retVal = new ArrayList<>();
		retVal.addAll(retValR4);
		retVal.addAll(retValR5);

		return retVal;
	}


	@Nonnull
	public static Collection<TestCase> loadTestSpec(FhirContext theContext, String theTestSpec) throws IOException, SAXException, TransformerException {
		List<TestCase> retVal = new ArrayList<>();

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

			String error = null;
			Element errorElement = (Element) next.getElementsByTagName("error").item(0);
			if (errorElement != null) {
				error = errorElement.getAttribute("msg");
				assertThat(error).isNotBlank();
			}

			Element outputElement = (Element) next.getElementsByTagName("output").item(0);
			if (outputElement == null && error == null) {
				fail("Unable to parse case: " + XmlUtil.encodeDocument(next, true));
			}
			IBaseResource output = null;
			if (outputElement != null) {
				Element outputResourceElement = getFirstChildElement(outputElement);
				String outputEncoded = XmlUtil.encodeDocument(outputResourceElement);
				output = theContext.newXmlParser().parseResource(outputEncoded);
			}

			retVal.add(new TestCase(name, mode, input, diff, output, error, theContext));

		}

		return retVal;
	}

	@Test
	void doDiffOnComposition() throws FileNotFoundException {

		FhirPatch differ = new FhirPatch(FhirContext.forR5());

		var original = FhirContext.forR5().newJsonParser().parseResource(Composition.class, new DataInputStream(new FileInputStream("src/test/resources/origin.json")));
		var focused = FhirContext.forR5().newJsonParser().parseResource(Composition.class, new DataInputStream(new FileInputStream("src/test/resources/focused.json")));

		var diff = differ.diff(original, focused);
		FhirContext.forR5().newJsonParser().encodeResourceToString(diff);
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

	public record TestCase(String name, String mode, IBaseResource input, IBaseResource diff, IBaseResource output,
						   String error, FhirContext theFhirContext) {
		@Nonnull
		@Override
		public String toString() {
			return theFhirContext.getVersion().getVersion().name() + " " + name;
		}
	}
}
