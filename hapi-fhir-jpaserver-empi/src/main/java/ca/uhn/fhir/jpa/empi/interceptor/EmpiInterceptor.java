package ca.uhn.fhir.jpa.empi.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Interceptor
@Service
public class EmpiInterceptor {

	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;

	@Hook(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING)
	public void expungeAllLiveBundleRecords(AtomicInteger theCounter) {
		theCounter.addAndGet(myExpungeEverythingService.expungeEverythingByType(EmpiLink.class));
	}
}
