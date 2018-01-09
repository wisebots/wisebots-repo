
<!DOCTYPE html> 
<html> 
  <head> 
    <title>TICTACTOE WISEBOT SAMPLE</title>
    <meta charset="utf-8"> 
	<script src="http://ajax.googleapis.com/ajax/libs/prototype/1.6.0.2/prototype.js" type="text/javascript"></script>
	<script src="/wisebots-game-service/tictactoe/tictactoe3.js?tok=5qweqwe4" type="text/javascript"></script>
    <script type="text/javascript">
	document.observe("dom:loaded", novo);
	function novo() { 
		document.getElementById('robot').value = ${robot.id};
		document.getElementById('player').value = ${robot.player};

		document.getElementById("history").innerHTML = "";
		tabuleiro = new Tabuleiro('velha');
	}
	function getQueryParameter ( parameterName ) {
		  var queryString = window.location.search.substring(1);
		  
		  var parameterName = parameterName + "=";
		  if ( queryString.length > 0 ) {
		    begin = queryString.indexOf ( parameterName );
		    if ( begin != -1 ) {
		      begin += parameterName.length;
		      end = queryString.indexOf ( "&" , begin );
		        if ( end == -1 ) {
		        end = queryString.length;
		      }
		      
		      return unescape ( queryString.substring ( begin, end ) );
		    }
		  }

		  return "0";
		}
	</script> 
 
  </head> 
  <body> 
    <canvas id="velha">Canvas nao habilitado no browser.</canvas> 
	<br/>
	<form name="game">
	<input type="hidden" name="robot" id="robot" value="0">
	<input type="hidden" name="player" id="player" value="1">

	<br/><br/>
	<input type="button" value="Novo" onclick="novo();"/>
	<input type="button" value="Restart" onclick="javascript:location.reload(true)"/>
	</form>
	<br/>
<br/>
  Game History: <br/>
  <div id="history"></div>
  </body> 
</html>