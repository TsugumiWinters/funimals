window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

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
			parent.toAccounts();
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
			var userName = document.getElementById('name').value;
			window.localStorage.setItem("user", userName);
			parent.toAge();
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

function saveData(theForm) {
	var userName = document.getElementById('name').value;
	window.localStorage.setItem("user", userName);
}