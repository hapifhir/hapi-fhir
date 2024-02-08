package ca.uhn.fhir.util;

import com.google.common.base.Charsets;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilTest {

	@Test
	public void testLeft() {
		assertThat(StringUtil.left(null, 1)).isNull();
		assertThat(StringUtil.left("", 10)).isEqualTo("");
		assertThat(StringUtil.left("STR", 10)).isEqualTo("STR");
		assertThat(StringUtil.left("...", 1)).isEqualTo(".");

		// check supplementary chars
		assertThat(StringUtil.left("\uD800\uDF01\uD800\uDF02", 1)).isEqualTo("\uD800\uDF01");
	}

	@Test
	public void testNormalizeString() {
		assertThat(StringUtil.normalizeStringForSearchIndexing("TEST teSt")).isEqualTo("TEST TEST");
		assertThat(StringUtil.normalizeStringForSearchIndexing("åéîøü")).isEqualTo("AEIØU");
		assertThat(StringUtil.normalizeStringForSearchIndexing("杨浩")).isEqualTo("杨浩");
		assertThat(StringUtil.normalizeStringForSearchIndexing(null)).isEqualTo(null);
	}

	@Test
	public void testToStringNoBom() {
		String input = "help i'm a bug";
		String output = StringUtil.toUtf8String(input.getBytes(Charsets.UTF_8));
		assertThat(output).isEqualTo(input);
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
		assertThat(output).isEqualTo("help i'm a bug");
	}


	@Test
	public void testChompCharacter() {
		assertThat(StringUtil.chompCharacter(null, '/')).isEqualTo(null);
		assertThat(StringUtil.chompCharacter("", '/')).isEqualTo("");
		assertThat(StringUtil.chompCharacter("/", '/')).isEqualTo("");
		assertThat(StringUtil.chompCharacter("a/", '/')).isEqualTo("a");
		assertThat(StringUtil.chompCharacter("a/a/", '/')).isEqualTo("a/a");
		assertThat(StringUtil.chompCharacter("a/a////", '/')).isEqualTo("a/a");
	}

	@Test
	public void testPrependLineNumbers() {
		assertThat(StringUtil.prependLineNumbers("A\nB")).isEqualTo("0: A\n1: B\n");
	}

}
