package ca.uhn.fhir.jpa.packages;

import org.junit.jupiter.api.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.thymeleaf.util.ListUtils.sort;

public class PackageVersionComparatorTest {

	private PackageVersionComparator myCmp = new PackageVersionComparator();

	@Test
	public void testCompareVersion() {
		assertThat(sort(newArrayList("10.1", "10.2"), myCmp), contains("10.1", "10.2"));
		assertThat(sort(newArrayList("10.2", "10.1"), myCmp), contains("10.1", "10.2"));
		assertThat(sort(newArrayList("10.1.2.3", "9.1.2.3"), myCmp), contains("9.1.2.3", "10.1.2.3"));
		assertThat(sort(newArrayList("9.1.2.3", "10.1.2.3"), myCmp), contains("9.1.2.3", "10.1.2.3"));
		assertThat(sort(newArrayList("9.1.2.3", "9.1"), myCmp), contains("9.1", "9.1.2.3"));
		assertThat(sort(newArrayList("9.1", "9.1.2.3"), myCmp), contains("9.1", "9.1.2.3"));
		assertThat(sort(newArrayList("A", "1"), myCmp), contains("1", "A"));
		assertThat(sort(newArrayList("1", "A"), myCmp), contains("1", "A"));
		assertThat(sort(newArrayList("A", "B"), myCmp), contains("A", "B"));
	}

	@Test
	public void testIsEquivalent() {
		assertTrue(PackageVersionComparator.isEquivalent("1.2.x", "1.2.3"));
		assertTrue(PackageVersionComparator.isEquivalent("1.2", "1.2.3"));
		assertTrue(PackageVersionComparator.isEquivalent("1.2.3", "1.2.3"));
		assertFalse(PackageVersionComparator.isEquivalent("1.2.4", "1.2.3"));
		assertFalse(PackageVersionComparator.isEquivalent("1.3", "1.2.3"));

	}


}
