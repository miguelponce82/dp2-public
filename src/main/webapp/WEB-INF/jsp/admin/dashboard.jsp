	<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	

	
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->

<petclinic:layout pageName="admins">

<h2>Dashboard</h2>
    <sec:authorize access="hasAuthority('admin')">
    	<a class="btn btn-default" href='<spring:url value="admin/diagnosis" htmlEscape="true"/>'>All diagnosis</a>            	
	</sec:authorize>
	    <sec:authorize access="hasAuthority('admin')">
    	<a class="btn btn-default" href='<spring:url value="/residences" htmlEscape="true"/>'>Active residences</a>            	
	</sec:authorize>
	<sec:authorize access="hasAuthority('admin')">
    	<a class="btn btn-default" href='<spring:url value="vets/choose" htmlEscape="true"/>'>Visit history of our vets</a>            	
	</sec:authorize>
	<sec:authorize access="hasAuthority('admin')">
    	<a id="frequentPet" class="btn btn-default" href='<spring:url value="pets/frequentPet" htmlEscape="true"/>'>Most frequent treated type of pet</a>            	
	</sec:authorize>
	<br>
	<br>
	<br>
	<h2>Vet with most visits:</h2>
	<c:out value="${vetWithMostVisits}"></c:out>
</petclinic:layout>