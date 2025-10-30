package ca.uhn.fhir.jpa;

import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class SnapshotGenerationConcurrencyTest extends BaseJpaR4Test {

	public static final int CONCURRENT_THREADS = 20;

	private StructureDefinition myStructDef;
	private StructureDefinition myChildStructDef;

	ExecutorService executor = Executors.newCachedThreadPool();


	@BeforeEach
	void setUp() throws IOException {
		myStructDef = loadResourceFromClasspath(StructureDefinition.class, "/r4/profile-differential-patient-r4.json");
		assertThat(myStructDef.getSnapshot().getElement()).isEmpty();

		StructureDefinition baseStructDef = loadResourceFromClasspath(StructureDefinition.class, "/r4/StructureDefinition-kfdrc-patient.json");
		myStructureDefinitionDao.update(baseStructDef, new SystemRequestDetails());

		myChildStructDef = loadResourceFromClasspath(StructureDefinition.class, "/r4/StructureDefinition-kfdrc-patient-no-phi.json");
		myStructureDefinitionDao.update(myChildStructDef, new SystemRequestDetails());
	}

	@Test
	void testGenerateSnapshotConcurrently() {
		runSuppliedFunctionConcurrently( () -> testGenerateSnapshot(myStructDef) );

		runSuppliedFunctionConcurrently( () -> testGenerateSnapshotChained(myChildStructDef) );
	}

	void runSuppliedFunctionConcurrently(Supplier<Void> theFunctionSupplier) {
		List<CompletableFuture<Void>> futures = new ArrayList<>();

		for (int i = 0; i < CONCURRENT_THREADS; i++) {
			futures.add(CompletableFuture.supplyAsync(theFunctionSupplier, executor));
		}

		List<Void> validatedResources = futures.stream()
			.map(CompletableFuture::join)
			.toList();

		assertThat(validatedResources).hasSize(CONCURRENT_THREADS);
	}

	/**
	 * Make sure that if one SD extends another SD, and the parent SD hasn't been snapshotted itself, the child can
	 * be snapshotted.
	 */
	public Void testGenerateSnapshotChained(StructureDefinition sd2)  {
		StructureDefinition snapshotted = myStructureDefinitionDao.generateSnapshot(sd2, null, null, null);

		assertThat(snapshotted.getSnapshot().getElement()).isNotEmpty();
		return null;
	}

	public Void testGenerateSnapshot(StructureDefinition differential)  {
		// Create a validation chain that includes default validation support and a
		// snapshot generator
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(myFhirContext);
		SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(myFhirContext);
		ValidationSupportChain chain = new ValidationSupportChain(defaultSupport, snapshotGenerator);

		// Generate the snapshot
		chain.generateSnapshot(new ValidationSupportContext(chain), differential, "http://foo", null, "THE BEST PROFILE");

		String url = "http://foo";
		String webUrl = null;
		String name = "Foo Profile";
		StructureDefinition output = myStructureDefinitionDao.generateSnapshot(differential, url, webUrl, name);

		assertThat(output.getSnapshot().getElement()).hasSize(54);
		return null;
	}
}
