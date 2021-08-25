package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.provider.r4.ResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AAAATests extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4VersionedReferenceTest.class);

	@AfterEach
	public void afterEach() {
		myFhirCtx.getParserOptions().setStripVersionsFromReferences(true);
		myFhirCtx.getParserOptions().getDontStripVersionsFromReferencesAtPaths().clear();
		myDaoConfig.setDeleteEnabled(new DaoConfig().isDeleteEnabled());
		myModelConfig.setRespectVersionsForSearchIncludes(new ModelConfig().isRespectVersionsForSearchIncludes());
		myModelConfig.setAutoVersionReferenceAtPaths(new ModelConfig().getAutoVersionReferenceAtPaths());
	}


	@Test
	@DisplayName("GH-2901 Test no NPE is thrown on autoversioned references")
	public void testNoNpeMinimal() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(false);
		myModelConfig.setAutoVersionReferenceAtPaths("Observation.subject");

//		ParserOptions options = new ParserOptions();
//		options.setDontStripVersionsFromReferencesAtPaths("Observation.subject");
//		myFhirCtx.setParserOptions(options);

		Patient patient = new Patient();
		patient.setId("Patient/RED");
		myPatientDao.update(patient);

		Observation obs = new Observation();
		obs.setId("Observation/DEF");
		obs.setSubject(new Reference("Patient/RED"));
		BundleBuilder builder = new BundleBuilder(myFhirCtx);
		builder.addTransactionUpdateEntry(obs);

		mySystemDao.transaction(new SystemRequestDetails(), (Bundle) builder.getBundle());
	}


}
