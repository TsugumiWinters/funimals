window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

function initApp() {
	document.getElementById("back").onclick = function () {
		history.back();
	};
}