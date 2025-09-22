package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BundleProviderUtilTest {

    private static final Resource PATIENT_1 = new Patient().setId("pat1");

    private final RequestDetails requestDetails = new SystemRequestDetails();
    private final IPagingProvider pagingProvider = new FifoMemoryPagingProvider(10);

    @Test
    void createBundleFromBundleProvider_withValidInputs_returnsBundle() {
        IBaseResource bundle = BundleProviderUtil.createBundleFromBundleProvider(
                getRestfulServerWithDefaultPagingProvider(),
                requestDetails,
                10,
                "selfLink",
                Set.of(),
                getBundleProvider(PATIENT_1),
                0,
                BundleTypeEnum.SEARCHSET,
                null);

        assertNotNull(bundle);
        assertFalse(bundle.isEmpty());
    }

    @Test
    void createBundleFromBundleProvider_withEmptyResources_returnsEmptyBundle() {
        IBaseResource bundle = BundleProviderUtil.createBundleFromBundleProvider(
                getRestfulServerWithDefaultPagingProvider(),
                requestDetails,
                10,
                "selfLink",
                Set.of(),
                getBundleProvider(),
                0,
                BundleTypeEnum.SEARCHSET,
                null);

        assertNotNull(bundle);
        assertFalse(bundle.isEmpty());
    }

    @Test
    void createBundleFromBundleProvider_withNullPagingProvider_returnsBundle() {
        IBaseResource bundle = BundleProviderUtil.createBundleFromBundleProvider(
                getRestfulServer(null),
                requestDetails,
                10,
                "selfLink",
                Set.of(),
                getBundleProvider(),
                0,
                BundleTypeEnum.SEARCHSET,
                null);

        assertNotNull(bundle);
        assertFalse(bundle.isEmpty());
    }

    @Test
    void createBundleFromBundleProvider_withMinusOneOffset_returnsBundle() {
        final Set<Include> includes = Set.of();
        final RestfulServer restfulServer = getRestfulServerWithDefaultPagingProvider();
        final IBundleProvider bundleProvider = getBundleProvider();

        assertThrows(IndexOutOfBoundsException.class, () -> {
            BundleProviderUtil.createBundleFromBundleProvider(
                    restfulServer,
                    requestDetails,
                    10,
                    "selfLink",
                    includes,
                    bundleProvider,
                    -1,
                    BundleTypeEnum.SEARCHSET,
                    null);
        });
    }

    @Test
    void createBundleFromBundleProvider_withNonEmptyResources_returnsBundleWithResources() {
        IBaseResource bundle = BundleProviderUtil.createBundleFromBundleProvider(
                getRestfulServer(null),
                requestDetails,
                10,
                "selfLink",
                Set.of(),
                getBundleProvider(PATIENT_1),
                0,
                BundleTypeEnum.SEARCHSET,
                null);

        assertNotNull(bundle);
        assertFalse(bundle.isEmpty());
    }

    private static IBundleProvider getBundleProvider(IBaseResource... resources) {
        return new SimpleBundleProvider(resources);
    }

    private RestfulServer getRestfulServerWithDefaultPagingProvider() {
        return getRestfulServer(pagingProvider);
    }

    private static RestfulServer getRestfulServer(@Nullable IPagingProvider pagingProvider) {
        final RestfulServer restfulServer = new RestfulServer(FhirContext.forR4Cached());

        restfulServer.setPagingProvider(pagingProvider);

        return restfulServer;
    }
}
