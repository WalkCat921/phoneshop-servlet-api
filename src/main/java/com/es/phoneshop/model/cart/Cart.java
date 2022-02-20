package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.entity.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class Cart extends Entity implements Serializable {
    private List<CartItem> itemList;
    private int totalQuantity;
    private BigDecimal totalCost;
    private Currency currency = Currency.getInstance("USD");

    public Cart() {
        this.itemList = new ArrayList<>();
    }

    public List<CartItem> getItemList() {
        return itemList;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setItemList(List<CartItem> itemList) {
        this.itemList = itemList;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Cart[" + itemList + ']';
    }
}