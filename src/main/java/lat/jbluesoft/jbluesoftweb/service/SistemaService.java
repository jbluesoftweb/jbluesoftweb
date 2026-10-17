package lat.jbluesoft.jbluesoftweb.service;

import java.util.List;
import java.util.Optional;

import lat.jbluesoft.jbluesoftweb.model.Sistema;
import org.springframework.stereotype.Service;

@Service
public class SistemaService {

    private static final String DESCRIPCION_PROVISIONAL = "Descripción detallada próximamente.";

    private final List<Sistema> sistemas = List.of(
            new Sistema(
                    "erp-admin",
                    "ERP-ADMIN",
                    "ERP administrativo genérico: gestión de personal, asistencia y reportes consolidados para cualquier institución u organización — pública o privada.",
                    DESCRIPCION_PROVISIONAL,
                    "/assets/img/portafolio-ameztravel.webp",
                    null,
                    "https://github.com/jbluesoftweb/erp-admin",
                    null),
            new Sistema(
                    "drivex",
                    "DriveX",
                    "Aplicación de gestión de archivos en la nube.",
                    DESCRIPCION_PROVISIONAL,
                    "/assets/img/drivex.webp",
                    null,
                    null,
                    null),
            new Sistema(
                    "finanzasya",
                    "FinanzasYa",
                    "Aplicación de finanzas personales. Controla tus gastos.",
                    DESCRIPCION_PROVISIONAL,
                    "/assets/img/finanzasya.webp",
                    null,
                    null,
                    null)
    );

    public List<Sistema> findAll() {
        return sistemas;
    }

    public Optional<Sistema> findBySlug(String slug) {
        if (slug == null) {
            return Optional.empty();
        }
        return sistemas.stream()
                .filter(s -> s.slug().equals(slug))
                .findFirst();
    }

}
