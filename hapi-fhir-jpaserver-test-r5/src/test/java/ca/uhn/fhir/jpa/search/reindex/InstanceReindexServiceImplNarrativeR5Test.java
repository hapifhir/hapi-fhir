package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.test.utilities.HtmlUtil;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlTable;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the narrative generation in {@link InstanceReindexServiceImpl}. This is a separate test
 * from {@literal ReindexDryRunServiceImplTest} because this test doesn't need the JPA
 * infrastructure.
 */
@SuppressWarnings({"unchecked", "SqlDialectInspection"})
public class InstanceReindexServiceImplNarrativeR5Test {
	private static final Logger ourLog = LoggerFactory.getLogger(InstanceReindexServiceImplNarrativeR5Test.class);
	private final FhirContext myCtx = FhirContext.forR4Cached();
	private final InstanceReindexServiceImpl mySvc = new InstanceReindexServiceImpl();
	private final PartitionSettings myPartitionSettings = new PartitionSettings();
	private final JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	private final ResourceTable myEntity = new ResourceTable();

	@Test
	public void testIndexComboNonUnique() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myComboTokenNonUnique.add(new ResourceIndexedComboTokenNonUnique(myPartitionSettings, myEntity, "Patient?identifier=123"));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("NonUniqueIndexesTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ComboTokenNonUnique");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Patient?identifier=123");
	}

	@Test
	public void testIndexComboUnique() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myComboStringUniques.add(new ResourceIndexedComboStringUnique(myEntity, "Patient?identifier=123", new IdType("Parameter/foo")));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("UniqueIndexesTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ComboStringUnique");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Patient?identifier=123");
	}

	@Test
	public void testIndexMissing() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myTokenParams.add(new ResourceIndexedSearchParamToken(myPartitionSettings, "Observation", "identifier", true));
		SearchParamPresentEntity subject = new SearchParamPresentEntity("subject", false);
		subject.setResource(new ResourceTable());
		subject.setPartitionSettings(myPartitionSettings);
		subject.calculateHashes();
		newParams.mySearchParamPresentEntities.add(subject);

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("MissingIndexesTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("identifier");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Token");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("true");
		assertThat(getBodyCellValue(table, 1, 0)).isEqualTo("subject");
		assertThat(getBodyCellValue(table, 1, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 1, 2)).isEqualTo("Reference");
		assertThat(getBodyCellValue(table, 1, 3)).isEqualTo("true");
	}

	@Test
	public void testIndexNumber() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myNumberParams.add(new ResourceIndexedSearchParamNumber(myPartitionSettings, "Immunization", "dose", BigDecimal.ONE));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("NumberIndexesTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("dose");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Number");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("1");
	}

	@Test
	public void testIndexResourceLink() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myLinks.add(ResourceLink.forLocalReference("Observation.subject", myEntity, "Patient", 123L, "123", new Date(), 555L));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("ResourceLinksTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("Observation.subject");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Reference");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("Patient/123");
		assertThat(getBodyCellValue(table, 0, 4)).isEqualTo("555");
	}

	@Test
	public void testIndexResourceLinkLogical() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myLinks.add(ResourceLink.forLogicalReference("Observation.subject", myEntity, "http://foo/base/Patient/456", new Date()));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("ResourceLinksTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("Observation.subject");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Reference");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("");
		assertThat(getBodyCellValue(table, 0, 4)).isEqualTo("");
		assertThat(getBodyCellValue(table, 0, 5)).isEqualTo("http://foo/base/Patient/456");
	}

	@Test
	public void testIndexResourceLinkAbsolute() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myLinks.add(ResourceLink.forAbsoluteReference("Observation.subject", myEntity, new IdType("http://foo/base/Patient/123"), new Date()));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("ResourceLinksTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("Observation.subject");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Reference");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("");
		assertThat(getBodyCellValue(table, 0, 4)).isEqualTo("");
		assertThat(getBodyCellValue(table, 0, 5)).isEqualTo("http://foo/base/Patient/123");
	}

	@Test
	public void testIndexQuantity() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myQuantityParams.add(new ResourceIndexedSearchParamQuantity(myPartitionSettings, "Observation", "value-quantity", BigDecimal.valueOf(123), "http://unitsofmeasure.org", "kg"));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("QuantityIndexesTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("value-quantity");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Quantity");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("123");
		assertThat(getBodyCellValue(table, 0, 4)).isEqualTo("http://unitsofmeasure.org");
		assertThat(getBodyCellValue(table, 0, 5)).isEqualTo("kg");
	}

	@Test
	public void testIndexQuantityNormalized() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myQuantityNormalizedParams.add(new ResourceIndexedSearchParamQuantityNormalized(myPartitionSettings, "Observation", "value-quantity", 123.0, "http://unitsofmeasure.org", "kg"));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("QuantityIndexesTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("value-quantity");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("QuantityNormalized");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("123.0");
		assertThat(getBodyCellValue(table, 0, 4)).isEqualTo("http://unitsofmeasure.org");
		assertThat(getBodyCellValue(table, 0, 5)).isEqualTo("kg");
	}

	@Test
	public void testIndexString() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myStringParams.add(new ResourceIndexedSearchParamString(myPartitionSettings, myStorageSettings, "Patient", "family", "Simpson", "SIMPSON"));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("StringIndexesTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("family");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("String");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("Simpson");
		assertThat(getBodyCellValue(table, 0, 4)).isEqualTo("SIMPSON");
	}

	@Test
	public void testIndexToken() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myTokenParams.add(new ResourceIndexedSearchParamToken(myPartitionSettings, "Observation", "identifier", "http://id-system", "id-value"));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("TokenIndexesTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("identifier");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Token");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("http://id-system");
		assertThat(getBodyCellValue(table, 0, 4)).isEqualTo("id-value");
	}

	@Test
	public void testIndexUrl() throws IOException {
		// Setup
		ResourceIndexedSearchParams newParams = newParams();
		newParams.myUriParams.add(new ResourceIndexedSearchParamUri(myPartitionSettings, "CodeSystem", "uri", "http://some-codesystem"));

		// Test
		Parameters outcome = mySvc.buildIndexResponse(newParams(), newParams, true, Collections.emptyList());
		ourLog.info("Output:\n{}", myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		// Verify
		HtmlPage narrativeHtml = extractNarrative(outcome);
		HtmlTable table = (HtmlTable) narrativeHtml.getElementById("UriIndexesTable");
		assertThat(getBodyCellValue(table, 0, 0)).isEqualTo("uri");
		assertThat(getBodyCellValue(table, 0, 1)).isEqualTo("ADD");
		assertThat(getBodyCellValue(table, 0, 2)).isEqualTo("Uri");
		assertThat(getBodyCellValue(table, 0, 3)).isEqualTo("http://some-codesystem");
	}

	@Nonnull
	private static HtmlPage extractNarrative(Parameters outcome) throws IOException {
		StringType narrative = (StringType) outcome.getParameter().get(0).getValue();
		HtmlPage narrativeHtml = HtmlUtil.parseAsHtml(narrative.getValueAsString());
		ourLog.info("Narrative:\n{}", narrativeHtml.asXml());
		return narrativeHtml;
	}

	private static String getBodyCellValue(HtmlTable table, int theRow, int theCol) {
		return table.getBodies().get(0).getRows().get(theRow).getCell(theCol).asNormalizedText();
	}

	@Nonnull
	private static ResourceIndexedSearchParams newParams() {
		return ResourceIndexedSearchParams.withSets();
	}

}
