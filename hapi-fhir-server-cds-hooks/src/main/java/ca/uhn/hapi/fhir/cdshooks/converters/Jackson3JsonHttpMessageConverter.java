package ca.uhn.hapi.fhir.cdshooks.converters;

import ca.uhn.fhir.i18n.Msg;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * A Spring 6 compatible HTTP message converter for Jackson 3 ({@code tools.jackson}).
 *
 * <h2>Why this class exists</h2>
 *
 * <p>Spring Framework 6 ships only {@code MappingJackson2HttpMessageConverter}, which is
 * hardwired to the Jackson 2 package namespace ({@code com.fasterxml.jackson}).  The
 * Jackson 3 equivalent — {@code JacksonJsonHttpMessageConverter} — was introduced in
 * Spring Framework 7.0 and does not exist in Spring 6.
 *
 * <p>Jackson 3 relocated its entire API to the {@code tools.jackson} package namespace,
 * making it binary-incompatible with Jackson 2.  As a result, Spring 6's built-in
 * converter cannot accept a {@code tools.jackson.databind.ObjectMapper} instance, and
 * there is no official bridge provided by either Spring or Jackson for this combination.
 *
 * <p>This class fills that gap: it extends Spring 6's {@link AbstractHttpMessageConverter}
 * (a Spring-version-agnostic base class with no Jackson type dependencies) and delegates
 * all serialization and deserialization to a caller-supplied
 * {@code tools.jackson.databind.ObjectMapper}.  This allows Jackson 3 to be adopted
 * incrementally — upgrading the Jackson dependency ahead of a Spring 7 migration —
 * without requiring both upgrades to land simultaneously.
 *
 * <h2>Intended lifetime</h2>
 *
 * <p>This class is a <strong>transitional shim</strong>.  Once the project upgrades to
 * Spring Framework 7+, it should be replaced with the official
 * {@code org.springframework.http.converter.json.JacksonJsonHttpMessageConverter},
 * and this class should be deleted.
 *
 * <h2>Limitations</h2>
 *
 * <ul>
 *   <li>Generic type reads (e.g. {@code List<Foo>}) require implementing
 *       {@code GenericHttpMessageConverter} and using
 *       {@code ObjectMapper.constructType(Type)} — not covered here.</li>
 *   <li>{@code JacksonException} is unchecked in Jackson 3, so the compiler will not
 *       enforce catching it.  It is caught explicitly here to wrap it in Spring's
 *       {@link org.springframework.http.converter.HttpMessageNotReadableException} /
 *       {@link org.springframework.http.converter.HttpMessageNotWritableException},
 *       preserving standard Spring MVC error handling behaviour.</li>
 * </ul>
 *
 * @see org.springframework.http.converter.AbstractHttpMessageConverter
 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/converter/json/JacksonJsonHttpMessageConverter.html">
 *      JacksonJsonHttpMessageConverter (Spring 7+)</a>
 */
public class Jackson3JsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

	private final ObjectMapper myMapper;

	public Jackson3JsonHttpMessageConverter(ObjectMapper theMapper) {
		super(MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
		myMapper = theMapper;
	}

	@Override
	protected boolean supports(Class<?> theClazz) {
		return true;
	}

	@Override
	protected Object readInternal(Class<?> theClazz, HttpInputMessage theInputMessage)
			throws IOException, HttpMessageNotReadableException {
		try {
			return myMapper.readValue(theInputMessage.getBody(), theClazz);
		} catch (JacksonException e) {
			throw new HttpMessageNotReadableException(
					Msg.code(9901) + "Could not read JSON: " + e.getMessage(), e, theInputMessage);
		}
	}

	@Override
	protected void writeInternal(Object theObject, HttpOutputMessage theOutputMessage)
			throws IOException, HttpMessageNotWritableException {
		try {
			myMapper.writeValue(theOutputMessage.getBody(), theObject);
		} catch (JacksonException e) {
			throw new HttpMessageNotWritableException(Msg.code(9902) + "Could not write JSON: " + e.getMessage(), e);
		}
	}

	/**
	 * Optional: support typed reads (e.g. generic collections like List<Foo>).
	 * Override canRead/read on GenericHttpMessageConverter if needed.
	 */
	public boolean canRead(Type theType, Class<?> theContextClass, MediaType theMediaType) {
		return canRead(theMediaType);
	}
}
