<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="recentlyView" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not empty error}">
        <p class="error">Error adding to cart</p>
    </c:if>
    <c:if test="${not empty param.message}">
            <p class="success">${param.message}</p>
    </c:if>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc" cursor="&uArr;"/>
                <tags:sortLink sort="description" order="desc" cursor="&dArr;"/>
            </td>
            <td>
                Quantity
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc" cursor="&uArr;"/>
                <tags:sortLink sort="price" order="desc" cursor="&dArr;"/>
            </td>
            <td>
                Add
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}" varStatus="status">
            <tr>
                <form method="post" action="${pageContext.servletContext.contextPath}/products" >
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.code}">
                            ${product.description}
                    </a>
                </td>
                <td>
                    <input class="quantity" type="text" name="quantity" pattern="\d+" value="${not empty error[product.code]  ? quantity[product.code] : 1}">
                    <c:if test="${not empty error}">
                        <p class="error">
                                ${error[product.code]}
                        </p>
                    </c:if>
                    <input type="hidden" name="productCode" value="${product.code}">
                    <input type="hidden" name="query" value="${param.query}">
                </td>
                <td class="price">
                    <a href='#'
                       onclick='javascript:window.open("${pageContext.servletContext.contextPath}/price-history/${product.code}",
              "_blank", "scrollbars=0,resizable=0,height=600,width=450,top=250,left=780");' title='Pop Up'>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td>
                    <button>Add to cart</button>
                </td>
                </form>
            </tr>
        </c:forEach>
    </table>
    <tags:recentlyView recentlyView="${recentlyView}"/>
</tags:master>