<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
    <p id="product-description-text">
        Checkout page
    </p>
    <c:if test="${not empty param.message}">
        <p id="product-description-text" class="success">
            ${param.message}
        </p>
    </c:if>
    <c:if test="${not empty errors}">
        <p class="error" id="product-description-text">
            Something went wrong!
        </p>
    </c:if>
        <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <table id="details">
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Quantity</td>
            <td>Price</td>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.itemList}" varStatus="status">
            <tr>
                <td><img src="${item.product.imageUrl}" alt="phone_img"></td>
                <td>${item.product.description}</td>
                <td>
                    <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                    ${item.quantity}
                </td>
                <td><a href='#'
                       onclick='javascript:window.open("${pageContext.servletContext.contextPath}/price-history/${item.product.code}",
              "_blank", "scrollbars=0,resizable=0,height=600,width=450,top=250,left=780");' title='Pop Up'>
                    <fmt:formatNumber value="${item.product.price * item.quantity}" type="currency"
                                      currencySymbol="${item.product.currency.symbol}"/>
                </a>
                </td>
            </tr>
        </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>Subtotal:</td>
                <td class="price"><fmt:formatNumber value="${order.subtotal}" type="currency"
                                      currencySymbol="${order.currency.symbol}"/></td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>Delivery cost:</td>
                <td class="price"><fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                      currencySymbol="${order.currency.symbol}"/></td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>Total cost:</td>
                <td class="price"><fmt:formatNumber value="${order.totalCost}" type="currency"
                                      currencySymbol="${order.currency.symbol}"/></td>
            </tr>
    </table>
            <h1 id="product-description-text">Your details:</h1>
            <table class="details">
                <tags:orderFormRow type="text" name="firstName" order="${order}" lable="First Name" errors="${errors}"/>
                <tags:orderFormRow type="text" name="lastName" order="${order}" lable="Last Name" errors="${errors}"/>
                <tags:orderFormRow type="tel" name="phone" order="${order}" lable="Phone" errors="${errors}"/>
                <tags:orderFormRow type="date" name="deliveryDate" order="${order}" lable="Delivery date" errors="${errors}"/>
                <tags:orderFormRow type="text" name="deliveryAddress" order="${order}" lable="Delivery address" errors="${errors}"/>
                <tags:orderFormRow name="paymentMethod" order="${order}" lable="Payment method" errors="${errors}" paymentMethod="${paymentMethod}"/>
            </table>
    <div class="button-update">
        <button>Place order</button>
    </div>
    </form>
    <form method="post" id="deleteCartItem"></form>
</tags:master>