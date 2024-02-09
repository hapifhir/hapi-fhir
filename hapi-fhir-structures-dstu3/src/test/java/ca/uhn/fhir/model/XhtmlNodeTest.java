package ca.uhn.fhir.model;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.ExplanationOfBenefit;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XhtmlNodeTest {


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	
	@Test
	public void testParseRsquo() {
		XhtmlNode dt = new XhtmlNode();
		dt.setValueAsString("It&rsquo;s January again");
		assertThat(dt.getValueAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">It’s January again</div>");
		assertThat(new XhtmlNode().setValue(dt.getValue()).getValueAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">It’s January again</div>");
	}

	/**
	 * See #1658
	 */
	@Test
	public void testLangAttributePreserved() {
		XhtmlNode dt = new XhtmlNode();
		dt.setValueAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en-US\">help i'm a bug</div>");
		assertThat(dt.getValueAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en-US\">help i'm a bug</div>");
		assertThat(new XhtmlNode().setValue(dt.getValue()).getValueAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en-US\">help i'm a bug</div>");
	}

	/**
	 * See #443
	 */
	@Test
	public void testDeepEquals() {
		String input = 
			"<ExplanationOfBenefit xmlns=\"http://hl7.org/fhir\">" +
			"<text>" +
			  "<status value=\"generated\"/>" + 
			  "<div xmlns=\"http://www.w3.org/1999/xhtml\">A human-readable rendering of the ExplanationOfBenefit</div>" +
			"</text>" +
			"</ExplanationOfBenefit>";
		
		ExplanationOfBenefit copy1 = ourCtx.newXmlParser().parseResource(ExplanationOfBenefit.class, input);
		ExplanationOfBenefit copy2 = ourCtx.newXmlParser().parseResource(ExplanationOfBenefit.class, input);

		assertThat(copy1.equalsDeep(copy2)).isTrue();
		assertThat(copy1.equalsShallow(copy2)).isTrue();
		
	}
	
}
