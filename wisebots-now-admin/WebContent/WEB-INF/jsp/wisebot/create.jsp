<html>
<head>
    <title>CREATE NEW WISEBOT</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://jquery-json.googlecode.com/files/jquery.json-2.2.min.js"></script>
    <script type="text/javascript">
    
        jQuery(function($) {

            function processEvents(events) {
                if (events.length) {
                    $('#logs').append('<span style="color: blue;">[client] ' + events.length + ' events</span><br/>');
                } else {
                    $('#logs').append('<span style="color: red;">[client] no event</span><br/>');
                }
                for (var i in events) {
                    $('#logs').append('<span>[event] ' + events[i] + '</span><br/>');
                }
            }

            $('#check').click(function() {
                $('#logs').append('<span> Checking status ... </span><br/>');
                $.post('/wisebots-now-admin/now/ajax/check/${botProfile.id}/${botProfile.name}/${botProfile.game}/${botProfile.quality}', function(html) {
                    $('#logs').empty().append('<span>	' + html  + '</span>');
                    processEvents(data.events);
                });

            });
            
            $('#genesis').click(function() {
            	$('#logs').append('<br/><br/><span>&nbsp;&nbsp;&nbsp;&nbsp;Learning ... </span><br/>');
                $('#logs').append('<span> <img src="../images/ajax-loader.gif"/></span><br/><br/>');
                $.post('/wisebots-now-admin/now/ajax/genesis/${botProfile.id}/${botProfile.name}/${botProfile.game}/${botProfile.player}/${botProfile.method}/${botProfile.techopponent}/${botProfile.opponent}/${botProfile.trainingExploring}/${botProfile.trainingGreedy}/${botProfile.trainingPessimist}/${botProfile.alfa}/${botProfile.gama}/${botProfile.epsilon}/${botProfile.delta}/${botProfile.block}/${botProfile.cache}/${botProfile.quality}/${botProfile.exploration}/${botProfile.evaluation}/${botProfile.level}', function(html) {
                	$('#logs').empty().append('<span>	' + html  + '</span>');	
                    processEvents(data.events);
                });
            	parent.changeStatistics(this.form.botid.value,'${botProfile.game}','${botProfile.method}');
            	parent.changeGame(this.form.botid.value,'${botProfile.game}','${botProfile.method}');
            	parent.changePhaseTest(this.form.botid.value,'${botProfile.game}','${botProfile.method}');
                document.getElementById('divcreate').style.display = 'none';
            });

        });
    </script>
</head>
<body>
<a href="javascript:history.back();"><img src="../images/logo_prot_wisebots.png" width="350" height="80" /></a>
<br/><label><b>A Machine Learning Game Framework</b></label>
<br/><label><b>Training-Testing-Validation Platform</b></label><br/>
<span><b><h2>Bot Name: ${botProfile.name}</h2></b></span>&nbsp;<button id="genesis">Start</button>&nbsp;&nbsp;&nbsp;<button id="pause">Pause</button></div>&nbsp;&nbsp;&nbsp;<button id="recreate" onclick="parent.location.href='http://wisebots.servegame.com/wisebots-now-admin'">Recreate</button>
<br/>
<div id="logs" style="font-family: monospace;">
</div>
<div id="botgenesis" style="font-family: monospace;">
<br/><br/>
<span>Game: <b>${botProfile.game}</b>  /  Method: <b>${botProfile.method}</b></span> <br/>
<span>Technique:<b>${botProfile.techopponent}</b> </span> <br/>
<span>Player: <b>${botProfile.player}</b> / Opponent: <b>${botProfile.opponent}</b> </span> <br/>
<span>Alfa: <b>${botProfile.alfa}</b> / Gama: <b>${botProfile.gama}</b> / Epsilon: <b>${botProfile.epsilon}</b> / Delta: <b>${botProfile.delta}</b> </span> <br/>
<span>Statesizemax: <b>${botGenesis.statesize}</b> / Probsmax: <b>${botGenesis.probsmax}</b>  </span> <br/>
<span>TrainingQ-Learning: <b>${botProfile.trainingExploring}</b> / TrainingPessimistic: <b>${botProfile.trainingPessimist}</b> </span> <br/>
<span>Wisebot Complexity: <b>${botGenesis.complexity} gamenees *</b> </span> <br/>
<br/>
<br/>
<span>[${botProfile.id}-${botProfile.name}] Description: <b> ${botProfile.description}</b> </span> <br/>

</div>

<div id="botprofile" style="font-family: monospace;">
<form name="form">
<input type="hidden" name="botid" value="${botProfile.id}"/>
</form>
</div>
<div id="observation" style="font-family: monospace;"><b>* gamenees: </b>É uma terminologia criada pela Wisebots como tentativa de classificar matematicamente a dificuldade genérica de um jogo. (beta)</div>
</body>
</html> 

<script>
parent.changeStatsTraining(this.form.botid.value,'${botProfile.game}','${botProfile.method}');
//parent.changeStatsTesting(this.form.botid.value,'${botProfile.game}','${botProfile.method}');
parent.changeGame(this.form.botid.value,'${botProfile.game}','${botProfile.method}','${botProfile.player}');
parent.changePhaseTest(this.form.botid.value,'${botProfile.game}','${botProfile.method}','${botProfile.player}','${botProfile.opponent}');
parent.changeTableTraining(this.form.botid.value,'${botProfile.game}','${botProfile.method}');
//parent.changeTableTesting(this.form.botid.value,'${botProfile.game}','${botProfile.method}');
</script>
