package com.lambdaschool.foundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;


/**
 * The entity allowing interaction with the items table
 */
@ApiModel(value = "Item",
        description = "Yes, this is an actual item")
@Entity
@Table(name = "items")
public class Item
        extends Auditable {

    /**
     * The primary key (long) of the items table.
     */
    @ApiModelProperty(name = "item id",
            value = "primary key for Item",
            required = true,
            example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemid;

    /**
     * The item name (String). Cannot be null.
     */
    @ApiModelProperty(name = "Item Name",
            value = "Actual item name.",
            required = true,
            example = "Acer Desktop PC")
    @Size(min = 2,
            max = 30,
            message = "Item Name must be between 2 and 30 characters my dude.")
    @NotNull
    @Column(nullable = false)
    private String itemname;

    /**
     * Type of item (String). Cannot be null.
     */
    @ApiModelProperty(name = "primary email",
            value = "The type for this item",
            required = true,
            example = "Desktop")
    @NotNull
    @Column(nullable = false)
    private String itemtype;

    /**
     * The item description (String). Cannot be null.
     */
    @ApiModelProperty(name = "Item Name",
            value = "The description of the item",
            required = true,
            example = "Acer Desktop PC")
    @Size(min = 10,
            max = 140,
            message = "Item description must be between 10 and 140 characters my dude.")
    @NotNull
    @Column(nullable = false)
    private String itemdescr;

    /**
     * The item location (String). Cannot be null.
     */
    @ApiModelProperty(name = "Item Name",
            value = "Actual item name.",
            required = true,
            example = "11345 Beggars Canyon, Wamprat road")
    @Size(min = 2,
            max = 64,
            message = "Item location must be between 2 and 64 characters my dude.")
    @NotNull
    @Column(nullable = false)
    private String itemlocat;

    /**
     * The item availability (bool). Cannot be null.
     */
    @ApiModelProperty(name = "Item Availability",
            value = "True or false value.",
            required = true,
            example = "false")
    @NotNull
    @Column(nullable = false)
    private boolean isavailable;

    /**
     * The item rate (float). Cannot be null.
     */
    @ApiModelProperty(name = "Item Rate",
            value = "Item cost per iteration.",
            required = true,
            example = "26.95")
    @Digits(integer = 999, fraction = 2)
    @PositiveOrZero
    @NotNull
    @Column(nullable = false)
    private float itemrate;

    /**
     * The item image (String). Cannot be null.
     */
    @ApiModelProperty(name = "Item Image",
            value = "Image of the Item.",
            required = true,
            example = "http://www.tinypic.com/2451v233")
    //@Pattern(regexp = "(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)",
            //message = "Item image must be a valid image URL.")
    @NotNull
    @Column(nullable = false)
    private String itemimg;



    // Relationships /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Many to One Relationship with Users
     */
    @ManyToOne
    @JoinColumn(name = "userid",
            nullable = false)
    @JsonIgnoreProperties(value = "items",
            allowSetters = true)
    private User lender;

    // Relationships /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Default constructor used primarily by the JPA.
     */
    public Item() { }
    /*
    public Item(@Size(min = 2,
            max = 30,
            message = "Item Name must be between 2 and 30 characters my dude.") @NotNull String itemname, @NotNull String itemtype, @Size(min = 10,
            max = 140,
            message = "Item description must be between 10 and 140 characters my dude.") @NotNull String itemdescr, @Size(min = 2,
            max = 64,
            message = "Item location must be between 2 and 64 characters my dude.") @NotNull String itemlocat, @NotNull boolean isavailable, @Digits(integer = 999, fraction = 2) @PositiveOrZero @NotNull float itemrate, @Pattern(regexp = "(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)",
            message = "Item image must be a valid image URL.") @NotNull String itemimg, User lender) {
        this.itemname = itemname;
        this.itemtype = itemtype;
        this.itemdescr = itemdescr;
        this.itemlocat = itemlocat;
        this.isavailable = isavailable;
        this.itemrate = itemrate;
        this.itemimg = itemimg;
        this.lender = lender;
    }
     */

    public Item(@Size(min = 2,
            max = 30,
            message = "Item Name must be between 2 and 30 characters my dude.") @NotNull String itemname, @NotNull String itemtype, @Size(min = 10,
            max = 140,
            message = "Item description must be between 10 and 140 characters my dude.") @NotNull String itemdescr, @Size(min = 2,
            max = 64,
            message = "Item location must be between 2 and 64 characters my dude.") @NotNull String itemlocat, @NotNull boolean isavailable, @Digits(integer = 999, fraction = 2) @PositiveOrZero @NotNull float itemrate, @Pattern(regexp = "(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png)",
            message = "Item image must be a valid image URL.") @NotNull String itemimg, @NotNull User lender) {
        this.itemname = itemname;
        this.itemtype = itemtype;
        this.itemdescr = itemdescr;
        this.itemlocat = itemlocat;
        this.isavailable = isavailable;
        this.itemrate = itemrate;
        this.itemimg = itemimg;
        this.lender = lender;
    }

    public long getItemid() { return itemid; }

    public void setItemid(long itemid) { this.itemid = itemid; }

    public String getItemname() { return itemname; }

    public void setItemname(String itemname) { this.itemname = itemname; }

    public String getItemtype() { return itemtype; }

    public void setItemtype(String itemtype) { this.itemtype = itemtype; }

    public String getItemdescr() { return itemdescr; }

    public void setItemdescr(String itemdescr) { this.itemdescr = itemdescr; }

    public String getItemlocat() { return itemlocat; }

    public void setItemlocat(String itemlocat) { this.itemlocat = itemlocat; }

    public boolean getIsavailable() { return isavailable; }

    public void setIsavailable(boolean isavailable) { this.isavailable = isavailable; }

    public float getItemrate() { return itemrate; }

    public void setItemrate(float itemrate) { this.itemrate = itemrate; }

    public String getItemimg() { return itemimg; }

    public void setItemimg(String itemimg) { this.itemimg = itemimg; }

    // Relationships /////////////////////////////////////////////////////////////////////////////////////////////////

    public User getLender() { return lender; }

    public void setLender(User lender) { this.lender = lender; }

    // Relationships /////////////////////////////////////////////////////////////////////////////////////////////////

}
