window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

function initApp() {
	/*document.getElementById("back").ontouchstart = function () {
		history.back();
	};*/
	backButton = document.getElementById("back");
	textArea = document.getElementById("textArea");
	text = document.getElementById("text");
	
	//alert("font size: "+parseFloat(window.getComputedStyle(text, null).getPropertyValue('font-size')));
	while(text.offsetHeight < (textArea.offsetHeight*.87)) {
		//alert("text height: "+text.offsetHeight+"; textArea height: "+textArea.offsetHeight);
		text.style.fontSize = (parseFloat(window.getComputedStyle(text, null).getPropertyValue('font-size')) + 1) +"px";
	}
	
	backButton.ontouchstart = function () {
		buttonDown(backButton);
	};
	/*backButton.onmouseout = function () {
		
	};
	backButton.onmouseover = function () {
		backButton.style['-webkit-transform'] = "scale(.95,.95)";
	};*/
	backButton.ontouchend = function () {
		buttonUp(backButton);
		history.back();
	};
	
	$(function() {
    while( $('#fitin div').height() > $('#fitin').height() ) {
        $('#fitin div').css('font-size', (parseInt($('#fitin div').css('font-size')) - 1) + "px" );
    }
});
}

function buttonDown(button) {
	button.style['-webkit-transform'] = "scale(.95,.95)";
	button.ontouchleave = function () {
		button.style['-webkit-transform'] = "scale(1,1)";
	};
}

function buttonUp(button) {
	button.style['-webkit-transform'] = "scale(1,1)";
}