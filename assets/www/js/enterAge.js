window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

sixPressed = false;
sevenPressed = false;
eightPressed = false;

nextEnable = false;

function initApp() {
	//back
	backButton = document.getElementById("back");
	backButton.ontouchstart = function(e) {
		touchStart(e, backButton);
	};
	backButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			parent.toName();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//next
	nextButton = document.getElementById("next")
	nextButton.ontouchstart = function(e) {
		touchStart(e, nextButton);
	};
	nextButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			if (sixPressed)
				window.localStorage.setItem("userAge", 6);
			else if (sevenPressed)
				window.localStorage.setItem("userAge", 7);
			else if (eightPressed)
				window.localStorage.setItem("userAge", 8);
			parent.toGrade();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	document.getElementById("six").ontouchstart = function () {
		if (!nextEnable) {
			nextEnable = true;
			document.getElementById("next").style.display = "block";
		}
		if (sixPressed) {
			sixPressed = false;
			document.getElementById("sixImg").src="images/General/six.png";
			nextEnable = false;
			document.getElementById("next").style.display = "none";
		}
		else {
			if (sevenPressed) {
				sevenPressed = false;
				document.getElementById("sevenImg").src="images/General/seven.png";
			}
			if (eightPressed) {
				eightPressed = false;
				document.getElementById("eightImg").src="images/General/eight.png";
			}
			sixPressed = true;
			document.getElementById("sixImg").src="images/General/sixTapped.png";
		}
	};
	
	document.getElementById("seven").ontouchstart = function () {
		if (!nextEnable) {
			nextEnable = true;
			document.getElementById("next").style.display = "block";
		}
		if (sevenPressed) {
			sevenPressed = false;
			document.getElementById("sevenImg").src="images/General/seven.png";
			nextEnable = false;
			document.getElementById("next").style.display = "none";
		}
		else {
			if (sixPressed) {
				sixPressed = false;
				document.getElementById("sixImg").src="images/General/six.png";
			}
			if (eightPressed) {
				eightPressed = false;
				document.getElementById("eightImg").src="images/General/eight.png";
			}
			sevenPressed = true;
			document.getElementById("sevenImg").src="images/General/sevenTapped.png";
		}
	};
	
	document.getElementById("eight").ontouchstart = function () {
		if (!nextEnable) {
			nextEnable = true;
			document.getElementById("next").style.display = "block";
		}
		if (eightPressed) {
			eightPressed = false;
			document.getElementById("eightImg").src="images/General/eight.png";
			nextEnable = false;
			document.getElementById("next").style.display = "none";
		}
		else {
			if (sevenPressed) {
				sevenPressed = false;
				document.getElementById("sevenImg").src="images/General/seven.png";
			}
			if (sixPressed) {
				sixPressed = false;
				document.getElementById("sixImg").src="images/General/six.png";
			}
			eightPressed = true;
			document.getElementById("eightImg").src="images/General/eightTapped.png";
		}
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