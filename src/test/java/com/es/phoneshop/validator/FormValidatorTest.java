package com.es.phoneshop.validator;

import com.es.phoneshop.model.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Or;

import javax.xml.stream.events.EntityReference;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class FormValidatorTest {

    private static final String FIRST_NAME_PARAM = "firstName";
    private static final String LAST_NAME_PARAM = "lastName";
    private static final String PHONE_PARAM = "phone";
    private static final String DELIVERY_DATE_PARAM = "deliveryDate";
    private static final String DELIVERY_ADDRESS_PARAM = "deliveryAddress";
    private static final String PAYMENT_METHOD_PARAM = "paymentMethod";
    private Map<String,String> errors;
    private Order order;

    @Before
    public void setup(){
        order = new Order();
        errors = new HashMap<>();
    }

    @Test
    public void testValidateFirstNameSuccess() {
        final String firstName = "Egor";

        FormValidator.validateOrderForm(firstName,FIRST_NAME_PARAM,errors,order::setFirstName);

        assertTrue(errors.isEmpty());
        assertNotNull(order.getFirstName());
    }

    @Test
    public void testValidateLastNameSuccess() {
        final String lastName = "Tapok";

        FormValidator.validateOrderForm(lastName,LAST_NAME_PARAM,errors,order::setLastName);

        assertTrue(errors.isEmpty());
        assertNotNull(order.getLastName());
    }

    @Test
    public void testValidatePhoneSuccess() {
        final String phone = "375293451232";

        FormValidator.validateOrderForm(phone,PHONE_PARAM,errors,order::setPhone);

        assertTrue(errors.isEmpty());
        assertNotNull(order.getPhone());
    }

    @Test
    public void testValidateDeliveryDateSuccess() {
        final String currentDate = LocalDate.now().toString();

        FormValidator.validateOrderForm(currentDate,DELIVERY_DATE_PARAM,errors,order::setDeliveryDate);

        assertTrue(errors.isEmpty());
        assertNotNull(order.getDeliveryDate());
    }

    @Test
    public void testValidateDeliveryAddressSuccess() {
        final String address = "st. Test 13-2, Test";

        FormValidator.validateOrderForm(address,DELIVERY_ADDRESS_PARAM,errors,order::setDeliveryAddress);

        assertTrue(errors.isEmpty());
        assertNotNull(order.getDeliveryAddress());
    }

    @Test
    public void testValidatePaymentMethodSuccess() {
        final String paymentMethod = "CASH";

        FormValidator.validateOrderForm(paymentMethod,PAYMENT_METHOD_PARAM,errors,order::setPaymentMethod);

        assertTrue(errors.isEmpty());
        assertNotNull(order.getPaymentMethod());
    }

    @Test
    public void testValidateNullAddErrorInMap() {
        FormValidator.validateOrderForm(null,PAYMENT_METHOD_PARAM,errors,order::setPaymentMethod);

        assertTrue(!errors.isEmpty());
    }

    @Test
    public void testValidateEmptyAddErrorInMap() {
        FormValidator.validateOrderForm("",PAYMENT_METHOD_PARAM,errors,order::setPaymentMethod);

        assertTrue(!errors.isEmpty());
    }

    @Test
    public void testValidateWrongFirstLastNameAddErrorInMap() {
        final String wrongFirstName = "sj=-ndiw";
        final String wrongLastName = "Zi92jd92";

        FormValidator.validateOrderForm(wrongFirstName,FIRST_NAME_PARAM,errors,order::setFirstName);
        FormValidator.validateOrderForm(wrongLastName,LAST_NAME_PARAM,errors,order::setLastName);

        assertTrue(!errors.isEmpty());
        assertTrue(errors.keySet().contains(FIRST_NAME_PARAM));
        assertTrue(errors.keySet().contains(LAST_NAME_PARAM));
    }

    @Test
    public void testValidateWrongPhoneAddErrorInMap() {
        final String wrongPhone = "462hgebw72gd";

        FormValidator.validateOrderForm(wrongPhone,PHONE_PARAM,errors,order::setPhone);

        assertTrue(!errors.isEmpty());
        assertTrue(errors.keySet().contains(PHONE_PARAM));
    }

    @Test
    public void testValidateWrongDeliveryDateAddErrorInMap() {
        final String wrongDate = LocalDate.now().plusWeeks(3).toString();

        FormValidator.validateOrderForm(wrongDate,DELIVERY_DATE_PARAM,errors,order::setDeliveryDate);

        assertTrue(!errors.isEmpty());
        assertTrue(errors.keySet().contains(DELIVERY_DATE_PARAM));
    }

    @Test
    public void testValidateWrongDeliveryAddressAddErrorInMap() {
        final String wrongAddress = "test 23 2 1";

        FormValidator.validateOrderForm(wrongAddress,DELIVERY_ADDRESS_PARAM,errors,order::setDeliveryAddress);

        assertTrue(!errors.isEmpty());
        assertTrue(errors.keySet().contains(DELIVERY_ADDRESS_PARAM));
    }

    @Test
    public void testValidateWrongPaymentMethodAddErrorInMap() {
        final String wrongPayment = "FREE";

        FormValidator.validateOrderForm(wrongPayment,PAYMENT_METHOD_PARAM,errors,order::setPaymentMethod);

        assertTrue(!errors.isEmpty());
        assertTrue(errors.keySet().contains(PAYMENT_METHOD_PARAM));
    }
}