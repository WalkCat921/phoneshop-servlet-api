<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="recentlyView" required="true" type="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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