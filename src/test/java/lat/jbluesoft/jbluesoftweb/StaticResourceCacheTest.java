package lat.jbluesoft.jbluesoftweb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica las cabeceras reales de Tomcat: los recursos versionados se cachean un año
 * y el HTML no, para que siempre apunte a las URLs con el hash vigente.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StaticResourceCacheTest {

    private static final Pattern STYLES_VERSIONADO = Pattern.compile("/css/styles-[0-9a-f]+\\.css");
    private static final String UN_ANIO = "max-age=31536000";

    private final HttpClient http = HttpClient.newHttpClient();

    @LocalServerPort
    private int port;

    private HttpResponse<String> get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).build();
        return http.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void cssVersionadoSeCacheaUnAnio() throws Exception {
        HttpResponse<String> home = get("/");
        Matcher matcher = STYLES_VERSIONADO.matcher(home.body());
        assertTrue(matcher.find(), "el HTML debe referenciar styles.css con hash");

        HttpResponse<String> css = get(matcher.group());

        assertEquals(200, css.statusCode());
        String cacheControl = css.headers().firstValue("Cache-Control").orElse("");
        assertTrue(cacheControl.contains(UN_ANIO), "Cache-Control del CSS: " + cacheControl);
    }

    @Test
    void htmlNoSeCacheaUnAnio() throws Exception {
        HttpResponse<String> home = get("/");

        assertEquals(200, home.statusCode());
        String cacheControl = home.headers().firstValue("Cache-Control").orElse("");
        assertFalse(cacheControl.contains(UN_ANIO), "Cache-Control del HTML: " + cacheControl);
    }

}
