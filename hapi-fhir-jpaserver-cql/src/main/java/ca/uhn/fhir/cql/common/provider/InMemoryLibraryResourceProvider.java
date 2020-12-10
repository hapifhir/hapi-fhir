package ca.uhn.fhir.cql.common.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InMemoryLibraryResourceProvider<LibraryType> implements LibraryResolutionProvider<LibraryType> {

    private Map<String, LibraryType> libraries = new HashMap<>();
    private Function<LibraryType, String> getId;
    private Function<LibraryType, String> getName;
    private Function<LibraryType, String> getVersion;

    public InMemoryLibraryResourceProvider() {
    }

    public InMemoryLibraryResourceProvider(Collection<LibraryType> initialLibraries,
            Function<LibraryType, String> getId, Function<LibraryType, String> getName,
            Function<LibraryType, String> getVersion) {

        this.getId = getId;
        this.getName = getName;
        this.getVersion = getVersion;

        for (LibraryType library : initialLibraries) {
            this.update(library);
        }
    }

    @Override
    public LibraryType resolveLibraryById(String libraryId) {
        if (this.libraries.containsKey(libraryId)) {
            return this.libraries.get(libraryId);
        }

        throw new IllegalArgumentException(String.format("Could not resolve library id %s", libraryId));
    }

    @Override
    public LibraryType resolveLibraryByName(String libraryName, String libraryVersion) {
        List<LibraryType> libraries = this.libraries.values().stream()
                .filter(x -> this.getName.apply(x).equals(libraryName)).collect(Collectors.toList());
        LibraryType library = LibraryResolutionProvider.selectFromList(libraries, libraryVersion, this.getVersion);

        if (library == null) {
            throw new IllegalArgumentException(String.format("Could not resolve library name %s", libraryName));
        }

        return library;
    }

    @Override
    public void update(LibraryType library) {
        this.libraries.put(this.getId.apply(library), library);
    }

    @Override
    public LibraryType resolveLibraryByCanonicalUrl(String libraryUrl) {
        // TODO Auto-generated method stub
        return null;
    }

}
