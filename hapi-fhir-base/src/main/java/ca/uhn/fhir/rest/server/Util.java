package ca.uhn.fhir.rest.server;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.rest.annotation.Optional;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Required;
import ca.uhn.fhir.rest.param.Parameter;
import ca.uhn.fhir.util.ReflectionUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class Util {
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
	public static List<Parameter> getResourceParameters(Method method) {
		List<Parameter> parameters = new ArrayList<Parameter>();

		Class<?>[] parameterTypes = method.getParameterTypes();
		for (Annotation[] annotations : method.getParameterAnnotations()) {
			for (int i = 0; i < annotations.length; i++) {
				Annotation nextAnnotation = annotations[i];
				Parameter parameter = new Parameter();
				Class<?> parameterType = parameterTypes[i];
				
				Class<? extends java.util.Collection<?>> outerCollectionType = null;
				Class<? extends java.util.Collection<?>> innerCollectionType = null;
				
				if (Collection.class.isAssignableFrom(parameterType)) {
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(method, i);
				}

				if (Collection.class.isAssignableFrom(parameterType)) {
					outerCollectionType = innerCollectionType;
					innerCollectionType = (Class<? extends java.util.Collection<?>>) parameterType;
					parameterType = ReflectionUtil.getGenericCollectionTypeOfMethodParameter(method, i);
				}

				if (nextAnnotation instanceof Required) {
					parameter.setName(((Required) nextAnnotation).name());
					parameter.setRequired(true);
					parameter.setType(parameterType, innerCollectionType, outerCollectionType);

				} else if (nextAnnotation instanceof Optional) {
					parameter.setName(((Optional) nextAnnotation).name());
					parameter.setRequired(false);
					parameter.setType(parameterType, innerCollectionType, innerCollectionType);
				}
				parameters.add(parameter);
			}
		}
		return parameters;
	}

	public static Integer findReadIdParameterIndex(Method theMethod) {
		return findParamIndex(theMethod, Read.IdParam.class);
	}

	public static Integer findReadVersionIdParameterIndex(Method theMethod) {
		return findParamIndex(theMethod, Read.VersionIdParam.class);
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
}
