package com.lambdaschool.foundation.controllers;


import com.lambdaschool.foundation.models.*;
import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.models.Item;
import com.lambdaschool.foundation.services.ItemService;
import com.lambdaschool.foundation.services.ItemService;
import com.lambdaschool.foundation.services.ItemService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController
{
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemService userService;

    /**
     * Returns a list of all items
     * <br>Example: <a href="http://localhost:2019/items/items">http://localhost:2019/items/items</a>
     *
     * @return JSON list of all items with a status of OK
     * @see ItemService#findAll() ItemService.findAll()
     */
    @ApiOperation(value = "returns all Items",
            response = Item.class,
            responseContainer = "List")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/items",
            produces = {"application/json"})
    public ResponseEntity<?> listAllItems()
    {
        List<Item> myItems = itemService.findAll();
        return new ResponseEntity<>(myItems,
                HttpStatus.OK);
    }

    /**
     * Returns a single item based off a item id number
     * <br>Example: <a href="http://localhost:2019/items/item/7">http://localhost:2019/items/item/7</a>
     *
     * @param itemId The primary key of the item you seek
     * @return JSON object of the item you seek
     * @see ItemService#findItemById(long) ItemService.findItemById(long)
     */
    @ApiOperation(value = "Retrieve a item based of off item id",
            response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Item Found",
            response = Item.class), @ApiResponse(code = 404,
            message = "Item Not Found",
            response = ErrorDetail.class)})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/item/{itemId}",
            produces = {"application/json"})
    public ResponseEntity<?> getItemById(
            @ApiParam(value = "Item id",
                    required = true,
                    example = "4")
            @PathVariable
                    Long itemId)
    {
        Item u = itemService.findItemById(itemId);
        return new ResponseEntity<>(u,
                HttpStatus.OK);
    }

    /**
     * Return a item object based on a given itemname
     * <br>Example: <a href="http://localhost:2019/items/item/name/cinnamon">http://localhost:2019/items/item/name/cinnamon</a>
     *
     * @param itemName the name of item (String) you seek
     * @return JSON object of the item you seek
     * @see ItemService#findByName(String) ItemService.findByName(String)
     */
    @ApiOperation(value = "returns the item with the given itemname",
            response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Item Found",
            response = Item.class), @ApiResponse(code = 404,
            message = "Item Not Found",
            response = ErrorDetail.class)})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/item/name/{itemName}",
            produces = {"application/json"})
    public ResponseEntity<?> getItemByName(
            @ApiParam(value = "item name",
                    required = true,
                    example = "johnmitchell")
            @PathVariable
                    String itemName)
    {
        Item u = itemService.findByName(itemName);
        return new ResponseEntity<>(u,
                HttpStatus.OK);
    }

    /**
     * Returns a list of items whose itemname contains the given substring
     * <br>Example: <a href="http://localhost:2019/items/item/name/like/da">http://localhost:2019/items/item/name/like/da</a>
     *
     * @param itemName Substring of the itemname for which you seek
     * @return A JSON list of items you seek
     * @see ItemService#findByNameContaining(String) ItemService.findByNameContaining(String)
     */
    @ApiOperation(value = "returns all Items whose itemname contains the given substring",
            response = Item.class,
            responseContainer = "List")
    @ApiParam(value = "Item Name Substring",
            required = true,
            example = "john")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/item/name/like/{itemName}",
            produces = {"application/json"})
    public ResponseEntity<?> getItemLikeName(
            @PathVariable
                    String itemName)
    {
        List<Item> u = itemService.findByNameContaining(itemName);
        return new ResponseEntity<>(u,
                HttpStatus.OK);
    }

    /**
     * Given a complete Item Object, create a new Item record and accompanying itememail records
     * and item role records.
     * <br> Example: <a href="http://localhost:2019/items/item">http://localhost:2019/items/item</a>
     *
     * @param newitem A complete new item to add including emails and roles.
     *                roles must already exist.
     * @return A location header with the URI to the newly created item and a status of CREATED
     * @throws URISyntaxException Exception if something does not work in creating the location header
     * @see ItemService#save(Item) ItemService.save(Item)
     */
    @ApiOperation(value = "adds a item given in the request body",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Item Found",
            response = Item.class), @ApiResponse(code = 404,
            message = "Item Not Found",
            response = ErrorDetail.class)})
    // http://localhost:2019/items/item/
    @PostMapping(value = "/item", consumes = {"application/json"})
    public ResponseEntity<?> addNewItem(@Valid @RequestBody Item newItem) {
        newItem.setItemid(0);
        newItem = itemService.save(newItem);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newItemURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{itemid}")
                .buildAndExpand(newItem.getItemid())
                .toUri();
        responseHeaders.setLocation(newItemURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    /**
     * Given a complete Item Object
     * Given the item id, primary key, is in the Item table,
     * replace the Item record
     * <br> Example: <a href="http://localhost:2019/items/item/15">http://localhost:2019/items/item/15</a>
     *
     * @param updateItem A complete Item to be used to
     *                   replace the Item.
     * @param itemid     The primary key of the item you wish to replace.
     * @return status of OK
     * @see ItemService#save(Item) ItemService.save(Item)
     */

    @ApiOperation(value = "updates a item given in the request body",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Item Found",
            response = Item.class), @ApiResponse(code = 404,
            message = "Item Not Found",
            response = ErrorDetail.class)})
    @PutMapping(value = "/item/{itemid}",
            consumes = {"application/json"})
    public ResponseEntity<?> updateFullItem(
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
    {
        updateItem.setItemid(itemid);
        itemService.save(updateItem);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Updates the item record associated with the given id with the provided data. Only the provided fields are affected.
     * <br> Example: <a href="http://localhost:2019/items/item/7">http://localhost:2019/items/item/7</a>
     *
     * @param updateItem An object containing values for just the fields that are being updated. All other fields are left NULL.
     * @param id         The primary key of the item you wish to update.
     * @return A status of OK
     * @see ItemService#update(Item, long) ItemService.update(Item, long)
     */
    @ApiOperation(value = "updates a item with the information given in the request body",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Item Found",
            response = Item.class), @ApiResponse(code = 404,
            message = "Item Not Found",
            response = ErrorDetail.class)})
    @PatchMapping(value = "/item/{id}",
            consumes = {"application/json"})
    public ResponseEntity<?> updateItem(
            @ApiParam(value = "a item object with just the information needed to be updated",
                    required = true)
            @RequestBody
                    Item updateItem,
            @ApiParam(value = "itemid",
                    required = true,
                    example = "4")
            @PathVariable
                    long id)
    {
        itemService.update(updateItem,
                id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Deletes a given item
     * <br>Example: <a href="http://localhost:2019/items/item/14">http://localhost:2019/items/item/14</a>
     *
     * @param id the primary key of the item you wish to delete
     * @return Status of OK
     */
    @ApiOperation(value = "Deletes the given item",
            response = Void.class)
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
            @PathVariable
                    long id)
    {
        itemService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
