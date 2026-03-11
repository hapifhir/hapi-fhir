package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-sonnet-4-6
class RestfulServerExtensionTest {

	private RestfulServerExtension myExtension;

	@AfterEach
	void tearDown() throws Exception {
		if (myExtension != null) {
			myExtension.stopServer();
		}
	}

	@Nested
	class BeforeStart {

		@Test
		void isRunning_whenNotStarted_returnsFalse() {
			myExtension = new RestfulServerExtension(FhirVersionEnum.R4);

			assertThat(myExtension.isRunning()).isFalse();
		}

		@ParameterizedTest
		@EnumSource(value = FhirVersionEnum.class, names = {"R4", "DSTU3"})
		void setFhirVersion_changesFhirVersion(FhirVersionEnum theFhirVersion) {
			myExtension = new RestfulServerExtension(FhirVersionEnum.R4);
			myExtension.setFhirVersion(theFhirVersion);

			assertThat(myExtension.getFhirContext().getVersion().getVersion()).isEqualTo(theFhirVersion);
		}

		@Test
		void setFhirContext_replacesContext() {
			// Use FhirContext constructor (not version enum) so createContextIfNeeded() won't overwrite
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());
			FhirContext replacementContext = FhirContext.forDstu3Cached();

			myExtension.setFhirContext(replacementContext);

			assertThat(myExtension.getFhirContext()).isSameAs(replacementContext);
		}

		@Test
		void getFhirContext_withVersionEnumConstructor_createsContextLazily() {
			myExtension = new RestfulServerExtension(FhirVersionEnum.R4);

			FhirContext context = myExtension.getFhirContext();

			assertThat(context).isNotNull();
			assertThat(context.getVersion().getVersion()).isEqualTo(FhirVersionEnum.R4);
		}

		@Test
		void getRunningServerUserData_isInitiallyEmpty() {
			myExtension = new RestfulServerExtension(FhirVersionEnum.R4);

			assertThat(myExtension.getRunningServerUserData()).isNotNull().isEmpty();
		}

		@Test
		void withServer_whenNotStarted_queuesConsumerForApplicationOnStart() throws Exception {
			List<RestfulServer> captured = new ArrayList<>();
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());

			myExtension.withServer(captured::add);
			assertThat(captured).as("consumer should not fire before server starts").isEmpty();

