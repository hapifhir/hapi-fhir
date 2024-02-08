package ca.uhn.fhir.jpa.dao.dstu2;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirResourceDaoDstu2ParseTest extends BaseJpaDstu2Test {
	@Autowired
	DaoRegistry myDaoRegistry;

	@Test
	void testTagRoundTrip() {
	    // given
		Patient resource = new Patient();
		IBaseCoding tag = resource.getMeta().addTag();
		tag.setCode("code");
		tag.setDisplay("display");
		tag.setSystem("oid:123");
		tag.setVersion("v1");
		tag.setUserSelected(true);

		// when
		DaoMethodOutcome daoMethodOutcome = myPatientDao.create(resource, mySrd);
		Patient resourceOut = myPatientDao.read(daoMethodOutcome.getId(), mySrd);

		// then
		List<? extends IBaseCoding> tags = resourceOut.getMeta().getTag();
		assertThat(tags.size()).as("tag is present").isEqualTo(1);
		IBaseCoding tagOut = tags.get(0);
		assertThat(tagOut.getCode()).isEqualTo("code");
		assertThat(tagOut.getDisplay()).isEqualTo("display");
		assertThat(tagOut.getSystem()).isEqualTo("oid:123");
		assertThat(tagOut.getVersion()).isEqualTo("v1");
		assertThat(tagOut.getUserSelected()).isEqualTo(true);
	}


	@Test
	void testSecurityRoundTrip() {
		// given
		Patient resource = new Patient();
		IBaseCoding coding = resource.getMeta().addSecurity();
		coding.setCode("code");
		coding.setDisplay("display");
		coding.setSystem("oid:123");
		coding.setVersion("v1");
		coding.setUserSelected(true);

		// when
		DaoMethodOutcome daoMethodOutcome = myPatientDao.create(resource, mySrd);
		Patient resourceOut = myPatientDao.read(daoMethodOutcome.getId(), mySrd);

		// then
		List<? extends IBaseCoding> tags = resourceOut.getMeta().getSecurity();
		assertThat(tags.size()).as("coding is present").isEqualTo(1);
		IBaseCoding codingOut = tags.get(0);
		assertThat(codingOut.getCode()).isEqualTo("code");
		assertThat(codingOut.getDisplay()).isEqualTo("display");
		assertThat(codingOut.getSystem()).isEqualTo("oid:123");
		assertThat(codingOut.getVersion()).isEqualTo("v1");
		assertThat(codingOut.getUserSelected()).isEqualTo(true);
	}
}
