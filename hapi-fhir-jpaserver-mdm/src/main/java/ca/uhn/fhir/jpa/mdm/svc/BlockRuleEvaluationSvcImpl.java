package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockListRuleJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockedFieldJson;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockRuleEvaluationSvc;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class BlockRuleEvaluationSvcImpl implements IBlockRuleEvaluationSvc {
	private static final Logger ourLog = getLogger(BlockRuleEvaluationSvcImpl.class);

	private final FhirContext myFhirContext;
	private final IBlockListRuleProvider myBlockListRuleProvider;

	public BlockRuleEvaluationSvcImpl(
		FhirContext theContext,
		@Nullable IBlockListRuleProvider theIBlockListRuleProvider
	) {
		myFhirContext = theContext;
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
		List<BlockListRuleJson> rulesForResource = blockListJson.getBlockListItemJsonList()
			.stream().filter(r -> r.getResourceType().equals(resourceType))
			.collect(Collectors.toList());

		// we'll want to reuse the same resource definition since it's
		// "costly" to make
		RuntimeResourceDefinition runtimeResourceDefinition = myFhirContext.getResourceDefinition(theResource.fhirType());
		for (BlockListRuleJson rule : rulesForResource) {
			// these rules are 'or''d, so if any match,
			// mdm matching is blocked
			if (isMdmBlocked(runtimeResourceDefinition, theResource, rule)) {
				return true;
			}
		}
		// otherwise, do not block
		return false;
	}

	private boolean isMdmBlocked(RuntimeResourceDefinition theResourceDef, IAnyResource theResource, BlockListRuleJson theRule) {
		List<BlockedFieldJson> blockedFields = theRule.getBlockedFields();
		FhirTerser terser = myFhirContext.newTerser();

		// rules are anded
		// This means that if we detect any reason *not* to block
		// we don't; only if all block rules pass do we block
		for (BlockedFieldJson field : blockedFields) {
			String path = field.getFhirPath();
			String blockedValue = field.getBlockedValue();

			List<IPrimitiveType> values = terser.getValues(theResource, path, IPrimitiveType.class);

			if (values.isEmpty()) {
				// if there's no values at the given fhir path,
				// it can't be blocked
				return false;
			}

			switch (field.getBlockRule()) {
				case EXACT: {
					if (values.size() > 1) {
						// exact necessitates 1 and only 1 matching value;
						// more than 1 means it cannot be an exact match
						return false;
					}

					IPrimitiveType primitiveType = values.get(0);
					if (!primitiveType.getValueAsString().equalsIgnoreCase(blockedValue)) {
						// if the one value we have does *not* equal the blocked value
						// we aren't blocking on this
						return false;
					}
					break;
				}
				case ANY: {
					List<IPrimitiveType> matches = values.stream()
						.filter(v -> v.getValueAsString().equalsIgnoreCase(blockedValue))
						.collect(Collectors.toList());
					if (matches.isEmpty()) {
						// if there are no values that match our block value
						// then we do not block
						return false;
					}
					break;
				}
				default: {
					// unrecognized rule: do not block; log warning for now
					ourLog.warn("Unrecognized block rule type: {}. Blocking will not occur.", field.getBlockRule().name());
					return false;
				}
			}
		}

		return true;
	}
}
