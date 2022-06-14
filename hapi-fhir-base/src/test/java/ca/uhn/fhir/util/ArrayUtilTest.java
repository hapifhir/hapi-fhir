package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArrayUtilTest {

	@Test
	public void subdivideListIntoListOfLists_withValidInput_returnsExpectedSublists() {
		int size = 100;

		// 3 tests - to make sure we check the edge cases
		for (int adjust : new int[] {-1, 0, 1}) {
			int totalCount = 200 + adjust;
			HashSet<String> input = new HashSet<>();
			for (int i = 0; i < totalCount; i++) {
				String v = "String value " + i;
				input.add(v);
			}

			List<List<String>> output = ArrayUtil.subdivideListIntoListOfLists(input.stream().toList(), size);

			// we expect 1 more list than the total would evenly
			// divide into the size
			assertEquals(Math.ceil((float)totalCount /((float) size)), output.size());
			int retTotal = 0;
			for (List<String> list : output) {
				for (String v : list) {
					retTotal++;
					assertTrue(input.contains(v));
				}
			}
			assertEquals(totalCount, retTotal);
		}
	}
}
