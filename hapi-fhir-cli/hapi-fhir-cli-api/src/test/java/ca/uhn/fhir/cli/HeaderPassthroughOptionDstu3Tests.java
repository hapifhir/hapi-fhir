package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HeaderPassthroughOptionDstu3Tests extends BaseHeaderPassthroughOptionTests {
	final String FHIR_VERSION = "dstu3";
	private FhirContext myCtx = FhirContext.forDstu3();

	@RegisterExtension
	public final RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(myCtx);

	@Mock
	protected ITermLoaderSvc myTermLoaderSvc;

	@BeforeEach
	public void beforeEach() {
		super.beforeEach(myCtx, myTermLoaderSvc, myRestfulServerExtension);
	}

	@Test
	public void oneHeader() throws Exception {
		super.oneHeader(FHIR_VERSION, myRestfulServerExtension.getPort());
	}

	@Test
	public void twoHeadersSameKey() throws Exception {
		super.twoHeadersSameKey(FHIR_VERSION, myRestfulServerExtension.getPort());
	}

	@Test
	public void twoHeadersDifferentKeys() throws Exception {
		super.twoHeadersDifferentKeys(FHIR_VERSION, myRestfulServerExtension.getPort());
	}
}
