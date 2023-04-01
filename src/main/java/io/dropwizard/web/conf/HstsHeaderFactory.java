package io.dropwizard.web.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;

import java.util.Collections;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class HstsHeaderFactory extends HeaderFactory {
    private static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

    @Valid
    @NotNull
    @JsonProperty
    private Duration maxAge = Duration.days(365);

    @JsonProperty
    private boolean includeSubDomains = true;

    @JsonProperty
    private boolean preload = false;

    public Duration getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Duration maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isIncludeSubDomains() {
        return includeSubDomains;
    }

    public void setIncludeSubDomains(boolean includeSubDomains) {
        this.includeSubDomains = includeSubDomains;
    }

    public boolean isPreload() {
        return preload;
    }

    public void setPreload(boolean preload) {
        this.preload = preload;
    }

    @Override
    public Map<String, String> buildHeaders() {
        final StringBuilder valueBuilder = new StringBuilder("max-age=");
        valueBuilder.append(maxAge.toSeconds());

        if (includeSubDomains) {
            valueBuilder.append("; includeSubDomains");
        }

        if (preload) {
            valueBuilder.append("; preload");
        }

        return Collections.singletonMap(STRICT_TRANSPORT_SECURITY, valueBuilder.toString());
    }
}
