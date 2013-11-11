window.oncontextmenu = function(evt) {
		evt.preventDefault();
	};

tutorialMode = "true";//window.localStorage.getItem("tutorial");
tutorialStep = 0;
tutorialTextValue = "&nbsp;&nbsp;Welcome to Mobile Picture Books!";
authorID = parseInt(window.localStorage.getItem("authorID"));
user = window.localStorage.getItem("user");
var ageVal;
var gradeVal;
var age;
var grade;

if (window.localStorage.getItem("fromMain")=="true") {
	document.getElementById("clickBuster").style.display = "block";
	document.getElementById("clickBuster").onclick = function() {
		document.getElementById("clickBuster").style.display = "none";
	};
	window.localStorage.setItem("fromMain", "false");
}

if (tutorialMode == "true") {
	name = user;
	ageVal = parseInt(window.localStorage.getItem("userAge"));
	gradeVal = parseInt(window.localStorage.getItem("userGrade")); //0-prep
	age = ageVal+" Years Old";
	if (gradeVal==0)
		grade = "Prep";
	else grade = "Grade "+gradeVal;
}
else if (tutorialMode == "false") {
	name = window.localStorage.getItem("author");
	ageVal = parseInt(window.localStorage.getItem("ageVal"));
	gradeVal = parseInt(window.localStorage.getItem("gradeVal")); //0-prep
	age = ageVal+" Years Old";
	if (gradeVal==0)
		grade = "Prep";
	else grade = "Grade "+gradeVal;
}

skip = document.getElementById('skip');
tutorialBox = tutorialBox;
blackScreen = document.getElementById('blackScreen');
tutorialText = document.getElementById('tutorialText');
photoImg = document.getElementById('photoImg');
photo = document.getElementById('photo');
photoOptions = document.getElementById('photoOptions');
home = document.getElementById('home');
library = document.getElementById('library');
hand = document.getElementById('hand');
next = document.getElementById('next');
userInfo = userInfo;
nameArea = document.getElementById('nameArea');
ageArea = document.getElementById('ageArea');
gradeArea = document.getElementById('gradeArea');

