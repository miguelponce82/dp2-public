<%@ page session="false" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medicines">
    <h2>
        New Medicine
    </h2>
    <form:form modelAttribute="medicine" class="form-horizontal" id="add-medicine-form">
        <div class="form-group has-feedback">
        	<petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Description" name="description"/>
            <petclinic:inputField label="Side Effects" name="sideEffects"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit">Add Medicine</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>