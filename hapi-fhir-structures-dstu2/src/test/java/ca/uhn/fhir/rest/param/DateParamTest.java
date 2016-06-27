package ca.uhn.fhir.rest.param;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;

public class DateParamTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DateParamTest.class);

	@SuppressWarnings("deprecation")
	@Test
	public void testConstructors() {
		new DateParam();
		new DateParam("2011-01-02");
		new DateParam(ParamPrefixEnum.GREATERTHAN,new Date());
		new DateParam(ParamPrefixEnum.GREATERTHAN,new DateTimeDt("2011-01-02"));
		new DateParam(ParamPrefixEnum.GREATERTHAN,InstantDt.withCurrentTime());
		new DateParam(ParamPrefixEnum.GREATERTHAN,"2011-01-02");

		new DateParam(QuantityCompararatorEnum.GREATERTHAN,new Date());
		new DateParam(QuantityCompararatorEnum.GREATERTHAN,new DateTimeDt("2011-01-02"));
		new DateParam(QuantityCompararatorEnum.GREATERTHAN,InstantDt.withCurrentTime());
		new DateParam(QuantityCompararatorEnum.GREATERTHAN,"2011-01-02");
	}
	
	@Test
	public void testParse() {
		Date date = new Date();
		
		DateParam param = new DateParam();
		param.setValueAsString("gt2016-06-09T20:38:14.591-05:00");

		assertEquals(ParamPrefixEnum.GREATERTHAN, param.getPrefix());
		assertEquals("2016-06-09T20:38:14.591-05:00", param.getValueAsString());

		ourLog.info("PRE:  " + param.getValue());
		ourLog.info("PRE:  " + param.getValue().getTime());
		InstantDt dt = new InstantDt(new Date(param.getValue().getTime()));
		dt.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		ourLog.info("POST: " + dt.getValue());
		assertEquals("2016-06-09T21:38:14.591-04:00", dt.getValueAsString());
	}

}
