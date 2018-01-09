<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 

<html>
	<head>
    <title>LIST WISEBOTS</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://jquery-json.googlecode.com/files/jquery.json-2.2.min.js"></script>
    </head>
	<body>
		<h1>WISEBOTS NOW</h1>
		<span>List of Robots</span>
		<c:forEach items="${botProfileList}" var="botProfile">
		   <br />
		   <hr>
		   <span style="color:red"> [CODE:${botProfile.id}]. ${botProfile.name} - (${botProfile.description})</span><br />
		   <span style="color:red"> BOT QUALITY: ${botProfile.iquality}</span><br />
		   <span style="color:red"> CREATED AT: ${botGenesis.creationDate}</span><br />
		   <span style="color:red"> FINISHED AT: ${botProfile.botGenesis.finishedDate}</span><br />
		   <span style="color:red"> BRAIN CREATED AT: ${botProfile.botGenesis.finishedDate}</span><br />
		   <span>&nbsp;&nbsp;&nbsp;Game: ${botProfile.game}</span><br />
		   <span>&nbsp;&nbsp;&nbsp;Alfa: ${botProfile.alfa}</span><br />
		   <span>&nbsp;&nbsp;&nbsp;Gama: ${botProfile.gama}</span><br />
		   <span>&nbsp;&nbsp;&nbsp;Cache: ${botProfile.cache}</span><br />
		   <span>&nbsp;&nbsp;&nbsp;Block: ${botProfile.block}</span><br />
		   <span>&nbsp;&nbsp;&nbsp;Training Exploring:: ${botProfile.trainingExploring}</span><br />
		   <span>&nbsp;&nbsp;&nbsp;Training Pessimist: ${botProfile.trainingPessimist}</span><br />
		   <span>&nbsp;&nbsp;&nbsp;Quality Expected: ${botProfile.quality}</span><br />	
		   <hr>	   
		</c:forEach>
		<br/>
	</body>
</html>