<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="visits">
    <h2>Visits</h2>

    <table id="visitsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Date</th>
            <th style="width: 200px;">Time</th>
            <th style="width: 120px">Pet</th>
            <th style="width: 120px">Vet</th>
            <th style="width: 120px">Description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${visits}" var="visit">
            <tr>
                <td>
					<c:out value="${visit.date}"/>
                </td>
                <td>
                    <c:out value="${visit.time}"/>
                </td>
                <td>
                    <c:out value="${visit.pet.name}"/>
                </td>
                <td>
                    <c:out value="${visit.vet.firstName} ${visit.vet.lastName}"/>
                </td>
                <td>
                    <c:out value="${visit.description}"/>
                </td>
                
                <sec:authorize access="hasAuthority('veterinarian')">
	                <td>
	                    <spring:url value="/vets/{vetId}/visits/{visitId}/diagnosis/new" var="newUrl">
	                    <spring:param name="visitId" value="${visit.id}"></spring:param>
	                    <spring:param name="vetId" value="${visit.vet.id}"></spring:param>
	                    <spring:param name="petId" value="${visit.pet.id}"></spring:param>
	                    <spring:param name="pet" value="${visit.pet}"></spring:param>
	                    </spring:url>	                    
	            		<a href="${fn:escapeXml(newUrl)}" class="btn btn-default">Add Diagnosis</a>
	            			                   	                    
	                </td>
                </sec:authorize>
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
