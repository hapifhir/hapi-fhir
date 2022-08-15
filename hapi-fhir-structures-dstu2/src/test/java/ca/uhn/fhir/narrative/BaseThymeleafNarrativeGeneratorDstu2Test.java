package ca.uhn.fhir.narrative;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseThymeleafNarrativeGeneratorDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseThymeleafNarrativeGeneratorDstu2Test.class);

	@Test
	public void testTrimWhitespace() {

		//@formatter:off
		String input = "<div>\n" +
			"	<div class=\"hapiHeaderText\">\n" +
			"	\n" +
			"	joe \n" +
			"	john \n" +
			"	<b>BLOW </b>\n" +
			"	\n" +
			"</div>\n" +
			"	<table class=\"hapiPropertyTable\">\n" +
			"		<tbody>\n" +
			"			<tr>\n" +
			"				<td>Identifier</td>\n" +
			"				<td>123456</td>\n" +
			"			</tr>\n" +
			"			<tr>\n" +
			"				<td>Address</td>\n" +
			"				<td>\n" +
			"					\n" +
			"						<span>123 Fake Street</span><br />\n" +
			"					\n" +
			"					\n" +
			"						<span>Unit 1</span><br />\n" +
			"					\n" +
			"					<span>Toronto</span>\n" +
			"					<span>ON</span>\n" +
			"					<span>Canada</span>\n" +
			"				</td>\n" +
			"			</tr>\n" +
			"			<tr>\n" +
			"				<td>Date of birth</td>\n" +
			"				<td>\n" +
			"					<span>31 March 2014</span>\n" +
			"				</td>\n" +
			"			</tr>\n" +
			"		</tbody>\n" +
			"	</table>\n" +
			"</div>";
		//@formatter:on

		String actual = BaseThymeleafNarrativeGenerator.cleanWhitespace(input);
		String expected = "<div><div class=\"hapiHeaderText\"> joe john <b>BLOW </b></div><table class=\"hapiPropertyTable\"><tbody><tr><td>Identifier</td><td>123456</td></tr><tr><td>Address</td><td><span>123 Fake Street</span><br /><span>Unit 1</span><br /><span>Toronto</span><span>ON</span><span>Canada</span></td></tr><tr><td>Date of birth</td><td><span>31 March 2014</span></td></tr></tbody></table></div>";

		ourLog.info(actual);

		assertEquals(expected, actual);
	}


}
