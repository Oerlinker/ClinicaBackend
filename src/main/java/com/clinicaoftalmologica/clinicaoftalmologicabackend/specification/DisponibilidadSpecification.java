package com.clinicaoftalmologica.clinicaoftalmologicabackend.specification;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.DisponibilidadReportFilter;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Disponibilidad;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.JoinType;

public class DisponibilidadSpecification {
    public static Specification<Disponibilidad> withFilters(DisponibilidadReportFilter f) {
        Specification<Disponibilidad> spec = Specification.where(null);

        if (f.getDoctorId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(
                            root.join("empleado", JoinType.LEFT).get("id"),
                            f.getDoctorId()
                    )
            );
        }
        if (f.getFechaDesde() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(
                            root.get("fecha"),
                            f.getFechaDesde()
                    )
            );
        }
        if (f.getFechaHasta() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(
                            root.get("fecha"),
                            f.getFechaHasta()
                    )
            );
        }
        return spec;
    }
}
