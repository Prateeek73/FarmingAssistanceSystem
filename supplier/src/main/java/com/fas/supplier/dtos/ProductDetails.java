package com.fas.supplier.dtos;

import java.util.Objects;

public class ProductDetails {

    private Long id;
    private String name;
    private Long quantity;
    private Double sellingPrice;
    private Double buyingPrice;

    private Long farmerId;
    private String farmerFirstName;
    private String farmerLastName;
    private Long farmerPincode;
    private String farmerPhnNumber;

    private Long supplierId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerFirstName() {
        return farmerFirstName;
    }

    public void setFarmerFirstName(String farmerFirstName) {
        this.farmerFirstName = farmerFirstName;
    }

    public String getFarmerLastName() {
        return farmerLastName;
    }

    public void setFarmerLastName(String farmerLastName) {
        this.farmerLastName = farmerLastName;
    }

    public Long getFarmerPincode() {
        return farmerPincode;
    }

    public void setFarmerPincode(Long farmerPincode) {
        this.farmerPincode = farmerPincode;
    }

    public String getFarmerPhnNumber() {
        return farmerPhnNumber;
    }

    public void setFarmerPhnNumber(String farmerPhnNumber) {
        this.farmerPhnNumber = farmerPhnNumber;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

}
