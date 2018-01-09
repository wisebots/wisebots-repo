<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 

<html>
<head>
<title>Wisebots Statistics</title>
<c:if test='${botHistoryList == null}'>
<meta http-equiv="refresh" content="5">
</c:if>

<c:forEach items="${botHistoryList}" var="botHistory">
<meta http-equiv="refresh" content="${botHistory.timerefresh}">
</c:forEach>
</head>
<h2>Tabela com evolução da fase de Testes</h2><br/>
<table cellpadding="4" cellspacing="0" style="border-collapse: collapse;">
<tr>
<td width="100" align="center">METHOD</td><td width="80" align="center">EPOCHS</td><td width="80" align="center">TIME</td><td width="80" align="center">QUALITY</td><td width="80" align="center">VICTORY</td><td width="80" align="center">DRAW</td>
</tr>
		<c:forEach items="${botHistoryList}" var="botHistory">
		<tr>
		<td width="100" align="center" style="border:1px solid;">${botHistory.method}</td><td width="80" align="center" style="border:1px solid;">${botHistory.epochs}</td><td width="80" align="center" style="border:1px solid;">${botHistory.time} secs</td><td width="80" align="center" style="border:1px solid;">${botHistory.quality} %</td><td width="80" align="center" style="border:1px solid;">${botHistory.victory} %</td><td width="80" align="center" style="border:1px solid;">${botHistory.draw} %</td>   
		</tr>
		</c:forEach>
</table>

</html>