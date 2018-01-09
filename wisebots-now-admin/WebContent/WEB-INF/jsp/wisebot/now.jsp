<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 

<html>
<script type="text/javascript">

	function specified(){

		if(document.getElementById("botProfile.techopponent").value == "LEARNER"){
			document.getElementById('oppo').style.display = 'block';
		}
		else{
			document.getElementById('oppo').style.display = 'none';
		}
	}
</script>
</head>
	<body>
		<a href="javascript:history.back();"><img src="images/logo_prot_wisebots.png" width="350" height="80" /></a>
		
		<br/><label><b>A Machine Learning Game Framework</b></label>
		<br/><label><b>Training-Testing-Validation Platform</b></label><br/>
		<c:forEach var="error" items="${errors}">
		   <span style="color:red"> ${error.category} - ${error.message}</span><br />
		</c:forEach>
		<br/>

		<form name="now" action="/wisebots-now-admin/now/create" method="post">
			<fieldset>
			<div id="bot" style="font-family: monospace;">
			Load Bot
			<select id="botProfile.ebot" name="botProfile.ebot" onChange="document.now.submit();">  
			<option value="0">NONE</option>
            <c:forEach var="botProfile" items="${botProfileList}">  
                <option value="${botProfile.id}">${botProfile.name}</option>  
            </c:forEach>  
   		    </select><br/>
   		    </div>
   		    </fieldset>
			<fieldset>
			<div id="botprof" style="font-family: monospace;">
			Create Bot<br/>
			Name: <input type="text" name="botProfile.name" value="${botProfile.name}" /><br/>
			Description:<input type="text" name="botProfile.description" value="${botProfile.description}" /><br/>
			Game:
			<select name="botProfile.game" onchange="changeOpponent();">
				<option value="TICTACTOE">TICTACTOE</option>
				<option value="CONNECTFOUR">CONNECTFOUR</option>
				<option value="CARDPOINTS">CARDPOINTS</option>
			</select><br/>
			Player:
			<select name="botProfile.player">
				<option value="1">Player 1</option>
				<option value="2">Player 2</option>
			</select><br/>
			Method:
			<select name="botProfile.method">
				<option value="QLPESSIMISTIC">QLPESSIMISTIC</option>
				<option value="QLEARNING">QLEARNING</option>
				<option value="SPECIALIST">SPECIALIST</option>
				<option value="RANDOM">RANDOM</option>
			</select><br/>
			Opponent Type:
			<select id="botProfile.techopponent" name="botProfile.techopponent" onchange="specified();">
				<option value="BOTHLEARNER">BOTHLEARNER</option>
				<option value="SPECIALIST">SPECIALIST</option>
				<option value="LEARNER">LEARNER</option>
				<option value="RANDOM">RANDOM</option>
			</select><br/>
			Exploration:
			<select name="botProfile.exploration">
				<option value="MINMAX">MINMAX</option>
				<option value="RANDOM">RANDOM</option>
			</select><br/>
			Evaluation:
			<select name="botProfile.evaluation">
				<option value="QVALUE">QVALUE</option>
				<option value="TERMINAL">TERMINAL</option>
			</select><br/>
			</div>
			<div id="oppo" style="display:none ">
			<div id="o" style="font-family: monospace;">
			Opponent:
			<select id="botProfile.opponent" name="botProfile.opponent" onChange="getdivisao()" >  
			    <option value="0">None</option>
            <c:forEach var="botProfile" items="${botProfileList}">  
                <option value="${botProfile.id}">${botProfile.name}</option>  
            </c:forEach>  
   		    </select><br/>
   		    </div>
   		    </div>
			<div id="botprof" style="font-family: monospace;">
			Training Exploring:<input type="text" name="botProfile.trainingExploring" value="${botProfile.trainingExploring}" /><br/>
			Training Pessimist:<input type="text" name="botProfile.trainingPessimist" value="${botProfile.trainingPessimist}" /><br/>
			<!-- Training Greedy:<input type="text" name="botProfile.trainingGreedy" value="${botProfile.trainingGreedy}" /><br/>  -->
			<!-- Block size:<input type="text" name="botProfile.block" value="${botProfile.bock}" /><br/>  -->
			Alfa:<input type="text" name="botProfile.alfa" value="0.7"/><br/>
			Gama:<input type="text" name="botProfile.gama" value="0.7"/><br/>
			Epsilon:<input type="text" name="botProfile.epsilon" value="0.3"/><br/>
			Delta:<input type="text" name="botProfile.delta" value="1.0"/><br/>
			Quality Required:
			<select name="botProfile.quality">
				<option value="100">100</option><option value="99">99</option><option value="98">98</option><option value="97">97</option><option value="96">96</option><option value="95">95</option><option value="94">94</option><option value="93">93</option><option value="92">92</option><option value="91">91</option><option value="90">90</option><option value="89">89</option><option value="88">88</option><option value="87">87</option><option value="86">86</option><option value="85">85</option><option value="84">84</option><option value="83">83</option><option value="82">82</option><option value="81">81</option><option value="80">80</option>
			</select>
			Level:
			<select name="botProfile.level">
				<option value="1">1</option><option value="2">2</option><option value="3" selected="selected">3</option><option value="4">4</option><option value="5">5</option><option value="6">6</option><option value="7">7</option><option value="8">8</option><option value="9">9</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option>
			</select>
			Cache type:<select name="botProfile.cache">
				<option value="0">SimpleHashCache</option>
				<option value="3">SQLiteCache</option>
				<option value="4">MembaseCache</option>
				<option value="2">LuceneCache</option>
				<option value="1">ExpireCache</option>
			</select><br/>
			</div>
			</fieldset>
			<input type="submit" value="Create" /><br/>
			</form>
	</body>
</html>