function initApp() {
	if (tutorialMode == "true") {
		skip.style.display = "block";
		tutorialBox.style.display = "block";
		blackScreen.style.display = "block";
		tutorialText.innerHTML = tutorialTextValue;
		tutorialBox.style.height = "15%";
		photoImg.src="images/Users/Default.jpg";
		//auto-resize text
		while(tutorialText.offsetHeight < (tutorialBox.offsetHeight*.87)) {
			//alert("text height: "+tutorialText.offsetHeight+"; textArea height: "+tutorialBox.offsetHeight);
			tutorialText.style.fontSize = (parseFloat(window.getComputedStyle(tutorialText, null).getPropertyValue('font-size')) + 1) +"px";
		}
		while(tutorialText.offsetHeight > (tutorialBox.offsetHeight*.87)) {
			//alert("text height: "+tutorialText.offsetHeight+"; textArea height: "+tutorialBox.offsetHeight);
			tutorialText.style.fontSize = (parseFloat(window.getComputedStyle(tutorialText, null).getPropertyValue('font-size')) - 1) +"px";
		}
	}
	else if (tutorialMode == "false") {
		photoImg.src="images/Users/"+authorID+".jpg";
	}
	document.getElementById('name').innerHTML = name;
	document.getElementById('age').innerHTML = age;
	document.getElementById('grade').innerHTML = grade;
	
	autoresize(document.getElementById('name'), nameArea);
	autoresize(document.getElementById('age'), ageArea);
	autoresize(document.getElementById('grade'), gradeArea);
	
	skip.onclick = function () {
		window.localStorage.setItem("tutorial", "false");
		window.Connector.changeActivity(user, parseInt(window.localStorage.getItem('userAge')), parseInt(window.localStorage.getItem('userGrade')));
	};
	
	photo.ontouchstart = function () {
		if (tutorialMode == "true") {
			if (tutorialStep == 5) {
				photoOptions.style.display = "block";
			}
			tutorialStepFunc();
		}
		else {
			if (user == name) {
				if (photoOptions.style.display == "block")
					photoOptions.style.display = "none";
				else photoOptions.style.display = "block";
			}
		}
	};
	
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
	
	//home
	home.ontouchstart = function(e) {
		touchStart(e, home);
	};
	home.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			if (tutorialMode == "false") {
				parent.toHome();
			}
			else tutorialStepFunc();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//library
	library.ontouchstart = function(e) {
		touchStart(e, library);
	};
	library.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			if (tutorialMode == "false") {
				parent.toLibrary2();
				//window.location = "libraryModule.html";
			}
			else tutorialStepFunc();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	userInfo.onclick = function () {
		if (tutorialMode == "true")
			tutorialStepFunc();
	};
	
	nameArea.onclick = function () {
		if (tutorialMode == "true")
			tutorialStepFunc();
	};
	
	gradeArea.onclick = function () {
		if (tutorialMode == "true")
			tutorialStepFunc();
	};
	
	ageArea.onclick = function () {
		if (tutorialMode == "true")
			tutorialStepFunc();
	};
	
	hand.onclick = function () {
		tutorialStepFunc();
	};
	
	next.onclick = function () {
		playAudio("/android_asset/www/page_flip.mp3");
		parent.toUserStories();
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

function tutorialStepFunc() {
	tutorialStep++;
	switch(tutorialStep) {
		case 1: tutorialBox.style.height = "23.3333%";
				tutorialTextValue = "Since you are new, we will teach you how to make your own stories."; break;
		case 2: tutorialTextValue = "Right now you are at the first page of your very own virtual storybook!"; break;
		case 3: tutorialTextValue = "Your basic details are over here at the right page of your book.";
				tutorialBox.style.top = "29%";
				tutorialBox.style.left = "7.8125%";
				tutorialBox.style.zIndex = 10001;
				userInfo.style.zIndex = 9999;
				photo.style.zIndex = 10000;
				nameArea.style.zIndex = 10000;
				ageArea.style.zIndex = 10000;
				gradeArea.style.zIndex = 10000;
				userInfo.style.background = "rgba(255,255,255,1)";
				hand.style.display = "block"; break;
		case 4: tutorialTextValue = "&nbsp;Oops, it looks like you don’t have your profile picture set up yet!"; 
				userInfo.style.zIndex = 5554;
				userInfo.style.background = "rgba(255,255,255,0)";
				tutorialBox.style.left = "23.2421875%";
				nameArea.style.zIndex = 5555;
				ageArea.style.zIndex = 5555;
				gradeArea.style.zIndex = 5555;
				hand.style.display = "none"; break;
		case 5: tutorialTextValue = "&nbsp;Tap this to upload your own picture!";
				document.getElementById('tappy').style.display = "block";
				document.getElementById('tappy').style.top = "46.66666%";
				document.getElementById('tappy').style.left = "58.59375%"; 
				tutorialBox.style.height = "15%"; break;
		case 6: tutorialTextValue = "Wow, you look good!";
				document.getElementById('tappy').style.display = "none";
				tutorialBox.style.top = "57%";
				tutorialBox.style.left = "46.484375%";
				tutorialBox.style.height = "8%"; break;
		/*case 7: tutorialText = "You can always close your book and go back to the library by tapping the library button…";
				document.getElementById('photo').style.zIndex = 5555;
				document.getElementById('library').style.zIndex = 10000;
				tutorialBox.style.top = "180px";
				tutorialBox.style.left = "20px";
				tutorialBox.style.height = "170px";
				document.getElementById('hand').style.display = "block";
				document.getElementById('hand').style.top = "390px";
				document.getElementById('hand').style.left = "110px";
				document.getElementById('hand').style['-webkit-transform'] = "scaleY(-1) rotate(220deg)"; break;
		case 8: tutorialText = "…or you can choose to go back to the main screen by tapping the home button.";
				document.getElementById('library').style.zIndex = 5555;
				document.getElementById('home').style.zIndex = 10000;
				document.getElementById('hand').style.left = "220px"; break;*/
		case 7: tutorialTextValue = "&nbsp;&nbsp;Tap this button to go to the next page.";
				home.style.zIndex = 5555;
				next.style.zIndex = 10000;
				tutorialBox.style.top = "13.33333%";
				tutorialBox.style.left = "64.453125%";
				tutorialBox.style.height = "15%";
				document.getElementById('tappy').style.display = "block";
				document.getElementById('tappy').style['-webkit-transform'] = "scaleX(-1)";
				document.getElementById('tappy').style.top = "38.33333%";
				document.getElementById('tappy').style.left = "88.8671875%";
				hand.style.display = "none"; break;
		
		/*
		case 1: tutorialText = "This page of the book is where you will see all the stories you have saved."; break;
		case 2: tutorialText = "Right now this page has two sample stories to help you understand the basics."; break;
		case 3: tutorialText = "The titles of your stories will be shown here."; break;
		case 4: tutorialText = "Tapping the titles changes the preview picture to the right."; break;
		case 5: tutorialText = "Now here is your story page and it’s kind of empty!"; break;
		case 6: tutorialText = "Try adding a new story by tapping this button."; break;
		
		case 1: tutorialText = "This screen is where you make your own pictures!"; break;
		case 2: tutorialText = "First you need to tap this paint brush to choose a scene for your picture."; break;
		case 3: tutorialText = "Tap the left and right arrows to change scenes."; break;
		case 4: tutorialText = "Tap the pictures to select them."; break;
		case 5: tutorialText = "Tap these tabs to add characters and objects."; break;
		case 6: tutorialText = "Tap this button when you're finished."; break;
		*/
	}
	tutorialText.innerHTML = tutorialTextValue;
	
	//auto-resize text
	while(tutorialText.offsetHeight < (tutorialBox.offsetHeight*.87)) {
		//alert("text height: "+tutorialText.offsetHeight+"; textArea height: "+tutorialBox.offsetHeight);
		tutorialText.style.fontSize = (parseFloat(window.getComputedStyle(tutorialText, null).getPropertyValue('font-size')) + 1) +"px";
	}
	while(tutorialText.offsetHeight > (tutorialBox.offsetHeight*.87)) {
		//alert("text height: "+tutorialText.offsetHeight+"; textArea height: "+tutorialBox.offsetHeight);
		tutorialText.style.fontSize = (parseFloat(window.getComputedStyle(tutorialText, null).getPropertyValue('font-size')) - 1) +"px";
	}
}

function autoresize(text, container) {
	//auto-resize text
	while(text.offsetHeight < container.offsetHeight) {
		text.style.fontSize = (parseFloat(window.getComputedStyle(text, null).getPropertyValue('font-size')) + 1) +"px";
	}
	while(text.offsetHeight > container.offsetHeight) {
		text.style.fontSize = (parseFloat(window.getComputedStyle(text, null).getPropertyValue('font-size')) - 1) +"px";
	}
}