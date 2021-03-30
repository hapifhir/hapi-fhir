package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.SampledData;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoR4MetaTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4MetaTest.class);

	/**
	 * See #1731
	 */
	@Test
	public void testMetaExtensionsPreserved() {
		Patient patient = new Patient();
		patient.setActive(true);
		patient.getMeta().addExtension("http://foo", new StringType("hello"));
		IIdType id = myPatientDao.create(patient).getId();

		patient = myPatientDao.read(id);
		assertTrue(patient.getActive());
		assertEquals(1, patient.getMeta().getExtensionsByUrl("http://foo").size());
		assertEquals("hello", patient.getMeta().getExtensionByUrl("http://foo").getValueAsPrimitive().getValueAsString());
	}

	/**
	 * See #1731
	 */
	@Test
	public void testBundleInnerResourceMetaIsPreserved() {
		Patient patient = new Patient();
		patient.setActive(true);
		patient.getMeta().setLastUpdatedElement(new InstantType("2011-01-01T12:12:12Z"));
		patient.getMeta().setVersionId("22");
		patient.getMeta().addProfile("http://foo");
		patient.getMeta().addTag("http://tag", "value", "the tag");
		patient.getMeta().addSecurity("http://tag", "security", "the tag");
		patient.getMeta().addExtension("http://foo", new StringType("hello"));

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.COLLECTION);
		bundle.addEntry().setResource(patient);
		IIdType id = myBundleDao.create(bundle).getId();

		bundle = myBundleDao.read(id);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		patient = (Patient) bundle.getEntryFirstRep().getResource();
		assertTrue(patient.getActive());
		assertEquals(1, patient.getMeta().getExtensionsByUrl("http://foo").size());
		assertEquals("22", patient.getMeta().getVersionId());
		assertEquals("http://foo", patient.getMeta().getProfile().get(0).getValue());
		assertEquals("hello", patient.getMeta().getExtensionByUrl("http://foo").getValueAsPrimitive().getValueAsString());
	}

	/**
	 * See #1731
	 */
	@Test
	public void testMetaValuesNotStoredAfterDeletion() {
		Patient patient = new Patient();
		patient.setActive(true);
		patient.getMeta().addProfile("http://foo");
		patient.getMeta().addTag("http://tag", "value", "the tag");
		patient.getMeta().addSecurity("http://tag", "security", "the tag");
		IIdType id = myPatientDao.create(patient).getId();

		Meta meta = new Meta();
		meta.addProfile("http://foo");
		meta.addTag("http://tag", "value", "the tag");
		meta.addSecurity("http://tag", "security", "the tag");
		myPatientDao.metaDeleteOperation(id, meta, mySrd);

		patient = myPatientDao.read(id);
		assertThat(patient.getMeta().getProfile(), empty());
		assertThat(patient.getMeta().getTag(), empty());
		assertThat(patient.getMeta().getSecurity(), empty());
	}

}
