
<!doctype html>
<html><!-- #BeginTemplate "/Templates/Main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Four In A Line</title>
<style type="text/css">
	img {border: 0}
	.bluetext {font-size: 14px; font-family: "Comic Sans MS", Verdana; color: blue; font-weight: bold}
	.redtext {font-size: 14px; font-family: "Comic Sans MS", Verdana; color: red; font-weight: bold}
.blacktext { font-size: 14px; font-family: "Comic Sans MS", Verdana; color: #000000; font-weight: bold }
</style>
<script src="http://ajax.googleapis.com/ajax/libs/prototype/1.6.0.2/prototype.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript">
<!-- Begin
// Note when upgrading template, remember to add onload="AutoStart();" to BODY statement

var FIRSTIMAGE = 0;
var ROWS = 6;
var COLS = 7;
var WINVAL = 100000;
var theAnim = new Animation();
var moves = 0;
var gameActive = 0;
var isdropping = 0;
var RedNum=1;
var BlkNum=2;
var debugstr;
var whosFirst;
var matchMade = 0;
var lookForSrc;
var redScore = 0;
var bluScore = 0;
var colcount = new Array(COLS);

var board = new Array(ROWS);
for (var row = 0; row < ROWS; row++) {
	board[row] = new Array(COLS);
	for (var col=0; col < COLS; col++) {
		board[row][col] = 0;
	}
}

var linecount = 0; 
var linecoords = new Array(70);
fill_lines();
function fill_lines() {
	for(row = 0; row<ROWS; row++) {
		for(col = 0; col<COLS; col++) {
			for(rowchg = -1; rowchg <= 1; rowchg++) {
				for(colchg = -1; colchg <= 1; colchg++) {
					if(
						( 
						((rowchg == 1) && (colchg == 0)) ||
						((rowchg == 1) && (colchg == 1)) ||
						((rowchg == 0) && (colchg == 1)) ||
						((rowchg == -1) && (colchg == 1))
						) &&
						(inbounds( row + (1*rowchg), col + (1*colchg) )) &&
						(inbounds( row + (2*rowchg), col + (2*colchg) )) &&
						(inbounds( row + (3*rowchg), col + (3*colchg) )) 
					   ) {
						
						linecoords[linecount] = new Array(5);
						linecoords[linecount][0] = row;
						linecoords[linecount][1] = col;
						linecoords[linecount][2] = rowchg;
						linecoords[linecount][3] = colchg;
						
						// line weight
						if (row==0 && rowchg==0 && colchg==1) {
							linecoords[linecount][4]=2;
						} else {
							linecoords[linecount][4]=1;
						}

						linecount += 1;
					}
				}
			}
		}
	}
}
function inbounds(row, col) {
	return ((row >= 0) && (col >= 0) && (row < ROWS) && (col < COLS));
}


function rePlay() {
	if (gameActive == 1) {
	    document.formo.redScoreBoard.value = redScore + "";
	    document.formo.bluScoreBoard.value = bluScore + "";
	    clearBoard();
	}
	for (var col=0; col < COLS; col++) {
		colcount[col] = 0;
	}
	for (var row=0; row < ROWS; row++) {
		for (var col=0; col < COLS; col++) {
			board[row][col] = 0;
		}
	}
   	moves = 0;
   	isdropping = 0;

}

var redSpot = new Image();
var bluSpot = new Image();
var emptySpot = new Image();
var emptyPiece = new Image();
var redPiece = new Image();
var bluPiece = new Image();

redSpot.src = "/wisebots-game-service/connectfour/c4fillred.gif";
bluSpot.src = "/wisebots-game-service/connectfour/c4fillblu.gif";
emptySpot.src = "/wisebots-game-service/connectfour/c4fillempty.gif";
emptyPiece.src = "/wisebots-game-service/connectfour/c4clearness.gif";
redPiece.src = "/wisebots-game-service/connectfour/c4redpiece.gif";
bluPiece.src = "/wisebots-game-service/connectfour/c4blupiece.gif";

var whosTurn = RedNum;
var whosTurnSpot = new Image();
var whosTurnPiece = new Image();
whosTurnSpot.src = redSpot.src;
whosTurnPiece.src = redPiece.src ;

function clearBoard() {
	for (var a = 7; a <= 48; a++) {
		document.images[FIRSTIMAGE+a].src = emptySpot.src;
	}
}

function placeTop(picToPlace) {
	if (gameActive == 1) {
		// alert("imagem: " + whosTurnPiece.src);
		// alert("em " + FIRSTIMAGE + "-" + picToPlace)
		document.images[FIRSTIMAGE + picToPlace].src = whosTurnPiece.src;
	}
}

function unPlaceTop(picToUnplace) {
	if (gameActive == 1) {
		document.images[FIRSTIMAGE + picToUnplace].src = emptyPiece.src;
	}
}


function dropIt(whichCol) {
	
	if (gameActive == 1) {
		if (isdropping == 0) {
			
			isdropping = 1;
			
		    var placeLoc = FIRSTIMAGE + (ROWS - colcount[whichCol]) * COLS + whichCol;
		    // alert(placeLoc);
		    if (colcount[whichCol] < 6) {
	
				theAnim.addFrame( FIRSTIMAGE + whichCol, emptyPiece.src);
		        for (var i=(ROWS-1); i>colcount[whichCol]; i--) {
		    		tempLoc = FIRSTIMAGE + (ROWS - i) * COLS + whichCol;
					theAnim.addFrame( tempLoc, whosTurnSpot.src);
					theAnim.addFrame( tempLoc, emptySpot.src);
				}
				theAnim.finalcall = "checkForWinner(" + whosTurn + ")";
				theAnim.addFrame( placeLoc, whosTurnSpot.src);
				
				//document.images[placeLoc].src = whosTurnSpot.src;
				
				colcount[whichCol] += 1;
				
				if (whosTurn==RedNum) {
					board[colcount[whichCol]-1][whichCol] = RedNum;
				} else {
					board[colcount[whichCol]-1][whichCol] = BlkNum;
				}
				
				
				//checkForWinner(whosTurn);
				//placeTop(whichRow);
			}

		    sendState(placeLoc);
		}
	}
}

function dropItComputer(whichCol) {

	if (gameActive == 1) {
			whosTurn = BlkNum;
			whosTurnSpot.src = bluSpot.src;
			whosTurnPiece.src = bluPiece.src;
			
			/*
			if (whosTurn == RedNum) {
				whosTurn = BlkNum;
				whosTurnSpot.src = bluSpot.src;
				whosTurnPiece.src = bluPiece.src;
			} else {
				whosTurn = RedNum;
				whosTurnSpot.src = redSpot.src;
				whosTurnPiece.src = redPiece.src;
			}
			*/
			
		    var placeLoc = FIRSTIMAGE + (ROWS - colcount[whichCol]) * COLS + whichCol;
		    if (colcount[whichCol] < 6) {
	
				theAnim.addFrame( FIRSTIMAGE + whichCol, emptyPiece.src);
		        for (var i=(ROWS-1); i>colcount[whichCol]; i--) {
		    		tempLoc = FIRSTIMAGE + (ROWS - i) * COLS + whichCol
					theAnim.addFrame( tempLoc, whosTurnSpot.src);
					theAnim.addFrame( tempLoc, emptySpot.src);
				}
				theAnim.finalcall = "checkForWinner(" + whosTurn + ")";
				theAnim.addFrame( placeLoc, whosTurnSpot.src);
				
				//document.images[placeLoc].src = whosTurnSpot.src;
				
				colcount[whichCol] += 1;
				if (whosTurn==RedNum) {
					board[colcount[whichCol]-1][whichCol] = RedNum;
				} else {
					board[colcount[whichCol]-1][whichCol] = BlkNum;
				}
				
				//checkForWinner(whosTurn);
				//placeTop(whichRow);
			}
	}
}

function boarddump() {
	s = "";
	for(var col = 0; col<COLS; col++) {  
		s += "Col " + col + "=" + colcount[col] + " -> ";
		for(var row = 0; row<ROWS; row++) {
			s += board[row][col] + ",";
		}
		s += ";\n";			
	}	
	return s;
}

function whoGoesFirst() {
	
	whosFirst = RedNum;
	whosTurn == RedNum;
	
}

function switchTurns() {

	if (gameActive == 1) {
		if (whosTurn == RedNum) {
			whosTurn = BlkNum;
			whosTurnSpot.src = bluSpot.src;
			whosTurnPiece.src = bluPiece.src;
			document.formo.texter.value = document.formo.blkplayer.value + "'s turn.";
		} else {
			whosTurn = RedNum;
			whosTurnSpot.src = redSpot.src;
			whosTurnPiece.src = redPiece.src;
			document.formo.texter.value = document.formo.redplayer.value + "'s turn.";
		}
	
		if (whosTurn==RedNum && document.formo.redtype.value=="Computer") ComputersTurn(RedNum); 
		if (whosTurn==BlkNum && document.formo.blktype.value=="Computer") ComputersTurn(BlkNum); 
		
		isdropping = 0;

	}
}

function ComputersTurn(player) {
	setTimeout("AComputersTurn("+player+")", 1);
}

function AComputersTurn(player) {
	
	Difficulty = document.formo.difficulty.value;
	switch (Difficulty) {
		case "Too Easy":
			Levels = 2;
			StartStupid = 2;
			StupidProb = 0.9;
			break;
			
		case "Easy":
			Levels = 2;
			StartStupid = 4;
			StupidProb = 0.7;
			break;
			
		case "Medium":
			Levels = 2;
			StartStupid = 10;
			StupidProb = 0.2;
			break;
			
		case "Hard":
			Levels = 2;
			StartStupid = 1000;
			StupidProb = 0.0;
			break;
			
		case "Impossible":
			Levels = 4;
			StartStupid = 1000;
			StupidProb = 0.0;
			break;
			
		default :
			Levels = 2;
			StartStupid = 6;
			StupidProb = 0.7;
	} 
	//debug("Levels=" + Levels + "," + StupidProb + "," + Difficulty );
	
	BestCol = MaxMove(player, Levels, Number.MAX_VALUE, "Col");
	
	// make it easier by randomly being stupid
	moves += 1;
	if (moves > StartStupid ) {
		if ( Math.random() < StupidProb ) {
			TryCol = Math.floor(Math.random()*COLS);
			//debug("TryCol=" + TryCol + ",");
			if (colcount[TryCol]<ROWS) {
				BestCol = TryCol;
			}
		} 
	}
	 
	dropIt(BestCol);
}

function freerow(col) {
	var x=-1;
	for(var row=0; row<ROWS; row++) {
		if (board[row][col]==0) { // found where to put the Piece
			x=row;
			break;
		}
	}
	return(x);
}

function MaxMove (player, level, ParentMin, want) {

	if (level <=0) {  // NB or game ended 
		return GetBoardVal(player);
	} else {
	
		var MaxCol = 0;
		var MaxVal = -WINVAL*10;
		for(var col = 0; col<COLS; col++) {
		
			var row = freerow(col);
			if (row>=0) {

				board[row][col] = player;
				var TheVal = MinMove(player, level-1, MaxVal, "Val");
				board[row][col]=0; // undo move
				
				if (TheVal == WINVAL) return WINVAL; // game won 
				
				if(TheVal > ParentMin) {// no need to go further, because the max will be greater than the min already found
					return TheVal;
				} else {
					if (TheVal > MaxVal ) {
						MaxVal = TheVal ;
						MaxCol = col;
					}
				}
			}
		}
		//debug( "Max -> MaxVal=" + MaxVal + ", MaxCol=" + MaxCol + "\n" ); 
		
		if (want=="Val") {
			return MaxVal;
		} else {
			return MaxCol;
		}
	}
}

function MinMove (player, level, ParentMax, want) {

	if (level <=0) { // NB or game ended
		return GetBoardVal(player);
	} else {
	
		var MinCol = 0;
		var MinVal = WINVAL*10;
		
		for(var col = 0; col<COLS; col++) {
		
			var row = freerow(col);
			if (row>=0) {
		
				if (player==RedNum) { // do move with OPPOSITE player
					board[row][col] = BlkNum;
				} else {
					board[row][col] = RedNum;
				}
		
				var TheVal = MaxMove(player, level-1, MinVal, "Val");
				board[row][col]=0; // undo move
				
				if (TheVal == -WINVAL) return -WINVAL; // game won by opponent
				
				
				if(TheVal < ParentMax) {// no need to go further, because the min will be less than the max already found
					return TheVal;
				} else {
					if (TheVal < MinVal ) {  // less than
						MinVal = TheVal;
						MinCol = col;
					}
				}
			}
		}
		//debug( "Min -> MinVal=" + MinVal + ", MinCol=" + MinCol + "\n" ); 
		
		return MinVal;
	}
}


function GetBoardVal(player) {
	
	thesum = 0;
	
	for(var rowno=0; rowno<linecount; rowno++) {
		var theline = linecoords[rowno];
		thesum += Strength(player,theline[0],theline[1],theline[2],theline[3]) * theline[4];
	}

	return thesum;
}

function Strength(player, row, col, rowchg, colchg) {

	if (player==BlkNum) {
		player2 = RedNum;
	} else {
		player2 = BlkNum;
	}

	MeInARow = 0;
	MeMaxInARow = 0;
	YouInARow = 0;
	YouMaxInARow = 0;
	
	for(pos=0; pos<4; pos++) {
	
		posplayer = board[row+pos*rowchg][col+pos*colchg];
		
		if (posplayer==player) {
			MeInARow += 1;
			if (MeInARow>MeMaxInARow) MeMaxInARow=MeInARow;
		} else {
			MeInARow = 0;
		} 
		
		if (posplayer==player2) {
			YouInARow += 1;
			if (YouInARow>YouMaxInARow) YouMaxInARow=YouInARow;
		} else {
			YouInARow = 0;
		} 
	}
	
	x = 0;
	if (MeMaxInARow==1) x += 1;
	if (MeMaxInARow==2) {
		x += 4;
	}
	if (MeMaxInARow==3) {
		x += 64 - YouMaxInARow*16;
	}
	if (MeMaxInARow==4) x = WINVAL;
	
	if (YouMaxInARow==1) x -= 1;
	if (YouMaxInARow==2) {
		x -= 4;
	}
	if (YouMaxInARow==3) {
		x -= (64 - MeMaxInARow*16);
	}
	if (YouMaxInARow==4) x = -WINVAL;
	
	return x;
}

function AutoStart() {
	
	gameActive = 1;
	rePlay();
	
	redScore = 0;
	bluScore = 0;
	document.formo.redScoreBoard.value = redScore + "";
	document.formo.bluScoreBoard.value = bluScore + "";
	
	matchMade = 1;

	bluIsComputer = 1;
	whosFirst = RedNum;
	whosTurn = RedNum;
	
	/*
	if (whosFirst == RedNum) {
		document.formo.texter.value = document.formo.redplayer.value + "'s turn.";
		whosTurn = BlkNum;
		switchTurns();
		whosFirst = RedNum;
	} else {
		document.formo.texter.value = document.formo.blkplayer.value + "'s turn.";
		whosTurn = RedNum;
		switchTurns();
		whosFirst = BlkNum;
	}
	*/

	sendState(-1);
}



function checkForWinner(player) {
	if (gameActive == 1) {
		var someOneWon;
		someOneWon = 0;
		if (player == RedNum) {
			lookForSrc = redSpot.src;
		}
		if (player == BlkNum) {
			lookForSrc = bluSpot.src;
		}
		for (var ic = 7; ic <= 48; ic++) {
			if (document.images[FIRSTIMAGE + ic].src == lookForSrc) {
				if ((ic + 3 <= 48
				&& ic != 11 && ic != 12 && ic != 13
				&& ic != 18 && ic != 19 && ic != 20
				&& ic != 25 && ic != 26 && ic != 27
				&& ic != 32 && ic != 33 && ic != 34
				&& ic != 39 && ic != 40 && ic != 41
				&& document.images[FIRSTIMAGE + ic + 1].src == lookForSrc
				&& document.images[FIRSTIMAGE + ic + 2].src == lookForSrc
				&& document.images[FIRSTIMAGE + ic + 3].src == lookForSrc)
				|| (ic + 3 * 7 <= 48
				&& document.images[FIRSTIMAGE + ic + 7].src == lookForSrc
				&& document.images[FIRSTIMAGE + ic + 7*2].src == lookForSrc
				&& document.images[FIRSTIMAGE + ic + 7*3].src == lookForSrc)
				|| (ic + 3 * 7 <= 48
				&& ic != 11 && ic != 12 && ic != 13
				&& ic != 18 && ic != 19 && ic != 20
				&& ic != 25 && ic != 26 && ic != 27
				&& document.images[FIRSTIMAGE + ic + 7 + 1].src == lookForSrc
				&& document.images[FIRSTIMAGE + ic + 7*2 + 2].src == lookForSrc
				&& document.images[FIRSTIMAGE + ic + 7*3 + 3].src == lookForSrc)
				|| (ic - 3 * 7 >= 7
				&& ic != 32 && ic != 33 && ic != 34
				&& ic != 39 && ic != 40 && ic != 41
				&& ic != 46 && ic != 47 && ic != 48
				&& document.images[FIRSTIMAGE + ic - 7 + 1].src == lookForSrc
				&& document.images[FIRSTIMAGE + ic - 7*2 + 2].src == lookForSrc
				&& document.images[FIRSTIMAGE + ic - 7*3 + 3].src == lookForSrc)) {
					for (var col=0; col<COLS; col++) {
						unPlaceTop(col);
					}
					if (player == RedNum) {
						alert(document.formo.redplayer.value + " wins.");
						redScore += 1;
					} else if (player == BlkNum) {
						alert(document.formo.blkplayer.value + " wins.");
						bluScore += 1;
					}
					gameActive = 0;
					someOneWon = 1;
					ic = 49;
					document.formo.redScoreBoard.value = redScore + "";
					document.formo.bluScoreBoard.value = bluScore + "";
				}
			}
		}
		if (someOneWon != 1) {
			colsFull = 0;
			for (var col=0; col<COLS; col++) {
				if (colcount[col]==ROWS) {
					colsFull += 1;
				}
			}
			if (colsFull==COLS) {
				for (var col = 0; col<COLS; col++) {
					unPlaceTop(col);
				}
				gameActive = 0;
				alert("This game has reached a draw.");
					debug("end");

			}
		}
		
		// NB these go here, coz the last animation frame calls "CheckForWinner" and then has to do these:
		switchTurns();
		isdropping = 0;
		//placeTop(1); // NB

		//for (var col=0; col<COLS; col++) {
		//	unPlaceTop(col);
		//}
		
	}
}

function getState(placeloc){
	var theState = "";
	for (var ic = 7; ic <= 48; ic++) {
		if(placeloc != -1 && ic == placeloc){
			theState += "2,";
		}
		else{
			if (document.images[FIRSTIMAGE + ic].src == redSpot.src) {
				theState += "2,";
			}
			else if (document.images[FIRSTIMAGE + ic].src == bluSpot.src) {
				theState += "1,";
			}
			else{
				theState += "0,";
			}
		}

	}
	return theState;
}

function newGame() {
	if (matchMade == 1) {
		debug("stt");
		gameActive = 1;
		isdropping = 0;
		moves=0;
		rePlay();
		whoGoesFirst();
		sendState();
	}
}

function newMatchUp() {
	if (confirm("Are you sure you want to Start a new match?")) {
		gameActive = 1;
		isdropping = 0;
		rePlay();
		redScore = 0;
		bluScore = 0;
		document.formo.redScoreBoard.value = redScore + "";
		document.formo.bluScoreBoard.value = bluScore + "";
		matchMade = 1;
		whoGoesFirst();
		sendState();
	}
}

function setMsg(whatToSay) {
	window.status = whatToSay;
	return true;
}

function debugend() {
	debug("end");
}

function debug(thestr) {
	if (thestr=="stt") {
		debugstr ="";
		timestt = new Date();
	} else {
		if (thestr=="end") {
			timeend = new Date();
			//debugstr += "\n[" + (timeend.getTime() - timestt.getTime()) + "ms]";
			document.formo.comments.value = debugstr;
		} else {
			debugstr += thestr;
		}
	}
}


//
// Animation functions
//

// The Animation Object constructor
function Animation() {
  this.imageNum = new Array();  // Array of indicies document.images to be changed
  this.imageSrc = new Array();  // Array of new srcs for imageNum array
  this.frameIndex = 0;		  // the frame to play next
  this.alreadyPlaying = false;  // semaphore to ensure we play smoothly
 	this.finalcall = ""; 		// what to call when animation finished
 	
  // Methods
  this.getFrameCount = getframecount;   // the total number of frames so far
  this.moreFrames = moreframes;		 // tells us if there are more frames to play
  this.addFrame = addframe;			 // add a frame to the animation
  this.drawNextFrame = drawnextframe;   // draws the next frame
  this.startAnimation = startanimation; // start the animation if necessary
}

function finalcall(FuncToCall) {
	this.finalcall = FuncToCall;
}

function getframecount() {  return this.imageNum.length; }
function moreframes() {  return this.frameIndex < this.getFrameCount(); }
function startanimation() { 
  if (!this.alreadyPlaying) {
	theAnim.alreadyPlaying = true;
	setTimeout('theAnim.drawNextFrame()',5); 
  }
}

function addframe(num, src) {
  var theIndex = theAnim.imageNum.length;
  theAnim.imageSrc[theIndex] = src;
  theAnim.imageNum[theIndex] = num;
  theAnim.startAnimation();
}

function drawnextframe() {
  if (theAnim.moreFrames()) {
	document.images[ theAnim.imageNum[theAnim.frameIndex] ].src = theAnim.imageSrc[theAnim.frameIndex];
	theAnim.frameIndex++;
	setTimeout('theAnim.drawNextFrame()', 20);
  } else {
	theAnim.alreadyPlaying = false;
		if (this.finalcall.length>0) {
			setTimeout(this.finalcall,5);
		}
 	 }
  }	
  
  function sendState(placeloc) {

		robotid = document.getElementById('robot').value;
		var state = getState(placeloc);
		
		if(robotid == 0){
			return;
		}
		
		new Ajax.Request('/wisebots-game-service/gamecloud/' + robotid + '/' + state,
				{
				 method:'post',
				 onSuccess: function(transport){
				    var response = transport.responseText || "no response text";
				    // document.getElementById("history").innerHTML = document.getElementById("history").innerHTML + "<br>" + "Robot Move: " + response;
				 	response = (response)%7;
				    // alert("Success! \n\n Move: " + response);
				    // pausecomp(3000);
				    
				    dropItComputer(response);
				   },
				 onFailure: function(){ alert('Velha'); }
				});
		}

	  function pausecomp(millis)
	  {
	   var date = new Date();
	   var curDate = null;
	   do { curDate = new Date(); }
	   while(curDate-date < millis);
	 }

// End -->
</script>

<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<script src="/wisebots-game-service/connectfour/main.js?tok=5qweqwe4" type="text/javascript"></script>
</head>

<body>
<div class="centerfull">
	<div id="content"> 
	<center>
	<form name="formo" id="formo">
	<input type="hidden" name="redScoreBoard"  />
	<input type="hidden" name="bluScoreBoard"  />
	<input type="hidden" name="redplayer" value="User" />
	<input type="hidden" name="redtype" value="human" />
	<input type="hidden" name="blktype" value="human" />
	<input type="hidden" name="blkplayer" value="Computer" />
	<input type="hidden" name="texter" />
	<input type="hidden" name="difficult" value="Hard" />
	<input type="hidden" name="robot" id="robot" value="${robot.id}">
	<input type="hidden" name="player" id="player" value="${robot.player}">
	
	<table border="0">
		<tr>
			<td> 
				<table border="0" width="271">
					<tr> 
 							<td> 
							<table cellspacing="0" cellpadding="0" border="0" bgcolor="#FFFF99">
  								<tr> 
									<td><a href="javascript:void dropIt(0)" onMouseOver="placeTop(0); setMsg(''); return true" onMouseOut="unPlaceTop(0)"><img border="0" src="/wisebots-game-service/connectfour/c4clearness.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(1)" onMouseOver="placeTop(1); setMsg(''); return true" onMouseOut="unPlaceTop(1)"><img border="0" src="/wisebots-game-service/connectfour/c4clearness.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(2)" onMouseOver="placeTop(2); setMsg(''); return true" onMouseOut="unPlaceTop(2)"><img border="0" src="/wisebots-game-service/connectfour/c4clearness.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(3)" onMouseOver="placeTop(3); setMsg(''); return true" onMouseOut="unPlaceTop(3)"><img border="0" src="/wisebots-game-service/connectfour/c4clearness.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(4)" onMouseOver="placeTop(4); setMsg(''); return true" onMouseOut="unPlaceTop(4)"><img border="0" src="/wisebots-game-service/connectfour/c4clearness.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(5)" onMouseOver="placeTop(5); setMsg(''); return true" onMouseOut="unPlaceTop(5)"><img border="0" src="/wisebots-game-service/connectfour/c4clearness.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(6)" onMouseOver="placeTop(6); setMsg(''); return true" onMouseOut="unPlaceTop(6)"><img border="0" src="/wisebots-game-service/connectfour/c4clearness.gif" height="50" width="50" /></a></td>
								</tr>
  								<tr> 
									<td><a href="javascript:void dropIt(0)" onMouseOver="placeTop(0); setMsg(''); return true" onMouseOut="unPlaceTop(0)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(1)" onMouseOver="placeTop(1); setMsg(''); return true" onMouseOut="unPlaceTop(1)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(2)" onMouseOver="placeTop(2); setMsg(''); return true" onMouseOut="unPlaceTop(2)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(3)" onMouseOver="placeTop(3); setMsg(''); return true" onMouseOut="unPlaceTop(3)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(4)" onMouseOver="placeTop(4); setMsg(''); return true" onMouseOut="unPlaceTop(4)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(5)" onMouseOver="placeTop(5); setMsg(''); return true" onMouseOut="unPlaceTop(5)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(6)" onMouseOver="placeTop(6); setMsg(''); return true" onMouseOut="unPlaceTop(6)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
								</tr>
  								<tr> 
									<td><a href="javascript:void dropIt(0)" onMouseOver="placeTop(0); setMsg(''); return true" onMouseOut="unPlaceTop(0)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(1)" onMouseOver="placeTop(1); setMsg(''); return true" onMouseOut="unPlaceTop(1)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(2)" onMouseOver="placeTop(2); setMsg(''); return true" onMouseOut="unPlaceTop(2)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(3)" onMouseOver="placeTop(3); setMsg(''); return true" onMouseOut="unPlaceTop(3)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(4)" onMouseOver="placeTop(4); setMsg(''); return true" onMouseOut="unPlaceTop(4)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(5)" onMouseOver="placeTop(5); setMsg(''); return true" onMouseOut="unPlaceTop(5)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(6)" onMouseOver="placeTop(6); setMsg(''); return true" onMouseOut="unPlaceTop(6)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
								</tr>
  								<tr> 
									<td><a href="javascript:void dropIt(0)" onMouseOver="placeTop(0); setMsg(''); return true" onMouseOut="unPlaceTop(0)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(1)" onMouseOver="placeTop(1); setMsg(''); return true" onMouseOut="unPlaceTop(1)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(2)" onMouseOver="placeTop(2); setMsg(''); return true" onMouseOut="unPlaceTop(2)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(3)" onMouseOver="placeTop(3); setMsg(''); return true" onMouseOut="unPlaceTop(3)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(4)" onMouseOver="placeTop(4); setMsg(''); return true" onMouseOut="unPlaceTop(4)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(5)" onMouseOver="placeTop(5); setMsg(''); return true" onMouseOut="unPlaceTop(5)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(6)" onMouseOver="placeTop(6); setMsg(''); return true" onMouseOut="unPlaceTop(6)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
								</tr>
  								<tr> 
									<td><a href="javascript:void dropIt(0)" onMouseOver="placeTop(0); setMsg(''); return true" onMouseOut="unPlaceTop(0)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(1)" onMouseOver="placeTop(1); setMsg(''); return true" onMouseOut="unPlaceTop(1)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(2)" onMouseOver="placeTop(2); setMsg(''); return true" onMouseOut="unPlaceTop(2)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(3)" onMouseOver="placeTop(3); setMsg(''); return true" onMouseOut="unPlaceTop(3)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(4)" onMouseOver="placeTop(4); setMsg(''); return true" onMouseOut="unPlaceTop(4)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(5)" onMouseOver="placeTop(5); setMsg(''); return true" onMouseOut="unPlaceTop(5)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(6)" onMouseOver="placeTop(6); setMsg(''); return true" onMouseOut="unPlaceTop(6)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
								</tr>
  								<tr> 
									<td><a href="javascript:void dropIt(0)" onMouseOver="placeTop(0); setMsg(''); return true" onMouseOut="unPlaceTop(0)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(1)" onMouseOver="placeTop(1); setMsg(''); return true" onMouseOut="unPlaceTop(1)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(2)" onMouseOver="placeTop(2); setMsg(''); return true" onMouseOut="unPlaceTop(2)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(3)" onMouseOver="placeTop(3); setMsg(''); return true" onMouseOut="unPlaceTop(3)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(4)" onMouseOver="placeTop(4); setMsg(''); return true" onMouseOut="unPlaceTop(4)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(5)" onMouseOver="placeTop(5); setMsg(''); return true" onMouseOut="unPlaceTop(5)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(6)" onMouseOver="placeTop(6); setMsg(''); return true" onMouseOut="unPlaceTop(6)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
								</tr>
  								<tr> 
									<td><a href="javascript:void dropIt(0)" onMouseOver="placeTop(0); setMsg(''); return true" onMouseOut="unPlaceTop(0)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(1)" onMouseOver="placeTop(1); setMsg(''); return true" onMouseOut="unPlaceTop(1)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(2)" onMouseOver="placeTop(2); setMsg(''); return true" onMouseOut="unPlaceTop(2)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(3)" onMouseOver="placeTop(3); setMsg(''); return true" onMouseOut="unPlaceTop(3)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(4)" onMouseOver="placeTop(4); setMsg(''); return true" onMouseOut="unPlaceTop(4)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(5)" onMouseOver="placeTop(5); setMsg(''); return true" onMouseOut="unPlaceTop(5)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									<td><a href="javascript:void dropIt(6)" onMouseOver="placeTop(6); setMsg(''); return true" onMouseOut="unPlaceTop(6)"><img border="0" src="/wisebots-game-service/connectfour/c4fillempty.gif" height="50" width="50" /></a></td>
									</tr>
								</table>
								<div align="center">
								<input type="button" name="reStartButton" value="New Game" onClick="newGame()" class="blacktext" />
								</div>	
							</td>
						</tr>
 					</table>
				</td>
			</tr>
		</table>
	</form>
</center>

</form>		
		<script language="JavaScript" type="text/javascript">AutoStart();</script>
		</div>
</div>
<div class="left column zneg">
	<div id="leftcol"></div>
</div>
<div class="right column zneg">
	<div id="rightcol"></div>
</div>
<script type="text/javascript">getBodyEnd();</script>
</body>
</html>
