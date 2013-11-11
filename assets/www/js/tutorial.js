window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

sixPressed = false;
sevenPressed = false;
eightPressed = false;

function initApp() {
	document.getElementById("back").onclick = function () {
		history.back();
	};
	
	document.getElementById("skip").onclick = function () {
		window.location = "main.html";
	};
}