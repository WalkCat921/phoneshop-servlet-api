package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

public class CartServiceImpl implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = String.format("%s.cart", CartServiceImpl.class.getName());
    private static final Object LOCK = new Object();

    private static volatile CartService instance;

    private ProductDao productDao;

    private CartServiceImpl() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static CartService getInstance() {
        if (instance == null) {
            synchronized (CartService.class) {
                if (instance == null) {
                    instance = new CartServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpSession session) {
        synchronized (LOCK) {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                session.setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, String productCode, int quantity) throws OutOfStockException, IllegalArgumentException {
        synchronized (LOCK) {
            if (cart == null) {
                throw new NullPointerException("Cart is nulL");
            }
            Product product = productDao.getProduct(productCode);
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be more than 0");
            } else if (product.getStock() < quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            } else {
                try {
                    CartItem cartItemWithSameCode = cart.getItemList().stream()
                            .filter(cartItem -> cartItem.getProduct().getCode().equals(productCode))
                            .findAny()
                            .get();
                    int index = cart.getItemList().indexOf(cartItemWithSameCode);
                    if (cartItemWithSameCode.getQuantity() + quantity > product.getStock()) {
                        throw new OutOfStockException(product, quantity, product.getStock());
                    } else {
                        cartItemWithSameCode.setQuantity(cartItemWithSameCode.getQuantity() + quantity);
                        cart.getItemList().set(index, cartItemWithSameCode);
                    }
                } catch (NoSuchElementException exception) {
                    cart.getItemList().add(new CartItem(product, quantity));
                }
            }
        }
    }
}