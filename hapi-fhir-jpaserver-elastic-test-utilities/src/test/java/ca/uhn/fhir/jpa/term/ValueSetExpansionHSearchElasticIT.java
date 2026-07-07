package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticHSearch;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.jpa.test.ValueSetExpansionHSearchTestCases;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;

/**
 * Runs all ValueSetExpansionHSearchTestCases and IValueSetExpansionIT tests using Elasticsearch configuration,
 * plus Elasticsearch-specific tests for large ValueSets and nested object limits.
 */
// Created by Claude Opus 4.6
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestR4ConfigWithElasticHSearch.class)
@RequiresDocker
public class ValueSetExpansionHSearchElasticIT extends ValueSetExpansionHSearchTestCases implements IValueSetExpansionIT {

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public ITermDeferredStorageSvc getTerminologyDefferedStorageService() {
		return myTerminologyDeferredStorageSvc;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public IFhirResourceDaoValueSet<ValueSet> getValueSetDao() {
		return myValueSetDao;
	}

	@Override
	public JpaStorageSettings getJpaStorageSettings() {
		return myStorageSettings;
	}

	@Override
	public Batch2JobHelper getBatch2JobHelper() {
		return myBatch2JobHelper;
	}

	/**
	 * Reproduced: https://github.com/hapifhir/hapi-fhir/issues/3419
	 */
	@Test
	void expandValueSet_largerThanElasticDefaultScrollSize_expandsSuccessfully() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("SYSTEM NAME");
		codeSystem.setVersion("SYSTEM VERSION");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
		ResourceTable csResource = myResourceTableDao.findById(JpaPid.fromId(id.getIdPartAsLong())).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		codeSystemVersion.setResource(csResource);

		addTermConcepts(codeSystemVersion, 11_000);

		ValueSet valueSet = getValueSetWithAllCodeSystemConcepts(codeSystemVersion.getCodeSystemVersionId());

		myValueSetDao.update(valueSet, newSrd());

		myBatch2JobHelper.awaitNoJobsRunning();

		Slice<TermValueSet> page = runInTransaction(() ->
			myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 1), TermValueSetPreExpansionStatusEnum.EXPANDED));
		assertThat(page.getContent()).hasSize(1);
	}

	/**
	 * Reproduced: https://github.com/hapifhir/hapi-fhir/issues/3992
	 */
	@Test
	void expandValueSet_moreThanElasticDefaultNestedObjectCount_storesSuccessfully() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("SYSTEM NAME");
		codeSystem.setVersion("SYSTEM VERSION");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
		ResourceTable csResource = myResourceTableDao.findById(JpaPid.fromId(id.getIdPartAsLong())).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
		codeSystemVersion.setResource(csResource);

		TermConcept tc = new TermConcept(codeSystemVersion, "test-code-1");

		addTermConceptProperties(tc, 10_100, 200);

		codeSystemVersion.getConcepts().add(tc);

		ValueSet valueSet = new ValueSet();
		valueSet.setId(LOINC_ALL_VALUESET_ID);
		valueSet.setUrl(CS_URL + "/vs");
		valueSet.setVersion(codeSystemVersion.getCodeSystemVersionId());
		valueSet.setName("All LOINC codes");
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.setDate(new Date());
		valueSet.setDescription("A value set that includes all LOINC codes");
		valueSet.getCompose().addInclude().setSystem(CS_URL).setVersion(codeSystemVersion.getCodeSystemVersionId());

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(codeSystem, codeSystemVersion,
			new SystemRequestDetails(), Collections.singletonList(valueSet), Collections.emptyList());
	}

	private ValueSet getValueSetWithAllCodeSystemConcepts(String theCodeSystemVersionId) {
		ValueSet vs = new ValueSet();
		vs.setId(LOINC_ALL_VALUESET_ID);
		vs.setUrl(CS_URL + "/vs");
		vs.setVersion(theCodeSystemVersionId);
		vs.setName("All LOINC codes");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.setDate(new Date());
		vs.setDescription("A value set that includes all LOINC codes");
		vs.getCompose().addInclude().setSystem(CS_URL).setVersion(theCodeSystemVersionId);
		return vs;
	}

	private void addTermConcepts(TermCodeSystemVersion theCs, int theTermConceptQty) {
		for (int i = 0; i < theTermConceptQty; i++) {
			TermConcept tc = new TermConcept(theCs, String.format("code-%05d", i));
			theCs.getConcepts().add(tc);
		}
	}

	private void addTermConceptProperties(TermConcept theTermConcept, int thePropertiesCount, int thePropertyKeysCount) {
		int propsCreated = 0;
		while (propsCreated < thePropertiesCount) {
			int propKeysCreated = 0;
			while (propKeysCreated < thePropertyKeysCount && propsCreated < thePropertiesCount) {
				String propKey = String.format("%05d", propKeysCreated);
				String propSeq = String.format("%05d", propsCreated);
				theTermConcept.addPropertyString("prop-key-" + propKey, "value-" + propSeq);

				propKeysCreated++;
				propsCreated++;
			}
		}
	}
}
