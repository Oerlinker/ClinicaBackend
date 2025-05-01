package com.clinicaoftalmologica.clinicaoftalmologicabackend.specification;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.dto.CitaReportFilter;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cita;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CitaSpecification {
    public static Specification<Cita> withFilters(CitaReportFilter f) {
        return (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();

            if (f.getFechaDesde() != null) {
                preds.add(cb.greaterThanOrEqualTo(root.get("fecha"), f.getFechaDesde()));
            }
            if (f.getFechaHasta() != null) {
                preds.add(cb.lessThanOrEqualTo(root.get("fecha"), f.getFechaHasta()));
            }
            if (f.getEstado() != null && !f.getEstado().isBlank()) {
                preds.add(cb.equal(root.get("estado"), f.getEstado()));
            }
            if (f.getDoctorId() != null) {
                preds.add(cb.equal(root.get("doctor").get("id"), f.getDoctorId()));
            }
            if (f.getPacienteId() != null) {
                preds.add(cb.equal(root.get("paciente").get("id"), f.getPacienteId()));
            }

            return cb.and(preds.toArray(new Predicate[0]));
        };
    }
}