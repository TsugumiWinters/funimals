var containerX = 0;
var containerY = 0;
var started = false;
var buttonRect;
var buttonObj;

function initApp() {
	backButton = document.getElementById("back");
	infoButton = document.getElementById("info");
	newPictureButton = document.getElementById("newPicture");
	viewLibraryButton = document.getElementById("viewLibrary");
	tutorialButton = document.getElementById("tutorial");
	accountsButton = document.getElementById("userBackground");
	muteButton = document.getElementById("mute");
	mutedButton = document.getElementById("muted");
	textArea = document.getElementById("textArea");
	text = document.getElementById("text");
	nameArea = document.getElementById("name");
	nameText = document.getElementById("nameText");
	
	window.oncontextmenu = function(evt) {
		evt.preventDefault();
	};
	
	mute = window.localStorage.getItem("mute");
	if (mute=="true") {
		muteButton.style.display = "none"
		mutedButton.style.display = "block"
	}
	else {
		mutedButton.style.display = "none"
		muteButton.style.display = "block"
		parent.playAudio("/android_asset/www/main_menu.mp3");
	}
	
	userID = parseInt(window.localStorage.getItem("userID"));
	currentUser = window.localStorage.getItem("user").split(' ')[0];
	currentAge = parseInt(window.localStorage.getItem("userAge"));
	currentGrade = parseInt(window.localStorage.getItem("userGrade"));
	window.localStorage.setItem("tutorial", "false");

	nameText.innerHTML = ""+currentUser+"";
	document.getElementById('accountImg').src="images/Users/"+userID+".jpg";

	//container
	document.getElementById("container").ontouchmove = function(e) {
		containerX = e.touches[0].pageX;
		containerY = e.touches[0].pageY;
		if (started == true) {
			if (containerX < buttonRect.left || containerX > buttonRect.right || containerY < buttonRect.top || containerY > buttonRect.bottom)
				buttonObj.style['-webkit-transform'] = "scale(1,1)";
			else buttonObj.style['-webkit-transform'] = "scale(.95,.95)";
		}
	};
	document.getElementById("container").ontouchend = function(e) {
		e.preventDefault();
		if (started) {
			buttonObj.style['-webkit-transform'] = "scale(1,1)";
			started = false;
		}
	};
	document.getElementById("container2").ontouchmove = function(e) {
		containerX = e.touches[0].pageX;
		containerY = e.touches[0].pageY;
		if (started == true) {
			if (containerX < buttonRect.left || containerX > buttonRect.right || containerY < buttonRect.top || containerY > buttonRect.bottom)
				buttonObj.style['-webkit-transform'] = "scale(1,1)";
			else buttonObj.style['-webkit-transform'] = "scale(.95,.95)";
		}
	};
	document.getElementById("container2").ontouchend = function(e) {
		e.preventDefault();
		if (started) {
			buttonObj.style['-webkit-transform'] = "scale(1,1)";
			started = false;
		}
	};
	
	//newPictureButton
	newPictureButton.ontouchstart = function(e) {
		touchStart(e, newPictureButton);
	};
	newPictureButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			//parent.stopAudio();
			window.Connector.changeActivity(window.localStorage.getItem("user"), currentAge, currentGrade, 0);
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//viewLibraryButton
	viewLibraryButton.ontouchstart = function(e) {
		touchStart(e, viewLibraryButton);
	};
	viewLibraryButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			window.localStorage.setItem("fromMain", "true");
			parent.toLibrary();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//tutorialButton
	tutorialButton.ontouchstart = function(e) {
		touchStart(e, tutorialButton);
	};
	tutorialButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			window.localStorage.setItem("tutorial", "true");
			window.localStorage.setItem("fromMain", "true");
			window.localStorage.setItem("bookTutorialStep", 0);
			parent.toUserBook();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//infoButton
	infoButton.ontouchstart = function (e) {
		touchStart(e, infoButton);
	};
	infoButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			document.getElementById("container").style.display = "none";
			document.getElementById("container2").style.display = "block";
			//auto-resize text
			while(text.offsetHeight < (textArea.offsetHeight*.87)) {
				text.style.fontSize = (parseFloat(window.getComputedStyle(text, null).getPropertyValue('font-size')) + 1) +"px";
			}
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//backButton
	backButton.ontouchstart = function (e) {
		touchStart(e, backButton);
	};
	backButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			document.getElementById("container2").style.display = "none";
			document.getElementById("container").style.display = "block";
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//accountsButton
	accountsButton.ontouchstart = function (e) {
		touchStart(e, accountsButton);
	};
	accountsButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			window.localStorage.setItem("fromMain", "true");
			parent.toAccounts();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//muteButton
	muteButton.ontouchstart = function (e) {
		touchStart(e, muteButton);
	};
	muteButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			window.localStorage.setItem("mute", "true");
			parent.stopAudio();
			muteButton.style.display = "none"
			mutedButton.style.display = "block"
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//mutedButton
	mutedButton.ontouchstart = function (e) {
		touchStart(e, mutedButton);
	};
	mutedButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			window.localStorage.setItem("mute", "false");
			parent.playAudio("/android_asset/www/main_menu.mp3");
			mutedButton.style.display = "none"
			muteButton.style.display = "block"
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
}

function touchStart(e, object) {
	e.preventDefault();
	containerX = e.touches[0].pageX;
	containerY = e.touches[0].pageY;
	buttonObj = object;
	buttonRect = buttonObj.getBoundingClientRect();
	buttonObj.style['-webkit-transform'] = "scale(.95,.95)";
	started = true;
}