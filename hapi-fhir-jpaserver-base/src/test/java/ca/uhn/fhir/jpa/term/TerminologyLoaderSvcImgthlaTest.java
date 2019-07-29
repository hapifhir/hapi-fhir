package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class TerminologyLoaderSvcImgthlaTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcImgthlaTest.class);
	private TerminologyLoaderSvcImpl mySvc;

	@Mock
	private IHapiTerminologySvc myTermSvc;

	@Mock
	private IHapiTerminologySvcDstu3 myTermSvcDstu3;

	@Mock
	private RequestDetails details;
	private ZipCollectionBuilder myFiles;


	@Before
	public void before() {
		mySvc = new TerminologyLoaderSvcImpl();
		mySvc.setTermSvcForUnitTests(myTermSvc);
		mySvc.setTermSvcDstu3ForUnitTest(myTermSvcDstu3);

		myFiles = new ZipCollectionBuilder();
	}

	@Test
	public void testLoadImgthla() throws Exception {
		addImgthlaMandatoryFilesToZip(myFiles);

		// Actually do the load
		try {
			mySvc.loadImgthla(myFiles.getFiles(), details);
			fail("Expected \"not yet fully implemented\" InternalErrorException");
		} catch(InternalErrorException e) {
			// for now, expect "not yet fully implemented" exception
			assertThat(e.getMessage(), containsString("HLA nomenclature terminology upload not yet fully implemented"));
		}

		// TODO:  verify the code system was loaded correctly (similarly to TerminologyLoaderSvcLoincTest.testLoadLoinc)
	}

	@Test
	@Ignore
	public void testLoadImgthlaMandatoryFilesOnly() throws IOException {
		addImgthlaMandatoryFilesToZip(myFiles);

		// Actually do the load
		mySvc.loadImgthla(myFiles.getFiles(), details);

		// TODO:  verify the code system was loaded correctly (similarly to TerminologyLoaderSvcLoincTest.testLoadLoincMandatoryFilesOnly)
	}

	@Test
	public void testLoadImgthlaMissingMandatoryFiles() throws IOException {
		myFiles.addFileZip("/imgthla/", "bogus.txt");

		// Actually do the load
		try {
			mySvc.loadImgthla(myFiles.getFiles(), details);
			fail("Expected UnprocessableEntityException");
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Could not find the following mandatory files in input:"));
			assertThat(e.getMessage(), containsString("hla_nom.txt"));
			assertThat(e.getMessage(), containsString("hla.xml"));
		} catch(Throwable t) {
			fail("Expected UnprocessableEntityException");
		}
	}


	public static void addImgthlaMandatoryFilesToZip(ZipCollectionBuilder theFiles) throws IOException {
		theFiles.addFileZip("/imgthla/", TerminologyLoaderSvcImpl.IMGTHLA_HLA_NOM_TXT);
		theFiles.addFileZip("/imgthla/", TerminologyLoaderSvcImpl.IMGTHLA_HLA_XML);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
