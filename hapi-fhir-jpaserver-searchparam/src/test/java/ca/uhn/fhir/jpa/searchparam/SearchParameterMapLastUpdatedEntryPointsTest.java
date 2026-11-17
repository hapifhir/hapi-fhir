package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * <code>SearchParameterMap#setLastUpdatedAndList</code> and <code>JpaParamUtil#toLastUpdatedRangeAlternatives</code>
 * (used by the <code>_has</code> path) must apply the same rule to blank, missing, <code>ap</code> and normal values.
 */
class SearchParameterMapLastUpdatedEntryPointsTest {

	private static final List<String> ITEMS = List.of("MISSING", "BLANK", "ap2020", "ge2020", "lt2019-03", "ne2021", "2020-06-15T10:00:00Z", "eq2018");

	private static DateParam item(String theItem) {
		return switch (theItem) {
			case "MISSING" -> {
				DateParam d = new DateParam();
				d.setMissing(true);
				yield d;
			}
			case "BLANK" -> new DateParam();
			default -> LastUpdatedTestData.token(theItem);
		};
	}

	record Expr(List<List<String>> groups) {
		@Override
		public String toString() {
			return groups.toString();
		}

		DateAndListParam toAndList() {
			DateAndListParam retVal = new DateAndListParam();
			for (List<String> g : groups) {
				DateOrListParam or = new DateOrListParam();
				g.forEach(i -> or.addOr(item(i)));
				retVal.addAnd(or);
			}
			return retVal;
		}
	}

	static Stream<Arguments> exprs() {
		List<Expr> all = new ArrayList<>();
		// every ordered pair and triple inside one OR group, alone and next to a normal group
		for (String a : ITEMS) {
			for (String b : ITEMS) {
				all.add(new Expr(List.of(List.of(a, b))));
				all.add(new Expr(List.of(List.of(a, b), List.of("ge2019"))));
				for (String c : List.of("MISSING", "ap2020", "BLANK", "ge2020")) {
					all.add(new Expr(List.of(List.of(a, b, c))));
				}
			}
		}
		Random rnd = new Random(8857);
		for (int i = 0; i < 400; i++) {
			List<List<String>> groups = new ArrayList<>();
			for (int g = 0, n = 1 + rnd.nextInt(3); g < n; g++) {
				List<String> or = new ArrayList<>();
				for (int o = 0, m = 1 + rnd.nextInt(4); o < m; o++) {
					or.add(ITEMS.get(rnd.nextInt(ITEMS.size())));
				}
				groups.add(or);
			}
			all.add(new Expr(groups));
		}
		return all.stream().map(Arguments::of);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("exprs")
	void bothEntryPointsAgree(Expr theExpr) {
		boolean hasAp = theExpr.groups().stream().anyMatch(g -> g.contains("ap2020"));
		DateAndListParam list = theExpr.toAndList();

		if (hasAp) {
			assertThatThrownBy(() -> new SearchParameterMap().setLastUpdatedAndList(list))
					.isInstanceOf(InvalidRequestException.class)
					.hasMessageContaining("HAPI-1925");
			assertThatThrownBy(() -> JpaParamUtil.toLastUpdatedRangeAlternatives(list))
					.isInstanceOf(InvalidRequestException.class)
					.hasMessageContaining("HAPI-1925");
			return;
		}

		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdatedAndList(list);
		List<List<DateRangeParam>> alternatives = JpaParamUtil.toLastUpdatedRangeAlternatives(list);

		DateAndListParam stored = map.getLastUpdatedAndList();
		List<List<String>> storedTokens = new ArrayList<>();
		List<List<DateRangeParam>> storedRanges = new ArrayList<>();
		if (stored != null) {
			for (DateOrListParam or : stored.getValuesAsQueryTokens()) {
				List<String> tokens = new ArrayList<>();
				List<DateRangeParam> ranges = new ArrayList<>();
				for (DateParam d : or.getValuesAsQueryTokens()) {
					tokens.add(d.getValueAsQueryToken());
					ranges.add(JpaParamUtil.toLastUpdatedRange(d));
				}
				storedTokens.add(tokens);
				storedRanges.add(ranges);
			}
		}
		assertThat(map.hasLastUpdated()).isEqualTo(!alternatives.isEmpty());
		assertThat(alternatives).hasSameSizeAs(storedRanges);
		for (int g = 0; g < alternatives.size(); g++) {
			assertThat(alternatives.get(g)).hasSameSizeAs(storedRanges.get(g));
			for (int a = 0; a < alternatives.get(g).size(); a++) {
				DateRangeParam x = alternatives.get(g).get(a);
				DateRangeParam y = storedRanges.get(g).get(a);
				assertThat(x.getLowerBoundAsInstant()).as(storedTokens.toString()).isEqualTo(y.getLowerBoundAsInstant());
				assertThat(x.getUpperBoundAsInstant()).isEqualTo(y.getUpperBoundAsInstant());
				assertThat(x.toString()).isEqualTo(y.toString());
			}
		}

		// expected groups from the rule itself: blanks skipped, a MISSING drops the group
		List<List<String>> expected = new ArrayList<>();
		for (List<String> g : theExpr.groups()) {
			if (g.contains("MISSING")) {
				continue;
			}
			List<String> kept = g.stream().filter(i -> !i.equals("BLANK")).toList();
			if (!kept.isEmpty()) {
				expected.add(kept);
			}
		}
		assertThat(storedTokens).hasSameSizeAs(expected);
		for (int g = 0; g < expected.size(); g++) {
			assertThat(storedTokens.get(g)).hasSameSizeAs(expected.get(g));
		}
	}

	@Test
	void missingAndApInEitherOrderRejectedByBoth() {
		for (List<String> group : List.of(List.of("MISSING", "ap2020"), List.of("ap2020", "MISSING"))) {
			DateAndListParam list = new Expr(List.of(group)).toAndList();
			assertThatThrownBy(() -> new SearchParameterMap().setLastUpdatedAndList(list))
					.hasMessageContaining("HAPI-1925");
			assertThatThrownBy(() -> JpaParamUtil.toLastUpdatedRangeAlternatives(list))
					.hasMessageContaining("HAPI-1925");
		}
	}
}
