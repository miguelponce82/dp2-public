<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="vets">
    
    <jsp:body>
            <script>
            $(function () {
                $("#startDate").datepicker({dateFormat: 'yy/mm/dd'});
                $("#endDate").datepicker({dateFormat: 'yy/mm/dd'});

            });
        </script>
        <h2><c:if test="${diagnosis['new']}">New </c:if>Diagnosis</h2>

        <b>Pet</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Birth Date</th>
                <th>Type</th>
                <th>Owner</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${pet.name}"/></td>
                <td><petclinic:localDate date="${pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${pet.type.name}"/></td>
                <td><c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="diagnosis" class="form-horizontal">
        <input type="hidden" name="id" value="${diagnosis.id}"/>
           <input type="hidden" name="pet" value="${pet}"/>
            <div class="form-group has-feedback">
                <petclinic:inputField label="Description" name="description"/>
                       <div class="control-group">
                    <petclinic:selectField name="status" label="Status " names="${status2}" size="3"/>
                   <petclinic:selectField name="vet" label="Vet " names="${vet}" size="5"/>
            		   </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    
                    <button class="btn btn-default" type="submit"> Add Diagnosis</button>
                </div>
            </div>
        </form:form>

        <br/>
    </jsp:body>

</petclinic:layout>
