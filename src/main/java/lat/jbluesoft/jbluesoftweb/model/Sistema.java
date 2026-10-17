package lat.jbluesoft.jbluesoftweb.model;

import java.util.regex.Pattern;

public record Sistema(
        String slug,
        String titulo,
        String descripcionCorta,
        String descripcionLarga,
        String imagen,
        String youtubeListaId,
        String repoUrl,
        String sitioUrl
) {

    private static final Pattern SLUG = Pattern.compile("^[a-z0-9]+(-[a-z0-9]+)*$");
    private static final Pattern YOUTUBE_LISTA_ID = Pattern.compile("^[A-Za-z0-9_-]+$");
    private static final String IMAGEN_PREFIJO = "/assets/img/";
    private static final String HTTPS = "https://";

    public Sistema {
        requerido(slug, "slug");
        requerido(titulo, "titulo");
        requerido(descripcionCorta, "descripcionCorta");
        requerido(descripcionLarga, "descripcionLarga");
        requerido(imagen, "imagen");

        if (!SLUG.matcher(slug).matches()) {
            throw new IllegalArgumentException("slug inválido: " + slug);
        }
        if (youtubeListaId != null && !YOUTUBE_LISTA_ID.matcher(youtubeListaId).matches()) {
            throw new IllegalArgumentException("youtubeListaId inválido");
        }
        https(repoUrl, "repoUrl");
        https(sitioUrl, "sitioUrl");
        if (!imagen.startsWith(IMAGEN_PREFIJO) || imagen.contains("..")) {
            throw new IllegalArgumentException("imagen debe estar en " + IMAGEN_PREFIJO);
        }
    }

    private static void requerido(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
    }

    private static void https(String url, String campo) {
        if (url != null && !url.startsWith(HTTPS)) {
            throw new IllegalArgumentException(campo + " debe empezar con " + HTTPS);
        }
    }

}
