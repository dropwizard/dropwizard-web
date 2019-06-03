package io.dropwizard.web.test;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.web.WebBundle;
import io.dropwizard.web.conf.WebConfiguration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class TestApp extends Application<TestConfig> {

    @Override
    public void initialize(Bootstrap<TestConfig> bootstrap) {
        bootstrap.addBundle(new WebBundle<TestConfig>() {
            @Override
            public WebConfiguration getWebConfiguration(TestConfig config) {
                return config.getWebConfiguration();
            }
        });
    }

    @Override
    public void run(TestConfig configuration, Environment environment) throws Exception {
        environment.jersey().register(TestResource.class);
    }

    @Path("/test")
    public static class TestResource {
        @GET
        public String test() {
            return "test response";
        }
    }
}
