/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2025 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.fhir.repository.impl.kalm;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.IRepositoryLoader;
import ca.uhn.fhir.repository.impl.BaseSchemeBasedFhirRepositoryLoader;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Optional;

/**
 * ServiceLoader for the fhir-repository: url scheme.
 * Placeholder until this can move to cqf-fhir-utility or hapi-fhir-repositories.
 * @see ca.uhn.fhir.repository.Repositories#repositoryForUrl
 * TODO Either push this down to cqf-fhir-utility or upstream the IgRepository here to hapi-fhir-repositories.
 */
public class KalmFilesystemRepositoryLoader extends BaseSchemeBasedFhirRepositoryLoader implements IRepositoryLoader {
	public static final String URL_SUB_SCHEME = "exp-kalm-filesystem";

	public KalmFilesystemRepositoryLoader() {
		super(URL_SUB_SCHEME);
	}

	@Nonnull
	@Override
	public IRepository loadRepository(@Nonnull IRepositoryRequest theRepositoryRequest) {
		// Validate
		Optional<FhirContext> maybeContext = theRepositoryRequest.getFhirContext();
		Validate.isTrue(maybeContext.isPresent(), "The :%s: FHIR repository requires a FhirContext.", URL_SUB_SCHEME);

		// we expect the details to be a file path for now.
		Path configPath = getConfigPath(theRepositoryRequest.getDetails());

		return makeKalmRepository(maybeContext.get(), configPath);
	}

	static Path getConfigPath(String theDetails) {
		Path configPath = Path.of(theDetails);
		File directory = configPath.toFile();

		if (!(directory.exists() && directory.isDirectory())) {
			throw new IllegalArgumentException(
					Msg.code(2754) + "The provided path does not exist or is not a directory: " + directory);
		}
		return configPath;
	}

	static IRepository makeKalmRepository(FhirContext theFhirContext, Path thePath) {
		try {
			// we use reflection in case cqf-utilities is not on the classpath.  We don't want to force it as a
			// dependency.
			Class<?> repositoryClass = KalmFilesystemRepositoryLoader.class
					.getClassLoader()
					.loadClass("ca.uhn.fhir.repository.impl.kalm.PatchedKalmFileSystemRepository");
			Constructor<?> constructor = repositoryClass.getConstructor(FhirContext.class, Path.class);
			return (IRepository) constructor.newInstance(theFhirContext, thePath);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(Msg.code(2752) + "Can't find IgRepository from cqf-utilities.jar", e);
		} catch (NoSuchMethodException
				| InvocationTargetException
				| InstantiationException
				| IllegalAccessException e) {
			throw new IllegalStateException(Msg.code(2753) + "Error building IgRepository.", e);
		}
	}
}
