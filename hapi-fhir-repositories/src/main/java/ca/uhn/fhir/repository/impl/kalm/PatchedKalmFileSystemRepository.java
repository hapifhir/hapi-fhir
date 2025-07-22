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
 * The current 3.23 version form cfq-utilities has a nop implementation.
 * TODO Delete this once the cfq implementation adds transaction support
 */
public class PatchedKalmFileSystemRepository extends IgRepository {
	public PatchedKalmFileSystemRepository(FhirContext fhirContext, Path root) {
		super(fhirContext, root);
	}

	@Override
	public <B extends IBaseBundle> B transaction(B transaction, Map<String, String> headers) {
		return new NaiveRepositoryTransactionProcessor(this).processTransaction(transaction);
	}
}
