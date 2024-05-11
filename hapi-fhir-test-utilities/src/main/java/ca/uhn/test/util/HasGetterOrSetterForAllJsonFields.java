package ca.uhn.test.util;

import ca.uhn.fhir.model.api.IModelJson;
import org.assertj.core.api.AbstractAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.IntrospectionException;
import java.beans.FeatureDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class HasGetterOrSetterForAllJsonFields extends AbstractAssert<HasGetterOrSetterForAllJsonFields, Class<? extends IModelJson>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HasGetterOrSetterForAllJsonFields.class);

	public HasGetterOrSetterForAllJsonFields(Class<? extends IModelJson> actual) {
		super(actual, HasGetterOrSetterForAllJsonFields.class);
	}

	public static HasGetterOrSetterForAllJsonFields assertThatJsonProperties(Class<? extends IModelJson> actual) {
		return new HasGetterOrSetterForAllJsonFields(actual);
	}

	public HasGetterOrSetterForAllJsonFields hasGetterAndSetterForAllJsonFields() {
		isNotNull();

		List<String> jsonPropertyFields = getJsonPropertyFields(actual);
		List<String> properties = getProperties(actual);
		LOGGER.info("{}: testing {} @JsonProperty fields", actual.getSimpleName(), jsonPropertyFields.size());

		if (!properties.containsAll(jsonPropertyFields)) {
			failWithMessage("Expected class <%s> to have getters and setters for all JSON property fields <%s>, but some were missing <%s>",
				actual.getName(), jsonPropertyFields, properties);
		}

		return this;
	}

	@Nonnull
	private List<String> getJsonPropertyFields(Class<? extends IModelJson> item) {
		List<Field> fields = new ArrayList<>();
		populateFields(fields, item);

		return fields.stream()
			.filter(this::isJsonProperty)
			.filter(this::isNotCollection)
			.filter(this::isNotMap)
			.map(Field::getName)
			.map(this::stripPrefix)
			.map(this::stripUnderscoreSuffix)
			.sorted()
			.collect(Collectors.toList());
	}

	private boolean isJsonProperty(Field theField) {
		if (!theField.isAnnotationPresent(JsonProperty.class)) {
			return false;
		}
		Schema apiModelProperty = theField.getAnnotation(Schema.class);
		if (apiModelProperty != null && apiModelProperty.accessMode() == Schema.AccessMode.READ_ONLY) {
			return false;
		}
		return apiModelProperty == null || !apiModelProperty.hidden();
	}

	private boolean isNotCollection(Field field) {
		return !java.util.Collection.class.isAssignableFrom(field.getType());
	}

	private boolean isNotMap(Field field) {
		return !java.util.Map.class.isAssignableFrom(field.getType());
	}

	private String stripPrefix(String fieldName) {
		if (fieldName.startsWith("my")) {
			return fieldName.substring(2, 3).toLowerCase() + fieldName.substring(3);
		}
		return fieldName;
	}

	private String stripUnderscoreSuffix(String fieldName) {
		if (fieldName.endsWith("_")) {
			return fieldName.substring(0, fieldName.length() - 1);
		}
		return fieldName;
	}

	private List<String> getProperties(Class<? extends IModelJson> item) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(item);
			return Arrays.stream(beanInfo.getPropertyDescriptors())
				.map(FeatureDescriptor::getName)
				.filter(name -> !"class".equals(name))
				.map(this::lowerCaseFirstLetter)
				.sorted()
				.collect(Collectors.toList());
		} catch (IntrospectionException e) {
			throw new AssertionError("Unable to introspect " + item.getName(), e);
		}
	}

	private String lowerCaseFirstLetter(String propertyName) {
		return propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
	}

	private static void populateFields(List<Field> fields, Class<?> item) {
		fields.addAll(Arrays.asList(item.getDeclaredFields()));

		if (item.getSuperclass() != null) {
			populateFields(fields, item.getSuperclass());
		}
	}
}
