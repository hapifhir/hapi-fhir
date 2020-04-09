package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Lazy
@Service
public class EmpiInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiInterceptor.class);
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private IEmpiConfig myEmpiConfig;

	// FIXME KHS pull out empi intercept stuff from filter and move it back here
// FIXME use EmpiUtil for resource types
	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING)
	public void expungeAllEmpiLinks(AtomicInteger theCounter) {
		ourLog.debug("Expunging all EmpiLink records");
		theCounter.addAndGet(myExpungeEverythingService.expungeEverythingByType(EmpiLink.class));
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE)
	public void expungeAllMatchedEmpiLinks(AtomicInteger theCounter, IBaseResource theResource) {
		// FIXME EMPI
	}
}
