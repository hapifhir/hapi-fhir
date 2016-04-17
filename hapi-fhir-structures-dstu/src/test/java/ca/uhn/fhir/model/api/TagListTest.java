package ca.uhn.fhir.model.api;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.util.TestUtil;

public class TagListTest {

	private FhirContext myCtx = FhirContext.forDstu1();

	@Test
	public void testEquals() {
		TagList tagList1 = new TagList();
		tagList1.addTag(null, "Dog", "Puppies");
		tagList1.addTag("http://foo", "Cat", "Kittens");

		TagList tagList2 = new TagList();
		tagList2.addTag(null, "Dog", "Puppies");
		tagList2.addTag("http://foo", "Cat", "Kittens");
		
		assertEquals(tagList1,tagList2);
	}

	@Test
	public void testEqualsIgnoresLabel() {
		TagList tagList1 = new TagList();
		tagList1.addTag(null, "Dog", "AAAA");
		tagList1.addTag("http://foo", "Cat", "BBBB");

		TagList tagList2 = new TagList();
		tagList2.addTag(null, "Dog", "Puppies");
		tagList2.addTag("http://foo", "Cat", "Kittens");
		
		assertEquals(tagList1,tagList2);
	}

	
	@Test
	public void testEqualsIgnoresOrder() {
		TagList tagList1 = new TagList();
		tagList1.addTag(null, "Dog", "Puppies");
		tagList1.addTag("http://foo", "Cat", "Kittens");

		TagList tagList2 = new TagList();
		tagList2.addTag("http://foo", "Cat", "Kittens");
		tagList2.addTag(null, "Dog", "Puppies");
		
		assertEquals(tagList1,tagList2);
	}

	@Test
	public void testPreventDuplication() {
		
		Patient patient = new Patient();
		patient.addIdentifier("urn:system", "testTagsWithCreateAndReadAndSearch");
		patient.addName().addFamily("Tester").addGiven("Joe");
		TagList tagList = new TagList();
		tagList.addTag(null, "Dog", "Puppies");
		// Add this twice
		tagList.addTag("http://foo", "Cat", "Kittens");
		tagList.addTag("http://foo", "Cat", "Kittens");
		
		patient.getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
		
		Bundle b = new Bundle();
		b.addResource(patient, myCtx, "http://foo");
		
		String encoded = myCtx.newXmlParser().encodeBundleToString(b);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("Cat", "Kittens")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("Cat", "Kittens", "Cat", "Kittens"))));
		
	}
	

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
