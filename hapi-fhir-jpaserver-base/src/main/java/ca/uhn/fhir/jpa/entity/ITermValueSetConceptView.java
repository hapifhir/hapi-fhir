package ca.uhn.fhir.jpa.entity;

public interface ITermValueSetConceptView {
	String getConceptSystemUrl();

	String getConceptCode();

	String getConceptDisplay();

	Long getSourceConceptPid();

	String getSourceConceptDirectParentPids();

	Long getConceptPid();

	Long getDesignationPid();

	String getDesignationUseSystem();

	String getDesignationUseCode();

	String getDesignationUseDisplay();

	String getDesignationVal();

	String getDesignationLang();
}
