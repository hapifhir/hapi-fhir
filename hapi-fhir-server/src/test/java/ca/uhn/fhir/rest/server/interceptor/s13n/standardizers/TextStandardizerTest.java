package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextStandardizerTest {

	TextStandardizer myStandardizer = new TextStandardizer();

	@Test
	public void testCleanNoiseCharacters() {
		assertEquals("public", myStandardizer.standardize("\u0070\u0075\u0062\u006c\u0069\u0063\u0020\u0020\u0020\u0020"));
		assertEquals("textÃ#", myStandardizer.standardize("\t\r\ntext┴\t┬\t├\t─\t┼!\\\\Ã #% \n"));
		assertEquals("nbsp", myStandardizer.standardize("nbsp \u00A0"));
	}

	@Test
	public void testCleanBaseAscii() {
		for (int i = 0; i < 31; i++) {
			assertEquals("", myStandardizer.standardize(Character.toString((char) i)));
		}
	}

	@Test
	public void testCleanExtendedAsciiExceptForInternationalSupport() {
		for (int i = 127; i < 255; i++) {
			if (!myStandardizer.isNoiseCharacter(i) || myStandardizer.isTranslate(i)) {
				continue;
			}
			assertEquals("", myStandardizer.standardize(Character.toString((char) i)), String.format("Expected char #%s to be filtered out", i));
		}
	}

	@Test
	public void testExtendedUnicodeSet() {
		String[] testLiterals = new String[]{
			".", ".",
			"𧚓𧚔𧜎𧜏𨩃𨩄𨩅𨩆𨩇𨩈𨩉𨩊𨩋", "𧚓𧚔𧜎𧜏𨩃𨩄𨩅𨩆𨩇𨩈𨩉𨩊𨩋",
			"𐌁𐌂𐌃𐌄𐌅𐌆𐌇𐌈𐌉𐌊𐌋𐌌𐌍𐌎𐌏𐌐𐌑𐌒𐌓𐌔𐌕𐌖𐌗𐌘𐌙𐌚𐌛𐌜𐌝𐌞", "𐌁𐌂𐌃𐌄𐌅𐌆𐌇𐌈𐌉𐌊𐌋𐌌𐌍𐌎𐌏𐌐𐌑𐌒𐌓𐌔𐌕𐌖𐌗𐌘𐌙𐌚𐌛𐌜𐌝𐌞",
			"𐌰𐌱𐌲𐌳𐌴𐌵𐌶𐌷𐌸𐌹𐌺𐌻𐌼𐌽𐌾𐌿𐍀𐍁𐍂𐍃𐍄𐍅𐍆𐍇𐍈𐍉𐍊", "𐌰𐌱𐌲𐌳𐌴𐌵𐌶𐌷𐌸𐌹𐌺𐌻𐌼𐌽𐌾𐌿𐍀𐍁𐍂𐍃𐍄𐍅𐍆𐍇𐍈𐍉",
			"𐎀𐎁𐎂𐎃𐎄𐎅𐎆𐎇𐎈𐎉𐎊𐎋𐎌𐎍𐎎𐎏𐎐𐎑𐎒𐎓𐎔", "𐎀𐎁𐎂𐎃𐎄𐎅𐎆𐎇𐎈𐎉𐎊𐎋𐎌𐎍𐎎𐎏𐎐𐎑𐎒𐎓𐎔",
			"𐏈𐏉𐏊𐏋𐏌𐏍𐏎𐏏𐏐𐏑𐏒𐏓𐏔𐏕", "",
			"𐒀𐒁𐒂𐒃𐒄𐒅𐒆𐒇𐒈𐒉𐒊𐒋", "𐒀𐒁𐒂𐒃𐒄𐒅𐒆𐒇𐒈𐒉𐒊𐒋",
			"𐅄𐅅𐅆𐅇", "",
			"\uD802\uDD00\uD802\uDD01\uD802\uDD02\uD802\uDD03\uD802\uDD04\uD802\uDD05\uD802\uDD06\uD802\uDD07", "",
			"\uD802\uDD08\uD802\uDD09\uD802\uDD0A\uD802\uDD0B\uD802\uDD0C\uD802\uDD0D\uD802\uDD0E\uD802\uDD0F", "",
			"\uD802\uDD10\uD802\uDD11\uD802\uDD12\uD802\uDD13\uD802\uDD14\uD802\uDD15\uD802\uDD16\uD802\uDD17", "",
			"\uD802\uDD18\uD802\uDD19\uD802\uDD1A", "",
			"𐒌𐒍𐒎𐒏𐒐𐒑𐒒𐒓𐒔𐒕𐒖𐒗𐒘𐒙𐒚𐒛𐒜𐒝", "𐒌𐒍𐒎𐒏𐒐𐒑𐒒𐒓𐒔𐒕𐒖𐒗𐒘𐒙𐒚𐒛𐒜𐒝",
			"𐒠𐒡𐒢𐒣𐒤𐒥𐒦𐒧𐒨𐒩", "𐒠𐒡𐒢𐒣𐒤𐒥𐒦𐒧𐒨𐒩"};

		for (int i = 0; i < testLiterals.length; i += 2) {
			assertEquals(testLiterals[i + 1], myStandardizer.standardize(testLiterals[i]));
		}
	}
}
