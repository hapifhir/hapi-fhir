package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FhirPatchR5CoreTest extends BaseFhirPatchCoreTest {

	private static final FhirContext ourCtx = FhirContext.forR5();

	public FhirPatchR5CoreTest(String theName, String theMode, IBaseResource theInput, IBaseResource thePatch, IBaseResource theOutput) {
		super(theName, theMode, theInput, thePatch, theOutput);
	}

	@Override
	protected FhirContext getContext() {
		return ourCtx;
	}

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> parameters() throws IOException, SAXException, TransformerException {
		String testSpec = "/org/hl7/fhir/testcases/r5/patch/fhir-path-tests.xml";
		return loadTestSpec(ourCtx, testSpec);
	}

}
