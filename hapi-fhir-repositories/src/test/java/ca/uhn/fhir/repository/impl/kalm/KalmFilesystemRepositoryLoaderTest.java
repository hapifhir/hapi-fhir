package ca.uhn.fhir.repository.impl.kalm;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.repository.impl.UrlRepositoryFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MockitoSettings
class KalmFilesystemRepositoryLoaderTest {
	@Mock(stubOnly = true, answer = Answers.RETURNS_DEEP_STUBS)
	FhirContext myFhirContext;
	@TempDir
	Path myTempDir;


	@Test
	void testRepositoryCreation() {
	    // given
		KalmFilesystemRepositoryLoader loader = new KalmFilesystemRepositoryLoader();
		Mockito.when(myFhirContext.getVersion().getVersion()).thenReturn(FhirVersionEnum.R4);

	    // when
		var repo = loader.loadRepository(UrlRepositoryFactory.buildRequest("fhir-repository:exp-kalm-filesystem:" + myTempDir, myFhirContext));

	    // then
	    assertThat(repo).isInstanceOf(PatchedKalmFileSystemRepository.class);
	}

	@Test
	void testPathExists() {
	    // given

	    // when
		Path configPath = KalmFilesystemRepositoryLoader.getConfigPath(myTempDir.toAbsolutePath().toString());

	    // then
	    assertThat(configPath).isNotNull();
	}

	@Test
	void testPathNotExists_error() {
		// given
		String nonexistent = myTempDir.resolve("nonexistent").toAbsolutePath().toString();

		// when
		assertThatThrownBy(()->KalmFilesystemRepositoryLoader.getConfigPath(nonexistent))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("HAPI-2754: The provided path does not exist or is not a directory: " + nonexistent);
	}

	@Test
	void testPathNotDirectory_error() throws IOException {
		// given
		Path filename = myTempDir.resolve("file").toAbsolutePath();
		filename.toFile().createNewFile(); // create a file instead of a directory
		String filenameString = filename.toString();

		// when
		assertThatThrownBy(()->KalmFilesystemRepositoryLoader.getConfigPath(filenameString))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("HAPI-2754: The provided path does not exist or is not a directory: " + filename);
	}


}
