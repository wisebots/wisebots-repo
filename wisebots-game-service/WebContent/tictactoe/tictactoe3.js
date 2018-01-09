//var
var tabuleiro;
var state = new Array(9);
var robotid;
var player;

//constantes
const imgBola = new Image();
imgBola.src = '/wisebots-game-service/tictactoe/verde.jpg';
const imgX = new Image();
imgX.src = '/wisebots-game-service/tictactoe/vermelho.jpg';

//classe Ponto
var Ponto = Class.create();
Ponto.prototype = {
	initialize: function(x, y) {
		this.setX(x);
		this.setY(y);
	},
	setX: function(x) {
		this.x = x;
	},
	setY: function(y) {
		this.y = y;
	}
}

//classe Tabuleiro
var Tabuleiro = Class.create();
Tabuleiro.prototype = {
	initialize: function(canvasId) {
		initializeState();
		var canvas = document.getElementById(canvasId);
		this.setCanvas(canvas);

		this.canvas.width = 300;
		this.canvas.height= 300;
		this.setContext(canvas.getContext('2d'));
		this.setPosicoes(new Array(10));
		this.setJogadorAtivo(true);
		this.desenhar();
		Event.observe(canvasId, 'click', function(event) {
			return main(tabuleiro, event);
		});
		if(document.getElementById('player').value == 1){
			sendstate(state);
		}
	},
	setCanvas: function(canvas) {
		this.canvas = canvas;
	},
	setContext: function(context) {
		this.context = context;
	},
	setPosicoes: function(posicoes) {
		this.posicoes = posicoes;
	},
	setJogadorAtivo: function(jogadorAtivo) {
		this.jogadorAtivo = jogadorAtivo;
	},
	desenhar: function() {
		this.context.fillRect(100,10,5,300);
		this.context.fillRect(200,10,5,300);
		this.context.fillRect(10,100,300,5);
		this.context.fillRect(10,200,300,5);
	},
	venceu: function() {
		if (this.igual([0,1,2])) {
			return true;
		} else if (this.igual([3,4,5])) {
			return true;
		} else if (this.igual([6,7,8])) {
			return true;
		} else if (this.igual([0,3,6])) {
			return true;
		} else if (this.igual([1,4,7])) {
			return true;
		} else if (this.igual([2,5,8])) {
			return true;
		} else if (this.igual([0,4,8])) {
			return true;
		} else if (this.igual([2,4,6])) {
			return true;
		} else {
			return false;
		}
	},
	igual: function(array) {
		var retorno = true;
		for (i=0;i<array.length;i++) {
			if (this.posicoes[array[i]] != this.jogadorAtivo) {
				retorno = false;
				break;
			}
		}
		return retorno;
	},
	posicaoCursor: function(e) {
		var x;
		var y;
		if (e.pageX || e.pageY) {
		  x = e.pageX;
		  y = e.pageY;
		} else {
		  x = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
		  y = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
		}
		x -= this.canvas.offsetLeft;
		y -= this.canvas.offsetTop;
		return new Ponto(x,y);
	},
	/*
	1 de (0,0)  	até (99,99)		; 2 de (106,0) 	até (199,99)	; 3 de (206,0)    	até (299,99)
	4 de (0,106) até (99,199)	; 5 de (106,199) 	até (199,199)	; 6 de (206,199)	até (299,199)
	7 de (0,206) até (99,299)	; 8 de (106,206)  	até (199,299)	; 9 de (206,206)	até (299,299)	
	*/
	obterQuadradoSelecionado: function(ponto) {
		if (ponto.x >=0 && ponto.x <=99) { //1, 4 ou 7
			if (ponto.y >=0 && ponto.y <= 99) {
				return 0;
			} else if (ponto.y >= 106 && ponto.y <= 199) {
				return 3;
			} else if (ponto.y >= 206 && ponto.y <= 299) {
				return 6;
			} 
		} else if (ponto.x >=106 && ponto.x <=199) { //2, 5 ou 8
			if (ponto.y >=0 && ponto.y <= 99) {
				return 1;
			} else if (ponto.y >= 106 && ponto.y <= 199) {
				return 4;
			} else if (ponto.y >= 206 && ponto.y <= 299) {
				return 7;
			} 
		} else if (ponto.x >=206 && ponto.x <=299) { //3, 6 ou 9
			if (ponto.y >=0 && ponto.y <= 99) {
				return 2;
			} else if (ponto.y >= 106 && ponto.y <= 199) {
				return 5;
			} else if (ponto.y >= 206 && ponto.y <= 299) {
				return 8;
			} 
		} else {
			return null;
		}
	},
	marcarBola: function(context, ponto) {
		context.drawImage(imgBola, ponto.x, ponto.y);
	},
	marcarX: function(context, ponto) {
		context.drawImage(imgX, ponto.x, ponto.y);
	},
	marcarJogada: function(number){
		if(this.jogadorAtivo == true)
			state[number] = 1;
		else
			state[number] = 2;
		
		this.posicoes[number] = this.jogadorAtivo;
		var marcar = this.jogadorAtivo ? this.marcarX : this.marcarBola;
		if (number == 0) {
			marcar(this.context, new Ponto(30, 30));
		} else if (number == 1) {
			marcar(this.context, new Ponto(130, 30));
		} else if (number == 2) {
			marcar(this.context, new Ponto(230, 30));
		} else if (number == 3) {
			marcar(this.context, new Ponto(30, 130));
		} else if (number == 4) {
			marcar(this.context, new Ponto(130, 130));
		} else if (number == 5) {
			marcar(this.context, new Ponto(230, 130));
		} else if (number == 6) {
			marcar(this.context, new Ponto(30, 230));
		} else if (number == 7) {
			marcar(this.context, new Ponto(130, 230));
		} else if (number == 8) {
			marcar(this.context, new Ponto(230, 230));
		}
	}
}

