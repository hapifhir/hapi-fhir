package ca.uhn.fhir.jpa.dao.dstu2;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
		assertEquals("code", tagOut.getCode());
		assertEquals("display", tagOut.getDisplay());
		assertEquals("oid:123", tagOut.getSystem());
		assertEquals("v1", tagOut.getVersion());
		assertEquals(true, tagOut.getUserSelected());
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
		assertEquals("code", codingOut.getCode());
		assertEquals("display", codingOut.getDisplay());
		assertEquals("oid:123", codingOut.getSystem());
		assertEquals("v1", codingOut.getVersion());
		assertEquals(true, codingOut.getUserSelected());
	}
}
