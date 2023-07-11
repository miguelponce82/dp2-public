<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="vets">
    <h2>Veterinarians</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Specialties</th>
            <sec:authorize access="hasAuthority('veterinarian')">
            	<th>Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${vets.vetList}" var="vet">
            <tr>
                <td>
                    <c:out value="${vet.firstName} ${vet.lastName}"/>
                </td>
                <td>
                    <c:forEach var="specialty" items="${vet.specialties}">
                        <c:out value="${specialty.name} "/>
                    </c:forEach>
                    <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
                </td>
                <sec:authorize access="hasAuthority('veterinarian')">
                	<td>
						<spring:url value="/vets/{vetId}/visits" var="visitsUrl">
		        			<spring:param name="vetId" value="${vet.id}"/>
		    			</spring:url>
		    			<a href="${fn:escapeXml(visitsUrl)}">Order visits</a>
	    			</td>
	    			 <td>
						<spring:url value="/vets/{vetId}/diagnosis" var="diagnosisUrl">
		        			<spring:param name="vetId" value="${vet.id}"/>
		    			</spring:url>
		    			<a href="${fn:escapeXml(diagnosisUrl)}">List Diagnosis</a>
	    			</td>
    			</sec:authorize>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <sec:authorize access="hasAnyAuthority('veterinarian','admin')">
    	<a href="/medicines" title="medicines" class="btn btn-default">Medicine list</a>
	</sec:authorize>
	
	<sec:authorize access="hasAuthority('admin')">
    	<a href="/diagnosis" title="all diagnosis" class="btn btn-default">All Diagnosis list</a>
	</sec:authorize>

    <table class="table-buttons">
        <tr>
            <td>
                <a href="<spring:url value="/vets.xml" htmlEscape="true" />">View as XML</a>
            </td>            
        </tr>
    </table>
</petclinic:layout>
