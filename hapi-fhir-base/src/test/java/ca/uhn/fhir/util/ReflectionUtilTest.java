package ca.uhn.fhir.util;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.fail;


public class ReflectionUtilTest {

	@Test
	public void testNewInstance() {
		assertThat(ReflectionUtil.newInstance(ArrayList.class).getClass()).isEqualTo(ArrayList.class);
	}

	@Test
	public void testNewInstanceOrReturnNullString() {
		assertThat(ReflectionUtil.newInstanceOrReturnNull(ArrayList.class.getName(), List.class).getClass()).isEqualTo(ArrayList.class);
	}

	@Test
	public void testNewInstanceOrReturnNullWrong1() {
		assertThat(ReflectionUtil.newInstanceOrReturnNull("foo.Foo", List.class)).isNull();
	}

	@Test
	public void testNewInstanceOrReturnNullWrong2() {
		try {
			ReflectionUtil.newInstanceOrReturnNull("java.lang.String", List.class);
			fail("");
		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1787) + "java.lang.String is not assignable to interface java.util.List");
		}
	}

	@Test
	public void testNewInstanceFail() {
		try {
			ReflectionUtil.newInstance(List.class);
			fail("");
		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1784) + "Failed to instantiate java.util.List");
		}
	}

	@Test
	public void testTypeExists() {
		assertThat(ReflectionUtil.typeExists("ca.Foo")).isFalse();
		assertThat(ReflectionUtil.typeExists(String.class.getName())).isTrue();
	}

	@Test
	public void testDescribeMethod() throws NoSuchMethodException {
		Method method = String.class.getMethod("startsWith", String.class, int.class);
		String description = ReflectionUtil.describeMethodInSortFriendlyWay(method);
		assertThat(description).isEqualTo("startsWith returns(boolean) params(java.lang.String, int)");
	}

}
