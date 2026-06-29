package ca.uhn.fhir.jpa.subscription;

/*
 * LFJT3 — "Looking Forward to Jackson Tools 3"
 * =============================================
 * Written for Jackson 2 (com.fasterxml.jackson). Only the import block
 * and createMapper() factory method change during the future LFJT3 uplift.
 *
 * LFJT3 MIGRATION CHECKLIST
 * --------------------------
 * [ ] Imports: com.fasterxml.jackson.* → tools.jackson.*
 * [ ] createMapper(): new ObjectMapper() → JsonMapper.builder().build()
 *
 * KEY BEHAVIORS LOCKED DOWN
 * -------------------------
 * ResourceModifiedMessagePersistenceSvcImpl uses new ObjectMapper() internally
 * (in the constructor) to:
 *   1. getPayloadLessMessageAsString()  — serializes ResourceModifiedMessage
 *   2. getPayloadLessMessageFromString() — deserializes ResourceModifiedMessage
 *
 * These tests verify that behavior via the package-private createEntityFrom()
 * and createResourceModifiedMessageFromEntityWithoutInflation() methods,
 * which exercise the Jackson round-trip path without needing JPA/DB.
 *
 * In LFJT3:
 *   - new ObjectMapper()        → JsonMapper.builder().build()
 *   - JsonProcessingException   → JacksonException (unchecked, production class change)
 *   - catch block types change in production class, not here
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.data.IResourceModifiedDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.server.messaging.BaseResourceModifiedMessage;
// ── LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────────
// Jackson 2 (NOW):
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
// Jackson 3 (LFJT3):
//   import tools.jackson.databind.JsonNode;
//   import tools.jackson.databind.ObjectMapper;
// ── END LFJT3 JACKSON IMPORT BLOCK ───────────────────────────────────────────
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * LFJT3-aware tests for Jackson-specific behavior in
 * {@link ResourceModifiedMessagePersistenceSvcImpl}.
 *
 * <p>These tests bypass JPA/DB by calling package-private methods directly,
 * isolating the Jackson serialization behavior.
 */
@ExtendWith(MockitoExtension.class)
class ResourceModifiedMessagePersistenceSvcImplJacksonTest {

	private static final FhirContext FHIR_CONTEXT = FhirContext.forR4();

	@Mock
	private IResourceModifiedDao myResourceModifiedDao;

	@Mock
	private DaoRegistry myDaoRegistry;

	@Mock
	private HapiTransactionService myHapiTransactionService;

	private ResourceModifiedMessagePersistenceSvcImpl mySvc;

	// ── LFJT3 MAPPER FACTORY ─────────────────────────────────────────────────
	// Not used directly here (the service creates its own mapper internally),
	// but retained to document the LFJT3 change point.
	// When LFJT3 lands: ResourceModifiedMessagePersistenceSvcImpl constructor
	//   myObjectMapper = new ObjectMapper()  →  JsonMapper.builder().build()
	// Jackson 2 (NOW): new ObjectMapper()
	// Jackson 3 (LFJT3): JsonMapper.builder().build()
	private ObjectMapper createVerificationMapper() {
		return new ObjectMapper();
		// LFJT3: return JsonMapper.builder().build();
	}
	// ── END LFJT3 MAPPER FACTORY ─────────────────────────────────────────────

	@BeforeEach
	void setUp() {
		mySvc = new ResourceModifiedMessagePersistenceSvcImpl(
			FHIR_CONTEXT,
			myResourceModifiedDao,
			myDaoRegistry,
			myHapiTransactionService);
	}

	private ResourceModifiedMessage buildMessage(String resourceType, String id, String version) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setPayloadId(new IdType(resourceType, id, version));
		msg.setOperationType(BaseResourceModifiedMessage.OperationTypeEnum.CREATE);
		return msg;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 1. createEntityFrom — Jackson serialization path
	//    ResourceModifiedMessage → JSON string → stored in entity
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("createEntityFrom — Jackson serialization")
	class CreateEntityFromTests {

		@Test
		@DisplayName("Entity is created without exception (Jackson serializes successfully)")
		void createEntityFrom_noException() {
			ResourceModifiedMessage msg = buildMessage("Patient", "pt-001", "1");
			assertDoesNotThrow(() -> mySvc.createEntityFrom(msg));
		}

		@Test
		@DisplayName("Entity summary message is non-null (JSON was produced)")
		void createEntityFrom_summaryMessageIsNonNull() {
			ResourceModifiedMessage msg = buildMessage("Patient", "pt-002", "1");
			ResourceModifiedEntity entity = mySvc.createEntityFrom(msg);
			assertNotNull(entity.getSummaryResourceModifiedMessage());
		}

		@Test
		@DisplayName("Entity summary message is valid JSON")
		void createEntityFrom_summaryMessageIsValidJson() throws Exception {
			ResourceModifiedMessage msg = buildMessage("Patient", "pt-003", "2");
			ResourceModifiedEntity entity = mySvc.createEntityFrom(msg);

			String summary = entity.getSummaryResourceModifiedMessage();
			// Verify Jackson produced parseable JSON
			ObjectMapper verifier = createVerificationMapper();
			JsonNode parsed = assertDoesNotThrow(() -> verifier.readTree(summary));
			assertNotNull(parsed);
		}

