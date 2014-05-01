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

import static org.apache.commons.lang3.StringUtils.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.ServerBase;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.util.ReflectionUtil;

public class ParameterUtil {

	@SuppressWarnings("unchecked")
	public static List<IParameter> getResourceParameters(Method theMethod) {
		List<IParameter> parameters = new ArrayList<IParameter>();

		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		int paramIndex = 0;
		for (Annotation[] annotations : theMethod.getParameterAnnotations()) {

			IParameter param = null;
			Class<?> parameterType = parameterTypes[paramIndex];
			Class<? extends java.util.Collection<?>> outerCollectionType = null;
			Class<? extends java.util.Collection<?>> innerCollectionType = null;
			if (Collection.class.isAssignableFrom(parameterType)) {
				innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
				parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, paramIndex);
			}
			if (Collection.class.isAssignableFrom(parameterType)) {
				outerCollectionType = innerCollectionType;
				innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
				parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(theMethod, paramIndex);
			}
			if (Collection.class.isAssignableFrom(parameterType)) {
				throw new ConfigurationException("Argument #" + paramIndex + " of Method '" + theMethod.getName() + "' in type '" + theMethod.getDeclaringClass().getCanonicalName() + "' is of an invalid generic type (can not be a collection of a collection of a collection)");
			}

			if (parameterType.equals(HttpServletRequest.class) || parameterType.equals(ServletRequest.class)) {
				param = new ServletRequestParameter();
			} else if (parameterType.equals(HttpServletResponse.class) || parameterType.equals(ServletResponse.class)) {
				param = new ServletResponseParameter();
			} else {
				for (int i = 0; i < annotations.length && param == null; i++) {
					Annotation nextAnnotation = annotations[i];

					if (nextAnnotation instanceof RequiredParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((RequiredParam) nextAnnotation).name());
						parameter.setRequired(true);
						parameter.setType(parameterType, innerCollectionType, outerCollectionType);
						extractDescription(parameter, annotations);
						param = parameter;
					} else if (nextAnnotation instanceof OptionalParam) {
						SearchParameter parameter = new SearchParameter();
						parameter.setName(((OptionalParam) nextAnnotation).name());
						parameter.setRequired(false);
						parameter.setType(parameterType, innerCollectionType, outerCollectionType);
						extractDescription(parameter, annotations);
						param = parameter;
					} else if (nextAnnotation instanceof IncludeParam) {
						Class<? extends Collection<PathSpecification>> instantiableCollectionType;
						Class<?> specType;

						if (parameterType == String.class) {
							instantiableCollectionType=null;
							specType=String.class;
						}else if (parameterType != PathSpecification.class || innerCollectionType == null || outerCollectionType != null) {
							throw new ConfigurationException("Method '" + theMethod.getName() + "' is annotated with @" + IncludeParam.class.getSimpleName() + " but has a type other than Collection<" + PathSpecification.class.getSimpleName() + ">");
						} else {
							instantiableCollectionType = (Class<? extends Collection<PathSpecification>>) CollectionBinder.getInstantiableCollectionType(innerCollectionType, "Method '" + theMethod.getName() + "'");
							specType = PathSpecification.class;
						}
						
						param = new IncludeParameter((IncludeParam) nextAnnotation, instantiableCollectionType, specType);
					} else if (nextAnnotation instanceof ResourceParam) {
						if (!IResource.class.isAssignableFrom(parameterType)) {
							throw new ConfigurationException("Method '" + theMethod.getName() + "' is annotated with @" + ResourceParam.class.getSimpleName() + " but has a type that is not an implemtation of " + IResource.class.getCanonicalName());
						}
						param = new ResourceParameter((Class<? extends IResource>) parameterType);
					} else if (nextAnnotation instanceof IdParam || nextAnnotation instanceof VersionIdParam) {
						param = new NullParameter();
					} else if (nextAnnotation instanceof ServerBase) {
						param = new ServerBaseParameter();
					} else if (nextAnnotation instanceof Since) {
						param = new SinceParameter();
					} else if (nextAnnotation instanceof Count) {
						param = new CountParameter();
					} else {
						continue;
					}

				}

			}

			if (param == null) {
				throw new ConfigurationException("Parameter #" + paramIndex + " of method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName()
						+ "' has no recognized FHIR interface parameter annotations. Don't know how to handle this parameter");
			}

			param.initializeTypes(theMethod, outerCollectionType, innerCollectionType, parameterType);
			parameters.add(param);

			paramIndex++;
		}
		return parameters;
	}

	private static void extractDescription(SearchParameter theParameter, Annotation[] theAnnotations) {
		for (Annotation annotation : theAnnotations) {
			if (annotation instanceof Description) {
				Description desc = (Description) annotation;
				if (isNotBlank(desc.formalDefinition())) {
					theParameter.setDescription(desc.formalDefinition());
				} else {
					theParameter.setDescription(desc.shortDefinition());
				}
			}
		}
	}

	public static InstantDt toInstant(Object theArgument) {
		if (theArgument instanceof InstantDt) {
			return (InstantDt) theArgument;
		}
		if (theArgument instanceof Date) {
			return new InstantDt((Date) theArgument);
		}
		if (theArgument instanceof Calendar) {
			return new InstantDt((Calendar) theArgument);
		}
		return null;
	}

	public static Set<Class<?>> getBindableInstantTypes() {
		// TODO: make this constant
		HashSet<Class<?>> retVal = new HashSet<Class<?>>();
		retVal.add(InstantDt.class);
		retVal.add(Date.class);
		retVal.add(Calendar.class);
		return retVal;
	}

	public static Object fromInstant(Class<?> theType, InstantDt theArgument) {
		if (theType.equals(InstantDt.class)) {
			if (theArgument == null) {
				return new InstantDt();
			}
			return theArgument;
		}
		if (theType.equals(Date.class)) {
			if (theArgument == null) {
				return null;
			}
			return theArgument.getValue();
		}
		if (theType.equals(Calendar.class)) {
			if (theArgument == null) {
				return null;
			}
			return DateUtils.toCalendar(theArgument.getValue());
		}
		throw new IllegalArgumentException("Invalid instant type:" + theType);
	}

	public static IntegerDt toInteger(Object theArgument) {
		if (theArgument instanceof IntegerDt) {
			return (IntegerDt) theArgument;
		}
		if (theArgument instanceof Integer) {
			return new IntegerDt((Integer) theArgument);
		}
		return null;
	}

	public static Set<Class<?>> getBindableIntegerTypes() {
		// TODO: make this constant
		HashSet<Class<?>> retVal = new HashSet<Class<?>>();
		retVal.add(IntegerDt.class);
		retVal.add(Integer.class);
		return retVal;
	}

	public static Object fromInteger(Class<?> theType, IntegerDt theArgument) {
		if (theType.equals(IntegerDt.class)) {
			if (theArgument == null) {
				return new IntegerDt();
			}
			return theArgument;
		}
		if (theType.equals(Integer.class)) {
			if (theArgument == null) {
				return null;
			}
			return theArgument.getValue();
		}
		throw new IllegalArgumentException("Invalid Integer type:" + theType);
	}

}
