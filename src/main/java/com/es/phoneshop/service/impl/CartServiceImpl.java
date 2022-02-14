package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.product.ArrayListProductDao;
import com.es.phoneshop.dao.product.ProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import lombok.NonNull;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public void add(@NonNull Cart cart, String productCode, int quantity) throws OutOfStockException, IllegalArgumentException, NullPointerException {
        synchronized (LOCK) {
            Product product = productDao.getProduct(productCode);
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be more than 0");
            } else if (product.getStock() < quantity) {
                throw new OutOfStockException(product.getStock());
            } else {
                try {
                    CartItem cartItemWithSameCode = getCartItemByProductCode(cart, productCode);
                    int index = cart.getItemList().indexOf(cartItemWithSameCode);
                    if (cartItemWithSameCode.getQuantity() + quantity > product.getStock()) {
                        throw new OutOfStockException(product.getStock());
                    } else {
                        cartItemWithSameCode.setQuantity(cartItemWithSameCode.getQuantity() + quantity);
                        cart.getItemList().set(index, cartItemWithSameCode);
                    }
                } catch (NoSuchElementException exception) {
                    cart.getItemList().add(new CartItem(product, quantity));
                }
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void update(@NonNull Cart cart, String productCode, int quantity) throws OutOfStockException, IllegalArgumentException, NullPointerException {
        synchronized (LOCK) {
            Product product = productDao.getProduct(productCode);
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be more Then 0");
            } else if (product.getStock() < quantity) {
                throw new OutOfStockException(product.getStock());
            } else {
                CartItem cartItemWithSameCode = getCartItemByProductCode(cart, productCode);
                int index = cart.getItemList().indexOf(cartItemWithSameCode);
                if (quantity > product.getStock()) {
                    throw new OutOfStockException(product.getStock());
                } else {
                    cartItemWithSameCode.setQuantity(quantity);
                    cart.getItemList().set(index, cartItemWithSameCode);
                }
            }
            recalculateCart(cart);
        }
    }

    @Override
    public void delete(@NonNull Cart cart, String productCode) throws NullPointerException {
        synchronized (LOCK) {
            cart.getItemList().removeIf(item -> productCode.equals(item.getProduct().getCode()));
            recalculateCart(cart);
        }
    }

    private CartItem getCartItemByProductCode(@NonNull Cart cart, String productCode) throws NullPointerException {
        return cart.getItemList().stream()
                .filter(cartItem -> cartItem.getProduct().getCode().equals(productCode))
                .findAny()
                .get();
    }

    private void recalculateCart(@NonNull Cart cart) throws NullPointerException {
        cart.setTotalQuantity(cart.getItemList().stream()
                .map(CartItem::getQuantity)
                .collect(Collectors.summingInt(q -> q.intValue()))
        );
        cart.setTotalCost(cart.getItemList().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }
}