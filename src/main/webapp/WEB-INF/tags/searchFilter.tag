<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="lable" required="true" %>
<%@ attribute name="value" required="true" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>

    <tr>
        <td>${lable}</td>
        <td>
            <c:set var="error" value="${errors[name]}"/>
            <input type="text" name="${name}"
                   value="${value}"/>
            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>
        </td>
    </tr>
