/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.rest.server.RestfulServer;
import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * The purpose of this Condition is to verify that the CR dependent beans RestfulServer and EvaluationSettings exist.
 */
public class CrConfigCondition implements Condition {
	private static final Logger ourLog = LoggerFactory.getLogger(CrConfigCondition.class);

	@Override
	public boolean matches(ConditionContext theConditionContext, AnnotatedTypeMetadata theAnnotatedTypeMetadata) {
		ConfigurableListableBeanFactory beanFactory = theConditionContext.getBeanFactory();
		try {
			RestfulServer bean = beanFactory.getBean(RestfulServer.class);
			if (bean == null) {
				return false;
			}
		} catch (Exception e) {
			ourLog.warn("CrConfigCondition not met: Missing RestfulServer bean");
			return false;
		}
		try {
			EvaluationSettings bean = beanFactory.getBean(EvaluationSettings.class);
			if (bean == null) {
				return false;
			}
		} catch (Exception e) {
			ourLog.warn("CrConfigCondition not met: Missing EvaluationSettings bean");
			return false;
		}
		return true;
	}
}
