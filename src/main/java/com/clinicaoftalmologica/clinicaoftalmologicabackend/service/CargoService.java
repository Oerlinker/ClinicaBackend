package com.clinicaoftalmologica.clinicaoftalmologicabackend.service;

import com.clinicaoftalmologica.clinicaoftalmologicabackend.model.Cargo;
import com.clinicaoftalmologica.clinicaoftalmologicabackend.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    public List<Cargo> getAllCargos() {
        return cargoRepository.findAll();
    }

    public Cargo getCargoById(Long id) throws Exception {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new Exception("Cargo no encontrado"));
    }

    public Cargo createCargo(Cargo cargo) throws Exception {
        if (cargoRepository.findByNombre(cargo.getNombre()).isPresent()) {
            throw new Exception("El cargo ya existe");
        }
        return cargoRepository.save(cargo);
    }

    public Cargo updateCargo(Long id, Cargo updatedCargo) throws Exception {
        Cargo cargo = getCargoById(id);
        cargo.setNombre(updatedCargo.getNombre());
        cargo.setDescripcion(updatedCargo.getDescripcion());
        return cargoRepository.save(cargo);
    }

    public void deleteCargo(Long id) throws Exception {
        Cargo cargo = getCargoById(id);
        cargoRepository.delete(cargo);
    }
}
