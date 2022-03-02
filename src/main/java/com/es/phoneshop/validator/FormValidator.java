package com.es.phoneshop.validator;

import com.es.phoneshop.model.order.PaymentMethod;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class FormValidator {

    private static final String FIRST_NAME_PARAM = "firstName";
    private static final String LAST_NAME_PARAM = "lastName";
    private static final String PHONE_PARAM = "phone";
    private static final String DELIVERY_DATE_PARAM = "deliveryDate";
    private static final String DELIVERY_ADDRESS_PARAM = "deliveryAddress";
    private static final String PAYMENT_METHOD_PARAM = "paymentMethod";
    private static final String MESSAGE_REQUIRED_VALUE = "Value is required";
    private static final String MESSAGE_INCORRECT_VALUE = "Value is incorrect";
    private static final String MIN_PRICE_PARAM = "minPrice";
    private static final String MAX_PRICE_PARAM = "maxPrice";
    private static final String MIN_STOCK_PARAM = "minStock";
    private static final String REGEX_PHONE = "375[0-9]{9}";
    private static final String REGEX_NAME = "[A-Z][a-z]*";
    private static final String REGEX_ADDRESS = "([a-z]{2})+(. )([A-Z][a-z]*)+( )+([0-9]{1,4})(-[0-9]{0,3}|)-[0-9]{1,4}(,| |  |, )[A-Z][a-z]*";

    public static <T> void validateOrderForm(String value, String parameter, Map<String, String> errors,
                                             Consumer<T> consumer) {
        if (value == null || value.isEmpty()) {
            errors.put(parameter, MESSAGE_REQUIRED_VALUE);
        } else {
            switch (parameter) {
                case PHONE_PARAM:
                    if (!StringUtils.isNumeric(value) && !isPhoneCorrect(value)) {
                        errors.put(parameter, MESSAGE_INCORRECT_VALUE);
                    }
                    break;
                case FIRST_NAME_PARAM:
                case LAST_NAME_PARAM:
                    if (!isNameStringCorrect(value)) {
                        errors.put(parameter, MESSAGE_INCORRECT_VALUE);
                    }
                    break;
                case DELIVERY_DATE_PARAM:
                    LocalDate currentDate = LocalDate.now();
                    LocalDate dateInTwoWeeks = LocalDate.now().plusWeeks(2);
                    LocalDate date = LocalDate.parse(value);
                    if (date.isBefore(currentDate) || date.isAfter(dateInTwoWeeks)) {
                        errors.put(parameter, MESSAGE_INCORRECT_VALUE);
                        consumer.accept((T) date);
                        return;
                    } else {
                        consumer.accept((T) date);
                        return;
                    }
                case DELIVERY_ADDRESS_PARAM:
                    if (!isAddressCorrect(value)) {
                        errors.put(parameter, MESSAGE_INCORRECT_VALUE);
                    }
                    break;
                case PAYMENT_METHOD_PARAM:
                    if (!isPaymentMethodCorrect(value)) {
                        errors.put(parameter, MESSAGE_INCORRECT_VALUE);
                        return;
                    } else {
                        consumer.accept((T) PaymentMethod.valueOf(value));
                        return;
                    }
            }
            consumer.accept((T) value);
        }
    }

    public static <T> void validateSearchForm(String value, String parameter, Map<String, String> errors) {
        switch (parameter) {
            case MIN_PRICE_PARAM:
            case MAX_PRICE_PARAM:
            case MIN_STOCK_PARAM:
                if (!StringUtils.isNumeric(value) && !value.isEmpty()) {
                    errors.put(parameter, MESSAGE_INCORRECT_VALUE);
                }
                break;
        }
    }

    private static boolean isPaymentMethodCorrect(String payment) {
        try {
            return PaymentMethod.valueOf(payment.toUpperCase(Locale.ROOT)).equals(PaymentMethod.CASH)
                    || PaymentMethod.valueOf(payment.toUpperCase(Locale.ROOT)).equals(PaymentMethod.CREDIT_CARD);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isPhoneCorrect(String phoneNumber) {
        return phoneNumber.matches(REGEX_PHONE);
    }

    private static boolean isNameStringCorrect(String nameString) {
        return nameString.matches(REGEX_NAME);
    }

    private static boolean isAddressCorrect(String addressString) {
        return addressString.matches(REGEX_ADDRESS);
    }
}