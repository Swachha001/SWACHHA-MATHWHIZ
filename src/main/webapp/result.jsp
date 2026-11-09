<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${initParam.appTitle}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="card">
  <h1><c:out value="${initParam.appTitle}"/></h1>
  <p class="sub">Player: <b><c:out value="${playerName}"/></b></p>
  <div class="score">Final Score: ${score} / 10</div>
  <table>
    <tr><th>Q</th><th>Your Answer</th><th>Correct</th><th>Result</th></tr>
    <c:forEach var="q" items="${questions}" varStatus="st">
      <tr>
        <td>${st.index + 1}</td>
        <td><c:out value="${given[st.index]}"/></td>
        <td>${expected[st.index]}</td>
        <td class="${correct[st.index] ? 'ok' : 'bad'}">${correct[st.index] ? '+' : '0'}${correct[st.index] ? q.marks : ''}</td>
      </tr>
    </c:forEach>
  </table>
  <a class="btn" href="${pageContext.request.contextPath}/index.jsp">Play Again</a>
  <a class="link" href="${pageContext.request.contextPath}/leaderboard">View Leaderboard</a>
</div>
</body>
</html>
