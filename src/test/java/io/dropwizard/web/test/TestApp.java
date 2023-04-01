package io.dropwizard.web.test;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.web.WebBundle;
import io.dropwizard.web.conf.WebConfiguration;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

public class TestApp extends Application<TestConfig> {

    @Override
    public void initialize(Bootstrap<TestConfig> bootstrap) {
        bootstrap.addBundle(new WebBundle<TestConfig>() {
            @Override
            public WebConfiguration getWebConfiguration(TestConfig config) {
                return config.getWeb1Configuration();
            }
        });
        bootstrap.addBundle(new WebBundle<TestConfig>() {
            @Override
            public WebConfiguration getWebConfiguration(TestConfig config) {
                return config.getWeb2Configuration();
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
        @Path("one")
        public String one() {
            return "test response 1";
        }

        @GET
        @Path("two")
        public String two() {
            return "test response 2";
        }
    }
}
