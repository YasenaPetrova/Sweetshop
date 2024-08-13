package com.academy.cakeshop.service;

import com.academy.cakeshop.persistance.entity.Storage;
import com.academy.cakeshop.persistance.entity.Product;
import com.academy.cakeshop.persistance.repository.StorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {

    private StorageService storageService;

    @Mock
    private StorageRepository storageRepository;

    @BeforeEach
    void setUp() {
        storageRepository = Mockito.mock(StorageRepository.class);
        storageService = new StorageService(storageRepository);
    }

    @Test
    void createStorage() {
        Storage storage = new Storage(5L, new Product(), 100.0);
        Mockito.when(storageRepository.save(storage)).thenReturn(storage);
        Storage createdStorage = storageService.createStorage(storage);
        assertEquals(storage, createdStorage);
    }

    @Test
    void givenValidID_WhenGettingStorageByID_ThenCorrectStorageReturned() {
        Long id = 5L;
        Storage expectedStorage = new Storage(5L, new Product(), 100.0);
        Mockito.when(storageRepository.findById(id)).thenReturn(Optional.of(expectedStorage));
        Optional<Storage> actualStorage = storageService.getStorageById(id);
        assertTrue(actualStorage.isPresent());
        assertEquals(expectedStorage, actualStorage.get(), "Storages don't match");
    }

    @Test
    void givenInvalidID_WhenGettingStorageByID_ThenThrowException() {
        Long id = 5L;
        Mockito.when(storageRepository.findById(id)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> storageService.getStorageById(id));
        String expectedMessage = "Storage not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void getAllStorages() {
        Storage expectedStorage1 = new Storage(5L, new Product(), 100.0);
        Storage expectedStorage2 = new Storage(10L, new Product(), 200.0);
        List<Storage> expectedList = new ArrayList<>();
        expectedList.add(expectedStorage1);
        expectedList.add(expectedStorage2);

        Mockito.when(storageRepository.findAll()).thenReturn(expectedList);
        List<Storage> actualList = storageService.getAllStorages();
        assertEquals(expectedList, actualList, "Lists don't match");
    }

    @Test
    void updateStorage() {
        Long id = 5L;
        Storage existingStorage = new Storage(id, new Product(), 100.0);
        Storage updatedDetails = new Storage(id, new Product(), 200.0);

        Mockito.when(storageRepository.findById(id)).thenReturn(Optional.of(existingStorage));
        Mockito.when(storageRepository.save(existingStorage)).thenReturn(updatedDetails);

        Storage updatedStorage = storageService.updateStorage(id, updatedDetails);
        assertEquals(updatedDetails.getQuantity(), updatedStorage.getQuantity());
        assertEquals(updatedDetails.getProduct(), updatedStorage.getProduct());
    }

    @Test
    void givenInvalidID_WhenUpdatingStorage_ThenThrowException() {
        Long id = 5L;
        Storage updatedDetails = new Storage(id, new Product(), 200.0);

        Mockito.when(storageRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> storageService.updateStorage(id, updatedDetails));
        String expectedMessage = "Storage not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deleteStorage() {
        Long id = 5L;
        Mockito.doNothing().when(storageRepository).deleteById(id);

        assertDoesNotThrow(() -> storageService.deleteStorage(id));
        Mockito.verify(storageRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    void givenInvalidID_WhenDeletingStorage_ThenThrowException() {
        Long id = 5L;
        Mockito.doThrow(new ResourceNotFoundException("Storage not found")).when(storageRepository).deleteById(id);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> storageService.deleteStorage(id));
        String expectedMessage = "Storage not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
