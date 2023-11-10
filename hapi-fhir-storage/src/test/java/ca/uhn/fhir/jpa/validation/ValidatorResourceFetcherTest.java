package ca.uhn.fhir.jpa.validation;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.test.BaseTest;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.exceptions.FHIRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.utils.XVerExtensionManager;
import org.hl7.fhir.validation.instance.InstanceValidator;
import org.hl7.fhir.common.hapi.validation.validator.VersionSpecificWorkerContextWrapper;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;


public class ValidatorResourceFetcherTest extends BaseTest {
  private static FhirContext ourCtx = FhirContext.forR4();
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport(ourCtx);
	private static ValidatorResourceFetcher fetcher;
	private static DaoRegistry mockDaoRegistry;
	private static IFhirResourceDao mockResourceDao;

	@BeforeEach
	public void before() {
    mockDaoRegistry = mock(DaoRegistry.class);
    mockResourceDao = mock(IFhirResourceDao.class);
		fetcher = new ValidatorResourceFetcher(ourCtx, myDefaultValidationSupport, mockDaoRegistry);
  }

  @Test
	public void checkFetchByUrl() {
    // setup mocks
    String resource;
    try {
      resource = loadResource("/q_jon_with_url_version.json");
    } catch (IOException e) {
      throw new FHIRException("Couldn't read");
    }
    doReturn(mockResourceDao).when(mockDaoRegistry).getResourceDao("Questionnaire");
    doThrow(new ResourceNotFoundException("Not Found")).when(mockResourceDao).read(any(),any());
    doReturn(new SimpleBundleProvider(Arrays.asList(
      ourCtx.newJsonParser().parseResource(resource)
    ))).when(mockResourceDao).search(any(),any());
    VersionSpecificWorkerContextWrapper wrappedWorkerContext = VersionSpecificWorkerContextWrapper.newVersionSpecificWorkerContextWrapper(myDefaultValidationSupport);
    InstanceValidator v = new InstanceValidator(
      wrappedWorkerContext,
      new FhirInstanceValidator.NullEvaluationContext(),
      new XVerExtensionManager(null));
    RequestDetails r = new SystemRequestDetails();
    // test
    Element returnedResource = fetcher.fetch(v, r,"http://www.test-url-for-questionnaire.com/Questionnaire/test-id|1.0.0");
    assertNotNull(returnedResource);
  }
}
