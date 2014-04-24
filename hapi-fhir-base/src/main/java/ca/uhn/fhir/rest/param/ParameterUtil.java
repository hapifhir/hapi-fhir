package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.ServerBase;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.util.ReflectionUtil;

public class ParameterUtil {

	@SuppressWarnings("unchecked")
	public static List<IParameter> getResourceParameters(Method method) {
		List<IParameter> parameters = new ArrayList<IParameter>();

		Class<?>[] parameterTypes = method.getParameterTypes();
		int paramIndex = 0;
		for (Annotation[] annotations : method.getParameterAnnotations()) {
			boolean haveHandledMethod = false;

			Class<?> parameterType = parameterTypes[paramIndex];
			if (parameterType.equals(HttpServletRequest.class) || parameterType.equals(ServletRequest.class)) {
				ServletRequestParameter param = new ServletRequestParameter();
				parameters.add(param);
			} else if (parameterType.equals(HttpServletResponse.class) || parameterType.equals(ServletResponse.class)) {
				ServletResponseParameter param = new ServletResponseParameter();
				parameters.add(param);
			} else {
				for (int i = 0; i < annotations.length; i++) {
					Annotation nextAnnotation = annotations[i];

					Class<? extends java.util.Collection<?>> outerCollectionType = null;
					Class<? extends java.util.Collection<?>> innerCollectionType = null;

					if (Collection.class.isAssignableFrom(parameterType)) {
						innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
						parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(method, paramIndex);
					}

					if (Collection.class.isAssignableFrom(parameterType)) {
						outerCollectionType = innerCollectionType;
						innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
						parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(method, paramIndex);
					}

					IParameter param;
					if (nextAnnotation instanceof RequiredParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((RequiredParam) nextAnnotation).name());
						parameter.setRequired(true);
						parameter.setType(parameterType, innerCollectionType, outerCollectionType);
						param = parameter;
					} else if (nextAnnotation instanceof OptionalParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((OptionalParam) nextAnnotation).name());
						parameter.setRequired(false);
						parameter.setType(parameterType, innerCollectionType, outerCollectionType);
						param = parameter;
					} else if (nextAnnotation instanceof IncludeParam) {
						if (parameterType != PathSpecification.class || innerCollectionType == null || outerCollectionType != null) {
							throw new ConfigurationException("Method '" + method.getName() + "' is annotated with @" + IncludeParam.class.getSimpleName() + " but has a type other than Collection<"
									+ PathSpecification.class.getSimpleName() + ">");
						}
						Class<? extends Collection<PathSpecification>> instantiableCollectionType = (Class<? extends Collection<PathSpecification>>) CollectionBinder.getInstantiableCollectionType(
								innerCollectionType, "Method '" + method.getName() + "'");

						param = new IncludeParameter((IncludeParam) nextAnnotation, instantiableCollectionType);
					} else if (nextAnnotation instanceof ResourceParam) {
						if (!IResource.class.isAssignableFrom(parameterType)) {
							throw new ConfigurationException("Method '" + method.getName() + "' is annotated with @" + ResourceParam.class.getSimpleName()
									+ " but has a type that is not an implemtation of " + IResource.class.getCanonicalName());
						}
						param = new ResourceParameter((Class<? extends IResource>) parameterType);
					} else if (nextAnnotation instanceof IdParam || nextAnnotation instanceof VersionIdParam) {
						param = null;
					} else if (nextAnnotation instanceof ServerBase) {
						param = new ServerBaseParameter();
					} else {
						continue;
					}

					haveHandledMethod = true;
					parameters.add(param);
					break;

				}

				if (!haveHandledMethod) {
					throw new ConfigurationException("Parameter #" + paramIndex + " of method '" + method.getName() + "' on type '" + method.getDeclaringClass().getCanonicalName()
							+ "' has no recognized FHIR interface parameter annotations. Don't know how to handle this parameter");
				}
			}

			paramIndex++;
		}
		return parameters;
	}

	
}
