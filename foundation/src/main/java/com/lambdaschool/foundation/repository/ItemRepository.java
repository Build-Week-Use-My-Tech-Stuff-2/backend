package com.lambdaschool.foundation.repository;

import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.views.JustTheCount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemRepository  extends CrudRepository<Item, Long> {
    /**
     * Find a item based off over itemname
     *
     * @param itemname the name (String) of item you seek
     * @return the first item object with the name you seek
     */
    Item findByItemname(String itemname);

    /**
     * Find all items whose name contains a given substring ignoring case
     *
     * @param name the substring of the names (String) you seek
     * @return List of items whose name contain the given substring ignoring case
     */
    List<Item> findByItemnameContainingIgnoreCase(String name);

}
