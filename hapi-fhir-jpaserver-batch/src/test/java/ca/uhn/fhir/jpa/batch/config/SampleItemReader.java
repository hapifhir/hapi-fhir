package ca.uhn.fhir.jpa.batch.config;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class SampleItemReader implements ItemReader<String> {


	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return "zoop";
	}
}
