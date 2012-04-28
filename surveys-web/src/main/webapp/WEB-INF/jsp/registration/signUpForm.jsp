<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<layout:application pageTitle="New User Registration">
    <c:if test="${not empty errorsCount}">
        validation failed with ${errorsCount} errors
    </c:if>

    <form:form>
        <form:errors path="*"/>
        <table>
            <tr>
                <td>Name:</td>
                <td><form:input path="name"/></td>
                <td><form:errors path="name" /></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><form:input path="email"/></td>
                <td><form:errors path="email" /></td>
            </tr>
            <tr>
                <td>Confirm email:</td>
                <td><form:input path="emailConfirmation"/></td>
                <td><form:errors path="emailConfirmation" /></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><form:input path="password"/></td>
                <td><form:errors path="password" /></td>
            </tr>
            <tr>
                <td>Confirm password:</td>
                <td><form:input path="passwordConfirmation"/></td>
                <td><form:errors path="passwordConfirmation" /></td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="Save Changes"/>
                </td>
            </tr>
        </table>
    </form:form>
</layout:application>