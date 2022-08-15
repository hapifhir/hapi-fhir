package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.SearchPreferHandlingInterceptor;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SearchPreferHandlingInterceptorJpaTest extends BaseResourceProviderR4Test {

	private SearchPreferHandlingInterceptor mySvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		mySvc = new SearchPreferHandlingInterceptor(mySearchParamRegistry);
		ourRestServer.registerInterceptor(mySvc);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		ourRestServer.unregisterInterceptor(mySvc);
	}


	@Test
	public void testSearchWithInvalidParam_NoHeader() {
		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(new StringClientParam("foo").matches().value("bar"))
				.prettyPrint()
				.returnBundle(Bundle.class)
				.encodedJson()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_content, _id, _lastUpdated, _profile, _security, _source, _tag, _text, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]"));
		}

	}

	@Test
	public void testSearchWithInvalidParam_StrictHeader() {
		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(new StringClientParam("foo").matches().value("bar"))
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_HANDLING + "=" + Constants.HEADER_PREFER_HANDLING_STRICT)
				.prettyPrint()
				.returnBundle(Bundle.class)
				.encodedJson()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_content, _id, _lastUpdated, _profile, _security, _source, _tag, _text, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]"));
		}

	}

	@Test
	public void testSearchWithInvalidParam_UnrelatedPreferHeader() {
		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(new StringClientParam("foo").matches().value("bar"))
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION)
				.prettyPrint()
				.returnBundle(Bundle.class)
				.encodedJson()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_content, _id, _lastUpdated, _profile, _security, _source, _tag, _text, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]"));
		}

	}

	@Test
	public void testSearchWithInvalidParam_LenientHeader() {
		Bundle outcome = myClient
			.search()
			.forResource(Patient.class)
			.where(new StringClientParam("foo").matches().value("bar"))
			.and(Patient.IDENTIFIER.exactly().codes("BLAH"))
			.prettyPrint()
			.returnBundle(Bundle.class)
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_HANDLING + "=" + Constants.HEADER_PREFER_HANDLING_LENIENT)
			.encodedJson()
			.execute();
		assertEquals(0, outcome.getTotal());

		assertEquals(ourServerBase + "/Patient?_format=json&_pretty=true&identifier=BLAH", outcome.getLink(Constants.LINK_SELF).getUrl());
	}

	@Test
	public void testSearchWithChain_Invalid() {
		try {
			myClient
				.search()
				.forResource(Patient.class)
				.where(new StringClientParam("foo.bar").matches().value("bar"))
				.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_HANDLING + "=" + Constants.HEADER_PREFER_HANDLING_STRICT)
				.prettyPrint()
				.returnBundle(Bundle.class)
				.encodedJson()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_content, _id, _lastUpdated, _profile, _security, _source, _tag, _text, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]"));
		}

	}

	@Test
	public void testSearchWithChain_Valid() {
		Bundle outcome = myClient
			.search()
			.forResource(Patient.class)
			.where(new StringClientParam("organization.name").matches().value("bar"))
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_HANDLING + "=" + Constants.HEADER_PREFER_HANDLING_STRICT)
			.prettyPrint()
			.returnBundle(Bundle.class)
			.encodedJson()
			.execute();
		assertEquals(0, outcome.getTotal());
	}


	@Test
	public void testSearchWithModifier_Valid() {
		Bundle outcome = myClient
			.search()
			.forResource(Patient.class)
			.where(new StringClientParam("name").matchesExactly().value("bar"))
			.withAdditionalHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_HANDLING + "=" + Constants.HEADER_PREFER_HANDLING_STRICT)
			.prettyPrint()
			.returnBundle(Bundle.class)
			.encodedJson()
			.execute();
		assertEquals(0, outcome.getTotal());
	}

}
