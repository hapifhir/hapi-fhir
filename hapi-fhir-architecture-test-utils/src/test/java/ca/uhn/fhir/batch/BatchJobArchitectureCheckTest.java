package ca.uhn.fhir.batch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.fail;

@AnalyzeClasses(
	packages = { "ca.uhn.fhir" },
	importOptions = {ImportOption.DoNotIncludeTests.class}
)
public class BatchJobArchitectureCheckTest {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchJobArchitectureCheckTest.class);

	/**
	 * Map of CreationMethod (where the SystemRequestDetails is created) -> justification.
	 * Do not add to this map unless the justification is valid.
	 */
	private static final Map<String, String> DIRECT_CREATION_ALLOWED = Map.of(
		"ca.uhn.fhir.jpa.batch2.Batch2DaoSvcImpl.streamResourceIdsWithUrl(java.util.Date, java.util.Date, java.lang.String, ca.uhn.fhir.interceptor.model.RequestPartitionId)", "Shared service used many places; passing RequestDetails down isn't worth it.",
		"ca.uhn.fhir.jpa.bulk.export.svc.BulkExportMdmFullResourceExpander.expandGroup(java.lang.String, ca.uhn.fhir.interceptor.model.RequestPartitionId)", "PartitionId is passed down from GenerateWorkPackagesV3Step.",
		"ca.uhn.fhir.jpa.bulk.export.svc.BulkExportMdmFullResourceExpander.expandPatient(java.lang.String, ca.uhn.fhir.interceptor.model.RequestPartitionId)", "Partition id is passed down from GenerateWorkPackagesV3Step.",
		"ca.uhn.fhir.jpa.mdm.svc.GoldenResourceSearchSvcImpl.fetchResourceIdsPageWithResourceType(java.util.Date, java.util.Date, java.lang.String, ca.uhn.fhir.interceptor.model.RequestPartitionId)", "Partition id is passed down from ResourceIdListStep",
		"ca.uhn.fhir.jpa.packages.PackageInstallerSvcImpl.createRequestDetails()", "Shared service called by many places; passing down a RequestDetails object not worth it.",
		"ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc.determineReadPartitionForRequest(ca.uhn.fhir.rest.api.server.RequestDetails, ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails)", "Created from a RequestPartition that already included a partition id",
		"ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc.determineCreatePartitionForRequest(ca.uhn.fhir.rest.api.server.RequestDetails, org.hl7.fhir.instance.model.api.IBaseResource, java.lang.String)", "Created from a RequestPartition that already included a partition id",
		"ca.uhn.fhir.jpa.term.TermDeferredStorageSvcImpl.deleteTermCodeSystemOffline(java.lang.Long)", "Shared service call; passing RequestDetails down is not worth it.",
		"ca.uhn.fhir.jpa.term.TermDeferredStorageSvcImpl.deleteTermCodeSystemVersionOffline(java.lang.Long)", "Shared service; passing RequestDetails down is not worth it.",
		"ca.uhn.fhir.batch2.jobs.imprt.ConsumeFilesStepV1.storeResources(java.util.List, ca.uhn.fhir.interceptor.model.RequestPartitionId)", "Direct creation uses partition id passed in or partitionsettings."
	);

	private static List<BatchClassWalker.CallChain> ourViolations;

	/**
	 * We walk the classes only once.
	 * `@AnalyzeClasses` caches the JavaClasses it injects,
	 * so the walk is effectively the exact same for all
	 * tests here.
	 * We start in the same root; we omit the same default classes.
	 */
	private static void doClassWalk(JavaClasses theClasses) {
		if (ourViolations == null) {
			ourViolations = new BatchClassWalker(theClasses)
				.rootsIn("ca.uhn.fhir")
				.stopAt(BatchClassWalker.DEFAULT_CLASSES_TO_OMIT)
				.findSystemRequestDetailsCreation();
		}
	}

	@ArchTest
	public void everySystemRequestDetailsHasAPartition(JavaClasses theJavaClasses) {
		doClassWalk(theJavaClasses);
		List<BatchClassWalker.CallChain> violations = ourViolations
			.stream()
			.filter(c -> !c.PartitionSet())
			.toList();

		checkViolations(violations);
	}

	@ArchTest
	public void batchJobsUseNewSystemRequestDetails(JavaClasses theClasses) {
		doClassWalk(theClasses);
		List<BatchClassWalker.CallChain> violations = ourViolations
			.stream()
			.filter(c -> !DIRECT_CREATION_ALLOWED.containsKey(c.CreationMethod()))
			.toList();

		checkViolations(violations);
	}

	private static void checkViolations(List<BatchClassWalker.CallChain> violations) {
		if (!violations.isEmpty()) {
			ourLog.error("Found {} errors", violations.size());

			StringBuilder vsb = new StringBuilder();
			violations.forEach(c -> {
				vsb.append("\n");
				/*
				 * NB: This line is commented out so future devs
				 * can quickly figure out what the entry should be for the
				 * DIRECT_CREATION_ALLOWED map above in the case that:
				 * * a method signature changed (but the logic didn't need to)
				 * * a new call is added that it's not worth passing down a RequestDetails obj
				 */
//				vsb.append(c.CreationMethod()).append("\n");
				vsb.append(c.CreationSite())
					.append(" Call Stack : ")
					.append("(")
					.append(String.join(", ", c.Methods()))
					.append(")");
			});

			String errorMsg = vsb.toString();
			fail(errorMsg);
		}
	}
}
