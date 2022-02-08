<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <p id="product-description-text">
        Your Cart
    </p>
    <c:if test="${empty cart.itemList}">
        <p id="product-description-text">
            Your cart empty!<a href="${pageContext.servletContext.contextPath}">Add some products?</a>
        </p>
    </c:if>
    <c:if test="${not empty cart.itemList}">
        <table id="details">
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Quantity</td>
        </tr>
        </thead>
        <c:forEach var="item" items="${cart.itemList}">
            <tr>
                <td><img src="${item.product.imageUrl}" alt="phone_img"></td>
                <td>${item.product.description}</td>
                <td>${item.quantity}</td>
            </tr>
        </c:forEach>

    </c:if>
    </table>
</tags:master>