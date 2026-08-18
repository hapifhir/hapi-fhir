package ca.uhn.fhir.jpa.packages.loader;

import org.hl7.fhir.utilities.http.ManagedWebAccess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Package loader settings are JVM-global, so these tests assert on static state and reset it after each
 * case. The scenario they exist for is several consumers applying settings in quick succession: each must
 * see the state it expects on entry and leave the previous one behind on exit.
 */
// Created by Claude Opus 5
class PackageLoaderSettingsScopeIT {

	/**
	 * Reset on the way in as well as out: surefire reuses forks, so this class cannot assume it inherits a
	 * clean global state from whatever ran before it.
	 */
	@BeforeEach
	void clearInheritedGlobalState() {
		PackageLoaderSvc.resetSettings();
	}

	@AfterEach
	void resetGlobalState() {
		PackageLoaderSvc.resetSettings();
	}

	private static PackageLoaderSettings settingsFor(String theRemotePrefix) {
		AllowedUrlPrefix pref = new AllowedUrlPrefix(theRemotePrefix, false, false);
		return new PackageLoaderSettings(PackageUrlAllowList.of(List.of(pref), List.of()));
	}

	@Test
	void close_whenNothingWasAppliedBefore_resetsToDefault() {
		PackageLoaderSettings settings = settingsFor("https://packages.fhir.org");

		try (PackageLoaderSettingsScope scope = PackageLoaderSvc.applySettings(settings)) {
			assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(settings);
		}

		assertThat(PackageLoaderSvc.getAppliedSettings()).isNull();
	}

	@Test
	void close_whenSettingsWereAlreadyApplied_restoresThem() {
		PackageLoaderSettings original = settingsFor("https://original.example.org");
		PackageLoaderSvc.initSettings(original);

		PackageLoaderSettings scoped = settingsFor("https://scoped.example.org");
		try (PackageLoaderSettingsScope scope = PackageLoaderSvc.applySettings(scoped)) {
			assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(scoped);
		}

		assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(original);
	}

	@Test
	void close_whenScopesAreNested_restoresInReverseOrder() {
		PackageLoaderSettings outer = settingsFor("https://outer.example.org");
		PackageLoaderSettings inner = settingsFor("https://inner.example.org");

		try (PackageLoaderSettingsScope outerScope = PackageLoaderSvc.applySettings(outer)) {
			try (PackageLoaderSettingsScope innerScope = PackageLoaderSvc.applySettings(inner)) {
				assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(inner);
			}
			assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(outer);
		}

		assertThat(PackageLoaderSvc.getAppliedSettings()).isNull();
	}

	@Test
	void close_whenScopesRunInSuccession_eachLeavesTheBaselineBehind() {
		PackageLoaderSettings baseline = settingsFor("https://baseline.example.org");
		PackageLoaderSvc.initSettings(baseline);

		for (int i = 0; i < 3; i++) {
			try (PackageLoaderSettingsScope scope = PackageLoaderSvc.applySettings(settingsFor("https://run-" + i))) {
				assertThat(PackageLoaderSvc.getAppliedSettings()).isNotSameAs(baseline);
			}
			assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(baseline);
		}
	}

	@Test
	void close_whenCalledTwice_doesNotRestoreASecondTime() {
		PackageLoaderSettings original = settingsFor("https://original.example.org");
		PackageLoaderSvc.initSettings(original);

		PackageLoaderSettingsScope scope = PackageLoaderSvc.applySettings(settingsFor("https://scoped.example.org"));
		scope.close();
		assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(original);

		// a second close must not clobber whatever the next consumer has since applied
		PackageLoaderSettings later = settingsFor("https://later.example.org");
		PackageLoaderSvc.initSettings(later);
		scope.close();

		assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(later);
	}

	@Test
	void close_whenSettingsChangedInsideTheScope_stillRestoresWhatTheScopeReplaced() {
		PackageLoaderSettings original = settingsFor("https://original.example.org");
		PackageLoaderSvc.initSettings(original);

		try (PackageLoaderSettingsScope scope = PackageLoaderSvc.applySettings(settingsFor("https://scoped.example.org"))) {
			PackageLoaderSvc.initSettings(settingsFor("https://interloper.example.org"));
		}

		assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(original);
	}

	// ---------------------------------------------------------------- SSRF flag

	@Test
	void close_whenNothingWasAppliedAndProtectionWasOff_restoresItOff() {
		// simulates a JVM where a permissive config already ran, e.g. a HAPI server with no allow list provider
		ManagedWebAccess.setSsrfProtectionEnabled(false);

		try (PackageLoaderSettingsScope scope = PackageLoaderSvc.applySettings(settingsFor("https://scoped.example.org"))) {
			assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isTrue();
		}

		assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isFalse();
	}

	@Test
	void close_whenNothingWasAppliedAndProtectionWasOn_leavesItOn() {
		ManagedWebAccess.setSsrfProtectionEnabled(true);

		try (PackageLoaderSettingsScope scope = PackageLoaderSvc.applySettings(settingsFor("https://scoped.example.org"))) {
			assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isTrue();
		}

		assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isTrue();
	}

	@Test
	void close_whenPreviousSettingsWereWildcard_recomputesProtectionAsOff() {
		// a wildcard allow list disables protection; restoring those settings must disable it again
		PackageLoaderSettings permissive = new PackageLoaderSettings(PackageUrlAllowList.allowAll());
		PackageLoaderSvc.initSettings(permissive);
		assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isFalse();

		try (PackageLoaderSettingsScope scope = PackageLoaderSvc.applySettings(settingsFor("https://scoped.example.org"))) {
			assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isTrue();
		}

		assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(permissive);
		assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isFalse();
	}

	@Test
	void close_whenScopeWasPermissive_restoresRestrictiveProtection() {
		PackageLoaderSettings restrictive = settingsFor("https://restrictive.example.org");
		PackageLoaderSvc.initSettings(restrictive);
		assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isTrue();

		try (PackageLoaderSettingsScope scope =
				PackageLoaderSvc.applySettings(new PackageLoaderSettings(PackageUrlAllowList.allowAll()))) {
			assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isFalse();
		}

		assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(restrictive);
		assertThat(ManagedWebAccess.isSsrfProtectionEnabled()).isTrue();
	}

	// ---------------------------------------------------------------- initSettings

	@Test
	void initSettings_whenCalledTwiceWithMatchingPrefixes_doesNotReapply() {
		PackageLoaderSettings first = settingsFor("https://packages.fhir.org");
		PackageLoaderSvc.initSettings(first);

		// same prefixes, different instance: the dedupe should short circuit and leave the original applied
		PackageLoaderSvc.initSettings(settingsFor("https://packages.fhir.org"));

		assertThat(PackageLoaderSvc.getAppliedSettings()).isSameAs(first);
	}
}
