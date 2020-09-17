package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchSqlBuilderTest {

	@Test
	public void testSearchNoParams() {
		SearchSqlBuilder builder = new SearchSqlBuilder(new ModelConfig(), new PartitionSettings(), null, "Patient");

		SearchSqlBuilder.GeneratedSql output = builder.generate();
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE ((t0.RES_TYPE = 'Patient') AND (t0.RES_DELETED_AT IS NULL))", output.getSql());
		assertEquals(0, output.getBindVariables().size());
	}

	@Test
	public void testSearchStringExact() {
		SearchSqlBuilder builder = new SearchSqlBuilder(new ModelConfig(), new PartitionSettings(), null, "Patient");
		SearchSqlBuilder.StringIndexTable stringSelector = builder.addStringSelector();
		stringSelector.addPredicateExact("family", "Smith");

		SearchSqlBuilder.GeneratedSql output = builder.generate();
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE (t0.SP_VALUE_EXACT = ?)", output.getSql());
		assertEquals(1, output.getBindVariables().size());
		assertEquals(-4860858536404807396L, output.getBindVariables().get(0));
	}

	@Test
	public void testSearchStringJoinedToToken() {
		SearchSqlBuilder builder = new SearchSqlBuilder(new ModelConfig(), new PartitionSettings(), null, "Patient");

		SearchSqlBuilder.StringIndexTable stringSelector = builder.addStringSelector();
		stringSelector.addPredicateExact("family", "Smith");

		SearchSqlBuilder.TokenIndexTable tokenSelector = builder.addTokenSelector();
		tokenSelector.addPredicateSystemAndValue("identifier", "http://foo", "12345");

		SearchSqlBuilder.GeneratedSql output = builder.generate();
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 LEFT OUTER JOIN HFJ_SPIDX_TOKEN t1 ON (t0.RES_ID = t1.RES_ID) WHERE ((t0.SP_VALUE_EXACT = ?) AND (t1.HASH_SYS_AND_VALUE = ?))", output.getSql());
		assertEquals(2, output.getBindVariables().size());
		assertEquals(-4860858536404807396L, output.getBindVariables().get(0));
		assertEquals(7679026864152406200L, output.getBindVariables().get(1));
	}

}
