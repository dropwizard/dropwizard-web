package io.dropwizard.web.conf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;

import java.util.Collections;
import java.util.Map;

public class FrameOptionsHeaderFactory extends HeaderFactory {
    private static final String X_FRAME_OPTIONS = "X-Frame-Options";

    public enum FrameOption {
        DENY("DENY"),
        SAMEORIGIN("SAMEORIGIN"),
        ALLOW_FROM("ALLOW-FROM");

        private final String value;

        FrameOption(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @JsonProperty
    private FrameOption option = FrameOption.DENY;

    @JsonProperty
    private String origin;

    @ValidationMethod(message = "'origin' is required if option is 'ALLOW_FROM'")
    @JsonIgnore
    private boolean isOriginValid() {
        return (option != FrameOption.ALLOW_FROM) || (origin != null && !origin.isEmpty());
    }

    public FrameOption getOption() {
        return option;
    }

    public void setOption(FrameOption option) {
        this.option = option;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public Map<String, String> buildHeaders() {
        final StringBuilder valueBuilder = new StringBuilder(option.getValue());

        if (FrameOption.ALLOW_FROM == option) {
            valueBuilder.append(" ").append(origin);
        }

        return Collections.singletonMap(X_FRAME_OPTIONS, valueBuilder.toString());
    }
}
