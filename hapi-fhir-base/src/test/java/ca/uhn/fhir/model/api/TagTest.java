package ca.uhn.fhir.model.api;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

	@Test
	public void testEquals() {
		Tag tag1 = new Tag().setScheme("scheme").setTerm("term").setLabel("label");
		Tag tag2 = new Tag().setScheme("scheme").setTerm("term").setLabel("label");
		Tag tag3 = new Tag().setScheme("scheme2").setTerm("term").setLabel("label");
		Tag tag4 = new Tag().setScheme("scheme").setTerm("term2").setLabel("label");

		assertEquals(tag1, tag1);
		assertEquals(tag1, tag2);
		assertNotEquals(tag1, tag3);
		assertNotEquals(tag1, tag4);
		assertNotEquals(tag1, null);
		assertNotEquals(tag1, "");
	}

	@Test
	public void testHashCode() {
		Tag tag1 = new Tag().setScheme("scheme").setTerm("term").setLabel("label");
		assertEquals(1920714536, tag1.hashCode());
	}

	@Test
	public void testConstructors() throws URISyntaxException {
		assertTrue(new Tag().isEmpty());
		assertFalse(new Tag("http://foo").isEmpty());
		assertFalse(new Tag("http://foo", "http://bar").isEmpty());
		assertFalse(new Tag(new URI("http://foo"), new URI("http://bar"), "Label").isEmpty());
		assertTrue(new Tag((URI)null, null, "Label").isEmpty());

		assertEquals("http://foo", new Tag(new URI("http://foo"), new URI("http://bar"), "Label").getSystem());
		assertEquals("http://bar", new Tag(new URI("http://foo"), new URI("http://bar"), "Label").getCode());
		assertEquals("Label", new Tag(new URI("http://foo"), new URI("http://bar"), "Label").getDisplay());
	}

}
