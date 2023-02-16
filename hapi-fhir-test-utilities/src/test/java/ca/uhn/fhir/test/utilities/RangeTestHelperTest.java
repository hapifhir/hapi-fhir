package ca.uhn.fhir.test.utilities;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RangeTestHelperTest {


	@Nested
	public class DefaultRange {

		@Test
		void checkInRange() {
				RangeTestHelper.checkInRange(.83d, .829999999d);
		}

		@Test
		void checkLower() {
			AssertionError thrown = assertThrows(
				AssertionError.class,
				() -> RangeTestHelper.checkInRange(.91, .83)
			);
		}

		@Test
		void checkHigher() {
			AssertionError thrown = assertThrows(
				AssertionError.class,
				() -> RangeTestHelper.checkInRange(.26, .25)
			);
		}

		@Nested
		public class WithinBounds {

			@Test
			void checkInRange() {
				RangeTestHelper.checkWithinBounds(.91001, .91002, .910013);
			}

			@Test
			void checkLower() {
				AssertionError thrown = assertThrows(
					AssertionError.class,
					() -> RangeTestHelper.checkWithinBounds(.91001, .91002, .9013)
				);
			}

			@Test
			void checkHigher() {
				AssertionError thrown = assertThrows(
					AssertionError.class,
					() -> RangeTestHelper.checkWithinBounds(.87, .88, .9)
				);
			}

			@Nested
			public class PassingStrings {
				@Test
				void checkInRange() {
					RangeTestHelper.checkWithinBounds(".91001", ".91002", ".910013");
				}

				@Test
				void checkLower() {
					AssertionError thrown = assertThrows(
						AssertionError.class,
						() -> RangeTestHelper.checkWithinBounds(".91001", ".91002", ".9013")
					);
				}

				@Test
				void checkHigher() {
					AssertionError thrown = assertThrows(
						AssertionError.class,
						() -> RangeTestHelper.checkWithinBounds(".87", ".88", ".9")
					);
				}
			}
		}

		@Nested
		public class PassingStrings {

			@Test
			void checkInRange() {
				RangeTestHelper.checkInRange("0.83", "0.829999999");
			}

			@Test
			void checkLower() {
				AssertionError thrown = assertThrows(
					AssertionError.class,
					() -> RangeTestHelper.checkInRange(".91", ".83")
				);
			}

			@Test
			void checkHigher() {
				AssertionError thrown = assertThrows(
					AssertionError.class,
					() -> RangeTestHelper.checkInRange(".26", "0.25")
				);
			}
		}

	}

	@Nested
	public class ProvidedRange {

		@Test
		void checkInRange() {
			// equals to higher bound
			RangeTestHelper.checkInRange(.83, .1, .83);
			RangeTestHelper.checkInRange(.831, .02, .833);
		}

		@Test
		void checkLower() {
			AssertionError thrown = assertThrows(
				AssertionError.class,
				() -> RangeTestHelper.checkInRange(.84, .01, .82)
			);
		}

		@Test
		void checkHigher() {
			AssertionError thrown = assertThrows(
				AssertionError.class,
				() -> RangeTestHelper.checkInRange(.2511,.0001, .2513)
			);
		}

		@Nested
		public class PassingStrings {

			@Test
			void checkInRange() {
				RangeTestHelper.checkInRange(".82", .01, ".83");
				RangeTestHelper.checkInRange(".83d", .829999999d, ".8312d");
			}

			@Test
			void checkLower() {
				AssertionError thrown = assertThrows(
					AssertionError.class,
					() -> RangeTestHelper.checkInRange(".91", .02, ".83")
				);
			}

			@Test
			void checkHigher() {
				AssertionError thrown = assertThrows(
					AssertionError.class,
					() -> RangeTestHelper.checkInRange(".26", .03, "0.3")
				);
			}
		}
	}
}
