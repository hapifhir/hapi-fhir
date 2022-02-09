package ca.uhn.fhir.batch2.api;

import java.util.Map;

public interface IJobDataSink {

	void accept(Map<String, Object> theData);

}
