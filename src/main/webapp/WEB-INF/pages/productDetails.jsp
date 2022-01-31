<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
  <p id="product-description-text">
    ${product.description} details:
  </p>
  <table id="details">
    <tr>
      <td>Image</td>
      <td>
        <img src="${product.imageUrl}">
      </td>
    </tr>
    <tr>
      <td>Price</td>
      <td>
        <a href='#'
           onclick='javascript:window.open("${pageContext.servletContext.contextPath}/price-history/${product.code}",
              "_blank", "scrollbars=0,resizable=0,height=600,width=450,top=250,left=780");' title='Pop Up'>
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
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
      <td>
        ${product.stock}
      </td>
    </tr>
  </table>
</tags:master>