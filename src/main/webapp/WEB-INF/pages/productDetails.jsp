<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <c:if test="${not empty param.message}">
        <p class="success" id="product-description-text">
                ${param.message}
        </p>
    </c:if>
    <c:if test="${not empty param.error}">
        <p class="error" id="product-description-text">
                ${param.error}
        </p>
    </c:if>
    <p id="product-description-text">
            ${product.description} details:
    </p>
    <form method="post">
        <table id="details">
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>Price</td>
                <td class="price">
                    <a href='#'
                       onclick='javascript:window.open("${pageContext.servletContext.contextPath}/price-history/${product.code}",
              "_blank", "scrollbars=0,resizable=0,height=600,width=450,top=250,left=780");' title='Pop Up'>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
            </tr>
            <tr>
                <td>Code</td>
                <td>
                        ${product.code}
                </td>
            </tr>
            <tr>
                <td>Stock</td>
                <td class="stock">
                        ${product.stock}
                </td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <input name="quantity" pattern="\d+" value="${not empty error ? param.quantity : 1}" class="quantity">
                    <br>
                    <br>
                    <button>Add to cart</button>
                    <c:if test="${not empty param.error}">
                        <p class="error">
                                ${param.error}
                        </p>
                    </c:if>
                </td>
            </tr>
        </table>
    </form>
    <tags:recentlyView recentlyView="${recentlyView}"/>
</tags:master>