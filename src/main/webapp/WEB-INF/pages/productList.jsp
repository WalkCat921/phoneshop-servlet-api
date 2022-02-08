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
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc" cursor="&uArr;"/>
                <tags:sortLink sort="price" order="desc" cursor="&dArr;"/>
            </td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.code}">
                            ${product.description}
                    </a>
                </td>
                <td class="price">
                    <a href='#'
                       onclick='javascript:window.open("${pageContext.servletContext.contextPath}/price-history/${product.code}",
              "_blank", "scrollbars=0,resizable=0,height=600,width=450,top=250,left=780");' title='Pop Up'>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <h1>Recently viewed products:</h1>
    <table>
        <tr id="recently-viewed-table">
            <c:forEach var="productViewed" items="${recentlyView}">
                <td>
                    <img class="product-tile" src="${productViewed.imageUrl}" alt="product_img">
                    <br>
                    <a href="${pageContext.servletContext.contextPath}/products/${productViewed.code}">
                            ${productViewed.description}
                    </a>
                    <br>
                    <fmt:formatNumber value="${productViewed.price}" type="currency"
                                      currencySymbol="${productViewed.currency.symbol}"/>
                </td>
            </c:forEach>
        </tr>
    </table>
</tags:master>