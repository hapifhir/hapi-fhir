package ca.uhn.fhir.rest.openapi;

import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class OpenApiInterceptorMarkdownTest {

	private static final OpenApiInterceptor ourInterceptor = new OpenApiInterceptor();

	static Stream<Arguments> markdownToHtml() {
		return Stream.of(
				Arguments.of(
						"This server is copyright **Example Org** 2021",
						"<p>This server is copyright <strong>Example Org</strong> 2021</p>\n"),
				Arguments.of(
						"# Heading 1\n## Heading 2\n### Heading 3",
						"<h1>Heading 1</h1>\n<h2>Heading 2</h2>\n<h3>Heading 3</h3>\n"),
				Arguments.of("Setext\n===\n\nSub\n---", "<h1>Setext</h1>\n<h2>Sub</h2>\n"),
				Arguments.of(
						"Some *emphasis*, _underscore_, **strong**, __strong2__ and ***both***",
						"<p>Some <em>emphasis</em>, <em>underscore</em>, <strong>strong</strong>, <strong>strong2</strong> and <em><strong>both</strong></em></p>\n"),
				Arguments.of("*a **b** c*", "<p><em>a <strong>b</strong> c</em></p>\n"),
				Arguments.of(
						"A [link](http://example.com \"Title\") and <http://auto.example.com> and http://bare.example.com",
						"<p>A <a href=\"http://example.com\" title=\"Title\">link</a> and <a href=\"http://auto.example.com\">http://auto.example.com</a> and http://bare.example.com</p>\n"),
				Arguments.of("Ref [link][1]\n\n[1]: http://example.com/ref", "<p>Ref <a href=\"http://example.com/ref\">link</a></p>\n"),
				Arguments.of("[a](http://x.com/?a=1&b=2)", "<p><a href=\"http://x.com/?a=1&amp;b=2\">a</a></p>\n"),
				Arguments.of("[a](http://x.com/ä)", "<p><a href=\"http://x.com/ä\">a</a></p>\n"),
				Arguments.of(
						"[t](http://x.com \"say \\\"hi\\\"\")",
						"<p><a href=\"http://x.com\" title=\"say &quot;hi&quot;\">t</a></p>\n"),
				Arguments.of("<foo@example.com>", "<p><a href=\"mailto:foo@example.com\">foo@example.com</a></p>\n"),
				// CommonMark allows spaces inside a <...> link destination
				Arguments.of("[a](<http://x.com/a b>)", "<p><a href=\"http://x.com/a b\">a</a></p>\n"),
				Arguments.of(
						"![alt text](http://example.com/img.png)",
						"<p><img src=\"http://example.com/img.png\" alt=\"alt text\" /></p>\n"),
				Arguments.of(
						"![img](http://x.com/i.png \"T\")",
						"<p><img src=\"http://x.com/i.png\" alt=\"img\" title=\"T\" /></p>\n"),
				Arguments.of(
						"- one\n- two\n  - nested\n- three",
						"<ul>\n<li>one</li>\n<li>two\n<ul>\n<li>nested</li>\n</ul>\n</li>\n<li>three</li>\n</ul>\n"),
				Arguments.of(
						"1. first\n2. second\n\n3) third",
						"<ol>\n<li>first</li>\n<li>second</li>\n</ol>\n<ol start=\"3\">\n<li>third</li>\n</ol>\n"),
				Arguments.of("* loose\n\n* list", "<ul>\n<li>\n<p>loose</p>\n</li>\n<li>\n<p>list</p>\n</li>\n</ul>\n"),
				Arguments.of(
						"> - quoted\n> - list",
						"<blockquote>\n<ul>\n<li>quoted</li>\n<li>list</li>\n</ul>\n</blockquote>\n"),
				Arguments.of(
						"Use `code span` and ``double `tick` span``",
						"<p>Use <code>code span</code> and <code>double `tick` span</code></p>\n"),
				Arguments.of(
						"```java\nint x = 1 < 2;\n```",
						"<pre><code class=\"language-java\">int x = 1 &lt; 2;\n</code></pre>\n"),
				Arguments.of("    indented code\n    line two", "<pre><code>indented code\nline two\n</code></pre>\n"),
				Arguments.of("\tTab start", "<pre><code>Tab start\n</code></pre>\n"),
				Arguments.of(
						"Inline <b>html</b> and <span class=\"x\">span</span>",
						"<p>Inline <b>html</b> and <span class=\"x\">span</span></p>\n"),
				Arguments.of("<div>\nhtml block\n</div>", "<div>\nhtml block\n</div>\n"),
				Arguments.of("<!-- comment -->\ntext", "<!-- comment -->\n<p>text</p>\n"),
				Arguments.of("<script>alert(1)</script>", "<script>alert(1)</script>\n"),
				Arguments.of("line one\nline two", "<p>line one\nline two</p>\n"),
				Arguments.of("hard break  \nnext line", "<p>hard break<br />\nnext line</p>\n"),
				Arguments.of("backslash break\\\nnext line", "<p>backslash break<br />\nnext line</p>\n"),
				Arguments.of("ends with hard break  ", "<p>ends with hard break</p>\n"),
				Arguments.of("Trailing spaces   ", "<p>Trailing spaces</p>\n"),
				Arguments.of("multi\n\n\n\nparagraphs", "<p>multi</p>\n<p>paragraphs</p>\n"),
				Arguments.of("Foo\r\nBar\r\n\r\nBaz", "<p>Foo\nBar</p>\n<p>Baz</p>\n"),
				Arguments.of("> quoted\n> text", "<blockquote>\n<p>quoted\ntext</p>\n</blockquote>\n"),
				Arguments.of("para\n\n---\n\npara2", "<p>para</p>\n<hr />\n<p>para2</p>\n"),
				Arguments.of(
						"Escapes: & < > \" ' \\* &amp; &copy; &#169;",
						"<p>Escapes: &amp; &lt; &gt; &quot; ' * &amp; © ©</p>\n"),
				Arguments.of("a&nbsp;b \\<b> not html", "<p>a b &lt;b&gt; not html</p>\n"),
				Arguments.of("Unicode: café — é", "<p>Unicode: café — é</p>\n"),
				Arguments.of("| a | b |\n|---|---|\n| 1 | 2 |", "<p>| a | b |\n|---|---|\n| 1 | 2 |</p>\n"),
				Arguments.of("~~strike~~ and www.example.com", "<p>~~strike~~ and www.example.com</p>\n"),
				Arguments.of("[x](javascript:alert(1))", "<p>x</p>\n"),
				Arguments.of("[**bold** link](javascript:void(0)) tail", "<p><strong>bold</strong> link tail</p>\n"),
				Arguments.of("<javascript:alert(1)>", "<p>javascript:alert(1)</p>\n"),
				Arguments.of("[r][j] text\n\n[j]: javascript:alert(1)", "<p>r text</p>\n"),
				Arguments.of("![i](javascript:alert(1)) after", "<p> after</p>\n"),
				Arguments.of("![r][j] text\n\n[j]: javascript:alert(1)", "<p> text</p>\n"),
				Arguments.of("[x](JavaScript:alert(1))", "<p><a href=\"JavaScript:alert(1)\">x</a></p>\n"));
	}

	@ParameterizedTest
	@MethodSource("markdownToHtml")
	void testRenderMarkdown(String theMarkdown, String theExpectedHtml) {
		String html = ourInterceptor.renderMarkdown(theMarkdown);

		assertThat(html).isEqualTo(theExpectedHtml);
	}

	@Test
	void testCreateResourceDescription_RendersAndSanitizesDocumentation() {
		CapabilityStatementRestResourceComponent resource = new CapabilityStatementRestResourceComponent();
		resource.setType("Patient");
		resource.setDocumentation("This is **bolded** documentation with [a link](http://example.com)\n\n"
				+ "<script>alert(1)</script>\n\n"
				+ "- one\n- two");

		String description = ourInterceptor.createResourceDescription(resource);

		assertThat(description)
				.isEqualTo("The Patient FHIR resource type<br/>"
						+ "<p>This is <strong>bolded</strong> documentation with a link</p>\n"
						+ "\n"
						+ "<ul><li>one</li><li>two</li></ul>\n");
	}
}
