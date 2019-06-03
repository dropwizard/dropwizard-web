package io.dropwizard.web;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.web.conf.WebConfiguration;
import org.eclipse.jetty.servlets.HeaderFilter;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

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
        configureHeaderFilter(environment, urlPattern, headers);

        // cors
        if (webConfig.getCorsFilterFactory() != null) {
            webConfig.getCorsFilterFactory().build(environment, urlPattern);
        }
    }

    protected void configureHeaderFilter(Environment environment, String urlPattern, Map<String, String> headers) {
        final String headerConfig = headers.entrySet().stream()
                .map(entry -> "set " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(","));
        final Map<String, String> filterConfig = Collections.singletonMap("headerConfig", headerConfig);
        final FilterRegistration.Dynamic filter = environment.servlets()
                .addFilter("header-filter", HeaderFilter.class);
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

    public abstract WebConfiguration getWebConfiguration(T config);
}
