package ca.uhn.fhir.jpa.dao.index;

import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.util.CanonicalIdentifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DaoResourceLinkResolverTest {

	@Test
	public void testLinkResolution() {
		DaoResourceLinkResolver resolver = new DaoResourceLinkResolver<JpaPid>();
		CanonicalIdentifier canonicalIdentifier = resolver.extractIdentifierFromUrl("Patient?_patient?" +
			"identifier=http://hapifhir.io/fhir/namingsystem/my_id|123456");
		assertThat(canonicalIdentifier.getSystemElement().getValueAsString()).isEqualTo("http://hapifhir.io/fhir/namingsystem/my_id");
		assertThat(canonicalIdentifier.getValueElement().getValueAsString()).isEqualTo("123456");

		canonicalIdentifier = resolver.extractIdentifierFromUrl("Patient?_patient?" +
			"identifier=http://hapifhir.io/fhir/namingsystem/my_id|123456&identifier=https://www.id.org/identifiers/member|1101331");
		assertThat(canonicalIdentifier.getSystemElement().getValueAsString()).isEqualTo("http://hapifhir.io/fhir/namingsystem/my_id");
		assertThat(canonicalIdentifier.getValueElement().getValueAsString()).isEqualTo("123456");

		canonicalIdentifier = resolver.extractIdentifierFromUrl("Patient?_tag:not=http://hapifhir.io/fhir/namingsystem/mdm-record-status|GOLDEn_rEcorD" +
			"&identifier=https://www.my.org/identifiers/memBER|123456");
		assertThat(canonicalIdentifier.getSystemElement().getValueAsString()).isEqualTo("https://www.my.org/identifiers/memBER");
		assertThat(canonicalIdentifier.getValueElement().getValueAsString()).isEqualTo("123456");

		canonicalIdentifier = resolver.extractIdentifierFromUrl("Patient?_tag:not=http://hapifhir.io/fhir/namingsystem/mdm-record-status|GOLDEn_rEcorD");
		assertNull(canonicalIdentifier);

	}

}


