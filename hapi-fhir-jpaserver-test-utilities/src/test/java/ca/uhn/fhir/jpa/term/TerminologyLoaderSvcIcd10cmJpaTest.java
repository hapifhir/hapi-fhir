package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.util.ClasspathUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TerminologyLoaderSvcIcd10cmJpaTest extends BaseJpaR4Test {
	private TermLoaderSvcImpl mySvc;
	private ZipCollectionBuilder myFiles;

	@BeforeEach
	public void before() {
		mySvc = new TermLoaderSvcImpl(myTerminologyDeferredStorageSvc, myTermCodeSystemStorageSvc);

		myFiles = new ZipCollectionBuilder();
	}

	@Test
	public void testLoadIcd10cm() throws IOException {
		String filename = "icd/icd10cm_tabular_2021.xml";

		String resource = ClasspathUtil.loadResource(filename);
		List<ITermLoaderSvc.FileDescriptor> descriptors = new ArrayList<>();
		descriptors.add(new ITermLoaderSvc.ByteArrayFileDescriptor(filename, resource.getBytes(StandardCharsets.UTF_8)));
		mySvc.loadIcd10cm(descriptors, new SystemRequestDetails());

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermValueSetDao.count());
			assertEquals(0, myTermConceptMapDao.count());
			assertEquals(1, myResourceTableDao.count());
			assertEquals(17, myTermConceptDao.count());
			TermCodeSystem codeSystem = myTermCodeSystemDao.findByCodeSystemUri(ITermLoaderSvc.ICD10CM_URI);

			assertEquals("2021", codeSystem.getCurrentVersion().getCodeSystemVersionId());

			TermCodeSystemVersion codeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(codeSystem.getPid(), "2021");
			assertEquals(codeSystem.getCurrentVersion().getPid(), codeSystemVersion.getPid());
			assertEquals(codeSystem.getResource().getId(), codeSystemVersion.getResource().getId());
		});

	}

}
