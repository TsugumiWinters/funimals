function initApp() {
	backButton = document.getElementById("back");
	window.oncontextmenu = function(evt) {
		evt.preventDefault();
	};
	
	/*if (window.localStorage.getItem("fromMain")=="true") {
		document.getElementById("clickBuster").style.display = "block";
		document.getElementById("clickBuster").onclick = function() {
			document.getElementById("clickBuster").style.display = "none";
		};
		window.localStorage.setItem("fromMain", "false");
	}*/

	mute = window.localStorage.getItem("mute");
	if (mute=="false")
		parent.playAudio("/android_asset/www/library.mp3");

	var i=0;
	var newHTML = "";
	//document.getElementById('bookContainer').style.width = ""+(((numAccounts) * 490)+(4 * numAccounts))+"px";
	
	numAccounts = window.Connector.getNumUsers();
	
	if (numAccounts!=0)
	for (i=0;i<numAccounts;i++) {
		libraryBook = "<div class='book' id='book"+i+"' onclick='bookClicked("+i+")'>	<img src='images/Library/book.png' class='photo' width='100%' height='100%'> <input id='accountVal"+i+"' type='text' class='author' readOnly='true' value='"+window.Connector.getUserName(i).split(' ')[0]+"'> </div>";
		newHTML = newHTML.concat(libraryBook);
	}
	
	document.getElementById("books").innerHTML = newHTML;
	
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
	
	backButton.ontouchstart = function (e) {
		touchStart(e, backButton);
	};
	backButton.ontouchend = function(e) {
		e.preventDefault();
		if (started == true && ((containerX >= buttonRect.left && containerX <= buttonRect.right) && (containerY >= buttonRect.top && containerY <= buttonRect.bottom))) {
			parent.toHome();
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

function bookClicked(i) {
	window.localStorage.setItem("authorID", i);
	window.localStorage.setItem("author", window.Connector.getUserName(i));
	window.localStorage.setItem("ageVal", window.Connector.getUserAge(i));
	window.localStorage.setItem("gradeVal", window.Connector.getUserGrade(i));
	//window.location = "userBook.html";
	parent.toUserBook();
}