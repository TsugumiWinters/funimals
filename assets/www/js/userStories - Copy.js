window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

tutorialMode = window.localStorage.getItem("tutorial");
tutorialStep = 0;
tutorialTextValue = "This page of the book is where you will see all the stories you have saved.";

var selectedStoryId = 0;
numStories = 8;
stories = new Array();
stories[0] = "Roy the Chicken Learns to Take a Bath";
stories[1] = "Bertha Learns to Share";
stories[2] = "Popper the Penguin Learns to Read";
stories[3] = "Sam the Cat Finds Courage";
stories[4] = "Trixie the Turtle Makes New Friends";
stories[5] = "Lucas the Lion Learns to Be Clean";
stories[6] = "Philip the Pig Becomes Obedient";
stories[7] = "Garry the Sheep Becomes Respectful";
storyPressed = new Array();
storyPressed[0] = false;
storyPressed[1] = false;
storyPressed[2] = false;
storyPressed[3] = false;
storyPressed[4] = false;
storyPressed[5] = false;
storyPressed[6] = false;
storyPressed[7] = false;

skip = document.getElementById('skip');
tutorialBox = tutorialBox;
blackScreen = document.getElementById('blackScreen');
tutorialText = document.getElementById('tutorialText');
photo = document.getElementById('photo');
photoImg = document.getElementById('photoImg');
titleText = document.getElementById('titleText');
home = document.getElementById('home');
library = document.getElementById('library');
add = document.getElementById('add');
back = document.getElementById('back');
hand = document.getElementById('hand');
titleArea = document.getElementById('titleArea');
titleTextArea = document.getElementById('titleTextArea');

function initApp() {
	
	if (tutorialMode == "true") {
		skip.style.display = "block";
		tutorialBox.style.display = "block";
		blackScreen.style.display = "block";
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
	
	document.getElementById("story0").style.background = "rgba(227,147,41,1)";
	photoImg.src="images/Library/Previews/preview0.png";
	titleText.innerHTML = stories[0];
	autoresize(titleText, titleTextArea);
	
	var i=0;
	
	skip.onclick = function () {
		window.localStorage.setItem("tutorial", "false");
		window.Connector.changeActivity(window.localStorage.getItem("user"), parseInt(window.localStorage.getItem('userAge')), parseInt(window.localStorage.getItem('userGrade')));
	};
	
	//container
	document.getElementById("minicontainer").ontouchmove = function(e) {
		containerX = e.touches[0].pageX;
		containerY = e.touches[0].pageY;
		if (started == true) {
			if (containerX < buttonRect.left || containerX > buttonRect.right || containerY < buttonRect.top || containerY > buttonRect.bottom)
				buttonObj.style['-webkit-transform'] = "scale(1,1)";
			else buttonObj.style['-webkit-transform'] = "scale(.95,.95)";
		}
	};
	document.getElementById("minicontainer").ontouchend = function(e) {
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
			}
			else tutorialStepFunc();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//add
	add.ontouchstart = function(e) {
		touchStart(e, add);
	};
	add.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			parent.stopAudio();
			window.Connector.changeActivity(window.localStorage.getItem('user'), parseInt(window.localStorage.getItem('userAge')), parseInt(window.localStorage.getItem('userGrade')));
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//back
	back.ontouchstart = function(e) {
		touchStart(e, back);
	};
	back.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			if (tutorialMode == "false") {
				playAudio("/android_asset/www/page_flip.mp3");
				parent.backToUserBook();
			}
			else tutorialStepFunc();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	photo.onclick = function () {
		if (tutorialMode == "true") {
			tutorialStepFunc();
		}
	};
	
	for (i=0;i<=numStories;i++) 
		document.getElementById("title"+i).innerHTML = stories[i];
	/*for (i=0;i<=numStories;i++) {
		document.getElementById("title"+(i+1)).value = stories[i];
		document.getElementById("story"+(i+1)).onclick = function () {
			var j = i-1;
			if (storyPressed[j] == false) {
				storyPressed[j] = true;
				this.style.background = "rgb(255,0,0)";
			}
			else if (storyPressed[j]) {
				storyPressed[j] = false;
				this.style.background = "rgba(255,255,255,0.1)";
			}
			else {
				var j=0;
				for (j=0;j<numStories;j++) {
					if (j!=i)
						storyPressed[j] = false;
						this.style.background = "rgb(255,255,255,0.1)";
				}
				storyPressed[i] = true;
				this.style.background = "rgb(255,0,0)";
			}
		};
	}*/
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

function storyClick(clicked_id) {
	var i=0;
	selectedStoryId = clicked_id;
	photoImg.src="images/Library/Previews/preview"+selectedStoryId+".png";
	titleText.innerHTML = stories[selectedStoryId];
	autoresize(titleText, titleTextArea);
	for(i=0;i<numStories;i++) {
		if (i==selectedStoryId)
			document.getElementById("story"+selectedStoryId).style.background = "rgba(227,147,41,1)";
		else
			document.getElementById("story"+i).style.background = "white";
	}
}

function tutorialStepFunc() {
	tutorialStep++;
	switch(tutorialStep) {
		case 1: tutorialTextValue = "Right now this page has sample stories to help you understand the basics."; break;
		case 2: hand.style.display = "block";
				document.getElementById('stories').style.zIndex = 10000;
				hand.style.display = "block";
				tutorialBox.style.height = "15%";
				tutorialTextValue = "The titles of your stories will be shown here."; break;
		case 3: photo.style.zIndex = 10000;
				document.getElementById('titleArea').style.zIndex = 10000;
				hand.style.display = "none";
				document.getElementById('tappy').style.display = "block";
				tutorialBox.style.top = "26.6666%";
				tutorialBox.style.left = "25.390625%";
				tutorialTextValue = "Tap the titles to select a story."; break;
		case 4: document.getElementById('stories').style.zIndex = 10000;
				titleArea.style.zIndex = 5555;
				document.getElementById('stories').innerHTML = "<div id='storyContainer'><div class='story' id='story0'><div class='title' id='title0'>No stories yet...</div></div></div>";
				photoImg.src="images/Library/Previews/defaultPreview.png";
				tutorialBox.style.top = "68.33333%";
				tutorialBox.style.left = "61.5234375%";
				tutorialBox.style.height = "23.3333%";
				document.getElementById('tappy').style.display = "none";
				tutorialTextValue = "Now here is your <br/>story page and it’s <br/>kind of empty!"; break;
		case 5: document.getElementById('stories').style.zIndex = 5555;
				photo.style.zIndex = 5555;
				document.getElementById('add').style.zIndex = 10000;
				tutorialBox.style.top = "55%";
				tutorialBox.style.left = "26.3671875%";
				tutorialBox.style.height = "15%";
				document.getElementById('tappy').style.display = "block";
				document.getElementById('tappy').style.top = "78.3333%";
				document.getElementById('tappy').style.left = "25.390625%";
				document.getElementById('tappy').style['-webkit-transform'] = "scaleY(-1) rotate(-90deg)";
				tutorialTextValue = "Tap this button to add a new story!"; break;
	}
	document.getElementById('tutorialText').innerHTML = tutorialTextValue;
	
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