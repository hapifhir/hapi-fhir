package ca.uhn.fhir.rest.method;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.PathSpecification;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.param.CollectionBinder;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.IncludeParameter;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.param.SearchParameter;
import ca.uhn.fhir.util.ReflectionUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
class Util {
	public static Integer findCountParameterIndex(Method theMethod) {
		return findParamIndex(theMethod, Count.class);
	}

	public static Integer findIdParameterIndex(Method theMethod) {
		return findParamIndex(theMethod, IdParam.class);
	}

	private static Integer findParamIndex(Method theMethod, Class<?> toFind) {
		int paramIndex = 0;
		for (Annotation[] annotations : theMethod.getParameterAnnotations()) {
			for (int annotationIndex = 0; annotationIndex < annotations.length; annotationIndex++) {
				Annotation nextAnnotation = annotations[annotationIndex];
				Class<? extends Annotation> class1 = nextAnnotation.getClass();
				if (toFind.isAssignableFrom(class1)) {
					return paramIndex;
				}
			}
			paramIndex++;
		}
		return null;
	}

	public static Integer findSinceParameterIndex(Method theMethod) {
		return findParamIndex(theMethod, Since.class);
	}

	public static Integer findVersionIdParameterIndex(Method theMethod) {
		return findParamIndex(theMethod, VersionIdParam.class);
	}

	public static Map<String, String> getQueryParams(String query) {
		try {

			Map<String, String> params = new HashMap<String, String>();
			for (String param : query.split("&")) {
				String[] pair = param.split("=");
				String key = URLDecoder.decode(pair[0], "UTF-8");
				String value = URLDecoder.decode(pair[1], "UTF-8");

				params.put(key, value);
			}
			return params;
		} catch (UnsupportedEncodingException ex) {
			throw new AssertionError(ex);
		}
	}

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
