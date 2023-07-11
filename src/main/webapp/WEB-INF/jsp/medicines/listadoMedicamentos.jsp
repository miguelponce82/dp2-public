<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="medicines">
    <h2>Medicines</h2>

    <table id="medicinesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th style="width: 200px;">Description</th>
            <th style="width: 120px">Side Effects</th>
            <sec:authorize access="hasAuthority('admin')">
            <th style="width: 120px">Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${medicines}" var="medicine">
            <tr>
                <td>
					<c:out value="${medicine.name}"/>
                </td>
                <td>
                    <c:out value="${medicine.description}"/>
                </td>
                <td>
                    <c:out value="${medicine.sideEffects}"/>
                </td>
                 
                <sec:authorize access="hasAuthority('admin')">
                <td>
                   <spring:url value="/medicines/edit/{medicineId}" var="editUrl">
                        <spring:param name="medicineId" value="${medicine.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(editUrl)}">Edit</a>
                </td>
                <td>
                   <spring:url value="/medicines/delete/{medicineId}" var="medicineUrl">
                        <spring:param name="medicineId" value="${medicine.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(medicineUrl)}">Delete</a>
                </td>
                </sec:authorize>

                
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <sec:authorize access="hasAuthority('admin')">
    	<a href="/medicines/new" title="Add medicine" class="btn btn-default">Add medicine</a>
	</sec:authorize>
</petclinic:layout>
