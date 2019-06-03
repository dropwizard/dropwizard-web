package io.dropwizard.web.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

public class CorsFilterFactory {
    @JsonProperty
    private List<String> allowedOrigins;
    @JsonProperty
    private List<String> allowedTimingOrigins;
    @JsonProperty
    private List<String> allowedMethods;
    @JsonProperty
    private List<String> allowedHeaders;
    @JsonProperty
    private Duration preflightMaxAge;
    @JsonProperty
    private Boolean allowCredentials;
    @JsonProperty
    private List<String> exposedHeaders;
    @JsonProperty
    private Boolean chainPreflight;

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedTimingOrigins() {
        return allowedTimingOrigins;
    }

    public void setAllowedTimingOrigins(List<String> allowedTimingOrigins) {
        this.allowedTimingOrigins = allowedTimingOrigins;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public Duration getPreflightMaxAge() {
        return preflightMaxAge;
    }

    public void setPreflightMaxAge(Duration preflightMaxAge) {
        this.preflightMaxAge = preflightMaxAge;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public List<String> getExposedHeaders() {
        return exposedHeaders;
    }

    public void setExposedHeaders(List<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public boolean isChainPreflight() {
        return chainPreflight;
    }

    public void setChainPreflight(boolean chainPreflight) {
        this.chainPreflight = chainPreflight;
    }

    public void build(Environment environment, String urlPattern) {
        // build map of init parameters
        final Map<String, String> builder = new HashMap<>();

        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            builder.put(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, String.join(",", allowedOrigins));
        }

        if (allowedTimingOrigins != null && !allowedTimingOrigins.isEmpty()) {
            builder.put(CrossOriginFilter.ALLOWED_TIMING_ORIGINS_PARAM, String.join(",", allowedTimingOrigins));
        }

        if (allowedMethods != null && !allowedMethods.isEmpty()) {
            builder.put(CrossOriginFilter.ALLOWED_METHODS_PARAM, String.join(",", allowedMethods));
        }

        if (allowedHeaders != null && !allowedHeaders.isEmpty()) {
            builder.put(CrossOriginFilter.ALLOWED_HEADERS_PARAM, String.join(",", allowedHeaders));
        }

        if (preflightMaxAge != null) {
            builder.put(CrossOriginFilter.PREFLIGHT_MAX_AGE_PARAM, String.valueOf(preflightMaxAge.toSeconds()));
        }

        if (allowCredentials != null) {
            builder.put(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, String.valueOf(allowCredentials));
        }

        if (exposedHeaders != null && !exposedHeaders.isEmpty()) {
            builder.put(CrossOriginFilter.EXPOSED_HEADERS_PARAM, String.join(",", exposedHeaders));
        }

        if (chainPreflight != null) {
            builder.put(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, String.valueOf(chainPreflight));
        }

        // configure filter
        final ServletEnvironment servlets = environment.servlets();
        final FilterRegistration.Dynamic cors = servlets.addFilter("cross-origin-filter", CrossOriginFilter.class);
        cors.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, urlPattern);
        cors.setInitParameters(Collections.unmodifiableMap(builder));
    }
}
