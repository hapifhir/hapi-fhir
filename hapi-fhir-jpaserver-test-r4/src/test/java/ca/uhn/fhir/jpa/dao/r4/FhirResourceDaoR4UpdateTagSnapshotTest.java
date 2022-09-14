package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class FhirResourceDaoR4UpdateTagSnapshotTest extends BaseJpaR4Test {

	@Test
	public void testUpdateWithDuplicateTagsWithHeader() {
		when(mySrd.getHeaders(eq(JpaConstants.HEADER_META_SNAPSHOT_MODE))).thenReturn(Lists.newArrayList("TAG"));

		Patient p = new Patient();
		p.setId("A");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.getMeta().addTag("urn:foo", "bar2", "baz");
		p.getMeta().addTag("urn:foo", "bar2", "baz");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = myPatientDao.read(new IdType("A"), mySrd);
		assertEquals(2, p.getMeta().getTag().size());

		p = new Patient();
		p.setId("A");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = myPatientDao.read(new IdType("A"), mySrd);
		assertEquals("1", p.getIdElement().getVersionIdPart());
		assertEquals(true, p.getActive());
		assertEquals(1, p.getMeta().getTag().size());
	}

	@Test
	public void testUpdateWithFewerTagsNoHeader() {
		Patient p = new Patient();
		p.setId("A");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.getMeta().addTag("urn:foo", "bar2", "baz");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = new Patient();
		p.setId("A");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = myPatientDao.read(new IdType("A"), mySrd);
		assertEquals("1", p.getIdElement().getVersionIdPart());
		assertEquals(true, p.getActive());
		assertEquals(2, p.getMeta().getTag().size());
		assertEquals("urn:foo", p.getMeta().getTag().get(0).getSystem());
		assertThat(p.getMeta().getTag().get(0).getCode(), Matchers.anyOf(Matchers.equalTo("bar"), Matchers.equalTo("bar2")));
	}
	@Test
	public void testUpdateWithFewerTagsWithHeader() {
		when(mySrd.getHeaders(eq(JpaConstants.HEADER_META_SNAPSHOT_MODE))).thenReturn(Lists.newArrayList("TAG"));

		Patient p = new Patient();
		p.setId("A");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.getMeta().addTag("urn:foo", "bar2", "baz");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = new Patient();
		p.setId("A");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = myPatientDao.read(new IdType("A"), mySrd);
		assertEquals("1", p.getIdElement().getVersionIdPart());
		assertEquals(true, p.getActive());
		assertEquals(1, p.getMeta().getTag().size());
		assertEquals("urn:foo", p.getMeta().getTag().get(0).getSystem());
		assertEquals("bar", p.getMeta().getTag().get(0).getCode());
		assertEquals("baz", p.getMeta().getTag().get(0).getDisplay());
	}

	@Test
	public void testUpdateWithNoTagsNoHeader() {
		Patient p = new Patient();
		p.setId("A");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = new Patient();
		p.setId("A");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = myPatientDao.read(new IdType("A"), mySrd);
		assertEquals("1", p.getIdElement().getVersionIdPart());
		assertEquals(true, p.getActive());
		assertEquals(1, p.getMeta().getTag().size());
		assertEquals("urn:foo", p.getMeta().getTag().get(0).getSystem());
		assertEquals("bar", p.getMeta().getTag().get(0).getCode());
		assertEquals("baz", p.getMeta().getTag().get(0).getDisplay());
	}

	@Test
	public void testUpdateWithNoTagsWithHeader() {
		when(mySrd.getHeaders(eq(JpaConstants.HEADER_META_SNAPSHOT_MODE))).thenReturn(Lists.newArrayList("TAG"));

		Patient p = new Patient();
		p.setId("A");
		p.getMeta().addTag("urn:foo", "bar", "baz");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = new Patient();
		p.setId("A");
		p.setActive(true);
		myPatientDao.update(p, mySrd);

		p = myPatientDao.read(new IdType("A"), mySrd);
		assertEquals(true, p.getActive());
		assertEquals(0, p.getMeta().getTag().size());
		assertEquals("1", p.getIdElement().getVersionIdPart());
	}


}
