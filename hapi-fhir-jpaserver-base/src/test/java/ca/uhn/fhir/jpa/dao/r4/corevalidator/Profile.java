package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Profile {

    @SerializedName("errorCount")
    @Expose
    private String errorCount;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("supporting")
    @Expose
    private List<String> supporting = null;
    @SerializedName("java")
    @Expose
    private TestResult testResult;

    public String getErrorCount() {
        return errorCount;
    }

    public Profile setErrorCount(String errorCount) {
        this.errorCount = errorCount;
        return this;
    }

    public String getSource() {
        return source;
    }

    public Profile setSource(String source) {
        this.source = source;
        return this;
    }

    public List<String> getSupporting() {
        return supporting;
    }

    public Profile setSupporting(List<String> supporting) {
        this.supporting = supporting;
        return this;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public Profile setTestResult(TestResult testResult) {
        this.testResult = testResult;
        return this;
    }
}
