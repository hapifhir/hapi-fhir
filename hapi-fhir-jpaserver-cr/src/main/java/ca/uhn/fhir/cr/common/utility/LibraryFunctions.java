package ca.uhn.fhir.cr.common.utility;

import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;
import java.util.function.Function;

final class LibraryFunctions {

	private final Function<IBase, List<IBase>> getAttachments;
	private final Function<IBase, String> getContentType;
	private final Function<IBase, byte[]> getContent;
	private final Function<IBase, String> getVersion;

	LibraryFunctions(Function<IBase, List<IBase>> getAttachments, Function<IBase, String> getContentType, Function<IBase, byte[]> getContent, Function<IBase, String> getVersion) {
		this.getAttachments = getAttachments;
		this.getContentType = getContentType;
		this.getContent = getContent;
		this.getVersion = getVersion;
	}
	
	public Function<IBase, List<IBase>> getAttachments() {
		return this.getAttachments;
	}

	public Function<IBase, String> getContentType() {
		return this.getContentType;
	}

	public Function<IBase, byte[]> getContent() {
		return this.getContent;
	}

	public Function<IBase, String> getVersion() {
		return this.getVersion;
	}
}
