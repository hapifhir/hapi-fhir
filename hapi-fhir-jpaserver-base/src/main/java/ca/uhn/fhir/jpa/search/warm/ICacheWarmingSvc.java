package ca.uhn.fhir.jpa.search.warm;

import org.springframework.scheduling.annotation.Scheduled;

public interface ICacheWarmingSvc {
	@Scheduled(fixedDelay = 1000)
	void performWarmingPass();
}
