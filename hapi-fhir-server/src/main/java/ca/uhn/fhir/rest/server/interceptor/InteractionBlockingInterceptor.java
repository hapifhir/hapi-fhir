package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.OperationMethodBinding;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This interceptor can be used to selectively block specific interactions/operations from
 * the server's capabilities. This interceptor must be configured and registered to a
 * {@link ca.uhn.fhir.rest.server.RestfulServer} prior to any resource provider
 * classes being registered to it. This interceptor will then examine any
 * provider classes being registered and may choose to discard some or all
 * of the method bindings on each provider.
 * <p>
 * For example, if this interceptor is configured to block resource creation, then
 * when a resource provider is registered that has both a
 * {@link ca.uhn.fhir.rest.annotation.Read @Read} method and a
 * {@link ca.uhn.fhir.rest.annotation.Create @Create} method, the
 * create method will be ignored and not bound.
 * </p>
 * <p>
 * Note: This interceptor is not a security interceptor! It can be used to remove
 * writes capabilities from a FHIR endpoint (for example) but it does not guarantee
 * that writes won't be possible. Security rules should be enforced using
 * {@link ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor} or
 * a similar strategy. However, this interceptor can be useful in order to
 * clarify the intent of an endpoint to the outside world. Of particular note,
 * even if a create method has been blocked from binding by this interceptor,
 * it may still be possible to create resources via a FHIR transaction unless
 * proper security has been implemented.
 * </p>
 *
 * @since 6.2.0
 */
@Interceptor
public class InteractionBlockingInterceptor {

	public static final Set<RestOperationTypeEnum> ALLOWED_OP_TYPES;
	private static final Logger ourLog = LoggerFactory.getLogger(InteractionBlockingInterceptor.class);

	static {
		Set<RestOperationTypeEnum> allowedOpTypes = new TreeSet<>();
		allowedOpTypes.add(RestOperationTypeEnum.META);
		allowedOpTypes.add(RestOperationTypeEnum.META_ADD);
		allowedOpTypes.add(RestOperationTypeEnum.META_DELETE);
		allowedOpTypes.add(RestOperationTypeEnum.PATCH);
		allowedOpTypes.add(RestOperationTypeEnum.READ);
		allowedOpTypes.add(RestOperationTypeEnum.CREATE);
		allowedOpTypes.add(RestOperationTypeEnum.UPDATE);
		allowedOpTypes.add(RestOperationTypeEnum.DELETE);
		allowedOpTypes.add(RestOperationTypeEnum.BATCH);
		allowedOpTypes.add(RestOperationTypeEnum.TRANSACTION);
		allowedOpTypes.add(RestOperationTypeEnum.VALIDATE);
		allowedOpTypes.add(RestOperationTypeEnum.SEARCH_TYPE);
		allowedOpTypes.add(RestOperationTypeEnum.HISTORY_TYPE);
		allowedOpTypes.add(RestOperationTypeEnum.HISTORY_INSTANCE);
		allowedOpTypes.add(RestOperationTypeEnum.HISTORY_SYSTEM);
		ALLOWED_OP_TYPES = Collections.unmodifiableSet(allowedOpTypes);
	}

	private final FhirContext myCtx;
	private final Set<String> myAllowedKeys = new HashSet<>();

	/**
	 * Constructor
	 */
	public InteractionBlockingInterceptor(@Nonnull FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		myCtx = theFhirContext;
	}

	@Hook(Pointcut.SERVER_PROVIDER_METHOD_BOUND)
	public BaseMethodBinding bindMethod(BaseMethodBinding theMethodBinding) {

		boolean allowed = true;
		String resourceName = theMethodBinding.getResourceName();
		RestOperationTypeEnum restOperationType = theMethodBinding.getRestOperationType();
		switch (restOperationType) {
			case EXTENDED_OPERATION_SERVER:
			case EXTENDED_OPERATION_TYPE:
			case EXTENDED_OPERATION_INSTANCE: {
				OperationMethodBinding operationMethodBinding = (OperationMethodBinding) theMethodBinding;
				if (!myAllowedKeys.isEmpty()) {
					if (!myAllowedKeys.contains(operationMethodBinding.getName())) {
						allowed = false;
					}
				}
				break;
			}
			default: {
				if (restOperationType == RestOperationTypeEnum.VREAD) {
					restOperationType = RestOperationTypeEnum.READ;
				}
				String key = toKey(resourceName, restOperationType);
				if (!myAllowedKeys.isEmpty()) {
					if (!myAllowedKeys.contains(key)) {
						allowed = false;
					}
				}
				break;
			}
		}

		if (!allowed) {
			ourLog.info("Skipping method binding for {}:{} provided by {}", resourceName, restOperationType, theMethodBinding.getMethod());
			return null;
		}

		return theMethodBinding;
	}

	/**
	 * Adds an interaction that will be permitted.
	 */
	public void addAllowedInteraction(String theResourceType, RestOperationTypeEnum theInteractionType) {
		Validate.notBlank(theResourceType, "theResourceType must not be null or blank");
		Validate.notNull(theInteractionType, "theInteractionType must not be null");
		Validate.isTrue(ALLOWED_OP_TYPES.contains(theInteractionType), "Operation type %s can not be used as an allowable rule", theInteractionType);
		Validate.isTrue(myCtx.getResourceType(theResourceType) != null, "Unknown resource type: %s");
		String key = toKey(theResourceType, theInteractionType);
		myAllowedKeys.add(key);
	}


	/**
	 * Adds an interaction or operation that will be permitted. Allowable formats
	 * are:
	 * <ul>
	 *    <li>[resourceType]:[interaction]</li>
	 *    <li>$[operation-name]</li>
	 * </ul>
	 */
	public void addAllowedSpec(String theSpec) {
		Validate.notBlank(theSpec, "theSpec must not be null or blank");
		int colonIdx = theSpec.indexOf(':');
		Validate.isTrue(colonIdx > 0, "Invalid spec: %s", theSpec);

		String resourceName = theSpec.substring(0, colonIdx);
		String interactionName = theSpec.substring(colonIdx + 1);
		RestOperationTypeEnum interaction = RestOperationTypeEnum.forCode(interactionName);
		Validate.notNull(interaction, "Unknown interaction %s in spec %s", interactionName, theSpec);
		addAllowedInteraction(resourceName, interaction);
	}

	public void addAllowedOperation(String theOperationName) {
		Validate.notBlank(theOperationName, "theOperationName must not be null or blank");
		Validate.isTrue(theOperationName.startsWith("$"), "Invalid operation name: %s", theOperationName);
		myAllowedKeys.add(theOperationName);
	}


	private static String toKey(String theResourceType, RestOperationTypeEnum theRestOperationTypeEnum) {
		if (isBlank(theResourceType)) {
			return theRestOperationTypeEnum.getCode();
		}
		return theResourceType + ":" + theRestOperationTypeEnum.getCode();
	}


}
