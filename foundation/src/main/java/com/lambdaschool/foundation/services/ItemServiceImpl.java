package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.foundation.handlers.HelperFunctions;
import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.models.User;
import com.lambdaschool.foundation.models.UserRoles;
import com.lambdaschool.foundation.models.Useremail;
import com.lambdaschool.foundation.repository.ItemRepository;
import com.lambdaschool.foundation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * Implements the Itemservice Interface
 */
@Transactional
@Service(value = "itemService")
public class ItemServiceImpl
        implements ItemService
{
    /**
     * Connects this service to the Item table.
     */
    @Autowired
    private ItemRepository itemrepos;


    /**
     * Connects this service to the User table.
     */
    @Autowired
    private UserRepository userrepos;


    /**
     * Connects this service to the helper functions for this application
     */
    @Autowired
    private HelperFunctions helper;

    public Item findItemById(long id)
            throws
            ResourceNotFoundException
    {
        return itemrepos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item id " + id + " not found!"));
    }

    @Override
    public List<Item> findByNameContaining(String itemname)
    {
        return itemrepos.findByItemnameContainingIgnoreCase(itemname.toLowerCase());
    }

    @Override
    public List<Item> findAll()
    {
        List<Item> list = new ArrayList<>();
        /*
         * findAll returns an iterator set.
         * iterate over the iterator set and add each element to an array list.
         */
        itemrepos.findAll()
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }

    @Transactional
    @Override
    public void delete(long id)
    {
        itemrepos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item id " + id + " not found!"));
        itemrepos.deleteById(id);
    }

    @Override
    public Item findByName(String name)
    {
        Item uu = itemrepos.findByItemname(name.toLowerCase());
        if (uu == null)
        {
            throw new ResourceNotFoundException("Item name " + name + " not found!");
        }
        return uu;
    }



    @Transactional
    @Override
    public Item save(Item item)
    {
        Item newItem = new Item();

        if (item.getItemid() != 0) {
            System.out.println("item found, item id = "+ item.getItemid());
            itemrepos.findById(item.getItemid())
                    .orElseThrow(() -> new ResourceNotFoundException("Item id " + item.getItemid() + " not found"));
            newItem.setItemid(item.getItemid());
        }

        newItem.setItemname(item.getItemname().toLowerCase());
        newItem.setItemtype(item.getItemtype().toLowerCase());
        newItem.setItemdescr(item.getItemdescr());
        newItem.setItemlocat(item.getItemlocat());
        newItem.setIsavailable(item.getIsavailable());
        newItem.setItemrate(item.getItemrate());
        newItem.setItemimg(item.getItemimg());
        newItem.setLender(userrepos.findByUsername(item.getLender().getUsername()));




        //     use this if item has children
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

        return itemrepos.save(newItem);
    }

    /*
    @Transactional
    @Override
    public Item update(
            Item item,
            long id)
    {
        Item currentItem = findItemById(id);


        return itemrepos.save(currentItem);

    }
    */
    @Transactional
    @Override
    public Item update(
            Item item,
            long id)
    {
        User currentUser = userrepos.findByUsername(item.getLender().getUsername());
        Item updateitem = item;

        if (helper.isAuthorizedToMakeChange(currentUser.getUsername()))
        {
            updateitem = itemrepos.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item id " + item.getItemid() + " not found"));

            updateitem.setItemid(id);
            if (item.getItemname() != null){ updateitem.setItemname(item.getItemname().toLowerCase()); }
            if ( item.getItemtype() != null){ updateitem.setItemtype(item.getItemtype().toLowerCase()); }
            if ( item.getItemdescr() != null){ updateitem.setItemdescr(item.getItemdescr()); }
            if ( item.getItemlocat() != null){ updateitem.setItemlocat(item.getItemlocat()); }
            if ( item.getIsavailable() ){ updateitem.setIsavailable(item.getIsavailable()); }
            if ( item.getItemrate() != 0.0 ){ updateitem.setItemrate(item.getItemrate()); System.out.println("itemrate update");}
            if ( item.getItemimg() != null){ updateitem.setItemimg(item.getItemimg()); }

            return itemrepos.save(updateitem);
        } else
        {
            {
                // note we should never get to this line but is needed for the compiler
                // to recognize that this exception can be thrown
                throw new ResourceNotFoundException("You are not authorized to make changes on this item my dude.");
            }
        }
    }
}
