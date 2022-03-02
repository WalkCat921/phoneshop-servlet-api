<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>

<tags:master pageTitle="Search">
    <p id="product-description-text">
        Search page
    </p>
    <c:if test="${not empty errors}">
        <p class="error" id="product-description-text">
            Something went wrong!
        </p>
    </c:if>
    <c:if test="${empty errors}">
        <p class="success" id="product-description-text">
            Search completed!!!
        </p>
    </c:if>
    <h1 id="product-description-text">Your search filters:</h1>
    <form method="get">
    <table class="details">
        <tags:searchFilter name="productCode" value="${param.productCode}" lable="Product Code" errors="${errors}"/>
        <tags:searchFilter name="minPrice" value="${param.minPrice}" lable="Min price" errors="${errors}"/>
        <tags:searchFilter name="maxPrice" value="${param.maxPrice}" lable="Max price" errors="${errors}"/>
        <tags:searchFilter name="minStock" value="${param.minStock}" lable="Min stock" errors="${errors}"/>
    </table>
        <button>Search</button>
    </form>



        <table id="details">
        <thead>
        <tr>
            <td>Image</td>
            <td>Description</td>
            <td>Price</td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}" varStatus="status">
            <tr>
                <td><img src="${product.imageUrl}" alt="phone_img"></td>
                <td>${product.description}</td>
                <td><a href='#'
                       onclick='javascript:window.open("${pageContext.servletContext.contextPath}/price-history/${item.product.code}",
              "_blank", "scrollbars=0,resizable=0,height=600,width=450,top=250,left=780");' title='Pop Up'>
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </a>
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>