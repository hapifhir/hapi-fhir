package ca.uhn.fhir.jpa.batch2.api;

import java.util.Map;

public interface IJobDataSink {

	void accept(Map<String, Object> theData);

}
