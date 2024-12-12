package ca.uhn.fhir.jpa.migrate.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SqlUtilTest {

	@Test
	public void testSplit() {
		String input = """
			select
			  *
			       -- COMMENT
			  FROM FOO;
			
			-- Also a comment
			
			  ;
			      select BLAH
			""";
		List<String> statements = SqlUtil.splitSqlFileIntoStatements(input);
		assertEquals(2, statements.size());
		assertEquals("select  *  FROM FOO", statements.get(0).replace("\n ", " "));
		assertEquals("select BLAH", statements.get(1).replace("\n ", " "));
	}


}
