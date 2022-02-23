package ca.uhn.fhir.util;

import com.google.common.base.Charsets;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringUtilTest {

	@Test
	public void testLeft() {
		assertNull(StringUtil.left(null, 1));
		assertEquals("", StringUtil.left("", 10));
		assertEquals("STR", StringUtil.left("STR", 10));
		assertEquals(".", StringUtil.left("...", 1));

		// check supplementary chars
		assertEquals("\uD800\uDF01", StringUtil.left("\uD800\uDF01\uD800\uDF02", 1));
	}

	@Test
	public void testNormalizeString() {
		assertEquals("TEST TEST", StringUtil.normalizeStringForSearchIndexing("TEST teSt"));
		assertEquals("AEIØU", StringUtil.normalizeStringForSearchIndexing("åéîøü"));
		assertEquals("杨浩", StringUtil.normalizeStringForSearchIndexing("杨浩"));
		assertEquals(null, StringUtil.normalizeStringForSearchIndexing(null));
	}

	@Test
	public void testToStringNoBom() {
		String input = "help i'm a bug";
		String output = StringUtil.toUtf8String(input.getBytes(Charsets.UTF_8));
		assertEquals(input, output);
	}

	@Test
	public void testToStringWithBom() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(bos, StandardCharsets.UTF_8));
		out.write('\ufeff');
		out.write("help i'm a bug");
		out.close();

		byte[] bytes = bos.toByteArray();
		String output = StringUtil.toUtf8String(bytes);
		assertEquals("help i'm a bug", output);
	}


	@Test
	public void testChompCharacter() {
		assertEquals(null, StringUtil.chompCharacter(null, '/'));
		assertEquals("", StringUtil.chompCharacter("", '/'));
		assertEquals("", StringUtil.chompCharacter("/", '/'));
		assertEquals("a", StringUtil.chompCharacter("a/", '/'));
		assertEquals("a/a", StringUtil.chompCharacter("a/a/", '/'));
		assertEquals("a/a", StringUtil.chompCharacter("a/a////", '/'));
	}

	@Test
	public void testPrependLineNumbers() {
		assertEquals("0: A\n1: B\n", StringUtil.prependLineNumbers("A\nB"));
	}

}
