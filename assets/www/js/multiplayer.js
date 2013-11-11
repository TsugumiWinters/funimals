window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

bgNum = 3;
slipper1Num = 4;
slipper2Num = 5;
gameMode = 2; //1 score limit, 2 time limit
scoreLimit = 15;
timeLimit = 30;
muted = false;
playerTurn = 2;
var inCountdown = false;

function initApp() {
	initialize();
	
	document.getElementById("p1Hitbox").onclick = function () {
		var s = document.getElementById('slipper1');
		var style = window.getComputedStyle(s, null);
		// this string will be in the form 'matrix(a, b, c, d, tx, ty)'
		var left = style['left'];					   
		
		var o = document.getElementById('slipper1');
		o.style.position = 'absolute';
		o.style.left = left;
		$('#p1Hitbox').toggle();
		$("#slipper1").removeClass('slipper1Sway');
		$("#slipper1").addClass('slipper1Throw');
		
		setTimeout(function() {
			checkForPoints(1);
			$("#slipper1").removeClass('slipper1Throw');
			
			if (gameMode == 1) {
				playerTurn = 2;
				$("#slipper2").addClass('slipper2Sway');
				$('#p2Hitbox').toggle();
			}
			else {
				$("#slipper1").addClass('slipper1Sway');
				$('#p1Hitbox').toggle();
			}
			
			o.style.left = "355px";
			o.style.top = "15px";
		}, 1500);
	};
	
	document.getElementById("p2Hitbox").onclick = function () {
		var s = document.getElementById('slipper2');
		var style = window.getComputedStyle(s, null);
		// this string will be in the form 'matrix(a, b, c, d, tx, ty)'
		var left = style['left'];					   
		
		var o = document.getElementById('slipper2');
		o.style.position = 'absolute';
		o.style.left = left;
		$('#p2Hitbox').toggle();
		$("#slipper2").removeClass('slipper2Sway');
		$("#slipper2").addClass('slipper2Throw');
		
		setTimeout(function() {
			checkForPoints(2);
			$("#slipper2").removeClass('slipper2Throw');
			
			if (gameMode == 1) {
				playerTurn = 1;
				$("#slipper1").addClass('slipper1Sway');
				$('#p1Hitbox').toggle();
			}
			else {
				$("#slipper2").addClass('slipper2Sway');
				$('#p2Hitbox').toggle();
			}
			
			o.style.left = "355px";
			o.style.top = "1133px";
		}, 1500);
	};
	
	document.getElementById("pause1").onclick = function () {
		if (inCountdown == false) {
			clearInterval(timer2);
			document.getElementById("slipper1").style.WebkitAnimationPlayState="paused";
			document.getElementById("slipper2").style.WebkitAnimationPlayState="paused";
			$("#resume").css({'top': '290px', 'left': '414px', '-webkit-transform': 'rotate(180deg)'});
			$("#restart").css({'top': '290px', 'left': '179px', '-webkit-transform': 'rotate(180deg)'});
			$("#muted").css({'top': '144px', 'left': '326px', '-webkit-transform': 'rotate(180deg)'});
			$("#mute").css({'top': '144px', 'left': '326px', '-webkit-transform': 'rotate(180deg)'});
			if (muted)
				$('#muted').toggle();
			else if (!muted)
					$('#mute').toggle();
			$("#quit").css({'top': '0px', 'left': '0px', '-webkit-transform': 'rotate(180deg)'});
			$('#dark').toggle();
			$('#resume').toggle();
			$('#restart').toggle();
			$('#quit').toggle();
		}
	};
	
	document.getElementById("pause2").onclick = function () {
		if (inCountdown == false) {
			clearInterval(timer2);
			document.getElementById("slipper1").style.WebkitAnimationPlayState="paused";
			document.getElementById("slipper2").style.WebkitAnimationPlayState="paused";
			$("#resume").css({'top': '815px', 'left': '179px', '-webkit-transform': 'rotate(0deg)'});
			$("#restart").css({'top': '815px', 'left': '414px', '-webkit-transform': 'rotate(0deg)'});
			$("#muted").css({'top': '1020px', 'left': '326px', '-webkit-transform': 'rotate(0deg)'});
			$("#mute").css({'top': '1020px', 'left': '326px', '-webkit-transform': 'rotate(0deg)'});
			if (muted)
				$('#muted').toggle();
			else if (!muted)
					$('#mute').toggle();
			$("#quit").css({'top': '1195px', 'left': '568px', '-webkit-transform': 'rotate(0deg)'});
			$('#dark').toggle();
			$('#resume').toggle();
			$('#restart').toggle();
			$('#quit').toggle();
		}
	};
	
	document.getElementById("resume").onclick = function () {
		countdownTimer2();
		document.getElementById("slipper1").style.WebkitAnimationPlayState="running";
		document.getElementById("slipper2").style.WebkitAnimationPlayState="running";
		$('#dark').toggle();
		$('#resume').toggle();
		$('#restart').toggle();
		if (muted)
			$('#muted').toggle();
		else if (!muted)
				$('#mute').toggle();
		$('#quit').toggle();
	};
	
	document.getElementById("restart").onclick = function () {
		/*document.getElementById("slipper1").style.WebkitAnimationPlayState="running";
		document.getElementById("slipper2").style.WebkitAnimationPlayState="running";
		$('#dark').toggle();
		$('#resume').toggle();
		$('#restart').toggle();
		if (muted)
			$('#muted').toggle();
		else if (!muted)
				$('#mute').toggle();
		$('#quit').toggle();
		$('#quit2').toggle();*/
	};
	
	document.getElementById("mute").onclick = function () {
		$('#mute').toggle();
		$('#muted').toggle();
		muted = true;
	};
	
	document.getElementById("muted").onclick = function () {
		$('#mute').toggle();
		$('#muted').toggle();
		muted = false;
	};
	
	document.getElementById("quit").onclick = function () {
		window.location = "index.html";
	};
	
	document.getElementById("quit2").onclick = function () {
		window.location = "index.html";
	};
}

