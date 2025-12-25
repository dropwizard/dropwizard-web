package io.dropwizard.web.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Set;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.http.pathmap.PathSpec;
import org.eclipse.jetty.server.handler.CrossOriginHandler;
import org.eclipse.jetty.server.handler.PathMappingsHandler;

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

        final CrossOriginHandler corsHandler = new CrossOriginHandler();

        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            corsHandler.setAllowedOriginPatterns(Set.copyOf(allowedOrigins));
        }

        if (allowedTimingOrigins != null && !allowedTimingOrigins.isEmpty()) {
            corsHandler.setAllowedTimingOriginPatterns(Set.copyOf(allowedTimingOrigins));
        }

        if (allowedMethods != null && !allowedMethods.isEmpty()) {
            corsHandler.setAllowedMethods(Set.copyOf(allowedMethods));
        }

        if (allowedHeaders != null && !allowedHeaders.isEmpty()) {
            corsHandler.setAllowedHeaders(Set.copyOf(allowedHeaders));
        }

        if (preflightMaxAge != null) {
            corsHandler.setPreflightMaxAge(preflightMaxAge.toJavaDuration());
        }

        if (allowCredentials != null) {
            corsHandler.setAllowCredentials(allowCredentials);
        }

        if (exposedHeaders != null && !exposedHeaders.isEmpty()) {
            setExposedHeaders(exposedHeaders);
        }

        if (chainPreflight != null) {
            setChainPreflight(chainPreflight);
        }

        final PathMappingsHandler pathHandler = new PathMappingsHandler();
        pathHandler.addMapping(PathSpec.from(urlPattern), corsHandler);

        final ServletContextHandler contextHandler = environment.getApplicationContext();
        contextHandler.insertHandler(corsHandler);

    }
}
