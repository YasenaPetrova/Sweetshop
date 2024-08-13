package com.academy.cakeshop.service;

import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.persistance.entity.Unit;
import com.academy.cakeshop.persistance.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UnitServiceTest {
    private UnitService unitService;
    @Mock
    private UnitRepository unitRepository;

    @BeforeEach
    void setUp() {
        unitRepository = Mockito.mock(UnitRepository.class);
        unitService = new UnitService(unitRepository);
    }

    @Test
    void createUnit() {
    }

    @Test
    void givenValidID_WhenGettingUnitByID_ThenCorrectUnitReturned() {
        Long id = 5L;
        Unit expectedUnit = new Unit(5L, "Milliliters", 100.0);
        Mockito.when(unitRepository.getReferenceById(id)).thenReturn(expectedUnit);
        Unit actualUnit = unitService.getUnitById(id);
        assertEquals(expectedUnit,actualUnit,"Units dont match");
    }
    @Test
    void givenInvalidID_WhenGettingUnitByID_ThenThrowException() {
        Long id = 5L;
        Mockito.when(unitRepository.getReferenceById(id)).thenReturn(null);
        BusinessNotFound exception = assertThrows(BusinessNotFound.class,()-> unitService.getUnitById(id));
        String expectedMessage = "Unit ID does not exist.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage,actualMessage);
    }

    @Test
    void getAllUnits() {
        Unit expectedUnit = new Unit(5L, "Milliliters", 100.0);
        Unit expectedUnit2 = new Unit(10L, "Milliliters", 100.0);
        Unit expectedUnit3 = new Unit(70L, "Milliliters", 100.0);
        List<Unit> expectedList = new ArrayList<>();
        expectedList.add(expectedUnit);
        expectedList.add(expectedUnit2);
        expectedList.add(expectedUnit3);

        Mockito.when(unitRepository.findAll()).thenReturn(expectedList);
        List<Unit> actualList = unitService.getAllUnits();
        assertEquals(expectedList,actualList,"Lists dont match");
    }

    @Test
    void updateUnit() {

    }
    @Test
    void deleteUnit() {

    }

}