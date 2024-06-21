package ca.uhn.fhir.jpa.term;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.dao.r4b.BaseJpaR4BTest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.CodeSystem;
import org.hl7.fhir.r4b.model.ValueSet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ValueSetExpansionR4BTest extends BaseJpaR4BTest {

	/**
	 * This test intended to check that version of included CodeSystem in task-code ValueSet is correct
	 * There is a typo in the FHIR Specification <a href="http://hl7.org/fhir/R4B/valueset-task-code.xml.html"/>
	 * As agreed in FHIR-30377 included CodeSystem version for task-code ValueSet changed from 3.6.0 to 4.3.0
	 * <a href="https://jira.hl7.org/browse/FHIR-30377">Source resources for task-code ValueSet reference old version of CodeSystem</a>
	 */
	@Test
	public void testExpandTaskCodeValueSet_withCorrectedCodeSystemVersion_willExpandCorrectly() throws IOException {
		// load validation file
		Bundle r4bValueSets = loadResourceFromClasspath(Bundle.class, "/org/hl7/fhir/r4b/model/valueset/valuesets.xml");
		ValueSet taskCodeVs = (ValueSet) findResourceByFullUrlInBundle(r4bValueSets, "http://hl7.org/fhir/ValueSet/task-code");
		CodeSystem taskCodeCs = (CodeSystem) findResourceByFullUrlInBundle(r4bValueSets, "http://hl7.org/fhir/CodeSystem/task-code");

		// check valueSet and codeSystem versions
		String expectedCodeSystemVersion = "4.3.0";
		assertEquals(expectedCodeSystemVersion, taskCodeCs.getVersion());
		assertEquals(expectedCodeSystemVersion, taskCodeVs.getVersion());
		assertEquals(expectedCodeSystemVersion, taskCodeVs.getCompose().getInclude().get(0).getVersion());

		myCodeSystemDao.create(taskCodeCs);
		IIdType id = myValueSetDao.create(taskCodeVs).getId();

		ValueSet expandedValueSet = myValueSetDao.expand(id, new ValueSetExpansionOptions(), mySrd);

		// check expansion size and include CodeSystem version
		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(7);
		assertThat(expandedValueSet.getCompose().getInclude()).hasSize(1);
		assertEquals(expectedCodeSystemVersion, expandedValueSet.getCompose().getInclude().get(0).getVersion());
	}

	private IBaseResource findResourceByFullUrlInBundle(Bundle thebundle, String theFullUrl) {
		Optional<Bundle.BundleEntryComponent> bundleEntry = thebundle.getEntry().stream()
			.filter(entry -> theFullUrl.equals(entry.getFullUrl()))
			.findFirst();
		if (bundleEntry.isEmpty()) {
			fail("Can't find resource: " + theFullUrl);
		}
		return bundleEntry.get().getResource();
	}

}
