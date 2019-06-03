package io.dropwizard.web.conf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;

import java.util.HashMap;
import java.util.Map;

public class CspHeaderFactory extends HeaderFactory {
    private static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
    private static final String CONTENT_SECURITY_POLICY_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    @JsonProperty
    private String policy;

    @JsonProperty
    private String reportOnlyPolicy;

    @ValidationMethod(message = "either 'policy' or 'reportOnlyPolicy' must be defined")
    @JsonIgnore
    private boolean isPolicyDefined() {
        return (policy != null && !policy.isEmpty()) || (reportOnlyPolicy != null && !reportOnlyPolicy.isEmpty());
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getReportOnlyPolicy() {
        return reportOnlyPolicy;
    }

    public void setReportOnlyPolicy(String reportOnlyPolicy) {
        this.reportOnlyPolicy = reportOnlyPolicy;
    }

    @Override
    protected Map<String, String> buildHeaders() {
        final Map<String, String> headers = new HashMap<>();
        if (policy != null) {
            headers.put(CONTENT_SECURITY_POLICY, policy);
        }

        if (reportOnlyPolicy != null) {
            headers.put(CONTENT_SECURITY_POLICY_REPORT_ONLY, reportOnlyPolicy);
        }

        return headers;
    }
}
