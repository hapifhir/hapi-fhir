package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DependencyManagerTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private IFhirResourceDao<Basic> myBasicResourceDao;
	@Mock
	private PartitionSettings myPartitionSettings;

	private DependencyManager myDependencyManager;

	@BeforeEach
	public void beforeEach() {
		myDependencyManager = new DependencyManager(myFhirContext, myDaoRegistry, myPartitionSettings);

		when(myDaoRegistry.getResourceDao(Basic.class)).thenReturn(myBasicResourceDao);
	}

	@Test
	public void testCreateDependencyResource() {
		// set up
		DaoMethodOutcome outcome = new DaoMethodOutcome();
		IdDt theId = new IdDt("Basic/1/_history/1");
		outcome.setId(theId);

		when(myBasicResourceDao.create(any(Basic.class), any(RequestDetails.class))).thenReturn(outcome);

		// execute
		String actualId = myDependencyManager.createDependencyResource();

		// verify
		assertThat(actualId).isEqualTo("Basic/1");
	}

	@Test
	public void testShouldProcessDependency_emptyResource() {
		// set up
		String packageName = "hl7.fhir.us.core";
		String packageVersion = "5.0.1";

		String id = "Basic/1";

		Basic basicResource = new Basic();
		basicResource.setId(id);

		when(myBasicResourceDao.read(any(IIdType.class), any(RequestDetails.class))).thenReturn(basicResource);

		// execute
		boolean outcome = myDependencyManager.shouldProcessDependency(id, packageName, packageVersion);

		// verify
		assertThat(outcome).isTrue();

		List<Extension> extensions = basicResource.getExtension();
		assertThat(extensions).hasSize(1);
		Extension extension = extensions.get(0);
		assertThat(extension.getUrl()).isEqualTo(DependencyManager.EXTENSION_URL);
		assertThat(extension.getValue()).isNull();

		List<Extension> subextensions = extension.getExtension();
		assertThat(subextensions).hasSize(2);
		assertThat(subextensions.get(0).getUrl()).isEqualTo(DependencyManager.SUBEXTENSION_NAME_URL);
		assertThat(subextensions.get(0).getValue())
			.isInstanceOf(IPrimitiveType.class)
			.extracting(t -> ((IPrimitiveType<?>) t).getValueAsString())
			.isEqualTo(packageName);
		assertThat(subextensions.get(1).getUrl()).isEqualTo(DependencyManager.SUBEXTENSION_VERSION_URL);
		assertThat(subextensions.get(1).getValue())
			.isInstanceOf(IPrimitiveType.class)
			.extracting(t -> ((IPrimitiveType<?>) t).getValueAsString())
			.isEqualTo(packageVersion);

		verify(myBasicResourceDao).update(eq(basicResource), any(RequestDetails.class));

		ArgumentCaptor<IIdType> idCaptor = ArgumentCaptor.forClass(IIdType.class);
		verify(myBasicResourceDao).read(idCaptor.capture(), any(RequestDetails.class));
		assertThat(idCaptor.getValue().getValue()).isEqualTo(id);
	}

	@Test
	public void testShouldProcessDependency_noConflicts() {
		// set up
		String packageName = "hl7.fhir.us.core";
		String packageVersion = "5.0.1";

		String id = "Basic/1";

		Basic basicResource = new Basic();
		basicResource.setId(id);

		basicResource.addExtension(createDependencyExtension("hl7.fhir.us.core", "6.1.0"));
		basicResource.addExtension(createDependencyExtension("hl7.fhir.terminology", "5.0.1"));

		when(myBasicResourceDao.read(any(IIdType.class), any(RequestDetails.class))).thenReturn(basicResource);

		// execute
		boolean outcome = myDependencyManager.shouldProcessDependency(id, packageName, packageVersion);

		// verify
		assertThat(outcome).isTrue();

		List<Extension> extensions = basicResource.getExtension();
		assertThat(extensions).hasSize(3);
		Extension extension = extensions.get(2);
		assertThat(extension.getUrl()).isEqualTo(DependencyManager.EXTENSION_URL);
		assertThat(extension.getValue()).isNull();

		List<Extension> subextensions = extension.getExtension();
		assertThat(subextensions).hasSize(2);
		assertThat(subextensions.get(0).getUrl()).isEqualTo(DependencyManager.SUBEXTENSION_NAME_URL);
		assertThat(subextensions.get(0).getValue())
			.isInstanceOf(IPrimitiveType.class)
			.extracting(t -> ((IPrimitiveType<?>) t).getValueAsString())
			.isEqualTo(packageName);
		assertThat(subextensions.get(1).getUrl()).isEqualTo(DependencyManager.SUBEXTENSION_VERSION_URL);
		assertThat(subextensions.get(1).getValue())
			.isInstanceOf(IPrimitiveType.class)
			.extracting(t -> ((IPrimitiveType<?>) t).getValueAsString())
			.isEqualTo(packageVersion);

		verify(myBasicResourceDao).update(eq(basicResource), any(RequestDetails.class));

		ArgumentCaptor<IIdType> idCaptor = ArgumentCaptor.forClass(IIdType.class);
		verify(myBasicResourceDao).read(idCaptor.capture(), any(RequestDetails.class));
		assertThat(idCaptor.getValue().getValue()).isEqualTo(id);
	}

	@Test
	public void testShouldProcessDependency_withConflict_doesNotUpdateResource() {
		// set up
		String packageName = "hl7.fhir.us.core";
		String packageVersion = "5.0.1";

		String id = "Basic/1";

		Basic basicResource = new Basic();
		basicResource.setId(id);

		basicResource.addExtension(createDependencyExtension("hl7.fhir.us.core", "5.0.1"));

		when(myBasicResourceDao.read(any(IIdType.class), any(RequestDetails.class))).thenReturn(basicResource);

		// execute
		boolean outcome = myDependencyManager.shouldProcessDependency(id, packageName, packageVersion);

		// verify
		assertThat(outcome).isFalse();

		List<Extension> extensions = basicResource.getExtension();
		assertThat(extensions).hasSize(1);
		verify(myBasicResourceDao, never()).update(any(Basic.class), any(RequestDetails.class));
	}

	@Test
	public void testDeleteDependencyResource() {
		// set up
		String id = "Basic/1";

		// execute
		myDependencyManager.deleteDependencyResource(id);

		// verify
		ArgumentCaptor<IIdType> captor = ArgumentCaptor.forClass(IIdType.class);
		verify(myBasicResourceDao).delete(captor.capture(), any(RequestDetails.class));
		assertThat(captor.getValue().getValue()).isEqualTo(id);
	}

	private Extension createDependencyExtension(String thePackageName, String theVersion) {
		Extension extension = new Extension();
		extension.setUrl(DependencyManager.EXTENSION_URL);

		Extension subExtension1 = new Extension();
		subExtension1.setUrl(DependencyManager.SUBEXTENSION_NAME_URL);
		subExtension1.setValue(new StringType(thePackageName));
		extension.addExtension(subExtension1);

		Extension subExtension2 = new Extension();
		subExtension2.setUrl(DependencyManager.SUBEXTENSION_VERSION_URL);
		subExtension2.setValue(new StringType(theVersion));
		extension.addExtension(subExtension2);

		return extension;
	}
}
