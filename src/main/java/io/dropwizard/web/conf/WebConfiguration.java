package io.dropwizard.web.conf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.NotNull;

public class WebConfiguration {
    @NotNull
    @JsonProperty
    private String uriPath = "/";

    @JsonProperty("hsts")
    private HstsHeaderFactory hstsHeaderFactory = new HstsHeaderFactory();

    @JsonProperty("frame-options")
    private FrameOptionsHeaderFactory frameOptionsHeaderFactory = new FrameOptionsHeaderFactory();

    @JsonProperty("content-type-options")
    private ContentTypeOptionsHeaderFactory contentTypeOptionsHeaderFactory = new ContentTypeOptionsHeaderFactory();

    @JsonProperty("xss-protection")
    private XssProtectionHeaderFactory xssProtectionHeaderFactory = new XssProtectionHeaderFactory();

    @JsonProperty("csp")
    private CspHeaderFactory cspHeaderFactory;

    @JsonProperty("cors")
    private CorsFilterFactory corsFilterFactory;

    @JsonProperty
    private Map<String, String> headers = new HashMap<>();

    public String getUriPath() {
        return uriPath;
    }

    public void setUriPath(String uriPath) {
        this.uriPath = uriPath;
    }

    public HstsHeaderFactory getHstsHeaderFactory() {
        return hstsHeaderFactory;
    }

    public void setHstsHeaderFactory(HstsHeaderFactory hstsHeaderFactory) {
        this.hstsHeaderFactory = hstsHeaderFactory;
    }

    public FrameOptionsHeaderFactory getFrameOptionsHeaderFactory() {
        return frameOptionsHeaderFactory;
    }

    public void setFrameOptionsHeaderFactory(FrameOptionsHeaderFactory frameOptionsHeaderFactory) {
        this.frameOptionsHeaderFactory = frameOptionsHeaderFactory;
    }

    public ContentTypeOptionsHeaderFactory getContentTypeOptionsHeaderFactory() {
        return contentTypeOptionsHeaderFactory;
    }

    public void setContentTypeOptionsHeaderFactory(ContentTypeOptionsHeaderFactory contentTypeOptionsHeaderFactory) {
        this.contentTypeOptionsHeaderFactory = contentTypeOptionsHeaderFactory;
    }

    public XssProtectionHeaderFactory getXssProtectionHeaderFactory() {
        return xssProtectionHeaderFactory;
    }

    public void setXssProtectionHeaderFactory(XssProtectionHeaderFactory xssProtectionHeaderFactory) {
        this.xssProtectionHeaderFactory = xssProtectionHeaderFactory;
    }

    public CspHeaderFactory getCspHeaderFactory() {
        return cspHeaderFactory;
    }

    public void setCspHeaderFactory(CspHeaderFactory cspHeaderFactory) {
        this.cspHeaderFactory = cspHeaderFactory;
    }

    public CorsFilterFactory getCorsFilterFactory() {
        return corsFilterFactory;
    }

    public void setCorsFilterFactory(CorsFilterFactory corsFilterFactory) {
        this.corsFilterFactory = corsFilterFactory;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
