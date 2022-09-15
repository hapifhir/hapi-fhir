package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.dstu2.BaseJpaDstu2Test;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.r4.model.Coding;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminologySvcImplDstu2Test extends BaseJpaDstu2Test {

	@Autowired
	protected ITermReadSvc myTermReadSvc;

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	protected ServletRequestDetails mySrd;

	@Test
	public void testToCanonicalCoding() {
		TermReadSvcDstu2 myReadSvc = new TermReadSvcDstu2();
		IBaseCoding myCoding = new CodingDt("dstuSystem", "dstuCode");
		Coding convertedCoding = myReadSvc.toCanonicalCoding(myCoding);
		assertEquals("dstuCode", convertedCoding.getCode());
		assertEquals("dstuSystem", convertedCoding.getSystem());
	}

	@Test
	public void testFindCodesBelowBuiltInCodeSystem() {
		List<FhirVersionIndependentConcept> concepts;
		Set<String> codes;

		ValueSet upload = new ValueSet();
		upload.setId(new IdDt("testVs"));
		upload.setUrl("http://myVs");
		ValueSet.CodeSystem codeSystem = new ValueSet.CodeSystem().setSystem("http://myCs");
		codeSystem.addConcept(new ValueSet.CodeSystemConcept().setCode("codeA").addConcept(new ValueSet.CodeSystemConcept().setCode("codeAB")));
		upload.setCodeSystem(codeSystem);
		myValueSetDao.update(upload, mySrd);

		concepts = myTermReadSvc.findCodesBelow("http://myVs", "codeA");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("codeA", "codeAB"));

		concepts = myTermReadSvc.findCodesBelow("http://myVs", "codeAB");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("codeAB"));

		// Unknown code
		concepts = myTermReadSvc.findCodesBelow("http://myVs", "FOO");
		codes = toCodes(concepts);
		assertThat(codes, empty());

		// Unknown system
		concepts = myTermReadSvc.findCodesBelow("http://myVs2222", "codeA");
		codes = toCodes(concepts);
		assertThat(codes, empty());
	}

	@Test
	public void testFindCodesAboveBuiltInCodeSystem() {
		List<FhirVersionIndependentConcept> concepts;
		Set<String> codes;

		ValueSet upload = new ValueSet();
		upload.setId(new IdDt("testVs"));
		upload.setUrl("http://myVs");
		ValueSet.CodeSystem codeSystem = new ValueSet.CodeSystem().setSystem("http://myCs");
		codeSystem.addConcept(new ValueSet.CodeSystemConcept().setCode("codeA").addConcept(new ValueSet.CodeSystemConcept().setCode("codeAB")));
		upload.setCodeSystem(codeSystem);
		myValueSetDao.update(upload, mySrd);

		concepts = myTermReadSvc.findCodesAbove("http://myVs", "codeA");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("codeA"));

		concepts = myTermReadSvc.findCodesAbove("http://myVs", "codeAB");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("codeA", "codeAB"));

		// Unknown code
		concepts = myTermReadSvc.findCodesAbove("http://myVs", "FOO");
		codes = toCodes(concepts);
		assertThat(codes, empty());

		// Unknown system
		concepts = myTermReadSvc.findCodesBelow("http://myVs2222", "codeA");
		codes = toCodes(concepts);
		assertThat(codes, empty());
	}


}
