package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestEntry {

    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("explanation")
    @Expose
    private String explaination;
    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("java")
    @Expose
    private TestResult testResult;
    @SerializedName("valuesets")
    @Expose
    private List<String> valuesets = null;
    @SerializedName("codesystems")
    @Expose
    private List<String> codesystems = null;

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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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

}