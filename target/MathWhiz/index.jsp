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
  <p class="sub">4 questions &middot; 10 marks &middot; Difficulty: NORMAL</p>
  <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>
  <form method="post" action="${pageContext.request.contextPath}/game">
    <input type="hidden" name="action" value="start">

    <label for="playerName">Player Name</label>
    <input type="text" id="playerName" name="playerName" maxlength="100" required autofocus value="<c:out value='${param.playerName}'/>">

    <div class="q">
      <p><b>Q1 &ndash; Prime Number</b> (2 marks)</p>
      <label>Enter an integer</label>
      <input type="number" name="primeNum" required value="<c:out value='${param.primeNum}'/>">
    </div>

    <div class="q">
      <p><b>Q2 &ndash; Modulus</b> (2 marks)</p>
      <label>Dividend</label>
      <input type="number" name="dividend" required value="<c:out value='${param.dividend}'/>">
      <label>Divisor (not zero)</label>
      <input type="number" name="divisor" required value="<c:out value='${param.divisor}'/>">
    </div>

    <c:forEach var="i" begin="1" end="2">
      <div class="q">
        <p><b>Q${i + 2} &ndash; Trigonometry</b> (3 marks)</p>
        <label>Integer (e.g. 5 in 5sin30)</label>
        <input type="number" name="coef${i}" required>
        <label>Function</label>
        <select name="fn${i}">
          <option value="sin">sin</option>
          <option value="cos">cos</option>
          <option value="tan">tan</option>
        </select>
        <label>Angle (degrees)</label>
        <input type="number" name="angle${i}" required>
      </div>
    </c:forEach>

    <button type="submit">Start Game</button>
  </form>
  <a class="link" href="${pageContext.request.contextPath}/leaderboard">View Leaderboard</a>
</div>
</body>
</html>
