package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Rol;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;


@Service
public class RolService {
    @Autowired
    private RolRepository rolRepository;

    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    public Rol createRol(Rol rol) throws Exception {
        Optional<Rol> existing = rolRepository.findByNombre(rol.getNombre());
        if (existing.isPresent()) {
            throw new Exception("El rol ya existe");
        } else {
            return rolRepository.save(rol);
        }
    }

    public Rol getRolById(Long id) throws Exception {
        return rolRepository.findById(id)
                .orElseThrow(() -> new Exception("Rol no encontrado"));
    }


    public void deleteRol(Long id) throws Exception {
        Rol rol = getRolById(id);
        rolRepository.delete(rol);
    }

    public Rol updateRol(Long id, Rol updatedRol) throws Exception {
        Rol rol = getRolById(id);
        rol.setNombre(updatedRol.getNombre());
        return rolRepository.save(rol);
    }

}
