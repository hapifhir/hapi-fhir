package ca.uhn.fhir.rest.api.server;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class IBundleProviderTest {

	@Test
	public void testIsEmptyDefaultMethod_SizePopulated() {
		SimpleBundleProvider provider = new SimpleBundleProvider();
		assertTrue(provider.isEmpty());

		provider = new SimpleBundleProvider(Lists.newArrayList(new TestResource()));
		assertFalse(provider.isEmpty());
	}

	@Test
	public void testIsEmptyDefaultMethod_SizeReturnsNull() {
		SimpleBundleProvider provider = new SimpleBundleProvider() {
			@Override
			public Integer size() {
				return null;
			}
		};
		assertTrue(provider.isEmpty());

		provider = new SimpleBundleProvider(Lists.newArrayList(new TestResource())) {
			@Override
			public Integer size() {
				return null;
			}
		};
		assertFalse(provider.isEmpty());
	}

	@Test
	void getResources() {
		SimpleBundleProvider provider = new SimpleBundleProvider() {
			@Override
			public Integer size() {
				return null;
			}
		};
		try {
			provider.getAllResources();
			fail();		} catch (ConfigurationException e) {
			assertEquals(Msg.code(464) + "Attempt to request all resources from an asynchronous search result.  The SearchParameterMap for this search probably should have been synchronous.", e.getMessage());
		}
	}

	private static class TestResource implements IAnyResource {

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean hasFormatComment() {
			return false;
		}

		@Override
		public List<String> getFormatCommentsPre() {
			return null;
		}

		@Override
		public List<String> getFormatCommentsPost() {
			return null;
		}

		@Override
		public Object getUserData(String theName) {
			return null;
		}

		@Override
		public IAnyResource setId(String theId) {
			return null;
		}

		@Override
		public void setUserData(String theName, Object theValue) {

		}

		@Override
		public IBaseMetaType getMeta() {
			return null;
		}

		@Override
		public String getId() {
			return null;
		}

		@Override
		public IIdType getIdElement() {
			return null;
		}

		@Override
		public IPrimitiveType<String> getLanguageElement() {
			return null;
		}

		@Override
		public IBaseResource setId(IIdType theId) {
			return null;
		}

		@Override
		public FhirVersionEnum getStructureFhirVersionEnum() {
			return null;
		}
	}
}
