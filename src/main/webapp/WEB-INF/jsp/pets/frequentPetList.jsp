<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pets">
    <jsp:body>
        <h2>
            Most frequent type of pet that has come to the PetClinic with one or more visits:
        </h2>
        
              <c:out value="${frequentPet}"/>
         
    </jsp:body>
</petclinic:layout>
