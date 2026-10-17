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
 * Levanta Tomcat real: MockMvc no ejecuta el error dispatch del contenedor,
 * así que no renderiza templates/error/404.html.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ErrorPageTest {

    private static final Pattern STYLES_VERSIONADO = Pattern.compile("/css/styles-[0-9a-f]+\\.css");

    private final HttpClient http = HttpClient.newHttpClient();

    @LocalServerPort
    private int port;

    private HttpResponse<String> get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .header("Accept", "text/html")
                .build();
        return http.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void rutaInexistenteDevuelve404ConPaginaPropia() throws Exception {
        HttpResponse<String> response = get("/ruta-que-no-existe");

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("<title>Página no encontrada | JBlueSoft</title>"));
        assertTrue(response.body().contains("Página no encontrada"));
        assertFalse(response.body().contains("ruta-que-no-existe"), "no debe reflejar la ruta pedida");
        assertFalse(response.body().contains("Exception"), "no debe mostrar detalles técnicos");
    }

    @Test
    void cssVersionadoSeSirveYHashFalsoNo() throws Exception {
        Matcher matcher = STYLES_VERSIONADO.matcher(get("/").body());
        assertTrue(matcher.find(), "el HTML debe referenciar styles.css con hash");

        assertEquals(200, get(matcher.group()).statusCode());
        assertEquals(404, get("/css/styles-00000000000000000000000000000000.css").statusCode());
    }

}
