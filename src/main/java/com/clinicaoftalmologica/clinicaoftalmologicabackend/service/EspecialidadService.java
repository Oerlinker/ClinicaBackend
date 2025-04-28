
package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Especialidad;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository repo;

    public List<Especialidad> getAll() {
        return repo.findAll();
    }

    public Especialidad getById(Long id) throws Exception {
        return repo.findById(id)
                .orElseThrow(() -> new Exception("Especialidad no encontrada"));
    }

    public Especialidad create(Especialidad e) {
        return repo.save(e);
    }

    public Especialidad update(Long id, Especialidad datos) throws Exception {
        Especialidad existente = getById(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return repo.save(existente);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
