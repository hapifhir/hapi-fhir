package ca.uhn.fhir.rest.server.messaging;

import jakarta.annotation.Nullable;

public interface IHasPayloadMessageKey {
	@Nullable
	default String getPayloadMessageKey() {
		return null;
	}
}
