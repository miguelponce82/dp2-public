<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="residences">
    <h2>Residences</h2>

    <table id="residenceTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 200px;">Start date</th>
            <th style="width: 200px;">End date</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${residences}" var="residence">
            <tr>
                <td>
					<c:out value="${residence.dateStart}"/>
                </td>
                <td>
                    <c:out value="${residence.dateEnd}"/>
                </td>             
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