if (document.images) {
	countdown_3 = new Image();  img1_on.src ="images/3.png";
	countdown_2 = new Image();  img1_on.src ="images/2.png";
	countdown_1 = new Image();  img1_on.src ="images/1.png";
	img1_on = new Image();  img1_on.src ="images/resumePressed.png"; 
	img1_off = new Image();  img1_off.src="images/resumeButton.png"; 
	img2_on = new Image();  img2_on.src ="images/restartPressed.png"; 
	img2_off = new Image();  img2_off.src="images/restartButton.png"; 
	img3_on = new Image();  img3_on.src ="images/mutePressed.png"; 
	img3_off = new Image();  img3_off.src="images/muteButton.png"; 
	img4_on = new Image();  img4_on.src ="images/mutedPressed.png"; 
	img4_off = new Image();  img4_off.src="images/mutedButton.png"; 
}

function movr(k) {
 if (document.images) 
  eval('document.img'+k+'.src=img'+k+'_on.src');
}

function mup(k) {
 if (document.images) 
  eval('document.img'+k+'.src=img'+k+'_off.src');
}

function checkForPoints(slipper) {	
	var curleft = curtop = 0;
	if (slipper == 1)
		var obj = document.getElementById("slipper1");
	else if (slipper == 2)
		var obj = document.getElementById("slipper2");
	
	if (obj.offsetParent) {
		do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	}
	
	if (curleft>=342&&curleft<=378) {
		if (slipper == 1) {
			score1Val += 3;
			eScore2Val += 3;
		}
		else if (slipper == 2) {
			score2Val += 3;
			eScore1Val += 3;
		}
		//navigator.notification.vibrate(1000);
	}
	else if (curleft>=273&&curleft<342||curleft>378&&curleft<441) {
		if (slipper == 1) {
			score1Val += 2;
			eScore2Val += 2;
		}
		else if (slipper == 2) {
			score2Val += 2;
			eScore1Val += 2;
		}
	}
	else if (curleft>=210&&curleft<273||curleft>=441&&curleft<501) {
		if (slipper == 1) {
			score1Val += 1;
			eScore2Val += 1;
		}
		else if (slipper == 2) {
			score2Val += 1;
			eScore1Val += 1;
		}
	}
		
	document.getElementById('score1').innerHTML = score1Val;
	document.getElementById('eScore1').innerHTML = eScore1Val;
	document.getElementById('score2').innerHTML = score2Val;
	document.getElementById('eScore2').innerHTML = eScore2Val;
	
	if (gameMode == 1) {
		if (score1Val >= scoreLimit || score2Val >= scoreLimit) {
			$('#countdown1').toggle();
			$('#countdown2').toggle();
			ending();
		}
	}
}

var count;
var timer1;
var timer2;
var temp;

function countdown(){
	inCountdown = true;
	$('#timerArea1').toggle();
	$('#timerArea2').toggle();
	
	count = timeLimit;
	temp = 3;
	countdownTimer1();
	
	setTimeout(countdownTimer2, 3000);
}

function countdownTimer1() {
	timer1 = setInterval(function(){
		if (temp == 3) {
				$('#countdown1').toggle();
				$('#countdown2').toggle();
			}
			if (temp == 0) {
				document.getElementById("countdownImg1").style.width="400px";
				document.getElementById("countdownImg1").style.height="184px";
				document.getElementById("countdownImg1").src="images/Start.png";
				$("#countdown1").css({'top': '246px','left': '184px'});
				document.getElementById("countdownImg2").style.width="400px";
				document.getElementById("countdownImg2").style.height="184px";
				document.getElementById("countdownImg2").src="images/Start.png";
				$("#countdown2").css({'left': '184px'});
			}
			else if (temp == 1 || temp == 2) {
				document.getElementById("countdownImg1").src="images/"+temp+".png";
				document.getElementById("countdownImg2").src="images/"+temp+".png";
			}
			if (temp < 0) {
				$('#countdown1').toggle();
				$('#countdown2').toggle();
				clearInterval(timer1);
			}
			temp--;
	}, 1000);
}

