package io.dropwizard.web.conf;

import java.util.Collections;
import java.util.Map;

public class ContentTypeOptionsHeaderFactory extends HeaderFactory {
    public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";

    @Override
    public Map<String, String> buildHeaders() {
        return Collections.singletonMap(X_CONTENT_TYPE_OPTIONS, "nosniff");
    }
}
