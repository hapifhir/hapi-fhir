/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.test.junit;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotatedFields;
import static org.junit.platform.commons.util.ReflectionUtils.makeAccessible;

/**
 * Register any field annotated with @{@link JunitFieldProvider} as a parameter provider, matching parameters by type.
 * Can also be used directly via @RegisterExtension, using values passed to the constructor.
 */
public class JunitFieldParameterProviderExtension implements BeforeAllCallback, BeforeEachCallback, ParameterResolver {

	Set<Object> myValues = new HashSet<>();

	/**
	 * Junit constructor for @{@link org.junit.jupiter.api.extension.ExtendWith}
	 */
	public JunitFieldParameterProviderExtension() {}

	/**
	 * Used for explicit registration.
	 * @param theValues the values to register as provided junit parameters.
	 */
	public JunitFieldParameterProviderExtension(Object ...theValues) {
		Collections.addAll(myValues, theValues);
	}


	/**
	 * Do we have a value matching the parameter type?
	 */
	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Class<?> paramType = parameterContext.getParameter().getType();
		return myValues.stream().anyMatch(v->paramType.isAssignableFrom(v.getClass()));
	}

	/**
	 * Find the first value matching the parameter type.
	 */
	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		Class<?> paramType = parameterContext.getParameter().getType();
		return myValues.stream().filter(v->paramType.isAssignableFrom(v.getClass())).findAny().orElseThrow();
	}

	/**
	 * Collect all statics annotated with @JunitFieldProvider.
	 */
	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		collectFields(null, context.getRequiredTestClass(), ReflectionUtils::isStatic);
	}

	/**
	 * Collect any instance fields annotated with @JunitFieldProvider.
	 */
	@Override
	public void beforeEach(ExtensionContext context) {
		context.getRequiredTestInstances().getAllInstances() //
			.forEach(instance -> collectFields(instance, instance.getClass(), ReflectionUtils::isStatic));
	}

	private void collectFields(Object testInstance, Class<?> testClass,
							   Predicate<Field> predicate) {

		findAnnotatedFields(testClass, JunitFieldProvider.class, predicate).forEach(field -> {
			try {
				makeAccessible(field);
				myValues.add(field.get(testInstance));
			}
			catch (Exception t) {
				ExceptionUtils.throwAsUncheckedException(t);
			}
		});
	}

}
