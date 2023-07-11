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
         	<th style="width: 100px;">Pet</th>
            <th style="width: 200px;">Start date</th>
            <th style="width: 200px;">End date</th>
        </tr>
        </thead>
        <tbody>
        <tr>
        <c:forEach items="${pet}" var="pet">
	        <c:forEach items="${pet.diagnosis}" var="diagnosis">
	        	<c:if test="${diagnosis.residence.dateStart <= currentDate && diagnosis.residence.dateEnd >= currentDate}">
	               	<tr>
	                    <td>
						<c:out value="${pet.name}"/>
	                </td>
	                <td>
						<c:out value="${diagnosis.residence.dateStart}"/>
	                </td>
	                <td>
	                    <c:out value="${diagnosis.residence.dateEnd}"/>
	                </td> 
	             </tr>
	            </c:if>	            
	         </c:forEach>       
         </c:forEach>        
 
        </tbody>
    </table>
</petclinic:layout>
