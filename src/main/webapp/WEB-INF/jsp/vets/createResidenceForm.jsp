<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="vets">
    

        <jsp:attribute name="customScript">
            <script>
            $(function () {
                $("#dateStart").datepicker({dateFormat: 'yy/mm/dd'});
                $("#dateEnd").datepicker({dateFormat: 'yy/mm/dd'});

            });
        </script>
        </jsp:attribute>
         <jsp:body>
        <h2><c:if test="${residence['new']}">New </c:if>Residence</h2>

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

        <form:form modelAttribute="residence" class="form-horizontal">
           <input type="hidden" name="id" value="${residence.id}"/>
           <input type="hidden" name="diagnosisId" value="${diagnosis.id}"/>
            <div class="form-group has-feedback">
	                <petclinic:inputField label="Start Date" name="dateStart"/>
	                <petclinic:inputField label="Finnish Date" name="dateEnd"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    
                    <button class="btn btn-default" type="submit"> Add Residence</button>
                </div>
            </div>
        </form:form>

        <br/>
    </jsp:body>

</petclinic:layout>
