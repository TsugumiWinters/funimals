window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

/*window.localStorage.setItem("bgNum", bgNum);
window.localStorage.setItem("slipper1Num", slipper1Num);
window.localStorage.setItem("slipper2Num", slipper2Num);
window.localStorage.setItem("gameMode", gameMode);
window.localStorage.setItem("scoreValue", scoreValue);
window.localStorage.setItem("timeValue", timeValue);
window.localStorage.setItem("playerTurn", playerTurn);
window.localStorage.setItem("muted", muted);*/

var topOrBottom = 0; //1 = top, 2 = bottom
var playOrAbout = 0;
var singleOrMulti = 0;
var gameMode; //1 Score Limit, 2 Time Limit
var scoreValue = 15;
var timeValue = 30;
var back = 0;
var bgNum = 0;
var slipper1Num = 1;
var slipper2Num = 2;

var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicity call 'app.receivedEvent(...);'
    onDeviceReady: function() {
		app.initApp();
    },
	
	initApp: function() {	
		$("#title").css({'-webkit-animation-name': 'titleEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1.5s', '-webkit-animation-iteration-count': '1'});
		$("#playButton").css({'left': '-500px', '-webkit-animation-name': 'playEnter', '-webkit-animation-delay': '1.65s'});
		$("#aboutButton").css({'left': '-500px', '-webkit-animation-name': 'aboutEnter', '-webkit-animation-delay': '1.75s'});
		$("#target").css({'-webkit-animation-name': 'targetEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1.5s', '-webkit-animation-iteration-count': '1'});
		
		var playClick = false;
		var aboutClick = false;
		var singleplayerClick = false;
		var multiplayerClick = false;
		var startClick = false;
		var scoreLimitClick = false;
		var timeLimitClick = false;
		var nextClick = false;
		
		setTimeout(function() {
			app.sway();
		}, 1500);
		
		document.getElementById("playButton").onclick = function () {
			if (playClick == false) {
				playClick = true;
				playOrAbout = 1;

				$("#playButton").css({'-webkit-animation-name': 'playExit', '-webkit-animation-delay': '0s'});
				$("#aboutButton").css({'left': '238px', '-webkit-animation-name': 'aboutExit', '-webkit-animation-delay': '0.1s'});
			
				$("#back").css({'top': '1400px', '-webkit-animation-name': 'backEnter', '-webkit-animation-delay': '0.65s'});
				
				$("#singleplayer").css({'left': '-750px', '-webkit-animation-name': 'singleplayerEnter', '-webkit-animation-delay': '0.65s'});
				$("#multiplayer").css({'left': '-750px', '-webkit-animation-name': 'multiplayerEnter', '-webkit-animation-delay': '0.75s'});
				setTimeout(function() {
					playClick = false;
				}, 1000);
			}
		};
		
		document.getElementById("aboutButton").onclick = function () {
			if (aboutClick == false) {
				aboutClick = true;
				playOrAbout = 2;
				
				$("#playButton").css({'-webkit-animation-name': 'playExit', '-webkit-animation-delay': '0s'});
				$("#aboutButton").css({'left': '238px', '-webkit-animation-name': 'aboutExit', '-webkit-animation-delay': '0.1s'});
			
				$("#back").css({'top': '1400px', '-webkit-animation-name': 'backEnter', '-webkit-animation-delay': '0.65s'});
				$("#creators").css({'left': '-550px', '-webkit-animation-name': 'creatorsEnter', '-webkit-animation-delay': '0.65s'});
				setTimeout(function() {
					aboutClick = false;
				}, 1000);
			}
		};
		
		document.getElementById("back").onclick = function () {
			if (back == 0) {
				$("#back").css({'top': '1215px', '-webkit-animation-delay': '0', '-webkit-animation-name': 'backExit'});
				
				if (playOrAbout == 1) {
					$("#singleplayer").css({'-webkit-animation-name': 'singleplayerExit', '-webkit-animation-delay': '0s'});
					$("#multiplayer").css({'left': '110px', '-webkit-animation-name': 'multiplayerExit', '-webkit-animation-delay': '0.1s'});
				}
				else if (playOrAbout == 2) {
					$("#creators").css({'left': '130px', '-webkit-animation-delay': '0', '-webkit-animation-name': 'creatorsExit'});
				}
				
				$("#playButton").css({'left': '-500px', '-webkit-animation-name': 'playEnter', '-webkit-animation-delay': '0.65s'});
				$("#aboutButton").css({'left': '-500px', '-webkit-animation-name': 'aboutEnter', '-webkit-animation-delay': '0.75s'});
				
				playOrAbout = 0;
			}
			else if (back == 1) {
				back--;
				singleplayerClick = false;
				multiplayerClick = false;
				if (singleOrMulti == 1) {
					$("#multiplayer").css({'-webkit-animation-name': 'multiplayerEnter', '-webkit-animation-delay': '0.5s', '-webkit-animation-duration': '0.5s'});
					$("#title").css({'-webkit-animation-name': 'titleEnter', '-webkit-animation-delay': '0.5s', '-webkit-animation-duration': '0.5s', '-webkit-animation-iteration-count': '1'});
					$("#singleplayer").css({'-webkit-animation-name': 'singleplayerDown', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				
					$("#start").css({'-webkit-animation-name': 'startExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
					$("#groupName").css({'-webkit-animation-name': 'groupEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#school").css({'-webkit-animation-name': 'schoolEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				
					$("#selectBg").css({'-webkit-animation-name': 'selectBgExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#bgLeftArrow").css({'-webkit-animation-name': 'bgLeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#bgArea").css({'-webkit-animation-name': 'bgAreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#bgRightArrow").css({'-webkit-animation-name': 'bgRightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#selectSlipper2").css({'-webkit-animation-name': 'selectSlipper2Exit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#slipper2LeftArrow").css({'-webkit-animation-name': 'slipper2LeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#slipper2Area").css({'-webkit-animation-name': 'slipper2AreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#slipper2RightArrow").css({'-webkit-animation-name': 'slipper2RightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				}
				else if (singleOrMulti == 2) {
					$("#singleplayer").css({'-webkit-animation-name': 'singleplayerEnter', '-webkit-animation-delay': '0.5s', '-webkit-animation-duration': '0.5s'});
					$("#title").css({'-webkit-animation-name': 'titleEnter', '-webkit-animation-delay': '0.5s', '-webkit-animation-duration': '0.5s', '-webkit-animation-iteration-count': '1'});
					//$("#target").css({'-webkit-animation-name': 'targetDown', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '0.5s', '-webkit-animation-iteration-count': '1'});
					$("#multiplayer").css({'-webkit-animation-name': 'multiplayerDown', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
					
					$("#gameMode").css({'-webkit-animation-name': 'gameModeExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
					$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
					$("#timeLimit").css({'-webkit-animation-name': 'timeLimitExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				}
				
				singleOrMulti = 0;
			}
			else if (back == 2) {
				back--;
				scoreLimitClick = false;
				timeLimitClick = false;
				$("#groupName").css({'-webkit-animation-name': 'groupEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#school").css({'-webkit-animation-name': 'schoolEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#multiplayer").css({'-webkit-animation-name': 'multiplayerEnter2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				
				$("#gameMode").css({'-webkit-animation-name': 'gameModeEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#upArrow").css({'-webkit-animation-name': 'upArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#textArea").css({'-webkit-animation-name': 'textAreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#downArrow").css({'-webkit-animation-name': 'downArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1.3s'});
				
				$("#nextButton").css({'-webkit-animation-name': 'nextExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			
				if (topOrBottom == 1) {
					$("#timeLimit").css({'-webkit-animation-name': 'timeLimitEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
					$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitUp', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				}
				else if (topOrBottom == 2) {
					$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
					$("#timeLimit").css({'-webkit-animation-name': 'timeLimitUp', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});	
				}
				
				topOrBottom = 0;
			}
			else if (back == 3) {
				back--;
				nextClick = false;
				$("#upArrow").css({'-webkit-animation-name': 'upArrowEnter2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#textArea").css({'-webkit-animation-name': 'textAreaEnter2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#downArrow").css({'-webkit-animation-name': 'downArrowEnter2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1.3s'});
				
				$("#nextButton").css({'-webkit-animation-name': 'nextEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#start").css({'-webkit-animation-name': 'startExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			
				if (topOrBottom == 1) {
					$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitEnter2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				}
				else if (topOrBottom == 2) {
					$("#timeLimit").css({'-webkit-animation-name': 'timeLimitEnter2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});	
				}
				
				$("#selectSlipper1").css({'-webkit-animation-name': 'selectSlipper1Exit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper1LeftArrow").css({'-webkit-animation-name': 'slipper1LeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper1Area").css({'-webkit-animation-name': 'slipper1AreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper1RightArrow").css({'-webkit-animation-name': 'slipper1RightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#selectBg").css({'-webkit-animation-name': 'selectBgExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#bgLeftArrow").css({'-webkit-animation-name': 'bgLeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#bgArea").css({'-webkit-animation-name': 'bgAreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#bgRightArrow").css({'-webkit-animation-name': 'bgRightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#selectSlipper2").css({'-webkit-animation-name': 'selectSlipper2Exit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper2LeftArrow").css({'-webkit-animation-name': 'slipper2LeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper2Area").css({'-webkit-animation-name': 'slipper2AreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper2RightArrow").css({'-webkit-animation-name': 'slipper2RightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			}
		};
		
		document.getElementById("singleplayer").onclick = function () {
			if (singleplayerClick == false) {
				singleplayerClick = true;
				back++;
				singleOrMulti = 1;
				
				$("#multiplayer").css({'-webkit-animation-name': 'multiplayerExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#title").css({'-webkit-animation-name': 'titleExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '0.5s', '-webkit-animation-iteration-count': '1'});
				$("#singleplayer").css({'-webkit-animation-name': 'singleplayerUp', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				
				$("#groupName").css({'-webkit-animation-name': 'groupExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#school").css({'-webkit-animation-name': 'schoolExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#start").css({'-webkit-animation-name': 'startEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#selectBg").css({'-webkit-animation-name': 'selectBgEnter', '-webkit-animation-delay': '0.5s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#bgLeftArrow").css({'-webkit-animation-name': 'bgLeftArrowEnter', '-webkit-animation-delay': '0.6s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#bgArea").css({'-webkit-animation-name': 'bgAreaEnter', '-webkit-animation-delay': '0.6s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#bgRightArrow").css({'-webkit-animation-name': 'bgRightArrowEnter', '-webkit-animation-delay': '0.6s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#selectSlipper2").css({'-webkit-animation-name': 'selectSlipper2Enter', '-webkit-animation-delay': '0.7s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper2LeftArrow").css({'-webkit-animation-name': 'slipper2LeftArrowEnter', '-webkit-animation-delay': '0.8s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper2Area").css({'-webkit-animation-name': 'slipper2AreaEnter', '-webkit-animation-delay': '0.8s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper2RightArrow").css({'-webkit-animation-name': 'slipper2RightArrowEnter', '-webkit-animation-delay': '0.8s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			}
		};
		
		document.getElementById("multiplayer").onclick = function () {
			if (multiplayerClick == false) {
				multiplayerClick = true;
				back++;
				singleOrMulti = 2;
				
				$("#singleplayer").css({'-webkit-animation-name': 'singleplayerExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#title").css({'-webkit-animation-name': 'titleExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '0.5s', '-webkit-animation-iteration-count': '1'});
				//$("#target").css({'-webkit-animation-name': 'targetDown', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '0.5s', '-webkit-animation-iteration-count': '1'});
				//setTimeout(function() {
				//	$("#target").css({'top': '720px', 'left': '-235px', '-webkit-animation-name': 'targetSway', '-webkit-animation-iteration-count': 'infinite', '-webkit-animation-duration': '6s', '-webkit-animation-timing-function': 'linear', '-webkit-animation-delay': '0s'});
				//}, 1000);
				$("#multiplayer").css({'-webkit-animation-name': 'multiplayerUp', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				
				$("#gameMode").css({'-webkit-animation-name': 'gameModeEnter', '-webkit-animation-delay': '0.5s', '-webkit-animation-duration': '1s'});
				$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitEnter', '-webkit-animation-delay': '0.6s', '-webkit-animation-duration': '1s'});
				$("#timeLimit").css({'-webkit-animation-name': 'timeLimitEnter', '-webkit-animation-delay': '0.7s', '-webkit-animation-duration': '1s'});
			}
		};
		
		document.getElementById("scoreLimit").onclick = function () {
			if (scoreLimitClick == false) {
				scoreLimitClick = true;
				back++;
				topOrBottom = 1;
			
				gameMode = 1;
				if (gameMode == 1) {
					document.getElementById('textValue').innerHTML = scoreValue + 'pts';
				}
				
				$("#groupName").css({'-webkit-animation-name': 'groupExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#school").css({'-webkit-animation-name': 'schoolExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#multiplayer").css({'-webkit-animation-name': 'multiplayerExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				
				$("#gameMode").css({'-webkit-animation-name': 'gameModeExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#timeLimit").css({'-webkit-animation-name': 'timeLimitExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#upArrow").css({'-webkit-animation-name': 'upArrowEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#textArea").css({'-webkit-animation-name': 'textAreaEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#downArrow").css({'-webkit-animation-name': 'downArrowEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitDown', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#nextButton").css({'-webkit-animation-name': 'nextEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			}
		};
		
		document.getElementById("timeLimit").onclick = function () {
			if (timeLimitClick == false) {
				timeLimitClick = true;
				back++;
				topOrBottom = 2;
				
				gameMode = 2;
				if (gameMode == 2) {
					document.getElementById('textValue').innerHTML = timeValue + 'sec';
				}
				
				$("#groupName").css({'-webkit-animation-name': 'groupExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#school").css({'-webkit-animation-name': 'schoolExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#multiplayer").css({'-webkit-animation-name': 'multiplayerExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				
				$("#gameMode").css({'-webkit-animation-name': 'gameModeExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#upArrow").css({'-webkit-animation-name': 'upArrowEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#textArea").css({'-webkit-animation-name': 'textAreaEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#downArrow").css({'-webkit-animation-name': 'downArrowEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#timeLimit").css({'-webkit-animation-name': 'timeLimitDown', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#nextButton").css({'-webkit-animation-name': 'nextEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			}
		};
		
		document.getElementById("upArrow").onclick = function () {
			if (gameMode == 1) {
				if (scoreValue < 45)
					scoreValue += 15;
				document.getElementById('textValue').innerHTML = scoreValue + 'pts';
					window.localStorage.setItem("scoreValue", scoreValue);
			}
			else if (gameMode == 2) {
				if (timeValue < 90)
					timeValue += 30;
				document.getElementById('textValue').innerHTML = timeValue + 'sec';
					window.localStorage.setItem("timeValue", timeValue);
			}
		};
		
		document.getElementById("downArrow").onclick = function () {
			if (gameMode == 1) {
				if (scoreValue > 15)
					scoreValue -= 15;
				document.getElementById('textValue').innerHTML = scoreValue + 'pts';
					window.localStorage.setItem("scoreValue", scoreValue);
			}
			else if (gameMode == 2) {
				if (timeValue > 30)
					timeValue -= 30;
				document.getElementById('textValue').innerHTML = timeValue + 'sec';
					window.localStorage.setItem("timeValue", timeValue);
			}
		};
		
		document.getElementById("nextButton").onclick = function () {
			if (nextClick == false) {
				nextClick = true;
				back++;
				
				$("#upArrow").css({'-webkit-animation-name': 'upArrowExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#textArea").css({'-webkit-animation-name': 'textAreaExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#downArrow").css({'-webkit-animation-name': 'downArrowExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1.3s'});
				
				$("#nextButton").css({'-webkit-animation-name': 'nextExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				$("#start").css({'-webkit-animation-name': 'startEnter', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			
				if (topOrBottom == 1) {
					$("#timeLimit").css({'-webkit-animation-name': 'timeLimitExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
					$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
				}
				else if (topOrBottom == 2) {
					$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
					$("#timeLimit").css({'-webkit-animation-name': 'timeLimitExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});	
				}
				
				$("#selectSlipper1").css({'-webkit-animation-name': 'selectSlipper1Enter', '-webkit-animation-delay': '0.3s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper1LeftArrow").css({'-webkit-animation-name': 'slipper1LeftArrowEnter', '-webkit-animation-delay': '0.4s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper1Area").css({'-webkit-animation-name': 'slipper1AreaEnter', '-webkit-animation-delay': '0.4s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper1RightArrow").css({'-webkit-animation-name': 'slipper1RightArrowEnter', '-webkit-animation-delay': '0.4s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#selectBg").css({'-webkit-animation-name': 'selectBgEnter', '-webkit-animation-delay': '0.5s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#bgLeftArrow").css({'-webkit-animation-name': 'bgLeftArrowEnter', '-webkit-animation-delay': '0.6s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#bgArea").css({'-webkit-animation-name': 'bgAreaEnter', '-webkit-animation-delay': '0.6s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#bgRightArrow").css({'-webkit-animation-name': 'bgRightArrowEnter', '-webkit-animation-delay': '0.6s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#selectSlipper2").css({'-webkit-animation-name': 'selectSlipper2Enter', '-webkit-animation-delay': '0.7s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper2LeftArrow").css({'-webkit-animation-name': 'slipper2LeftArrowEnter', '-webkit-animation-delay': '0.8s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper2Area").css({'-webkit-animation-name': 'slipper2AreaEnter', '-webkit-animation-delay': '0.8s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
				$("#slipper2RightArrow").css({'-webkit-animation-name': 'slipper2RightArrowEnter', '-webkit-animation-delay': '0.8s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			}
		};
		
		document.getElementById("start").onclick = function () {
			if (startClick == false) {
				startClick = true;
				app.clear();
				
				setTimeout(function() {
					if (singleOrMulti == 1)
						window.location = "singleplayer.html";
					else if (singleOrMulti == 2)
						window.location = "multiplayer.html";
				}, 3150);
			}
		};
		
		document.getElementById("bgLeftArrow").onclick = function () {
			bgNum--;
			if (bgNum < 0)
				bgNum = 3;
			document.getElementById("bgImg").src="images/bg_"+bgNum+"thumb.png";
		};
		
		document.getElementById("bgRightArrow").onclick = function () {
			bgNum++;
			if (bgNum > 3)
				bgNum = 0;
			document.getElementById("bgImg").src="images/bg_"+bgNum+"thumb.png";
		};
		
		document.getElementById("slipper1LeftArrow").onclick = function () {
			if (singleOrMulti == 1) {
				slipper1Num--;
				if (slipper1Num < 1)
					slipper1Num = 6;
				document.getElementById("slipper1Img").src="images/slipper"+slipper1Num+".png";
			}
			else if (singleOrMulti == 2) {
				slipper1Num--;
				if (slipper1Num < 1)
					slipper1Num = 6;
				if (slipper1Num == slipper2Num) {
					if (slipper2Num == 1)
						slipper1Num = 6;
					else
						slipper1Num--;
				}
				document.getElementById("slipper1Img").src="images/slipper"+slipper1Num+".png";
			}
		};
		
		document.getElementById("slipper1RightArrow").onclick = function () {
			if (singleOrMulti == 1) {
				slipper1Num++;
				if (slipper1Num > 6)
					slipper1Num = 1;
				document.getElementById("slipper1Img").src="images/slipper"+slipper1Num+".png";
			}
			else if (singleOrMulti == 2) {
				slipper1Num++;
				if (slipper1Num > 6)
					slipper1Num = 1;
				if (slipper1Num == slipper2Num) {
					if (slipper2Num == 6)
						slipper1Num = 1;
					else
						slipper1Num++;
				}
				document.getElementById("slipper1Img").src="images/slipper"+slipper1Num+".png";
			}
		};
		
		document.getElementById("slipper2LeftArrow").onclick = function () {
			if (singleOrMulti == 1) {
				slipper2Num--;
				if (slipper2Num < 1)
					slipper2Num = 6;
				document.getElementById("slipper2Img").src="images/slipper"+slipper2Num+".png";
			}
			else if (singleOrMulti == 2) {
				slipper2Num--;
				if (slipper2Num < 1)
					slipper2Num = 6;
				if (slipper2Num == slipper1Num) {
					if (slipper1Num == 1)
						slipper2Num = 6;
					else
						slipper2Num--;
				}
				document.getElementById("slipper2Img").src="images/slipper"+slipper2Num+".png";
			}
		};
		
		document.getElementById("slipper2RightArrow").onclick = function () {
			if (singleOrMulti == 1) {
				slipper2Num++;
				if (slipper2Num > 6)
					slipper2Num = 1;
				document.getElementById("slipper2Img").src="images/slipper"+slipper2Num+".png";
			}
			else if (singleOrMulti == 2) {
				slipper2Num++;
				if (slipper2Num > 6)
					slipper2Num = 1;
				if (slipper2Num == slipper1Num) {
					if (slipper1Num == 6)
						slipper2Num = 1;
					else
						slipper2Num++;
				}
				document.getElementById("slipper2Img").src="images/slipper"+slipper2Num+".png";
			}
		};
	},
	
	sway: function() {
		$("#target").css({'-webkit-animation-name': 'targetSway', '-webkit-animation-iteration-count': 'infinite', '-webkit-animation-duration': '6s', '-webkit-animation-timing-function': 'linear', '-webkit-animation-delay': '0s'});
		$("#title").css({'-webkit-animation-name': 'titleSway', '-webkit-animation-iteration-count': 'infinite', '-webkit-animation-duration': '4s', '-webkit-animation-timing-function': 'linear', '-webkit-animation-delay': '0s'});
	},
	
	clear: function() {
		if (singleOrMulti == 1) {
			$("#singleplayer").css({'-webkit-animation-name': 'singleplayerExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			$("#start").css({'-webkit-animation-name': 'startExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
		
					$("#selectBg").css({'-webkit-animation-name': 'selectBgExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#bgLeftArrow").css({'-webkit-animation-name': 'bgLeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#bgArea").css({'-webkit-animation-name': 'bgAreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#bgRightArrow").css({'-webkit-animation-name': 'bgRightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#selectSlipper2").css({'-webkit-animation-name': 'selectSlipper2Exit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#slipper2LeftArrow").css({'-webkit-animation-name': 'slipper2LeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#slipper2Area").css({'-webkit-animation-name': 'slipper2AreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
					$("#slipper2RightArrow").css({'-webkit-animation-name': 'slipper2RightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
		}	
		else if (singleOrMulti == 2) {
			$("#upArrow").css({'-webkit-animation-name': 'upArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			$("#textArea").css({'-webkit-animation-name': 'textAreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			$("#downArrow").css({'-webkit-animation-name': 'downArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			if (gameMode == 2)
				$("#timeLimit").css({'-webkit-animation-name': 'timeLimitExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			else if (gameMode == 1)
				$("#scoreLimit").css({'-webkit-animation-name': 'scoreLimitExit2', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			$("#start").css({'-webkit-animation-name': 'startExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
			
			$("#selectSlipper1").css({'-webkit-animation-name': 'selectSlipper1Exit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#slipper1LeftArrow").css({'-webkit-animation-name': 'slipper1LeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#slipper1Area").css({'-webkit-animation-name': 'slipper1AreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#slipper1RightArrow").css({'-webkit-animation-name': 'slipper1RightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#selectBg").css({'-webkit-animation-name': 'selectBgExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#bgLeftArrow").css({'-webkit-animation-name': 'bgLeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#bgArea").css({'-webkit-animation-name': 'bgAreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#bgRightArrow").css({'-webkit-animation-name': 'bgRightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#selectSlipper2").css({'-webkit-animation-name': 'selectSlipper2Exit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#slipper2LeftArrow").css({'-webkit-animation-name': 'slipper2LeftArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#slipper2Area").css({'-webkit-animation-name': 'slipper2AreaExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#slipper2RightArrow").css({'-webkit-animation-name': 'slipper2RightArrowExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
		}
		$("#back").css({'-webkit-animation-name': 'backExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s'});
		$("#title").css({'-webkit-animation-name': 'titleExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '0.5s', '-webkit-animation-iteration-count': '1'});
		$("#target").css({'-webkit-animation-name': 'targetExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '0.5s', '-webkit-animation-iteration-count': '1'});
		$("#groupName").css({'-webkit-animation-name': 'groupExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
		$("#school").css({'-webkit-animation-name': 'schoolExit', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '1s', '-webkit-animation-iteration-count': '1'});
			$("#background").css({'-webkit-animation-name': 'blackoutScreen', '-webkit-animation-delay': '0s', '-webkit-animation-duration': '3s', '-webkit-animation-delay': '1s'});
	}	
};

if (document.images) {
	img_on = new Image();  img_on.src="images/arrowPressed.png"; 
	img_off = new Image();  img_off.src="images/arrow.png"; 
}

function movr(k) {
	if (document.images) {
		switch (k) {
			case 1: eval('document.arrowUp.src=img_on.src'); break;
			case 2: eval('document.arrowDown.src=img_on.src'); break;
			case 3: eval('document.bgLeft.src=img_on.src'); break;
			case 4: eval('document.bgRight.src=img_on.src'); break;
			case 5: eval('document.slipper2Left.src=img_on.src'); break;
			case 6: eval('document.slipper2Right.src=img_on.src'); break;
			case 7: eval('document.slipper1Left.src=img_on.src'); break;
			case 8: eval('document.slipper1Right.src=img_on.src'); break;
		}
	}
}

function mup(k) {
	if (document.images) {
		switch (k) {
			case 1: eval('document.arrowUp.src=img_off.src'); break;
			case 2: eval('document.arrowDown.src=img_off.src'); break;
			case 3: eval('document.bgLeft.src=img_off.src'); break;
			case 4: eval('document.bgRight.src=img_off.src'); break;
			case 5: eval('document.slipper2Left.src=img_off.src'); break;
			case 6: eval('document.slipper2Right.src=img_off.src'); break;
			case 7: eval('document.slipper1Left.src=img_off.src'); break;
			case 8: eval('document.slipper1Right.src=img_off.src'); break;
		}
	}
}

// custom toast message with button and callbacks
function toast(msg) {
	var options = {
		timeout: 5000
	};
	toastId = blackberry.ui.toast.show(msg, options);
}