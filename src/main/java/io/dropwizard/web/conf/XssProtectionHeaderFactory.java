package io.dropwizard.web.conf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

public class XssProtectionHeaderFactory extends HeaderFactory {
    private static final String X_XSS_PROTECTION = "X-XSS-Protection";

    @JsonProperty
    private boolean on = true;

    @JsonProperty
    private boolean block = true;

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    @Override
    protected Map<String, String> buildHeaders() {
        final StringBuilder valueBuilder = new StringBuilder();

        if (on) {
            valueBuilder.append("1");

            if (block) {
                valueBuilder.append("; mode=block");
            }
        } else {
            valueBuilder.append("0");
        }

        return Collections.singletonMap(X_XSS_PROTECTION, valueBuilder.toString());
    }
}
