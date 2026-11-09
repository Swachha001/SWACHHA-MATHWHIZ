<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
  <h2>Top 10 Leaderboard</h2>
  <table>
    <tr><th>#</th><th>Player</th><th>Score</th><th>Difficulty</th><th>Date/Time</th></tr>
    <c:forEach var="s" items="${scores}" varStatus="st">
      <tr>
        <td>${st.index + 1}</td>
        <td><c:out value="${s.playerName}"/></td>
        <td>${s.score}</td>
        <td><c:out value="${s.difficulty}"/></td>
        <td><fmt:formatDate value="${s.playedAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
      </tr>
    </c:forEach>
    <c:if test="${empty scores}"><tr><td colspan="5">No scores yet.</td></tr></c:if>
  </table>
  <a class="btn" href="${pageContext.request.contextPath}/index.jsp">Play Game</a>
</div>
</body>
</html>
