package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.healthmarketscience.sqlbuilder.Condition;
import ca.uhn.fhir.rest.client.method.SearchParameter;
import ca.uhn.fhir.rest.param.BaseParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenPredicateBuilderTest {

	private TokenPredicateBuilder myTokenPredicateBuilder;

	@Mock
	private SearchQueryBuilder mySearchQueryBuilder;

	@BeforeEach
	public void beforeEach(){
		DbSpec spec = new DbSpec();
		DbSchema schema = new DbSchema(spec, "schema");
		DbTable table = new DbTable(schema, "table");
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(table);
		when(mySearchQueryBuilder.getPartitionSettings()).thenReturn(new PartitionSettings());
		myTokenPredicateBuilder = new TokenPredicateBuilder(mySearchQueryBuilder);
	}

	@Test
	public void testCreatePredicateToken_withParameterHasSystemLengthGreaterThanMax_ShouldLogAndNotThrowException(){
		// setup
		ArrayList<IQueryParameterType> tokenParams = new ArrayList<>();
		String theLongStr = buildLongString();
		tokenParams.add(new TokenParam().setSystem(theLongStr).setValue("value"));
		RuntimeSearchParam runtimeSearchParam = new RuntimeSearchParam(null, null, "SearchParameter", null, null, null, null, null, null, null);

		// execute
		try {
			myTokenPredicateBuilder.createPredicateToken(tokenParams, "SearchParameter", "SPPrefix", runtimeSearchParam, RequestPartitionId.defaultPartition());
 		} catch (InvalidRequestException e){
			fail("Should not throw exception.");
		}
	}

	@Test
	public void testCreatePredicateToken_withParameterHasValueLengthGreaterThanMax_ShouldLogAndNotThrowException(){
		// setup
		ArrayList<IQueryParameterType> tokenParams = new ArrayList<>();
		String theLongStr = buildLongString();
		tokenParams.add(new TokenParam().setSystem("value").setValue(theLongStr));
		RuntimeSearchParam runtimeSearchParam = new RuntimeSearchParam(null, null, "SearchParameter", null, null, null, null, null, null, null);

		// execute
		try {
			myTokenPredicateBuilder.createPredicateToken(tokenParams, "SearchParameter", "SPPrefix", runtimeSearchParam, RequestPartitionId.defaultPartition());
		} catch (InvalidRequestException e){
			fail("Should not throw exception.");
		}
	}

	private String buildLongString(){
		int maxLength = ResourceIndexedSearchParamToken.MAX_LENGTH;
		String theStr = "";
		while (theStr.length() <= maxLength){
			theStr += "ThisIsPartOfAVeryLongString";
		}
		return theStr;
	}

}
