package ca.uhn.fhir.jpa.subscription.module;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.contains;

public class CanonicalSubscriptionTest {

	@Test
	public void testGetChannelExtension() {

		HashMap<String, List<String>> inMap = new HashMap<>();
		inMap.put("key1", Lists.newArrayList("VALUE1"));
		inMap.put("key2", Lists.newArrayList("VALUE2a", "VALUE2b"));

		CanonicalSubscription s = new CanonicalSubscription();
		s.setChannelExtensions(inMap);

		assertThat(s.getChannelExtension("key1"), Matchers.equalTo("VALUE1"));
		assertThat(s.getChannelExtension("key2"), Matchers.equalTo("VALUE2a"));
		assertThat(s.getChannelExtension("key3"), Matchers.nullValue());
	}

	@Test
	public void testGetChannelExtensions() {

		HashMap<String, List<String>> inMap = new HashMap<>();
		inMap.put("key1", Lists.newArrayList("VALUE1"));
		inMap.put("key2", Lists.newArrayList("VALUE2a", "VALUE2b"));

		CanonicalSubscription s = new CanonicalSubscription();
		s.setChannelExtensions(inMap);

		assertThat(s.getChannelExtensions("key1"), Matchers.contains("VALUE1"));
		assertThat(s.getChannelExtensions("key2"), Matchers.contains("VALUE2a", "VALUE2b"));
		assertThat(s.getChannelExtensions("key3"), Matchers.empty());
	}
}
