package ca.uhn.fhir.jpa.model.dao;

import org.junit.jupiter.api.Test;

import java.util.List;

import static ca.uhn.fhir.jpa.model.dao.JpaPid.fromId;
import static org.junit.jupiter.api.Assertions.*;

class JpaPidTest {

	@Test
	void testToLongList() {
		assertEquals(List.of(), JpaPid.toLongList(List.of()));
	    assertEquals(List.of(1L, 2L), JpaPid.toLongList(List.of(fromId(1L), fromId(2L))));
	}

	@Test
	void testArrayToLongList() {
		assertEquals(List.of(), JpaPid.toLongList(new JpaPid[0]));
		assertEquals(List.of(1L, 2L), JpaPid.toLongList(new JpaPid[]{fromId(1L), fromId(2L)}));
	}

}
