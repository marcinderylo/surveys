<%@ tag body-content="scriptless" %>
<%@ attribute name="pageTitle" required="true" type="java.lang.String" %>

<html>
<head>
    <title>${pageTitle}</title>
</head>
<body>
<div>
    ${notice}
</div>
<jsp:doBody/>
</body>
</html>
