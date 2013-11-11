window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

prepPressed = false;
grade1Pressed = false;
grade2Pressed = false;
grade3Pressed = false;

nextEnable = false;

function initApp() {
	user = window.localStorage.getItem("user");
	age = parseInt(window.localStorage.getItem("userAge"));
	window.localStorage.setItem("tutorial", "true");
	
	//back
	backButton = document.getElementById("back");
	backButton.ontouchstart = function(e) {
		touchStart(e, backButton);
	};
	backButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			parent.toAge();
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
			if (prepPressed)
				window.localStorage.setItem("userGrade", 0);
			else if (grade1Pressed)
				window.localStorage.setItem("userGrade", 1);
			else if (grade2Pressed)
				window.localStorage.setItem("userGrade", 2);
			else if (grade3Pressed)
				window.localStorage.setItem("userGrade", 3);
			
			grade = parseInt(window.localStorage.getItem("userGrade"));
			window.Connector.addUserInformation(user, age, grade);
			window.localStorage.setItem("bookTutorialStep", 0);
			parent.toUserBook();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	document.getElementById("prep").ontouchstart = function () {
		if (!nextEnable) {
			nextEnable = true;
			document.getElementById("next").style.display = "block";
		}
		if (prepPressed) {
			prepPressed = false;
			document.getElementById("prepImg").src="images/General/prep.png";
			nextEnable = false;
			document.getElementById("next").style.display = "none";
		}
		else {
			if (grade3Pressed) {
				grade3Pressed = false;
				document.getElementById("grade3Img").src="images/General/grade3.png";
			}
			if (grade1Pressed) {
				grade1Pressed = false;
				document.getElementById("grade1Img").src="images/General/grade1.png";
			}
			if (grade2Pressed) {
				grade2Pressed = false;
				document.getElementById("grade2Img").src="images/General/grade2.png";
			}
			prepPressed = true;
			document.getElementById("prepImg").src="images/General/prepTapped.png";
		}
	};
	
	document.getElementById("grade1").ontouchstart = function () {
		if (!nextEnable) {
			nextEnable = true;
			document.getElementById("next").style.display = "block";
		}
		if (grade1Pressed) {
			grade1Pressed = false;
			document.getElementById("grade1Img").src="images/General/grade1.png";
			nextEnable = false;
			document.getElementById("next").style.display = "none";
		}
		else {
			if (prepPressed) {
				prepPressed = false;
				document.getElementById("prepImg").src="images/General/prep.png";
			}
			if (grade3Pressed) {
				grade3Pressed = false;
				document.getElementById("grade3Img").src="images/General/grade3.png";
			}
			if (grade2Pressed) {
				grade2Pressed = false;
				document.getElementById("grade2Img").src="images/General/grade2.png";
			}
			grade1Pressed = true;
			document.getElementById("grade1Img").src="images/General/grade1Tapped.png";
		}
	};
	
	document.getElementById("grade2").ontouchstart = function () {
		if (!nextEnable) {
			nextEnable = true;
			document.getElementById("next").style.display = "block";
		}
		if (grade2Pressed) {
			grade2Pressed = false;
			document.getElementById("grade2Img").src="images/General/grade2.png";
			nextEnable = false;
			document.getElementById("next").style.display = "none";
		}
		else {
			if (prepPressed) {
				prepPressed = false;
				document.getElementById("prepImg").src="images/General/prep.png";
			}
			if (grade1Pressed) {
				grade1Pressed = false;
				document.getElementById("grade1Img").src="images/General/grade1.png";
			}
			if (grade3Pressed) {
				grade3Pressed = false;
				document.getElementById("grade3Img").src="images/General/grade3.png";
			}
			grade2Pressed = true;
			document.getElementById("grade2Img").src="images/General/grade2Tapped.png";
		}
	};
	
	document.getElementById("grade3").ontouchstart = function () {
		if (!nextEnable) {
			nextEnable = true;
			document.getElementById("next").style.display = "block";
		}
		if (grade3Pressed) {
			grade3Pressed = false;
			document.getElementById("grade3Img").src="images/General/grade3.png";
			nextEnable = false;
			document.getElementById("next").style.display = "none";
		}
		else {
			if (prepPressed) {
				prepPressed = false;
				document.getElementById("prepImg").src="images/General/prep.png";
			}
			if (grade1Pressed) {
				grade1Pressed = false;
				document.getElementById("grade1Img").src="images/General/grade1.png";
			}
			if (grade2Pressed) {
				grade2Pressed = false;
				document.getElementById("grade2Img").src="images/General/grade2.png";
			}
			grade3Pressed = true;
			document.getElementById("grade3Img").src="images/General/grade3Tapped.png";
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