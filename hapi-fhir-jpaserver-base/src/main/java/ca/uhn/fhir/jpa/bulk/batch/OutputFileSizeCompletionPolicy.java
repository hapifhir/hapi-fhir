package ca.uhn.fhir.jpa.bulk.batch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

import static org.slf4j.LoggerFactory.getLogger;

public class OutputFileSizeCompletionPolicy implements CompletionPolicy, ItemProcessListener<ResourcePersistentId, IBaseResource> {
	private static final Logger ourLog = getLogger(OutputFileSizeCompletionPolicy.class);


	private long myTotalBytesSoFar;
	private long myFileMaxChars = 500 * FileUtils.ONE_KB;

	@Autowired
	private FhirContext myFhirContext;

	private IParser myParser;

	@PostConstruct
	public void start() {
		myParser = myFhirContext.newJsonParser().setPrettyPrint(false);
	}

	@Override
	public void beforeProcess(ResourcePersistentId theResourcePersistentId) {
		ourLog.warn("Calling beforeProcess on Lisener");
	}

	@Override
	public void afterProcess(ResourcePersistentId theResourcePersistentId, IBaseResource theIBaseResource) {
		ourLog.warn("Calling afterProcess on Listener");
		myTotalBytesSoFar += myParser.encodeResourceToString(theIBaseResource).getBytes().length;
		ourLog.warn("total bytes in afterProcess: " + myTotalBytesSoFar);
	}

	@Override
	public void onProcessError(ResourcePersistentId theResourcePersistentId, Exception theE) {

	}

	@Override
	public boolean isComplete(RepeatContext theRepeatContext, RepeatStatus theRepeatStatus) {
		return RepeatStatus.FINISHED == theRepeatStatus || isComplete(theRepeatContext);
	}

	@Override
	public boolean isComplete(RepeatContext theRepeatContext) {
		ourLog.warn("TOTAL BYTES SO FAR: " +  myTotalBytesSoFar);
		return myTotalBytesSoFar >= myFileMaxChars;
	}

	@Override
	public RepeatContext start(RepeatContext theRepeatContext) {
		ourLog.warn("Calling start on CompletionPolicy");
		myTotalBytesSoFar = 0l;
		return theRepeatContext;
	}

	@Override
	public void update(RepeatContext theRepeatContext) {
		ourLog.warn("Calling update on CompletionPolicy");
	}
}
