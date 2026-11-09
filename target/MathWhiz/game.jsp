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
  <form method="post" action="${pageContext.request.contextPath}/game">
    <input type="hidden" name="action" value="submit">
    <c:forEach var="q" items="${questions}" varStatus="st">
      <div class="q">
        <p><b>Q${st.index + 1}</b> (${q.marks} marks)</p>
        <c:choose>
          <c:when test="${q.type == 'PRIME'}">
            <p>Your number: ${q.base} &nbsp; Seed: <b>${q.seedText}</b> &nbsp; Final number: <b>${q.finalValue}</b></p>
            <p>Is <b>${q.finalValue}</b> prime?</p>
          </c:when>
          <c:when test="${q.type == 'MOD'}">
            <p>Dividend: ${q.base} &nbsp; Seed: <b>${q.seedText}</b> &nbsp; Final dividend: <b>${q.finalValue}</b> &nbsp; Divisor: <b>${q.divisor}</b></p>
            <p>Calculate <b>${q.finalValue} % ${q.divisor}</b></p>
          </c:when>
          <c:otherwise>
            <p>Your integer: ${q.base} &nbsp; Seed: <b>${q.seedText}</b> &nbsp; Final integer: <b>${q.finalValue}</b></p>
            <p>Calculate <b>${q.finalValue} ${q.fn}(${q.angle}&deg;)</b> (2 decimal places)</p>
          </c:otherwise>
        </c:choose>
        <c:forEach var="o" items="${q.options}">
          <label class="opt">
            <input type="radio" name="answer${st.index}" value="${o}" required>
            <c:choose>
              <c:when test="${q.type == 'PRIME'}">${o} for ${o == '1' ? 'Prime' : 'Not Prime'}</c:when>
              <c:otherwise>${o}</c:otherwise>
            </c:choose>
          </label>
        </c:forEach>
      </div>
    </c:forEach>
    <button type="submit">Submit Answers</button>
  </form>
</div>
</body>
</html>
