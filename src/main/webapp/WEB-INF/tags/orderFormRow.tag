<%@ tag import="java.time.LocalDate" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>
<%@ attribute name="lable" required="true" %>
<%@ attribute name="type" required="false" %>
<%@ attribute name="paymentMethod" required="false" type="java.util.List" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>
<jsp:useBean id="currentDate" class="java.util.Date"/>

<c:if test="${type.equals('text')}">
    <tr>
        <td>${lable} <span style="color:red">*</span></td>
        <td>
            <c:set var="error" value="${errors[name]}"/>
            <input type="${type}" name="${name}" minlength="2"
                    <c:if test="${name.equals('deliveryAddress')}">
                        placeholder="st. Example 15-3-2, City"
                    </c:if>
                   value="${not empty errors ? param[name] : order[name]}"/>
            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>
        </td>
    </tr>
</c:if>
<c:if test="${type.equals('date')}">
    <tr>
        <td>${lable} <span style="color:red">*</span></td>
        <td>
            <c:set var="error" value="${errors[name]}"/>
            <input type="date"
                   min="<fmt:formatDate value="${currentDate}" pattern='yyyy-MM-dd' />"
                   max="<%=LocalDate.now().plusWeeks(2)%>"
                   name="${name}"
                   value="${not empty errors ? param[name] : order[name]}"/>
            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>
        </td>
    </tr>
</c:if>
<c:if test="${type.equals('tel')}">
    <tr>
        <td>${lable} <span style="color:red">*</span></td>
        <td>
            <c:set var="error" value="${errors[name]}"/>
            <input type="tel"
                   pattern="375[0-9]{9}"
                   placeholder="375xxxxxxxxx"
                   name="${name}"
                   value="${not empty errors ? param[name] : order[name]}"/>
            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>
        </td>
    </tr>
</c:if>
<c:if test="${name.equals('paymentMethod')}">
    <tr>
        <td>${lable} <span style="color:red">*</span></td>
        <td>
            <select name="${name}">
                <c:forEach var="paymentM" items="${paymentMethod}">
                    <option>${paymentM}</option>
                </c:forEach>
            </select>
            <c:set var="error" value="${errors[name]}"/>
            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>
        </td>
    </tr>
</c:if>