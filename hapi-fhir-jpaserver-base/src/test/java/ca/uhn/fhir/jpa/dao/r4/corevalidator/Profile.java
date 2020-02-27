package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("supporting")
    @Expose
    private List<String> supporting = null;
    @SerializedName("java")
    @Expose
    private TestResult testResult;

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
