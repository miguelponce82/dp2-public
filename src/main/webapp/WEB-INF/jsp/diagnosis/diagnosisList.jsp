<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="Diagnosis">
    <h2>Diagnosis</h2>

    <table id="diagnosisTable" class="table table-striped">
        <thead>
        <tr>
            <th>Description</th>
            <th>Status</th>
            <th>Vet</th>
            <th>Recomendations</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Medicine</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${diagnosis}" var="diagnosis">
            <tr>
                <td>
                    <c:out value="${diagnosis.description}"/>
                </td>
                <td>
                    <c:out value="${diagnosis.status}"/>
                </td>
                <td>
                    <c:out value="${diagnosis.vet.firstName} ${diagnosis.vet.lastName}"/>
                </td>
                <td>
                    <c:out value="${diagnosis.treatment.recomendations}"/>
                </td>
                <td>
                    <c:out value="${diagnosis.treatment.startDate}"/>
                </td>
                <td>
                    <c:out value="${diagnosis.treatment.endDate}"/>
                </td>
                <td>
                    <c:out value="${diagnosis.treatment.medicine.name}"/>
                </td>
                <sec:authorize access="hasAnyAuthority('veterinarian','admin')">
                
	                <td>
	                	<c:if test="${diagnosis.treatment == null}">
							<spring:url value="/vets/{vetId}/diagnosis/{diagnosisId}/treatment/new" var="treatmentUrl">
				        	<spring:param name="diagnosisId" value="${diagnosis.id}"/>
				        	<spring:param name="vetId" value="${diagnosis.vet.id}"/>
				    		</spring:url>
				    		<a href="${fn:escapeXml(treatmentUrl)}">Add Treatment</a>	    
			    		</c:if>	
		    		</td>
                
	    		
	    		<td>
	    			<c:if test="${diagnosis.residence == null}">
						<spring:url value="/vets/{vetId}/diagnosis/{diagnosisId}/residence/new" var="residenceUrl">
			        	<spring:param name="diagnosisId" value="${diagnosis.id}"/>
			        	<spring:param name="vetId" value="${diagnosis.vet.id}"/>
			    		</spring:url>
			    		<a href="${fn:escapeXml(residenceUrl)}">Add Residence</a>
		    		</c:if>
	    		</td>
      			</sec:authorize>
<!--
                <td> 
                    <c:out value="${owner.user.username}"/> 
                </td>
                <td> 
                   <c:out value="${owner.user.password}"/> 
                </td> 
-->
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
