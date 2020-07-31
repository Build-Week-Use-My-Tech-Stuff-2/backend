package com.lambdaschool.foundation.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.lang.String;
import java.util.Date;


/**
 * The entity allowing interaction with the contracts table
 */
@ApiModel(value = "Contract",
        description = "Yes, this is an actual contract")
@Entity
@Table(name = "contracts")
public class Contract
        extends Auditable {

    /**
     * The primary key (long) of the contracts table.
     */
    @ApiModelProperty(name = "contract id",
            value = "primary key for Contract",
            required = false,
            example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long contractid;

    /**
     * The rentee complete (bool). Cannot be null.
     */
    @ApiModelProperty(name = "Rentee Contract Completion Confirmation",
            value = "${some.key:false}",
            required = false,
            example = "false")
    @Column(nullable = true)
    private boolean renteecomplete;

    /**
     * The lender complete (bool). Cannot be null.
     */
    @ApiModelProperty(name = "Lender Contract Completion Confirmation",
            value = "${some.key:false}",
            required = false,
            example = "false")
    @Column(nullable = true)
    private boolean lendercomplete;

    /**
     * The rentee accept (bool). Cannot be null.
     */
    @ApiModelProperty(name = "Rentee Contract Start Confirmation",
            value = "${some.key:false}",
            required = false,
            example = "false")
    @Column(nullable = true)
    private boolean renteeaccept;

    /**
     * The lender accept (bool). Cannot be null.
     */
    @ApiModelProperty(name = "Lender Contract Start Confirmation",
            value = "${some.key:false}",
            required = false,
            example = "false")
    @Column(nullable = true)
    private boolean lenderaccept;

    /**
     * The isActive (bool). Cannot be null.
     */
    @ApiModelProperty(name = "Contract Activity Status",
            value = "True or false value.",
            required = false,
            example = "false")
    @NotNull
    @Column(nullable = true)
    @Value("${some.key:true}")
    private boolean isActive;

    /**
     * The contract fee (float). Cannot be null.
     */
    @ApiModelProperty(name = "Contract Fee",
            value = "Contract total fee.",
            required = false,
            example = "26.95")
    @Digits(integer = 999, fraction = 2)
    @PositiveOrZero
    @Column
    private float contractfee;

    /**
     * The contract length (int). Cannot be null.
     */
    @ApiModelProperty(name = "Contract Length",
            value = "Contract length in days.",
            required = true,
            example = "7")
    @Digits(integer = 999, fraction = 0)
    @PositiveOrZero
    @NotNull
    @Column(nullable = false)
    private int contractlength;

    private Date raw_contractstartdate;

    private Date raw_contractenddate;

    private String contractstartdate;

    private String contractenddate;

    // Relationships /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Many to One Relationship with Users
     */
    @ManyToOne
    @JoinColumn(name = "userid",
            nullable = false)
    @JsonIgnoreProperties(value = "contracts",
            allowSetters = true)
    private User rentee;

    /**
     * Many to One Relationship with Items
     */
    @ManyToOne
    @JoinColumn(name = "itemid",
            nullable = false)
    @JsonIgnoreProperties(value = "contracts",
            allowSetters = true)
    private Item item;

    // Relationships /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Default constructor used primarily by the JPA.
     */

    public Contract() { }

    public Contract(@Digits(integer = 999, fraction = 0) @PositiveOrZero @NotNull int contractlength) {
        this.contractlength = contractlength;
    }

    public long getContractid() { return contractid; }

    public void setContractid(long contractid) { this.contractid = contractid; }

    public boolean isRenteecomplete() { return renteecomplete; }

    public void setRenteecomplete(boolean renteecomplete) { this.renteecomplete = renteecomplete; }

    public boolean isLendercomplete() { return lendercomplete; }

    public void setLendercomplete(boolean lendercomplete) { this.lendercomplete = lendercomplete; }

    public boolean isActive() { return isActive; }

    public void setActive(boolean active) { isActive = active; }

    public float getContractfee() { return contractfee; }

    public void setContractfee(float contractfee) { this.contractfee = contractfee; }

    public int getContractlength() { return contractlength; }

    public void setContractlength(int contractlength) { this.contractlength = contractlength; }

    //// Time Stuff

    public boolean isRenteeaccept() { return renteeaccept; }

    public void setRenteeaccept(boolean renteeaccept) { this.renteeaccept = renteeaccept; }

    public boolean isLenderaccept() { return lenderaccept; }

    public void setLenderaccept(boolean lenderaccept) { this.lenderaccept = lenderaccept; }

    public Date getRaw_contractstartdate() { return raw_contractstartdate; }

    public void setRaw_contractstartdate(Date raw_contractstartdate) { this.raw_contractstartdate = raw_contractstartdate; }

    public Date getRaw_contractenddate() { return raw_contractenddate; }

    public void setRaw_contractenddate(Date raw_contractenddate) { this.raw_contractenddate = raw_contractenddate; }

    public String getContractstartdate() { return contractstartdate; }

    public void setContractstartdate(String contractstartdate) { this.contractstartdate = contractstartdate; }

    public String getContractenddate() { return contractenddate; }

    public void setContractenddate(String contractenddate) { this.contractenddate = contractenddate; }


    // Relationships /////////////////////////////////////////////////////////////////////////////////////////////////

    public User getRentee() { return rentee; }

    public void setRentee(User rentee) { this.rentee = rentee; }

    public Item getItem() { return item; }

    public void setItem(Item item) { this.item = item; }

    // Relationships /////////////////////////////////////////////////////////////////////////////////////////////////



}
