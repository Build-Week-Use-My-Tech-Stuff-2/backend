package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.handlers.HelperFunctions;
import com.lambdaschool.foundation.models.Contract;
import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.repository.ContractRepository;
import com.lambdaschool.foundation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Implements the Contractservice Interface
 */
@Transactional
@Service(value = "contractService")
public class ContractServiceImpl
        implements ContractService
{
    /**
     * Connects this service to the Contract table.
     */
    @Autowired
    private ContractRepository contractrepos;


    /**
     * Connects this service to the User table.
     */
    @Autowired
    private UserRepository userrepos;

    /**
     * Connects this service to the Item table.
     */
    @Autowired
    private UserRepository itemrepos;


    /**
     * Connects this service to the helper functions for this application
     */
    @Autowired
    private HelperFunctions helper;

    public Contract findContractById(long id)
            throws
            ResourceNotFoundException
    {
        return contractrepos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract id " + id + " not found!"));
    }

    @Override
    public List<Contract> findAll()
    {
        List<Contract> list = new ArrayList<>();
        /*
         * findAll returns an iterator set.
         * iterate over the iterator set and add each element to an array list.
         */
        contractrepos.findAll()
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }

    @Transactional
    @Override
    public void delete(long id)
    {
        contractrepos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract id " + id + " not found!"));
        contractrepos.deleteById(id);
    }

    @Transactional
    @Override
    public Contract save(Contract contract)
    {
        Contract newContract = new Contract();

        if (contract.getContractid() != 0) {
            contractrepos.findById(contract.getContractid())
                    .orElseThrow(() -> new ResourceNotFoundException("Contract id " + contract.getContractid() + " not found"));
            newContract.setContractid(contract.getContractid());
        }

        newContract.setContractlength(contract.getContractlength());
        newContract.setRentee(userrepos.findByUsername(contract.getRentee().getUsername()));
        newContract.setItem(contract.getItem());
        //really annoying math here to make sure we only have 2 decimal places on the itemrate * contractlength
        float fee = (float)contract.getContractlength() * contract.getItem().getItemrate();
        BigDecimal roundedFee = new BigDecimal(fee).setScale(2, RoundingMode.HALF_UP);
        newContract.setContractfee(roundedFee.floatValue());

        //unless the lender and the rentee have both agreed that the contract is complete set active true
        newContract.setActive(true);
        //
        System.out.println("contract:");
        System.out.println("rentee complete = " +  contract.isRenteecomplete() + "lender complete = " + contract.isLendercomplete());
        System.out.println("newcontract:");
        System.out.println("rentee complete = " +  newContract.isRenteecomplete() + "lender complete = " + newContract.isLendercomplete());
        //
        if(contract.isRenteecomplete() && contract.isLendercomplete()){newContract.setActive(false);}
        //
        System.out.println("contract:");
        System.out.println("rentee accept = " +  contract.isRenteeaccept() + "lender accept = " + contract.isLenderaccept());
        System.out.println("newcontract:");
        System.out.println("rentee accept = " +  newContract.isRenteeaccept() + "lender accept = " + newContract.isLenderaccept());
        //
        if(contract.isLenderaccept() && contract.isRenteeaccept()){
            System.out.println("we are in!");
            //build dates
            Date machineStart = new Date();
            Date machineEnd = Date.from(machineStart.toInstant().plusSeconds(86400 * contract.getContractlength())); //just adding the contract length
            SimpleDateFormat formatter = new SimpleDateFormat("EEEEE MMMMM yyyy hh:mm a z");

            //set raw dates
            newContract.setRaw_contractstartdate(machineStart);
            newContract.setRaw_contractenddate(machineEnd);

            //format readable dates
            newContract.setContractenddate(formatter.format(machineEnd));
            newContract.setContractstartdate(formatter.format(machineStart));
        }


        //     use this if contract has children
        /*
        newCustomer.getOrders().clear();
        for (Order o : customer.getOrders()) {
            Order newOrder = new Order(
                    o.getOrdamount(),
                    o.getAdvanceamount(),
                    o.getOrderdescription(),
                    newCustomer);
            newCustomer.getOrders().add(newOrder);
        }
        */

        return contractrepos.save(newContract);
    }

    @Transactional
    @Override
    public Contract update(
            Contract contract,
            long id,
            User currentuser)
    {
        //create blank contract
        Contract updatecontract = contract;

        //set blank contract to requested contract
        updatecontract = contractrepos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract id " + contract.getContractid() + " not found"));

        //check if currentuser is authorized
        if (helper.isAuthorizedToMakeChange(currentuser.getUsername()))
        {
            //set contract id
            updatecontract.setContractid(id);

            //check if currentuser is lender
            if(currentuser == updatecontract.getItem().getLender()){

                //update individual items
                if (contract.isLenderaccept()){ updatecontract.setLenderaccept(contract.isLenderaccept()); }
                if (contract.isLendercomplete()){ updatecontract.setLendercomplete(contract.isLendercomplete()); }
                //if (contract.getContractlength() != updatecontract.getContractlength()){  }

                return contractrepos.save(updatecontract);

                //check if currentuser is rentee
            }else if(currentuser == updatecontract.getRentee()){

                //update individual items
                if (contract.isRenteeaccept()){ updatecontract.setRenteeaccept(contract.isRenteeaccept()); }
                if (contract.isRenteecomplete()){ updatecontract.setRenteecomplete(contract.isRenteecomplete()); }

                return contractrepos.save(updatecontract);

                //if (contract.getContractlength() != updatecontract.getContractlength()){  }
            }else{ throw new ResourceNotFoundException("HEY! What are you doing in this section of the databa.. WAIT! GET BACK HERE! YOU CANT RUN FROM ME!"); }



        } else
        {
            {
                // note we should never get to this line but is needed for the compiler
                // to recognize that this exception can be thrown
                throw new ResourceNotFoundException("You are not authorized to make changes on this contract my dude.");
            }
        }
    }






}
