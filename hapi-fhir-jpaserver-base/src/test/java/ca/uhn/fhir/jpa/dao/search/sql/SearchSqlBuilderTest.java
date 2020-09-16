package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchSqlBuilderTest {

	@Test
	public void testSearchStringExact() {
		SearchSqlBuilder builder = new SearchSqlBuilder(new PartitionSettings(), null, "Patient");
		SearchSqlBuilder.StringIndexTable stringSelector = builder.addStringSelector();
		stringSelector.addPredicateExact("family", "Smith");

		SearchSqlBuilder.GeneratedSql output = builder.generate();
		assertEquals("SELECT t2.RES_ID FROM HFJ_SPIDX_STRING t2 WHERE (t2.SP_VALUE_EXACT = ?)", output.getSql());
		assertEquals(1, output.getBindVariables().size());
		assertEquals(-4860858536404807396L, output.getBindVariables().get(0));
	}

	@Test
	public void testSearchStringJoinedToToken() {
		SearchSqlBuilder builder = new SearchSqlBuilder(new PartitionSettings(), null, "Patient");

		SearchSqlBuilder.StringIndexTable stringSelector = builder.addStringSelector();
		stringSelector.addPredicateExact("family", "Smith");

		SearchSqlBuilder.TokenIndexTable tokenSelector = builder.addTokenSelector();
		tokenSelector.addPredicateSystemAndValue("identifier", "http://foo", "12345");

		SearchSqlBuilder.GeneratedSql output = builder.generate();
		assertEquals("SELECT t1.RES_ID FROM HFJ_SPIDX_STRING t1 LEFT OUTER JOIN HFJ_SPIDX_TOKEN t2 ON (t1.RES_ID = t2.RES_ID) WHERE ((t1.SP_VALUE_EXACT = ?) AND (t2.HASH_SYS_AND_VALUE = ?))", output.getSql());
		assertEquals(2, output.getBindVariables().size());
		assertEquals(-4860858536404807396L, output.getBindVariables().get(0));
		assertEquals(7679026864152406200L, output.getBindVariables().get(1));
	}

}
