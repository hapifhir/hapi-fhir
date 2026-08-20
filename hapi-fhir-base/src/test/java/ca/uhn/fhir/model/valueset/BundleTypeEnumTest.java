// Created by claude-opus-4-7
package ca.uhn.fhir.model.valueset;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BundleTypeEnumTest {

	/**
	 * Verifies that all codes from the FHIR Bundle.type ValueSet
	 * (https://hl7.org/fhir/valueset-bundle-type.html) are present.
	 */
	@Test
	public void forCode_returnsEnumForAllBundleTypeValueSetCodes() {
		assertThat(BundleTypeEnum.forCode("document")).isEqualTo(BundleTypeEnum.DOCUMENT);
		assertThat(BundleTypeEnum.forCode("message")).isEqualTo(BundleTypeEnum.MESSAGE);
		assertThat(BundleTypeEnum.forCode("transaction")).isEqualTo(BundleTypeEnum.TRANSACTION);
		assertThat(BundleTypeEnum.forCode("transaction-response")).isEqualTo(BundleTypeEnum.TRANSACTION_RESPONSE);
		assertThat(BundleTypeEnum.forCode("batch")).isEqualTo(BundleTypeEnum.BATCH);
		assertThat(BundleTypeEnum.forCode("batch-response")).isEqualTo(BundleTypeEnum.BATCH_RESPONSE);
		assertThat(BundleTypeEnum.forCode("history")).isEqualTo(BundleTypeEnum.HISTORY);
		assertThat(BundleTypeEnum.forCode("searchset")).isEqualTo(BundleTypeEnum.SEARCHSET);
		assertThat(BundleTypeEnum.forCode("collection")).isEqualTo(BundleTypeEnum.COLLECTION);
	}
}
