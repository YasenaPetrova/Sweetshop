package com.academy.cakeshop.service;

import com.academy.cakeshop.dto.StorageDTO;
import com.academy.cakeshop.errorHandling.BusinessNotFound;
import com.academy.cakeshop.errorHandling.ResourceNotFoundException;
import com.academy.cakeshop.persistance.entity.Storage;
import com.academy.cakeshop.persistance.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StorageService {
    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public Storage createStorage(Storage storage) {
        return storageRepository.save(storage);
    }

    public Storage getStorageById(Long id) {
        Storage storage = storageRepository.getReferenceById(id);
        if (storage != null) {
            return storage;
        } else {
            throw new BusinessNotFound("Storage ID does not exist.");
        }
    }

    public List<Storage> getAllStorages() {
        return storageRepository.findAll();
    }

    public Storage updateStorage(Long id, StorageDTO storageDetails) {
        Storage storage = storageRepository.getReferenceById(id);
        if (storage != null){
            storage.setQuantity(storageDetails.quantity());
            return storageRepository.save(storage);
        }else{
            throw new BusinessNotFound("No Storage with ID: " + id + " Found!");
        }
    }

    public void deleteStorage(Long id) {
        storageRepository.deleteById(id);
    }
}
