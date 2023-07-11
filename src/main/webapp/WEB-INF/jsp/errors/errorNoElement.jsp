<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="ERROR">
    <h2>THERE AREN'T ANY ELEMENT TO LIST</h2>
<!--     <label>Return <input action="action" type="button" onclick="history.go(-1);"></label>
 -->      <button class="btn btn-default" type="submit"  onclick="history.go(-1);">Return</button>
    

</petclinic:layout>
