package io.dropwizard.web;

import io.dropwizard.core.Configuration;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.web.conf.WebConfiguration;
import org.eclipse.jetty.servlets.HeaderFilter;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;

public abstract class WebBundle<T extends Configuration> implements ConfiguredBundle<T> {
    private static final String WILDCARD = "*";

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        // do nothing
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        final WebConfiguration webConfig = getWebConfiguration(configuration);
        final String urlPattern = deriveUrlPattern(webConfig.getUriPath());

        // collect headers
        final Map<String, String> headers = new HashMap<>();

        // hsts
        if (webConfig.getHstsHeaderFactory() != null) {
            headers.putAll(webConfig.getHstsHeaderFactory().build());
        }

        // frame-options
        headers.putAll(webConfig.getFrameOptionsHeaderFactory().build());

        // content-type-options
        headers.putAll(webConfig.getContentTypeOptionsHeaderFactory().build());

        // xss-protection
        headers.putAll(webConfig.getXssProtectionHeaderFactory().build());

        // csp
        if (webConfig.getCspHeaderFactory() != null) {
            headers.putAll(webConfig.getCspHeaderFactory().build());
        }

        // other headers
        headers.putAll(webConfig.getHeaders());

        // configure filter
        configureHeaderFilter(environment, webConfig.getUriPath(), urlPattern, headers);

        // cors
        if (webConfig.getCorsFilterFactory() != null) {
            webConfig.getCorsFilterFactory().build(environment, urlPattern);
        }
    }

    protected void configureHeaderFilter(Environment environment,
                                         String uriPath,
                                         String urlPattern,
                                         Map<String, String> headers) {
        final String headerConfig = headers.entrySet().stream()
                .map(entry -> "set " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(","));
        final Map<String, String> filterConfig = Collections.singletonMap("headerConfig", headerConfig);
        final FilterRegistration.Dynamic filter = getServletEnvironment(environment)
                .addFilter("header-filter-" + uriPath, HeaderFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, urlPattern);
        filter.setInitParameters(filterConfig);
    }

    private String deriveUrlPattern(String uri) {
        if (uri.endsWith("/")) {
            return uri + WILDCARD;
        } else {
            return uri + "/" + WILDCARD;
        }
    }

    /**
     * Define which {@link ServletEnvironment} should be configured. Default is {@link Environment#servlets()}.
     *
     * @param environment The environment of the Dropwizard application
     * @return The {@link ServletEnvironment} to configure
     */
    protected ServletEnvironment getServletEnvironment(Environment environment) {
        return environment.servlets();
    }

    public abstract WebConfiguration getWebConfiguration(T config);
}
