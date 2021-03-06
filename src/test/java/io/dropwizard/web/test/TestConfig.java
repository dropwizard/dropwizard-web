package io.dropwizard.web.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.web.conf.WebConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TestConfig extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("web1")
    private WebConfiguration web1Configuration = new WebConfiguration();

    @Valid
    @NotNull
    @JsonProperty("web2")
    private WebConfiguration web2Configuration = new WebConfiguration();

    public WebConfiguration getWeb1Configuration() {
        return web1Configuration;
    }

    public WebConfiguration getWeb2Configuration() {
        return web2Configuration;
    }

    public void setWeb1Configuration(WebConfiguration webConfiguration) {
        this.web1Configuration = webConfiguration;
    }

    public void setWeb2Configuration(WebConfiguration webConfiguration) {
        this.web2Configuration = webConfiguration;
    }
}
