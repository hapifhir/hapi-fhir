package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenPredicateBuilderTest {

	private Logger myLogger;

	private ListAppender<ILoggingEvent> myListAppender;

	private TokenPredicateBuilder myTokenPredicateBuilder;

	@Mock
	private SearchQueryBuilder mySearchQueryBuilder;

	@BeforeEach
	public void beforeEach() {
		myLogger = (Logger) LoggerFactory.getLogger(TokenPredicateBuilder.class);
		myListAppender = new ListAppender<>();
		myListAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
		myLogger.setLevel(Level.ALL);
		myLogger.addAppender(myListAppender);
		myListAppender.start();

		DbSpec spec = new DbSpec();
		DbSchema schema = new DbSchema(spec, "schema");
		DbTable table = new DbTable(schema, "table");
		when(mySearchQueryBuilder.addTable(Mockito.anyString())).thenReturn(table);
		when(mySearchQueryBuilder.getPartitionSettings()).thenReturn(new PartitionSettings());
		myTokenPredicateBuilder = new TokenPredicateBuilder(mySearchQueryBuilder);
	}

	@AfterEach
	public void afterEach(){
		myListAppender.stop();
		myLogger.detachAppender(myListAppender);
	}

	@Test
	public void testCreatePredicateToken_withParameterHasSystemLengthGreaterThanMax_ShouldLogAndNotThrowException() {
		// setup
		ArrayList<IQueryParameterType> tokenParams = new ArrayList<>();
		String theLongSystem = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamToken.MAX_LENGTH + 100);
		String theLongValue = RandomStringUtils.randomAlphanumeric(ResourceIndexedSearchParamToken.MAX_LENGTH + 100);
		tokenParams.add(new TokenParam().setSystem(theLongSystem).setValue(theLongValue));
		RuntimeSearchParam runtimeSearchParam = new RuntimeSearchParam(null, null, "SearchParameter", null, null, null, null, null, null, null);

		// execute
		myTokenPredicateBuilder.createPredicateToken(tokenParams, "SearchParameter", "SPPrefix", runtimeSearchParam, RequestPartitionId.defaultPartition());

		// verify
		List<ILoggingEvent> logList = myListAppender.list;
		assertThat(logList).hasSize(2);
		assertThat(logList.get(0).getFormattedMessage()).contains(theLongSystem);
		assertThat(logList.get(1).getFormattedMessage()).contains(theLongValue);
	}
}
