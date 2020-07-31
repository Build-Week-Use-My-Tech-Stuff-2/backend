package com.lambdaschool.foundation.controllers;


import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.models.*;
import com.lambdaschool.foundation.models.Contract;
import com.lambdaschool.foundation.repository.ItemRepository;
import com.lambdaschool.foundation.services.ContractService;
import com.lambdaschool.foundation.services.ItemService;
import com.lambdaschool.foundation.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/contracts")
public class ContractController
{
    @Autowired
    private ContractService contractService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userservice;
    private @Valid Contract body;

    /**
     * Returns a list of all contracts
     * <br>Example: <a href="http://localhost:2019/contracts/contracts">http://localhost:2019/contracts/contracts</a>
     *
     * @return JSON list of all contracts with a status of OK
     * @see ContractService#findAll() ContractService.findAll()
     */
    @ApiOperation(value = "returns all Contracts",
            response = Contract.class,
            responseContainer = "List")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/contracts",
            produces = {"application/json"})
    public ResponseEntity<?> listAllContracts()
    {
        List<Contract> myContracts = contractService.findAll();
        return new ResponseEntity<>(myContracts,
                HttpStatus.OK);
    }

    /**
     * Returns a single contract based off a contract id number
     * <br>Example: <a href="http://localhost:2019/contracts/contract/7">http://localhost:2019/contracts/contract/7</a>
     *
     * @param contractId The primary key of the contract you seek
     * @return JSON object of the contract you seek
     * @see ContractService#findContractById(long) ContractService.findContractById(long)
     */
    @ApiOperation(value = "Retrieve a contract based off of contract id",
            response = Contract.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Contract Found",
            response = Contract.class), @ApiResponse(code = 404,
            message = "Contract Not Found",
            response = ErrorDetail.class)})
    @GetMapping(value = "/contract/{contractId}",
            produces = {"application/json"})
    public ResponseEntity<?> getContractById(
            @ApiParam(value = "Contract id",
                    required = true,
                    example = "4")
            @PathVariable
                    Long contractId)
    {
        Contract u = contractService.findContractById(contractId);
        return new ResponseEntity<>(u,
                HttpStatus.OK);
    }
    /*
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Item Found",
            response = Item.class), @ApiResponse(code = 404,
            message = "Item Not Found",
            response = ErrorDetail.class)})
    @DeleteMapping(value = "/item/{id}")
    public ResponseEntity<?> deleteItemById(
            @ApiParam(value = "itemid",
                    required = true,
                    example = "4")
    */
    /*
                @Valid
            @ApiParam(value = "a full item object",
                    required = true)
            @RequestBody
                    Item updateItem,
            @ApiParam(value = "itemid",
                    required = true,
                    example = "4")
            @PathVariable
                    long itemid)

     */


    /**
     * Create a contract given the itemid in the url and a body containing the contractlength
     * <br> Example: <a href="http://localhost:2019/contracts/contract">http://localhost:2019/contracts/contract</a>
     *
     * @param itemid a body containing the contractlength
     *
     * @return A location header with the URI to the newly created contract and a status of CREATED
     * @throws URISyntaxException Exception if something does not work in creating the location header
     * @see ContractService#save(Contract) ContractService.save(Contract)
     */
    @ApiOperation(value = "adds a contract given a contract length in the request body",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Contract Found",
            response = Contract.class), @ApiResponse(code = 404,
            message = "Contract Not Found",
            response = ErrorDetail.class)})
    // http://localhost:2019/contracts/contract/{itemid}
    @PostMapping(value = "/new/{itemid}", consumes = {"application/json"})
    public ResponseEntity<?> addNewContract(@Valid
                                                @ApiParam(value = "contractlength: int",
                                                        required = true)
                                                @RequestBody Contract newContract,
                                                @ApiParam(value = "itemid",
                                                        required = true,
                                                        example = "4")
                                                @PathVariable
                                                        long itemid,
                                                Authentication authentication){

        User u = userservice.findByName(authentication.getName());
        newContract.setContractid(0);
        newContract.setRentee(u);
        newContract.setItem(itemService.findItemById(itemid));
        //newContract.setLenderid(newContract.getItem().getLender().getUserid());
        newContract = contractService.save(newContract);


        HttpHeaders responseHeaders = new HttpHeaders();
        URI newContractURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{contractid}")
                .buildAndExpand(newContract.getContractid())
                .toUri();
        responseHeaders.setLocation(newContractURI);

        return new ResponseEntity<>("Contract with ID#" + newContract.getContractid() + " created!", responseHeaders, HttpStatus.CREATED);
    }

    /**
     * Given a complete Contract Object
     * Given the contract id, primary key, is in the Contract table,
     * replace the Contract record
     * <br> Example: <a href="http://localhost:2019/contracts/contract/15">http://localhost:2019/contracts/contract/15</a>
     *
     * @param updateContract A complete Contract to be used to
     *                   replace the Contract.
     * @param contractid     The primary key of the contract you wish to replace.
     * @return status of OK
     * @see ContractService#save(Contract) ContractService.save(Contract)
     */

    @ApiOperation(value = "updates a contract given in the request body",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Contract Found",
            response = Contract.class), @ApiResponse(code = 404,
            message = "Contract Not Found",
            response = ErrorDetail.class)})
    @PutMapping(value = "/contract/{contractid}",
            consumes = {"application/json"})
    public ResponseEntity<?> updateFullContract(
            @Valid
            @ApiParam(value = "a full contract object",
                    required = true)
            @RequestBody
                    Contract updateContract,
            @ApiParam(value = "contractid",
                    required = true,
                    example = "4")
            @PathVariable
                    long contractid)
    {
        updateContract.setContractid(contractid);
        contractService.save(updateContract);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    /**
     * Updates the contract record associated with the given id with the provided data. Only the provided fields are affected.
     * <br> Example: <a href="http://localhost:2019/contracts/contract/7">http://localhost:2019/contracts/contract/7</a>
     *
     * @param updateContract An object containing values for just the fields that are being updated. All other fields are left NULL.
     * @param id         The primary key of the contract you wish to update.
     * @return A status of OK
     * @see ContractService#update(Contract, long) ContractService.update(Contract, long)
     */
    /*
    @ApiOperation(value = "updates a contract with the information given in the request body",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Contract Found",
            response = Contract.class), @ApiResponse(code = 404,
            message = "Contract Not Found",
            response = ErrorDetail.class)})
    @PatchMapping(value = "/contract/{id}",
            consumes = {"application/json"})
    public ResponseEntity<?> updateContract(
            @ApiParam(value = "a contract object with just the information needed to be updated",
                    required = true)
            @RequestBody
                    Contract updateContract,
            @ApiParam(value = "contractid",
                    required = true,
                    example = "4")
            @PathVariable
                    long id)
    {
        contractService.update(updateContract,
                id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
     */

    /**
     * Deletes a given contract
     * <br>Example: <a href="http://localhost:2019/contracts/contract/14">http://localhost:2019/contracts/contract/14</a>
     *
     * @param id the primary key of the contract you wish to delete
     * @return Status of OK
     */
    @ApiOperation(value = "Deletes the given contract",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Contract Found",
            response = Contract.class), @ApiResponse(code = 404,
            message = "Contract Not Found",
            response = ErrorDetail.class)})
    @PreAuthorize("hasAnyRole('ADMIN','LENDER')")
    @DeleteMapping(value = "/contract/{id}")
    public ResponseEntity<?> deleteContractById(
            @ApiParam(value = "contractid",
                    required = true,
                    example = "4")
            @PathVariable
                    long id)
    {
        contractService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation(value = "set agree to contract true",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Contract Found",
            response = Contract.class), @ApiResponse(code = 404,
            message = "Contract Not Found",
            response = ErrorDetail.class)})
    // http://localhost:2019/contracts/contract/{itemid}
    @PatchMapping(value = "/contract/agree/{contractid}", consumes = {"application/json"})
    public ResponseEntity<?> renteeAgree(@Valid
                                            @RequestBody Contract body,
                                            @ApiParam(value = "contractid",
                                                    required = true,
                                                    example = "4")
                                            @PathVariable
                                                    long contractid,
                                            Authentication authentication){

        Contract c = contractService.findContractById(contractid);

            if (userservice.findByName(authentication.getName()) == c.getRentee()){
                c.setRenteeaccept(false);
                c.setRenteecomplete(false);
                contractService.save(c);
                System.out.println("plz work");
            }else if (userservice.findByName(authentication.getName()) == c.getItem().getLender()){
                c.setLenderaccept(!c.isLenderaccept());
                c.setLendercomplete(!c.isLendercomplete());
                contractService.save(c);
                System.out.println("plz work 2");
            }else {
                throw new ResourceNotFoundException("You must be the lender or rentee to change this value");
            }

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newContractURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{contractid}")
                .buildAndExpand(c.getContractid())
                .toUri();
        responseHeaders.setLocation(newContractURI);

        return new ResponseEntity<>("Contract with ID#" + c.getContractid() + " updated!", responseHeaders, HttpStatus.CREATED);
    }

    /**
     * Updates the contrat record associated with the given contract id with the provided data. Only the provided fields are affected.
     * <br> Example: <a href="http://localhost:2019/items/item/7">http://localhost:2019/items/item/7</a>
     *
     * @param contractUpdate An object containing values for just the fields that are being updated. All other fields are left NULL.
     * @param contractid         The primary key of the item you wish to update.
     * @return A status of OK
     * @see ContractService#update(Contract, long, User) ContractService.update(Contract, long, User)
     */
    @ApiOperation(value = "updates a contract with the information given in the request body",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Contract Found",
            response = Contract.class), @ApiResponse(code = 404,
            message = "Contract Not Found",
            response = ErrorDetail.class)})
    @PreAuthorize("hasAnyRole('ADMIN','LENDER','USER')")
    @PatchMapping(value = "/contract/{contractid}",
            consumes = {"application/json"})
    public ResponseEntity<?> updateItem(
            @ApiParam(value = "a contract object with just the information needed to be updated",
                    required = true)
            @RequestBody
                    Contract contractUpdate,
            @ApiParam(value = "contractid",
                    required = true,
                    example = "4")
            @PathVariable
                    long contractid,
            Authentication auth)
    {
        //get user who is making update request
        User u = userservice.findByName(auth.getName());
        contractService.update(contractUpdate, contractid, u);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
