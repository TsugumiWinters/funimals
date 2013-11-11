window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

tutorialMode = window.localStorage.getItem("tutorial");
tutorialStep = window.localStorage.getItem("storiesTutorialStep");
tutorialTextValue = "This page of the book is where you will see all the stories you have saved.";

user = window.localStorage.getItem("user");
author = window.localStorage.getItem("author");
age = parseInt(window.localStorage.getItem("ageVal"));
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


numStories = window.Connector.getNumStories(author);

var a;
var newHTML = "";
var storyFile;
stories = new Array();
realStoryID = new Array();
if (tutorialMode == "false") {
	if (numStories!=0) {
		for (a=0;a<numStories;a++) {
			realStoryID[a] = window.Connector.retrieveStoryID(a);
			stories[a] = window.Connector.getStoryTitle(a);		
			storyFile = "<div class='story' id='story"+a+"' ontouchstart='storyClick("+a+")'> <div class='title' id='title"+a+"'>"+stories[a]+"</div> </div>";
			newHTML = newHTML.concat(storyFile);
		}
	}
	else
		newHTML = "<div class='story' id='story0'> <div class='title' id='title0'>No stories yet...</div> </div>";
	var selectedStoryID = realStoryID[0];
}
else if (tutorialMode == "true") {
		samples = new Array();
		samples[0] = "Roy the Chicken Learns to Take a Bath";
		samples[1] = "Bertha Learns to Share";
		samples[2] = "Popper the Penguin Learns to Read";
		samples[3] = "Sam the Cat Finds Courage";
		samples[4] = "Trixie the Turtle Makes New Friends";
		samples[5] = "Lucas the Lion Learns to Be Clean";
		samples[6] = "Philip the Pig Becomes Obedient";
		samples[7] = "Garry the Sheep Becomes Respectful";
		
	for (a=0;a<8;a++) {
		storyFile = "<div class='story' id='story"+a+"' ontouchstart='storyClick("+a+")'> <div class='title' id='title"+a+"'>"+samples[a]+"</div> </div>";
		newHTML = newHTML.concat(storyFile);
		
	}
}
document.getElementById("storyContainer").innerHTML = newHTML;

function initApp() {
	if (user == author)
		isUserAuthor = 1;
	else isUserAuthor = 0;
	if (tutorialMode == "true") {
		skip.style.display = "block";
		tutorialBox.style.display = "block";
		blackScreen.style.display = "block";
		tutorialText.innerHTML = tutorialTextValue;
		photoImg.src="images/Library/Previews/preview0.png";
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
		if (numStories!=0) {
			document.getElementById("story0").style.background = "rgba(227,147,41,1)";
			photoImg.src = ""+window.Connector.getSDCardDir()+author+"_"+realStoryID[0]+".png";
			titleText.innerHTML = stories[0];
			autoresize(titleText, titleTextArea);
		}
		else photoImg.src="images/Library/Previews/defaultPreview.png";
	}
	
	skip.onclick = function () {
		window.localStorage.setItem("tutorial", "false");
		window.Connector.changeActivity(window.localStorage.getItem('user'), parseInt(window.localStorage.getItem('userAge')), parseInt(window.localStorage.getItem('userGrade')), 0);
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
				parent.toHome2();
			}
			//else tutorialStepFunc();
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
			//else tutorialStepFunc();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	//add
	if (user != author) {
		add.style.backgroundColor="black";
		document.getElementById('addImg').style.opacity = "0.4";
	}
	else {
		document.getElementById('addImg').style.opacity = "1";
		add.style.backgroundColor="transparent";
	}
	if (tutorialMode == "true") {
		document.getElementById('addImg').style.opacity = "1";
		add.style.backgroundColor="transparent";
	}	
	add.ontouchstart = function(e) {
		touchStart(e, add);
	};
	add.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			//parent.stopAudio();
			if (user == author && tutorialMode == "false")
				window.Connector.changeActivity(window.localStorage.getItem('user'), parseInt(window.localStorage.getItem('userAge')), parseInt(window.localStorage.getItem('userGrade')), 0);
			else if (tutorialMode == "true")
				window.Connector.changeActivity(window.localStorage.getItem('user'), parseInt(window.localStorage.getItem('userAge')), parseInt(window.localStorage.getItem('userGrade')), 1);
			window.localStorage.setItem("storiesTutorialStep", tutorialStep);
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
			//else tutorialStepFunc();
		}
		buttonObj.style['-webkit-transform'] = "scale(1,1)";
		started = false;
	};
	
	photo.onclick = function () {
		if (tutorialMode == "true") {
			//tutorialStepFunc();
		}
		else if (numStories!=0) window.Connector.setStoryID(selectedStoryID, author, age, isUserAuthor);
	};
	
	read.onclick = function () {
		//playAudio("/android_asset/www/page_flip.mp3");
		if (numStories!=0)
			window.Connector.setStoryID(selectedStoryID, author, age, isUserAuthor);
	};
	
	if (window.localStorage.getItem("showNewest") == "true") {
		photoImg.src = ""+window.Connector.getSDCardDir()+author+"_"+realStoryID[numStories-1]+".png";
		titleText.innerHTML = stories[numStories-1];
		autoresize(titleText, titleTextArea);
		document.getElementById("story"+(numStories-1)).style.background = "rgba(227,147,41,1)";
		document.getElementById("story0").style.background = "white";
		window.localStorage.setItem("showNewest", "false"); 
	}
	
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

function storyClick(clicked_index) {
	if (tutorialMode == "false") {
		var i=0;
		selectedStoryID = realStoryID[clicked_index];
		//alert(""+window.Connector.getSDCardDir()+author+"_"+selectedStoryId+".png");
		photoImg.src = ""+window.Connector.getSDCardDir()+author+"_"+selectedStoryID+".png";//"images/Library/Previews/preview"+selectedStoryId+".png";
		titleText.innerHTML = stories[clicked_index];
		autoresize(titleText, titleTextArea);
		for(i=0;i<numStories;i++) {
			if (i==clicked_index)
				document.getElementById("story"+clicked_index).style.background = "rgba(227,147,41,1)";
			else
				document.getElementById("story"+i).style.background = "white";
		}
	}
	else if (tutorialMode == "true") {
		var i=0;
		photoImg.src="images/Library/Previews/preview"+clicked_index+".png";
		titleText.innerHTML = samples[clicked_index];
		autoresize(titleText, titleTextArea);
		for(i=0;i<8;i++) {
			if (i==clicked_index)
				document.getElementById("story"+clicked_index).style.background = "rgba(227,147,41,1)";
			else
				document.getElementById("story"+i).style.background = "white";
		}
		hand.style.display = "none";
		document.getElementById('tappy').style.display = "none";
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
				tutorialBox.style.top = "50.33333%";
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