function initApp() {
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
	//window.localStorage.setItem("mute", "false");
	window.localStorage.setItem("tutorial", "false");
	mute = window.localStorage.getItem("mute");
	if (mute == "false")
		parent.playAudio("/android_asset/www/main_menu.mp3");
	
	var i=0;
	
	var newHTML = "<div class='button' id='addAccount'> <img id='addAccImg' src='images/Users/addUser.png' class='photo' width='100%' height='100%'> <input id='addAccTxt' value='Add Account' type='text' class='account' readOnly='true'> </div>";
	numAccounts = window.Connector.getNumUsers();
	if (numAccounts!=0)
	for (i=0;i<numAccounts;i++) {
		newAccount = "<div class='button' id='account"+i+"' onclick='buttonClicked("+i+")'>	<img id='accountPic"+i+"' src='images/Users/"+i+".jpg' class='photo' width='100%' height='100%'>	<input id='accountVal"+i+"' type='text' class='account' readOnly='true' value='"+window.Connector.getUserName(i).split(' ')[0]+"'> </div>";
		newHTML = newHTML.concat(newAccount);
	}
	document.getElementById("accounts").innerHTML = newHTML;
	
	document.getElementById("addAccount").onclick = function () {
		window.localStorage.setItem("userID", numAccounts);
		parent.toForms();
	};
}

function buttonClicked(i) {
	window.localStorage.setItem("user", window.Connector.getUserName(i));
	window.localStorage.setItem("userAge", window.Connector.getUserAge(i));
	window.localStorage.setItem("userGrade", window.Connector.getUserGrade(i));
	window.localStorage.setItem("userID", i);
	parent.toHome2();
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