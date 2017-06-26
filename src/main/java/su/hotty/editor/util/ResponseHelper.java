package su.hotty.editor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Response;

import java.io.IOException;

/**
 *
 */
public class ResponseHelper {

    private static final Logger log = LoggerFactory.getLogger(ResponseHelper.class);

    public static synchronized String errorBody(Response<?> response) throws IOException {
        try {
            return response.errorBody() != null ? response.errorBody().string() : "";
        } catch (IOException e) {
            log.error("Can't extract error body.", e);
            throw e;
        }
    }
}