function countdownTimer2() {
		timer2 = setInterval(function(){
			if (count >= 20)
				$("#timerArea2").css({'top': '850px', 'left': '530px'});
			else if (count >= 10)
				$("#timerArea2").css({'top': '850px', 'left': '560px'});
			else
				$("#timerArea2").css({'top': '850px', 'left': '610px'});
			document.getElementById('timer1').innerHTML = count + 's';
			document.getElementById('timer2').innerHTML = count + 's';
			if (count == 0) {
				$('#p1Hitbox').toggle();
				$("#slipper1").removeClass('slipper1Sway');
				$('#p2Hitbox').toggle();
				$("#slipper2").removeClass('slipper2Sway');
				
				$('#countdown1').toggle();
				$('#countdown2').toggle();
				
				document.getElementById("countdownImg1").style.width="650px";
				document.getElementById("countdownImg1").style.height="184px";
				document.getElementById("countdownImg1").src="images/TimeUp.png";
				$("#countdown1").css({'top': '246px','left': '59px'});
				document.getElementById("countdownImg2").style.width="650px";
				document.getElementById("countdownImg2").style.height="184px";
				document.getElementById("countdownImg2").src="images/TimeUp.png";
				$("#countdown2").css({'left': '59px'});
				
				$('#timerArea1').toggle();
				$('#timerArea2').toggle();
			}
			if (count <= -3) {
				clearInterval(timer2);
				ending();
			}
			count--;
		}, 1000);
}

function ending() {
	$("#restart").css({'top': '552px', 'left': '296px'});
	$("#restart").addClass('rotate');
	$('#dark').toggle();
	$('#restart').toggle();
	$('#quit').css({'top': '598px', 'left': '20px',  '-webkit-transform': 'rotate(180deg)'});
	$('#quit').toggle();
	$('#quit2').css({'top': '598px', 'left': '548px'});
	$('#quit2').toggle();
	
	document.getElementById("countdownImg1").style.width="650px";
	document.getElementById("countdownImg1").style.height="184px";
	document.getElementById("countdownImg2").style.width="650px";
	document.getElementById("countdownImg2").style.height="184px";
	
	if (score1Val > score2Val) {
		$("#countdown1").css({'top': '300px','left': '59px'});
		document.getElementById("countdownImg1").src="images/Winner.png";
		$("#countdown2").css({'top': '830px','left': '59px'});
		document.getElementById("countdownImg2").src="images/Sorry.png";
	} else if (score2Val > score1Val) {
		$("#countdown1").css({'top': '300px','left': '59px'});
		document.getElementById("countdownImg1").src="images/Sorry.png";
		$("#countdown2").css({'top': '796px','left': '59px'});
		document.getElementById("countdownImg2").src="images/Winner.png";
	}
	else {
		$("#countdown1").css({'top': '260px','left': '59px'});
		document.getElementById("countdownImg1").src="images/Draw.png";
		$("#countdown2").css({'top': '830px','left': '59px'});
		document.getElementById("countdownImg2").src="images/Draw.png";
	}
	document.getElementById("slipper1").style.WebkitAnimationPlayState="paused";
	document.getElementById("slipper2").style.WebkitAnimationPlayState="paused";
}

function initialize() {
	document.getElementById('timer1').innerHTML = timeLimit + 's';
	document.getElementById('timer2').innerHTML = timeLimit + 's';

	document.getElementById("slipper1Img").src="images/slipper"+slipper1Num+".png";
	document.getElementById("slipper2Img").src="images/slipper"+slipper2Num+".png";
	document.getElementById("bgImg").src="images/bg_"+bgNum+".png";
	
	if (gameMode == 1) {
		if (playerTurn == 1) {
			$('#p1Hitbox').toggle();
			$("#slipper1").addClass('slipper1Sway');
		} else if (playerTurn == 2) {
			$('#p2Hitbox').toggle();
			$("#slipper2").addClass('slipper2Sway'); 
		}
	}
	else if (gameMode == 2) {
		countdown();
		
		$("#slipper1").addClass('slipper1Sway');
		$("#slipper2").addClass('slipper2Sway'); 
		
		setTimeout(function() {
			$('#p1Hitbox').toggle();
			$('#p2Hitbox').toggle();
			setTimeout(function() {
				inCountdown = false;
			}, 1000);
		}, 4000);
	}
	
	score1Val = 0;
	eScore1Val = 0;
	score2Val = 0;
	eScore2Val = 0;
	document.getElementById('score1').innerHTML = score1Val;
	document.getElementById('eScore1').innerHTML = eScore1Val;
	document.getElementById('score2').innerHTML = score2Val;
	document.getElementById('eScore2').innerHTML = eScore2Val;
}