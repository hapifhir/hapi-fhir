package ca.uhn.hapi.fhir.cdshooks.api;

import ca.uhn.hapi.fhir.cdshooks.svc.CdsServiceCache;

public interface ICdsHooksServiceCacheBuilder {
	CdsServiceCache buildCdsServiceCache();
}
