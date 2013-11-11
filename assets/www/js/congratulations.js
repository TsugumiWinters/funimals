window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

prepPressed = false;
grade1Pressed = false;
grade2Pressed = false;
grade3Pressed = false;

nextEnable = false;

function initApp() {
	document.getElementById("background").onclick = function () {
		window.location = "userBook.html";
	};
}