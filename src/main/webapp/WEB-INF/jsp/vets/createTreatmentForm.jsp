<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="vets">
        <jsp:attribute name="customScript">

            <script>
            $(function () {
                $("#startDate").datepicker({dateFormat: 'yy/mm/dd'});
                $("#endDate").datepicker({dateFormat: 'yy/mm/dd'});

            });
        </script>
        </jsp:attribute>
        <jsp:body>
        <h2><c:if test="${treatment['new']}">New </c:if>Treatment</h2>

        <b>Diagnosis</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Description</th>
                <th>Status</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${diagnosis.description}"/></td>
                <td><c:out value="${diagnosis.status}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="treatment" class="form-horizontal">
            <input type="hidden" name="id" value="${treatment.id}"/>
           	<input type="hidden" name="diagnosisId" value="${diagnosis.id}"/>
            <div class="form-group has-feedback">
            
                <petclinic:inputField label="Recomendations" name="recomendations" />
                <petclinic:inputField label="Start date" name="startDate" />
                <petclinic:inputField label="Finish date" name="endDate" />
                <div class="control-group">
                    <petclinic:selectField name="medicine" label="Medicine " names="${medicine}" size="5"/>
                </div>
           	</div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    
                    <button class="btn btn-default" type="submit"> Add Treatment</button>
                </div>
            </div>
        </form:form>

        <br/>
    </jsp:body>

</petclinic:layout>
