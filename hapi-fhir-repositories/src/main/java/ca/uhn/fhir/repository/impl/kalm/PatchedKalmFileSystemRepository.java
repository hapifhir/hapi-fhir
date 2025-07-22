/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2025 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.fhir.repository.impl.kalm;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.impl.NaiveRepositoryTransactionProcessor;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.fhir.utility.repository.ig.IgRepository;

import java.nio.file.Path;
import java.util.Map;

/**
 * A patched version of the IgRepository that adds support for transaction bundle processing.
 * The current 3.23 version from cqf-fhir-utility has a nop implementation of transaction().
 * TODO Delete this once the cfq implementation adds transaction support
 */
public class PatchedKalmFileSystemRepository extends IgRepository {
	public PatchedKalmFileSystemRepository(FhirContext theFhirContext, Path theRoot) {
		super(theFhirContext, theRoot);
	}

	@Override
	public <B extends IBaseBundle> B transaction(B theTransactionBundle, Map<String, String> theHeaders) {
		return new NaiveRepositoryTransactionProcessor(this).processTransaction(theTransactionBundle);
	}
}
