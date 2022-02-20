<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>

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
