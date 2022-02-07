package com.es.phoneshop.model.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> itemList;

    public Cart() {
        this.itemList = new ArrayList<>();
    }

    public List<CartItem> getItemList() {
        return itemList;
    }

    @Override
    public String toString() {
        return "Cart[" + itemList + ']';
    }
}