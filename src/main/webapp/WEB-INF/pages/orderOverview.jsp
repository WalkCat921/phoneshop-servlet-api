<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order overview">
    <p id="product-description-text">
        Order overview
    </p>
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
        <tags:totalRows order="${order}"/>
    </table>
    <h1 id="product-description-text">Your details:</h1>
    <table class="details">
        <tags:orderOverviewRow name="firstName" order="${order}" lable="First Name"/>
        <tags:orderOverviewRow name="lastName" order="${order}" lable="Last Name"/>
        <tags:orderOverviewRow name="phone" order="${order}" lable="Phone"/>
        <tags:orderOverviewRow name="deliveryDate" order="${order}" lable="Delivery date"/>
        <tags:orderOverviewRow name="deliveryAddress" order="${order}" lable="Delivery address"/>
        <tags:orderOverviewRow name="paymentMethod" order="${order}" lable="Payment method"/>
    </table>
    <a href="${pageContext.servletContext.contextPath}"><button>Back to  product list</button></a>
</tags:master>