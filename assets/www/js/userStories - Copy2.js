window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

tutorialMode = window.localStorage.getItem("tutorial");
tutorialStep = 0;
tutorialText = "This page of the book is where you will see all the stories you have saved.";

selectedStoryId = 0;
numStories = parseInt(window.Connector.getNumStories(window.localStorage.getItem("author")));

var newHTML = "";
var stories = new Array();
var j;
for (j=0;j<numStories;j++) {
	stories[j] = window.Connector.getStoryTitle(j);
	storyEntry = "<div class='story' id='story"+j+"' ontouchstart='storyClick("+j+")'> <div class='title' id='title"+j+"'> "+stories[j]+" </div> </div>";
	newHTML = newHTML.concat(storyEntry);
}
document.getElementById('stories').innerHTML = newHTML;

function initApp() {
	
	if (tutorialMode == "true") {
		document.getElementById('skip').style.display = "block";
		document.getElementById('tutorialBox').style.display = "block";
		document.getElementById('blackScreen').style.display = "block";
		document.getElementById('tutorialText').innerHTML = tutorialText;
	}
	
	var i=0;
	
	document.getElementById("story0").style.background = "rgba(227,147,41,1)";
	document.getElementById("photoImg").src="images/Library/Previews/preview0.png";
	document.getElementById("titleText").innerHTML = stories[0];
	
	document.getElementById('skip').onclick = function () {
		window.localStorage.setItem("tutorial", "false");
		window.Connector.changeActivity(user, parseInt(window.localStorage.getItem('userAge')), parseInt(window.localStorage.getItem('userGrade')));
	};
	
	document.getElementById("home").onclick = function () {
		window.location = "main.html";
	};
	
	document.getElementById("add").ontouchstart = function () {
		window.Connector.changeActivity(user, parseInt(window.localStorage.getItem('userAge')), parseInt(window.localStorage.getItem('userGrade')));
	};
	
	document.getElementById("library").onclick = function () {
		window.location = "library.html";
	};
	
	document.getElementById("photo").onclick = function () {
		if (tutorialMode == "true") {
			tutorialStepFunc();
		}
	};
	
	document.getElementById("back").ontouchstart = function () {
		history.back();
	};
}

function storyClick(clicked_id) {
	var i=0;
	selectedStoryId = clicked_id;
	document.getElementById("photoImg").src="/mnt/external_sd/MobilePictureBooks/SavedPictures/"+window.localStorage.getItem('author')+"_"+selectedStoryId+".png";
	document.getElementById("titleText").innerHTML = stories[selectedStoryId];
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
		case 1: tutorialText = "Right now this page has sample stories to help you understand the basics."; break;
		case 2: document.getElementById('hand').style.display = "block";
				document.getElementById('stories').style.zIndex = 10000;
				document.getElementById('hand').style.display = "block";
				tutorialText = "The titles of your stories will be shown here."; break;
		case 3: document.getElementById('photo').style.zIndex = 10000;
				document.getElementById('titleArea').style.zIndex = 10000;
				document.getElementById('hand').style.display = "none";
				document.getElementById('tappy').style.display = "block";
				document.getElementById('tutorialBox').style.top = "26.6666%";
				document.getElementById('tutorialBox').style.left = "25.390625%";
				tutorialText = "Tap the titles to select a story."; break;
		case 4: document.getElementById('stories').style.zIndex = 10000;
				document.getElementById('titleArea').style.zIndex = 5555;
				document.getElementById('stories').innerHTML = "<div id='storyContainer'><div class='story' id='story0'><div class='title' id='title0'>No stories yet...</div></div></div>";
				document.getElementById("photoImg").src="images/Library/Previews/defaultPreview.png";
				document.getElementById('tutorialBox').style.top = "68.33333%";
				document.getElementById('tutorialBox').style.left = "61.5234375%";
				document.getElementById('tappy').style.display = "none";
				tutorialText = "Now here is your <br/>story page and it’s <br/>kind of empty!"; break;
		case 5: document.getElementById('stories').style.zIndex = 5555;
				document.getElementById('photo').style.zIndex = 5555;
				document.getElementById('add').style.zIndex = 10000;
				document.getElementById('tutorialBox').style.top = "55%";
				document.getElementById('tutorialBox').style.left = "26.3671875%";
				document.getElementById('tappy').style.display = "block";
				document.getElementById('tappy').style.top = "78.3333%";
				document.getElementById('tappy').style.left = "25.390625%";
				document.getElementById('tappy').style['-webkit-transform'] = "scaleY(-1) rotate(-90deg)";
				tutorialText = "Tap this button to add a new story!"; break;
	}
	document.getElementById('tutorialText').innerHTML = tutorialText;
}