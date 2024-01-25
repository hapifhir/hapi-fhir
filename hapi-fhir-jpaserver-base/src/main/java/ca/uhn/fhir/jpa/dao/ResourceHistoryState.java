package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import com.google.common.hash.HashCode;
import jakarta.annotation.Nullable;

// LUKETODO:  javadoc
public record ResourceHistoryState(
		@Nullable String myResourceText,
		@Nullable byte[] myResourceBinary,
		ResourceEncodingEnum myEncoding,
		HashCode myHashCode) {}
