package lat.jbluesoft.jbluesoftweb.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SistemaTest {

    private static Sistema crear(String slug, String imagen, String youtubeListaId, String repoUrl) {
        return new Sistema(slug, "Título", "Descripción corta", "Descripción larga",
                imagen, youtubeListaId, repoUrl, null);
    }

    @Test
    void aceptaSistemaValido() {
        assertDoesNotThrow(() -> crear("erp-admin", "/assets/img/erp.webp",
                "PLabc_123-XYZ", "https://github.com/jbluesoftweb/erp-admin"));
    }

    @Test
    void aceptaCamposOpcionalesNulos() {
        assertDoesNotThrow(() -> crear("drivex", "/assets/img/drivex.webp", null, null));
    }

    @Test
    void rechazaSlugInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> crear("ERP Admin", "/assets/img/erp.webp", null, null));
        assertThrows(IllegalArgumentException.class,
                () -> crear("erp--admin", "/assets/img/erp.webp", null, null));
        assertThrows(IllegalArgumentException.class,
                () -> crear("../etc", "/assets/img/erp.webp", null, null));
    }

    @Test
    void rechazaRepoUrlHttp() {
        assertThrows(IllegalArgumentException.class,
                () -> crear("erp-admin", "/assets/img/erp.webp", null, "http://github.com/jbluesoftweb/erp-admin"));
        assertThrows(IllegalArgumentException.class,
                () -> crear("erp-admin", "/assets/img/erp.webp", null, "javascript:alert(1)"));
    }

    @Test
    void rechazaImagenConPathTraversal() {
        assertThrows(IllegalArgumentException.class,
                () -> crear("erp-admin", "/assets/img/../../application.properties", null, null));
        assertThrows(IllegalArgumentException.class,
                () -> crear("erp-admin", "https://evil.example/x.webp", null, null));
    }

    @Test
    void rechazaYoutubeListaIdInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> crear("erp-admin", "/assets/img/erp.webp", "PL123&autoplay=1", null));
        assertThrows(IllegalArgumentException.class,
                () -> crear("erp-admin", "/assets/img/erp.webp", "\"><script>", null));
    }

    @Test
    void rechazaCamposObligatoriosVacios() {
        assertThrows(IllegalArgumentException.class,
                () -> new Sistema("erp-admin", " ", "Corta", "Larga", "/assets/img/erp.webp", null, null, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Sistema(null, "Título", "Corta", "Larga", "/assets/img/erp.webp", null, null, null));
    }

}
