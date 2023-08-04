package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

public class PackageInstallerSvcImplCreateTest extends BaseJpaR4Test {
	private static final IIdType CONCEPT_MAP_TEST_ID = new IdDt("ConceptMap/PackageInstallerSvcImplRewriteHistoryTest");
	private static final String PACKAGE_ID_1 = "package1";
	private static final String PACKAGE_VERSION = "1.0";

	private FhirContext myCtx = FhirContext.forR4Cached();

	@Autowired
	private ITermValueSetDao myTermValueSetDao;

	@Autowired
	private PackageInstallerSvcImpl mySvc;

	@Test
	void createWithNoExistingResourcesNoIdOnValueSet() throws IOException {
		final CodeSystem cs = new CodeSystem();
		cs.setId("CodeSystem/mycs");
		cs.setUrl("http://my-code-system");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		final NpmPackage pkg = createPackage(cs, PACKAGE_ID_1);

		final PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName(PACKAGE_ID_1);
		spec.setVersion(PACKAGE_VERSION);
		spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setPackageContents(packageToBytes(pkg));

		final PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		final ValueSet valueSet = new ValueSet();
		valueSet.setUrl("http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1010.9");

		mySvc.create(valueSet, spec, outcome);

		final List<TermValueSet> all = myTermValueSetDao.findAll();
	}

	@Test
	void createWithNoExistingResourcesIdOnValueSet() throws IOException {
		final CodeSystem cs = new CodeSystem();
		cs.setId("CodeSystem/mycs");
		cs.setUrl("http://my-code-system");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		final NpmPackage pkg = createPackage(cs, PACKAGE_ID_1);

		final PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName(PACKAGE_ID_1);
		spec.setVersion(PACKAGE_VERSION);
		spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setPackageContents(packageToBytes(pkg));

		final PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		final ValueSet valueSetFromFirstIg = new ValueSet();
		valueSetFromFirstIg.setUrl("http://first.ig.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1010.9");
		valueSetFromFirstIg.setId(new IdDt(null, "ValueSet", "2.16.840.1.113762.1.4.1010.9", null));

		mySvc.create(valueSetFromFirstIg, spec, outcome);

		final List<TermValueSet> all = myTermValueSetDao.findAll();

		final ValueSet valueSetFromSecondIg = new ValueSet();
		valueSetFromSecondIg.setUrl("http://second.ig.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1010.9");
		valueSetFromSecondIg.setId(new IdDt(null, "ValueSet", "2.16.840.1.113762.1.4.1010.9", "43"));

		mySvc.create(valueSetFromSecondIg, spec, outcome);
	}

	@Nonnull
	private NpmPackage createPackage(CodeSystem cs, String packageId) throws IOException {
		PackageGenerator manifestGenerator = new PackageGenerator();
		manifestGenerator.name(packageId);
		manifestGenerator.version(PACKAGE_VERSION);
		manifestGenerator.description("a package");
		manifestGenerator.fhirVersions(List.of(FhirVersionEnum.R4.getFhirVersionString()));

		NpmPackage pkg = NpmPackage.empty(manifestGenerator);

		String csString = myCtx.newJsonParser().encodeResourceToString(cs);
		pkg.addFile("package", "cs.json", csString.getBytes(StandardCharsets.UTF_8), "CodeSystem");

		return pkg;
	}

	@Nonnull
	private static byte[] packageToBytes(NpmPackage pkg) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		pkg.save(stream);
		return stream.toByteArray();
	}
}
