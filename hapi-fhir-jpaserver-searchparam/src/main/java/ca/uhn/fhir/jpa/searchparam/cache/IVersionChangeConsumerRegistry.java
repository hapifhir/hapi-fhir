package ca.uhn.fhir.jpa.searchparam.cache;

public interface IVersionChangeConsumerRegistry {
	boolean refreshCacheIfNecessary();
}
