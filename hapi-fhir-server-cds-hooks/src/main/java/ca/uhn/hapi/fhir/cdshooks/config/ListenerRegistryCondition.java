package ca.uhn.hapi.fhir.cdshooks.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ListenerRegistryCondition implements Condition {
	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
		return conditionContext.getRegistry()
			.containsBeanDefinition("ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry");
	}
}
