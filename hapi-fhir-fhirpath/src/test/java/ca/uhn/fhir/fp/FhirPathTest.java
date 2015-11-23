package ca.uhn.fhir.fp;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBase;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.TimeDt;

public class FhirPathTest {

	private static final FhirContext ourCtx = FhirContext.forDstu2();

	@Test
	public void testPathSelection() {
		Patient pt = new Patient();
		pt.addName().addFamily("FAM1a").addFamily("FAM1b").addGiven("GIV1a").addGiven("GIV1b");
		pt.addName().addFamily("FAM2a").addFamily("FAM2b").addGiven("GIV2a").addGiven("GIV2b");
		pt.addContact().getName().addFamily("FAM3a").addFamily("FAM3b").addGiven("GIV3a").addGiven("GIV3b");

		FhirContext ctx = ourCtx;

		FhirPathCompiler compiler = new FhirPathCompiler(ctx);
		compiler.compile("name.family");
		List<IBase> output = compiler.applyTo(pt);

		assertThat(output, contains((IBase) new StringDt("FAM1a"), new StringDt("FAM1b"), new StringDt("FAM2a"), new StringDt("FAM2b")));
	}

	@Test
	public void testPathSelectionWithLeadingStar() {
		Patient pt = new Patient();
		pt.addName().addFamily("FAM1a").addFamily("FAM1b").addGiven("GIV1a").addGiven("GIV1b");
		pt.addName().addFamily("FAM2a").addFamily("FAM2b").addGiven("GIV2a").addGiven("GIV2b");
		pt.addContact().getName().addFamily("FAM3a").addFamily("FAM3b").addGiven("GIV3a").addGiven("GIV3b");

		FhirContext ctx = ourCtx;

		FhirPathCompiler compiler = new FhirPathCompiler(ctx);
		compiler.compile("*.family");
		List<IBase> output = compiler.applyTo(pt);

		assertEquals(4, output.size());
		assertThat(output, contains((IBase) new StringDt("FAM1a"), new StringDt("FAM1b"), new StringDt("FAM2a"), new StringDt("FAM2b")));
	}

	@Test
	public void testPathSelectionWithTrailingStar() {
		Patient pt = new Patient();
		pt.addName().addFamily("FAM1a").addFamily("FAM1b").addGiven("GIV1a").addGiven("GIV1b");
		pt.addName().addFamily("FAM2a").addFamily("FAM2b").addGiven("GIV2a").addGiven("GIV2b");
		pt.addContact().getName().addFamily("FAM3a").addFamily("FAM3b").addGiven("GIV3a").addGiven("GIV3b");

		FhirContext ctx = ourCtx;

		FhirPathCompiler compiler = new FhirPathCompiler(ctx);
		compiler.compile("name.*");
		List<IBase> output = compiler.applyTo(pt);

		//@formatter:off
		assertThat(output, containsInAnyOrder((IBase) 
				new StringDt("FAM1a"), new StringDt("FAM1b"), 
				new StringDt("GIV1a"), new StringDt("GIV1b"),
				new StringDt("FAM2a"), new StringDt("FAM2b"), 
				new StringDt("GIV2a"), new StringDt("GIV2b"))
				);
		//@formatter:on
	}

	@Test
	public void testPathSelectionWithLeadingStarStar() {
		Patient pt = new Patient();
		pt.addName().addFamily("FAM1a").addFamily("FAM1b").addGiven("GIV1a").addGiven("GIV1b");
		pt.addName().addFamily("FAM2a").addFamily("FAM2b").addGiven("GIV2a").addGiven("GIV2b");
		pt.addContact().getName().addFamily("FAM3a").addFamily("FAM3b").addGiven("GIV3a").addGiven("GIV3b");

		FhirContext ctx = ourCtx;

		FhirPathCompiler compiler = new FhirPathCompiler(ctx);
		compiler.compile("**.family");
		List<IBase> output = compiler.applyTo(pt);

		assertEquals(6, output.size());
		assertThat(output, containsInAnyOrder((IBase) new StringDt("FAM1a"), new StringDt("FAM1b"), new StringDt("FAM2a"), new StringDt("FAM2b"), new StringDt("FAM3a"), new StringDt("FAM3b")));
	}

	@Test
	public void testPathSelectionWithWhereClause() {
		Patient pt = new Patient();
		pt.addTelecom().setUse(ContactPointUseEnum.HOME).setValue("TELHOME1");
		pt.addTelecom().setUse(ContactPointUseEnum.WORK).setValue("TELWORK1");
		pt.addTelecom().setUse(ContactPointUseEnum.HOME).setValue("TELHOME2");
		pt.addTelecom().setUse(ContactPointUseEnum.WORK).setValue("TELWORK2");

		FhirContext ctx = ourCtx;

		FhirPathCompiler compiler = new FhirPathCompiler(ctx);
		compiler.compile("telecom.where(use = 'home').value");
		List<IBase> output = compiler.applyTo(pt);

		assertThat(output, containsInAnyOrder((IBase) new StringDt("TELHOME1"), new StringDt("TELHOME2")));
	}

	@Test
	public void testPathSelectionWithChoiceWithoutWildcard() {
		Observation obs = new Observation();
		obs.addComponent().setValue(new StringDt("COMP1"));
		obs.addComponent().setValue(new TimeDt("11:22"));
		
		FhirContext ctx = ourCtx;

		FhirPathCompiler compiler = new FhirPathCompiler(ctx);
		compiler.compile("component.valueString");
		List<IBase> output = compiler.applyTo(obs);

		assertThat(output, containsInAnyOrder((IBase) new StringDt("COMP1")));
	}

	@Test
	public void testPathSelectionWithChoiceWithWildcard() {
		Observation obs = new Observation();
		obs.addComponent().setValue(new StringDt("COMP1"));
		obs.addComponent().setValue(new TimeDt("11:22"));
		
		FhirContext ctx = ourCtx;

		FhirPathCompiler compiler = new FhirPathCompiler(ctx);
		compiler.compile("component.value[x]");
		List<IBase> output = compiler.applyTo(obs);

		assertThat(output, containsInAnyOrder((IBase) new StringDt("COMP1"), new TimeDt("11:22")));
	}

}
