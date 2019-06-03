package io.dropwizard.web.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.web.conf.WebConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TestConfig extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("web")
    private WebConfiguration webConfiguration = new WebConfiguration();

    public WebConfiguration getWebConfiguration() {
        return webConfiguration;
    }

    public void setWebConfiguration(WebConfiguration webConfiguration) {
        this.webConfiguration = webConfiguration;
    }
}
