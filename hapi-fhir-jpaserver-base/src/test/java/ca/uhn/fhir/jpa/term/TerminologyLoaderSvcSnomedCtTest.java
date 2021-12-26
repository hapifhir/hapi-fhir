package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.zip.ZipOutputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

public class TerminologyLoaderSvcSnomedCtTest extends BaseLoaderTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcSnomedCtTest.class);
	private TermLoaderSvcImpl mySvc;

	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Captor
	private ArgumentCaptor<TermCodeSystemVersion> myCsvCaptor;
	private ZipCollectionBuilder myFiles;
	@Mock
	private ITermDeferredStorageSvc myTermDeferredStorageSvc;

	@BeforeEach
	public void before() {
		mySvc = TermLoaderSvcImpl.withoutProxyCheck(myTermDeferredStorageSvc, myTermCodeSystemStorageSvc);

		myFiles = new ZipCollectionBuilder();
	}

	private ArrayList<ITermLoaderSvc.FileDescriptor> list(byte[]... theByteArray) {
		ArrayList<ITermLoaderSvc.FileDescriptor> retVal = new ArrayList<>();
		for (byte[] next : theByteArray) {
			retVal.add(new ITermLoaderSvc.FileDescriptor() {
				@Override
				public String getFilename() {
					return "aaa.zip";				}

				@Override
				public InputStream getInputStream() {
					return new ByteArrayInputStream(next);
				}
			});
		}
		return retVal;
	}

	@Test
	public void testLoadSnomedCt() throws Exception {
		myFiles.addFileZip("/sct/", "sct2_Concept_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Concept_Full-en_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Description_Full-en_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Relationship_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_TextDefinition_Full-en_INT_20160131.txt");

		mySvc.loadSnomedCt(myFiles.getFiles(), mySrd);

		verify(myTermCodeSystemStorageSvc).storeNewCodeSystemVersion(any(CodeSystem.class), myCsvCaptor.capture(), any(RequestDetails.class), anyList(), anyList());

		TermCodeSystemVersion csv = myCsvCaptor.getValue();
		TreeSet<String> allCodes = toCodes(csv, true);
		ourLog.info(allCodes.toString());

		assertThat(allCodes, hasItem("116680003"));
		assertThat(allCodes, not(hasItem("207527008")));

		allCodes = toCodes(csv, false);
		ourLog.info(allCodes.toString());
		assertThat(allCodes, hasItem("126816002"));
	}

	/**
	 * This is just for trying stuff, it won't run without
	 * local files external to the git repo
	 */
	@Disabled
	@Test
	public void testLoadSnomedCtAgainstRealFile() throws Exception {
		byte[] bytes = IOUtils.toByteArray(new FileInputStream("/Users/james/Downloads/SnomedCT_Release_INT_20160131_Full.zip"));

		mySvc.loadSnomedCt(list(bytes), mySrd);
	}

	@Test
	public void testLoadSnomedCtBadInput() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		myFiles.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");
		zos.close();

		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);

		try {
			mySvc.loadSnomedCt(list(bos.toByteArray()), mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Could not find the following mandatory files in input: "));
		}
	}

	private TreeSet<String> toCodes(TermCodeSystemVersion theCsv, boolean theAddChildren) {
		TreeSet<String> retVal = new TreeSet<>();
		for (TermConcept next : theCsv.getConcepts()) {
			toCodes(retVal, next, theAddChildren);
		}
		return retVal;
	}

	private void toCodes(TreeSet<String> theCodes, TermConcept theConcept, boolean theAddChildren) {
		theCodes.add(theConcept.getCode());
		if (theAddChildren) {
			for (TermConceptParentChildLink next : theConcept.getChildren()) {
				toCodes(theCodes, next.getChild(), theAddChildren);
			}
		}
	}


}
