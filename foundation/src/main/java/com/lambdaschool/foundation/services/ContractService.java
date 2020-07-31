package com.lambdaschool.foundation.services;


import com.lambdaschool.foundation.models.Contract;
import com.lambdaschool.foundation.models.User;

import java.util.List;

public interface ContractService {

    /**
     * Returns a list of all the Contracts
     *
     * @return List of Contracts. If no contracts, empty list.
     */
    List<Contract> findAll();

    /**
     * Returns the contract with the given primary key.
     *
     * @param id The primary key (long) of the contract you seek.
     * @return The given Contract or throws an exception if not found.
     */
    Contract findContractById(long id);

    /**
     * Deletes the contract record the provided primary key
     *
     * @param id id The primary key (long) of the contract you seek.
     */
    void delete(long id);

    /**
     * Given a complete contract object, saves that contract object in the database.
     * If a primary key is provided, the record is completely replaced
     * If no primary key is provided, one is automatically generated and the record is added to the database.
     *
     * @param contract the contract object to be saved
     * @return the saved contract object including any automatically generated fields
     */
    Contract save(Contract contract);

    /*
    /**
     * Updates the provided fields in the contract record referenced by the primary key.
     * <p>
     *
     * @param contract just the contract fields to be updated.
     * @param id   The primary key (long) of the contract to update
     * @return the complete contract object that got updated
     */
    /*
    Contract update(Contract contract, long id);
    */
}

