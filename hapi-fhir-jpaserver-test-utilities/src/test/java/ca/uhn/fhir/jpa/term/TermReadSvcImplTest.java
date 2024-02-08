package ca.uhn.fhir.jpa.term;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TermReadSvcImplTest {

	private final TermReadSvcImpl mySvc = new TermReadSvcImpl();

	@Test
	void applyFilterMatchWords() {
		assertThat(mySvc.applyFilter("abc def", "abc def")).isTrue();
		assertThat(mySvc.applyFilter("abc def", "abc")).isTrue();
		assertThat(mySvc.applyFilter("abc def", "def")).isTrue();
		assertThat(mySvc.applyFilter("abc def ghi", "abc def ghi")).isTrue();
		assertThat(mySvc.applyFilter("abc def ghi", "abc def")).isTrue();
		assertThat(mySvc.applyFilter("abc def ghi", "def ghi")).isTrue();
	}

	@Test
	void applyFilterSentenceStart() {
		assertThat(mySvc.applyFilter("manifold", "man")).isTrue();
		assertThat(mySvc.applyFilter("manifest destiny", "man")).isTrue();
		assertThat(mySvc.applyFilter("deep sight", "deep sigh")).isTrue();
		assertThat(mySvc.applyFilter("sink cottage", "sink cot")).isTrue();
	}

	@Test
	void applyFilterSentenceEnd() {
		assertThat(mySvc.applyFilter("rescue", "cue")).isFalse();
		assertThat(mySvc.applyFilter("very picky", "icky")).isFalse();
	}

	@Test
	void applyFilterSubwords() {
		assertThat(mySvc.applyFilter("splurge", "urge")).isFalse();
		assertThat(mySvc.applyFilter("sink cottage", "ink cot")).isFalse();
		assertThat(mySvc.applyFilter("sink cottage", "ink cottage")).isFalse();
		assertThat(mySvc.applyFilter("clever jump startle", "lever jump star")).isFalse();
	}
}