function initializeState(){
	state[0] = 0;
	state[1] = 0;
	state[2] = 0;
	state[3] = 0;
	state[4] = 0;
	state[5] = 0;
	state[6] = 0;
	state[7] = 0;
	state[8] = 0;
}

function sendstate(state) {

	robotid = document.getElementById('robot').value;

	if(robotid == 0){
		return;
	}
	
	new Ajax.Request('/wisebots-game-service/gamecloud/' + robotid + '/' + state,
			{
			 method:'post',
			 onSuccess: function(transport){
			    var response = transport.responseText || "no response text";
			    document.getElementById("history").innerHTML = document.getElementById("history").innerHTML + "<br>" + "Robot Move: " + response;
			    // alert("Success! \n\n Move: " + response);
			   	tabuleiro.marcarJogada(response);
				if (tabuleiro.venceu(tabuleiro.jogadorAtivo)) {
					alert('Jogador ' + (tabuleiro.jogadorAtivo ? 'xis vermelho' : 'bola verde') + ' venceu!');
					tabuleiro.setPosicoes(null);
				} else {
					tabuleiro.setJogadorAtivo(!tabuleiro.jogadorAtivo);
				}
			   },
			 onFailure: function(){ alert('Velha'); }
			});
}
	


//metodo com a regra do jogo
function main(tabuleiro, event) {

	robotid = document.getElementById('robot').value;
	player = document.getElementById('player').value;
		
	var quadrado = tabuleiro.obterQuadradoSelecionado(tabuleiro.posicaoCursor(event));
	if (tabuleiro.posicoes[quadrado] == null) {
		tabuleiro.marcarJogada(quadrado);
		document.getElementById("history").innerHTML = document.getElementById("history").innerHTML + "<br>" + "Human Move: " + quadrado;
		if (tabuleiro.venceu(tabuleiro.jogadorAtivo)) {
			alert('Jogador ' + (tabuleiro.jogadorAtivo ? 'xis vermelho' : 'bola verde') + ' venceu!');
			tabuleiro.setPosicoes(null);
		} else {
			tabuleiro.setJogadorAtivo(!tabuleiro.jogadorAtivo);
		}
	}
	// alert(state);
	document.getElementById("history").innerHTML = document.getElementById("history").innerHTML + "<br>" + "=> Estado do jogo["+robotid+"]: " + state;
	sendstate(state);
}