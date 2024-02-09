package ca.uhn.fhir.model.api;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class TagTest {

	@Test
	public void testEquals() {
		Tag tag1 = new Tag().setScheme("scheme").setTerm("term").setLabel("label");
		Tag tag2 = new Tag().setScheme("scheme").setTerm("term").setLabel("label");
		Tag tag3 = new Tag().setScheme("scheme2").setTerm("term").setLabel("label");
		Tag tag4 = new Tag().setScheme("scheme").setTerm("term2").setLabel("label");

		assertThat(tag1)
			.isEqualTo(tag1)
			.isNotNull();
		assertThat(tag2).isEqualTo(tag1);
		assertThat(tag3).isNotEqualTo(tag1);
		assertThat(tag4).isNotEqualTo(tag1);
		assertThat("").isNotEqualTo(tag1);
	}

	@Test
	public void testHashCode() {
		Tag tag1 = new Tag().setScheme("scheme").setTerm("term").setLabel("label");
		assertThat(tag1.hashCode()).isEqualTo(-1029268184);
	}

	@Test
	public void testConstructors() throws URISyntaxException {
		assertThat(new Tag().isEmpty()).isTrue();
		assertThat(new Tag("http://foo").isEmpty()).isFalse();
		assertThat(new Tag("http://foo", "http://bar").isEmpty()).isFalse();
		assertThat(new Tag(new URI("http://foo"), new URI("http://bar"), "Label").isEmpty()).isFalse();
		assertThat(new Tag((URI) null, null, "Label").isEmpty()).isTrue();

		assertThat(new Tag(new URI("http://foo"), new URI("http://bar"), "Label").getSystem()).isEqualTo("http://foo");
		assertThat(new Tag(new URI("http://foo"), new URI("http://bar"), "Label").getCode()).isEqualTo("http://bar");
		assertThat(new Tag(new URI("http://foo"), new URI("http://bar"), "Label").getDisplay()).isEqualTo("Label");
	}

}