			myExtension.beforeEach(null);
			assertThat(captured).as("consumer should fire on start").hasSize(1);
		}
	}

	@Nested
	class WhileRunning {

		@Test
		void isRunning_afterStart_returnsTrue() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());
			myExtension.beforeEach(null);

			assertThat(myExtension.isRunning()).isTrue();
		}

		@Test
		void getRestfulServer_afterStart_returnsNonNull() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());
			myExtension.beforeEach(null);

			assertThat(myExtension.getRestfulServer()).isNotNull();
		}

		@Test
		void withServer_whenRunning_appliesConsumerImmediately() throws Exception {
			List<RestfulServer> captured = new ArrayList<>();
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());
			myExtension.beforeEach(null);

			myExtension.withServer(captured::add);

			assertThat(captured).hasSize(1);
			assertThat(captured.get(0)).isSameAs(myExtension.getRestfulServer());
		}
	}

	@Nested
	class AfterStop {

		@Test
		void isRunning_afterStop_returnsFalse() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());
			myExtension.beforeEach(null);
			myExtension.stopServer();

			assertThat(myExtension.isRunning()).isFalse();
		}

		@Test
		void getRestfulServer_afterStop_returnsNull() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());
			myExtension.beforeEach(null);
			assertThat(myExtension.getRestfulServer()).isNotNull();

			myExtension.stopServer();

			assertThat(myExtension.getRestfulServer()).isNull();
		}

		@Test
		void stopServer_clearsRunningUserData() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());
			myExtension.beforeEach(null);
			myExtension.getRunningServerUserData().put("test-key", "test-value");
			assertThat(myExtension.getRunningServerUserData()).hasSize(1);

			myExtension.stopServer();

			assertThat(myExtension.getRunningServerUserData()).isEmpty();
		}

		@Test
		void stopServer_keepsProviders_soTheyAreStillRegisteredOnRestart() throws Exception {
			// Providers registered before the first start should still be present on restart
			// because stopServer() does NOT clear myProviders
			FhirContext ctx = FhirContext.forR4Cached();
			myExtension = new RestfulServerExtension(ctx);

			myExtension.registerProvider(new IResourceProvider() {
				@Override
				public Class<? extends IBaseResource> getResourceType() {
					return Patient.class;
				}

				@Read
				public Patient read(@IdParam IdType theId) {
					throw new ResourceNotFoundException(theId);
				}
			});

			myExtension.beforeEach(null);
			assertThat(myExtension.getRestfulServer().getResourceProviders())
				.as("provider registered on first start")
				.hasSize(1);

			myExtension.stopServer();
			myExtension.beforeEach(null);  // restart — provider list still intact

			assertThat(myExtension.getRestfulServer().getResourceProviders())
				.as("provider still registered after stop/restart")
				.hasSize(1);
		}

		/**
		 * Tests the {@code || myServlet != null} branch of {@link RestfulServerExtension#isRunning()}.
		 * <p>
		 * When {@code stopServer()} runs, it first calls {@code super.stopServer()} which sets
		 * {@code myServer = null}. Without the {@code || myServlet != null} branch, {@code isRunning()}
		 * would return {@code false} at that point, causing the early return to skip the cleanup of
		 * user data, providers, and the servlet reference. The overridden {@code isRunning()} ensures
		 * cleanup always runs when the servlet is still set.
		 */
		@Test
		void isRunning_myServletNullBranch_ensuresCleanupRunsAfterSuperStop() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());
			myExtension.beforeEach(null);
			myExtension.getRunningServerUserData().put("sensitive-key", "value");

			myExtension.stopServer();

			// Both cleanup actions ran (not skipped by early return):
			assertThat(myExtension.getRestfulServer()).as("myServlet should be nulled by cleanup").isNull();
			assertThat(myExtension.getRunningServerUserData()).as("user data should be cleared by cleanup").isEmpty();
		}
	}

	@Nested
	class KeepAliveBetweenTests {

		@Test
		void afterEach_withoutKeepAlive_stopsServer() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached());
			myExtension.beforeEach(null);
			assertThat(myExtension.isRunning()).isTrue();

			myExtension.afterEach(null);

			assertThat(myExtension.isRunning()).isFalse();
		}

		@Test
		void afterEach_withKeepAlive_doesNotStopServer() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached())
				.keepAliveBetweenTests();
			myExtension.beforeEach(null);
			assertThat(myExtension.isRunning()).isTrue();

			myExtension.afterEach(null);

			assertThat(myExtension.isRunning()).isTrue();
		}

		@Test
		void afterAll_withKeepAlive_stopsServer() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached())
				.keepAliveBetweenTests();
			myExtension.beforeEach(null);
			myExtension.afterEach(null);
			assertThat(myExtension.isRunning()).as("still running after afterEach").isTrue();

			myExtension.afterAll(null);

			assertThat(myExtension.isRunning()).isFalse();
		}

		@Test
		void beforeEach_withKeepAlive_reusesExistingServer() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached())
				.keepAliveBetweenTests();
			myExtension.beforeEach(null);
			RestfulServer firstServlet = myExtension.getRestfulServer();

			myExtension.afterEach(null);  // does not stop
			myExtension.beforeEach(null); // server already running — no new servlet created

			assertThat(myExtension.getRestfulServer())
				.as("same servlet instance reused when server is kept alive")
				.isSameAs(firstServlet);
		}

		@Test
		void beforeEach_withKeepAlive_preservesRunningServerUserData() throws Exception {
			myExtension = new RestfulServerExtension(FhirContext.forR4Cached())
				.keepAliveBetweenTests();
			myExtension.beforeEach(null);
			myExtension.getRunningServerUserData().put("shared-key", "value");

			myExtension.afterEach(null);  // does not stop or clear user data
			myExtension.beforeEach(null);

			assertThat(myExtension.getRunningServerUserData())
				.as("user data persists across tests when server is kept alive")
				.containsEntry("shared-key", "value");
		}
	}
}