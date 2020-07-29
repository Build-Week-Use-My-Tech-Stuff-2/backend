package com.lambdaschool.foundation.services;

import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.models.User;

import java.util.List;

public interface ItemService {

        /**
         * Returns a list of all the Items
         *
         * @return List of Items. If no items, empty list.
         */
        List<Item> findAll();

        /**
         * A list of all items whose itemname contains the given substring
         *
         * @param itemname The substring (String) of the itemname of the Items you seek
         * @return List of items whose itemname contains the given substring
         */
        List<Item> findByNameContaining(String itemname);

        /**
         * Returns the item with the given primary key.
         *
         * @param id The primary key (long) of the item you seek.
         * @return The given Item or throws an exception if not found.
         */
        Item findItemById(long id);

        /**
         * Returns the item with the given name
         *
         * @param name The full name (String) of the Item you seek.
         * @return The Item with the given name or throws an exception if not found.
         */
        Item findByName(String name);

        /**
         * Deletes the item record the provided primary key
         *
         * @param id id The primary key (long) of the item you seek.
         */
        void delete(long id);

        /**
         * Given a complete item object, saves that item object in the database.
         * If a primary key is provided, the record is completely replaced
         * If no primary key is provided, one is automatically generated and the record is added to the database.
         *
         * @param item the item object to be saved
         * @return the saved item object including any automatically generated fields
         */
        Item save(Item item);


        /**
         * Updates the provided fields in the item record referenced by the primary key.
         * <p>
         *
         * @param item just the item fields to be updated.
         * @param id   The primary key (long) of the item to update
         * @return the complete item object that got updated
         */
        Item update(
            Item item,
        long id);

}
