package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.UnitDTO;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.persistance.entity.Unit;
import com.academy.cakeshop.persistance.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {
    private UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Unit createUnit(Unit unit) {
        return unitRepository.save(unit);
    }

    public Unit getUnitById(Long id) {
      Unit unit = unitRepository.getReferenceById(id);
      if (unit != null) {
          return unit;
        }
      else {
          throw new BusinessNotFound("Unit ID does not exist.");
      }
    }

    public List<Unit> getAllUnits() {
        List<Unit> units = unitRepository.findAll();
        if(!units.isEmpty()){
            return units;
        }
        else{
            throw new BusinessNotFound("Unit not found.");
        }
    }

    public Unit updateUnit(Long id, UnitDTO unitDetails) {
        Unit unit = unitRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Unit not found"));
        unit.setName(unitDetails.name());
        return unitRepository.save(unit);
    }

    public void deleteUnit(Long id) {
        unitRepository.deleteById(id);
    }
}