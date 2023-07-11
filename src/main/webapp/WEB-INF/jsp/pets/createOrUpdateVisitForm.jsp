<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
                $('#time').timepicker({    
              		timeFormat: 'HH:mm',
              	    interval: '30',
              	    minTime: '9',
              	    maxTime: '21:00',
              	    defaultTime: '11',
              	    startTime: '09:00',
              	   	disableTimeRanges: ['14:00', '15:30'],
              	    dynamic: false,
              	    dropdown: true,
              	    scrollbar: true});
            });
        </script>
    </jsp:attribute>
    
    
    <jsp:body>
        <h2><c:if test="${visit['new']}">New </c:if>Visit</h2>

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
                <td><c:out value="${visit.pet.name}"/></td>
                <td><petclinic:localDate date="${visit.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${visit.pet.type.name}"/></td>
                <td><c:out value="${visit.pet.owner.firstName} ${visit.pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="visit" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Date" name="date"/>
                <petclinic:inputField label="Time" name="time"/>
                <div class="control-group">
                    <petclinic:selectField name="vet" label="Vet " names="${vets}" size="5"/>
                </div>
                <petclinic:inputField label="Description" name="description"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId" value="${visit.pet.id}"/>
                    <button class="btn btn-default" type="submit">Add Visit</button>
                </div>
            </div>
        </form:form>

        <br/>
        <b>Previous Visits</b>
        <table class="table table-striped">
            <tr>
                <th>Date</th>
                <th>Description</th>
                <th>Time</th>
                <th>Vet</th>
            </tr>
            <c:forEach var="visit" items="${visit.pet.visits}">
                <c:if test="${!visit['new']}">
                    <tr>
                        <td><petclinic:localDate date="${visit.date}" pattern="yyyy/MM/dd"/></td>
                        <td><c:out value="${visit.description}"/></td>
                        <td><c:out value="${visit.time}"/></td>
                        <td><c:out value="${visit.vet.firstName} ${visit.vet.lastName}" ></c:out>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </jsp:body>
</petclinic:layout>
