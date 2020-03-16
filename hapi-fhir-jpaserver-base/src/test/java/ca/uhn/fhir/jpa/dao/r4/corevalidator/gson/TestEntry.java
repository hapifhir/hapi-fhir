package ca.uhn.fhir.jpa.dao.r4.corevalidator.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestEntry {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("explanation")
    @Expose
    private String explaination;
    @SerializedName("errorCount")
    @Expose
    private Integer errorCount;
    @SerializedName("profile")
    @Expose
    private Profile profile = new Profile();
    @SerializedName("profiles")
    @Expose
    private List<String> profiles = null;
    @SerializedName("java")
    @Expose
    private TestResult testResult;
    @SerializedName("valuesets")
    @Expose
    private List<String> valuesets = null;
    @SerializedName("codesystems")
    @Expose
    private List<String> codesystems = null;
    @SerializedName("use-test")
    @Expose
    private Boolean usesTest = true;
    @SerializedName("validate")
    @Expose
    private String validate = null;
    @SerializedName("examples")
    @Expose
    private Boolean allowExamples = false;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExplaination() {
        return explaination;
    }

    public TestEntry setExplaination(String explaination) {
        this.explaination = explaination;
        return this;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public TestEntry setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
        return this;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }

    public List<String> getValuesets() {
        return valuesets;
    }

    public void setValuesets(List<String> valuesets) {
        this.valuesets = valuesets;
    }

    public List<String> getCodesystems() {
        return codesystems;
    }

    public void setCodesystems(List<String> codesystems) {
        this.codesystems = codesystems;
    }

    public Boolean getUsesTest() {
        return usesTest;
    }

    public TestEntry setUsesTest(Boolean usesTest) {
        this.usesTest = usesTest;
        return this;
    }

    public String getValidate() {
        return validate;
    }

    public TestEntry setValidate(String validate) {
        this.validate = validate;
        return this;
    }

    public Boolean getAllowExamples() {
        return allowExamples;
    }

    public TestEntry setAllowExamples(Boolean allowExamples) {
        this.allowExamples = allowExamples;
        return this;
    }
}