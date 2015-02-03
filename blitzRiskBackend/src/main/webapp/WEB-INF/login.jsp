<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
  <form method="post" class="signin" action="j_spring_security_check">
    <label for="username">Username/e-mail</label>
    <input id="username" name="j_username" type="text" />
    <label for="password">Password</label>
    <input id="password" name="j_password" type="password" />
    <input name="commit" type="submit" value="Sign In" />
  </form>

</body>
</html>
