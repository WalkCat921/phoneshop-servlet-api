<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="priceHistory" type="com.es.phoneshop.model.price.PriceHistory" scope="request"/>
<tags:popup pageTitle="Price history ${product.description}">
    <p id="product-description-text">
        Price History of ${product.description}
    </p>
    <table style="width:100px;float:left">
        <thead>
        <tr>
            <td>Date</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="date" items="${priceHistory.dates}">
            <tr>
                <td>${date}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <table style="width:100px;float:left">
        <thead>
        <tr>
            <td>Price</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="price" items="${priceHistory.prices}">
            <tr>
                <td><fmt:formatNumber value="${price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</tags:popup>