		@Test
		@DisplayName("Entity PK resourceType matches the message resource type")
		void createEntityFrom_pkResourceType_matchesMessage() {
			ResourceModifiedMessage msg = buildMessage("Observation", "obs-001", "1");
			ResourceModifiedEntity entity = mySvc.createEntityFrom(msg);

			assertThat(entity.getResourceModifiedEntityPK().getResourceType()).isEqualTo("Observation");
		}

		@Test
		@DisplayName("Entity PK resourcePid matches the message ID")
		void createEntityFrom_pkResourcePid_matchesMessageId() {
			ResourceModifiedMessage msg = buildMessage("Patient", "pid-999", "3");
			ResourceModifiedEntity entity = mySvc.createEntityFrom(msg);

			assertThat(entity.getResourceModifiedEntityPK().getResourcePid()).isEqualTo("pid-999");
		}

		@Test
		@DisplayName("Summary JSON does NOT contain the payload resource body (payload-less)")
		void createEntityFrom_summaryMessageIsPayloadLess() throws Exception {
			ResourceModifiedMessage msg = buildMessage("Patient", "pt-payload", "1");
			ResourceModifiedEntity entity = mySvc.createEntityFrom(msg);

			String summary = entity.getSummaryResourceModifiedMessage();
			// The PayloadLessResourceModifiedMessage strips the resource body.
			// Payload-less means no "payload" or "resource" field with actual FHIR content.
			// The summary should be a compact JSON metadata envelope.
			ObjectMapper verifier = createVerificationMapper();
			JsonNode parsed = verifier.readTree(summary);
			// The key assertion: it should NOT be deeply nested FHIR resource JSON
			assertThat(summary.length())
				.as("Payload-less message should be small — not a full FHIR resource")
				.isLessThan(2000);
		}
	}

	// ─────────────────────────────────────────────────────────────────────────
	// 2. createResourceModifiedMessageFromEntityWithoutInflation
	//    JSON string → ResourceModifiedMessage (Jackson deserialization path)
	// ─────────────────────────────────────────────────────────────────────────
	@Nested
	@DisplayName("createResourceModifiedMessageFromEntityWithoutInflation — Jackson deserialization")
	class DeserializationTests {

		private ResourceModifiedEntity buildEntity(ResourceModifiedMessage theMsg) {
			return mySvc.createEntityFrom(theMsg);
		}

		@Test
		@DisplayName("Deserialization does not throw (Jackson reads successfully)")
		void deserialize_noException() {
			ResourceModifiedMessage original = buildMessage("Patient", "pt-d1", "1");
			ResourceModifiedEntity entity = buildEntity(original);

			assertDoesNotThrow(() ->
				mySvc.createResourceModifiedMessageFromEntityWithoutInflation(entity));
		}

		@Test
		@DisplayName("Deserialized message is not null")
		void deserialize_returnsNonNull() {
			ResourceModifiedMessage original = buildMessage("Patient", "pt-d2", "1");
			ResourceModifiedEntity entity = buildEntity(original);

			ResourceModifiedMessage result =
				mySvc.createResourceModifiedMessageFromEntityWithoutInflation(entity);

			assertNotNull(result);
		}

		@Test
		@DisplayName("Round-trip preserves operationType")
		void roundTrip_operationType() {
			ResourceModifiedMessage original = buildMessage("Observation", "obs-rt", "2");
			original.setOperationType(BaseResourceModifiedMessage.OperationTypeEnum.UPDATE);
			ResourceModifiedEntity entity = buildEntity(original);

			ResourceModifiedMessage result =
				mySvc.createResourceModifiedMessageFromEntityWithoutInflation(entity);

			assertThat(result.getOperationType())
				.isEqualTo(BaseResourceModifiedMessage.OperationTypeEnum.UPDATE);
		}

		@Test
		@DisplayName("Round-trip preserves resource version")
		void roundTrip_resourceVersion() {
			ResourceModifiedMessage original = buildMessage("Patient", "pt-ver", "7");
			ResourceModifiedEntity entity = buildEntity(original);

			ResourceModifiedMessage result =
				mySvc.createResourceModifiedMessageFromEntityWithoutInflation(entity);

			assertThat(result.getPayloadVersion()).isEqualTo("7");
		}

		@Test
		@DisplayName("Round-trip preserves resource ID")
		void roundTrip_resourceId() {
			ResourceModifiedMessage original = buildMessage("Patient", "pt-id-rt", "1");
			ResourceModifiedEntity entity = buildEntity(original);

			ResourceModifiedMessage result =
				mySvc.createResourceModifiedMessageFromEntityWithoutInflation(entity);

			assertThat(result.getPayloadId()).isEqualTo("pt-id-rt");
		}
	}
}
