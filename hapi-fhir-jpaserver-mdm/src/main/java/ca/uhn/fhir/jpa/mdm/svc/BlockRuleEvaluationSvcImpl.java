/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockListRuleJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockedFieldJson;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockRuleEvaluationSvc;
import ca.uhn.fhir.util.FhirTypeUtil;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * An implementation of IBlockRuleEvaluationSvc.
 * Evaluates whether or not a provided resource
 * is blocked from mdm matching or not.
 */
public class BlockRuleEvaluationSvcImpl implements IBlockRuleEvaluationSvc {
	private static final Logger ourLog = getLogger(BlockRuleEvaluationSvcImpl.class);

	private final IFhirPath myFhirPath;

	private final IBlockListRuleProvider myBlockListRuleProvider;

	public BlockRuleEvaluationSvcImpl(
			FhirContext theContext, @Nullable IBlockListRuleProvider theIBlockListRuleProvider) {
		myFhirPath = theContext.newFhirPath();
		myBlockListRuleProvider = theIBlockListRuleProvider;
	}

	private boolean hasBlockList() {
		return myBlockListRuleProvider != null && myBlockListRuleProvider.getBlocklistRules() != null;
	}

	@Override
	public boolean isMdmMatchingBlocked(IAnyResource theResource) {
		if (hasBlockList()) {
			return isMdmMatchingBlockedInternal(theResource);
		}
		return false;
	}

	private boolean isMdmMatchingBlockedInternal(IAnyResource theResource) {
		BlockListJson blockListJson = myBlockListRuleProvider.getBlocklistRules();
		String resourceType = theResource.fhirType();

		// gather only applicable rules
		// these rules are 'or''d, so if any match,
		// mdm matching is blocked
		return blockListJson.getBlockListItemJsonList().stream()
				.filter(r -> r.getResourceType().equals(resourceType))
				.anyMatch(rule -> isMdmBlockedForFhirPath(theResource, rule));
	}

	private boolean isMdmBlockedForFhirPath(IAnyResource theResource, BlockListRuleJson theRule) {
		List<BlockedFieldJson> blockedFields = theRule.getBlockedFields();

		// rules are 'and'ed
		// This means that if we detect any reason *not* to block
		// we don't; only if all block rules pass do we block
		for (BlockedFieldJson field : blockedFields) {
			String path = field.getFhirPath();
			String blockedValue = field.getBlockedValue();

			List<IBase> results;
			try {
				// can throw FhirPathExecutionException if path is incorrect
				// or functions are invalid.
				// single() explicitly throws this (but may be what is desired)
				// so we'll catch and not block if this fails
				results = myFhirPath.evaluate(theResource, path, IBase.class);
			} catch (FhirPathExecutionException ex) {
				ourLog.warn(
						"FhirPath evaluation failed with an exception."
								+ " No blocking will be applied and mdm matching will continue as before.",
						ex);
				return false;
			}

			// fhir path should return exact values
			if (results.size() != 1) {
				// no results means no blocking
				// too many matches means no blocking
				ourLog.trace("Too many values at field {}", path);
				return false;
			}

			IBase first = results.get(0);

			if (FhirTypeUtil.isPrimitiveType(first.fhirType())) {
				IPrimitiveType<?> primitiveType = (IPrimitiveType<?>) first;
				if (!primitiveType.getValueAsString().equalsIgnoreCase(blockedValue)) {
					// doesn't match
					// no block
					ourLog.trace("Value at path {} does not match - mdm will not block.", path);
					return false;
				}
			} else {
				// blocking can only be done by evaluating primitive types
				// additional fhirpath values required
				ourLog.warn(
						"FhirPath {} yields a non-primitive value; blocking is only supported on primitive field types.",
						path);
				return false;
			}
		}

		// if we got here, all blocking rules evaluated to true
		return true;
	}
}
