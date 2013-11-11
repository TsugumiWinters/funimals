window.oncontextmenu = function(evt) {
	evt.preventDefault();
};

backgroundSrc = 0;

function initApp() {
	window.onload = function() {
	window.ondragstart = function() { return false; } 
	
	sideBarElement = $('sideBar');
	sideBarHeadElement = $('sideBarHead');
	adultsTabElement = $('adultsTab');
	adultsTabHeadElement = $('adultsTabHead');
	kidsTabElement = $('kidsTab');
	kidsTabHeadElement = $('kidsTabHead');
	thingsTabElement = $('thingsTab')
	thingsTabHeadElement = $('thingsTabHead');
	sideBarHeadElement.onmousedown = sideBarHeadElement.ontouchstart = startDragSideBar;
	adultsTabHeadElement.onmousedown = adultsTabHeadElement.ontouchstart = startDragAdultsTab;
	kidsTabHeadElement.onmousedown = kidsTabHeadElement.ontouchstart = startDragKidsTab;
	thingsTabHeadElement.onmousedown = thingsTabHeadElement.ontouchstart = startDragThingsTab;
	
	adultsTabLeft = $('adultsTabLeft');
	adultsTabRight = $('adultsTabRight');
	adultsTabLeft.onmousedown = adultsTabLeft.ontouchstart = moveAdultsTabLeft;
	adultsTabRight.onmousedown = adultsTabRight.ontouchstart = moveAdultsTabRight;
	
	kidsTabLeft = $('kidsTabLeft');
	kidsTabRight = $('kidsTabRight');
	kidsTabLeft.onmousedown = kidsTabLeft.ontouchstart = moveKidsTabLeft;
	kidsTabRight.onmousedown = kidsTabRight.ontouchstart = moveKidsTabRight;
	
	thingsTabLeft = $('thingsTabLeft');
	thingsTabRight = $('thingsTabRight');
	thingsTabLeft.onmousedown = thingsTabLeft.ontouchstart = moveThingsTabLeft;
	thingsTabRight.onmousedown = thingsTabRight.ontouchstart = moveThingsTabRight;
	
	catManImage = $('catManImage');
	catManImage.onmousedown = catManImage.ontouchstart = catManImageMove;
	
	adultCharacters = $('adultCharacters');
	kidCharacters = $('kidCharacters');
	things = $('things');
	trash = $('trash');
	}
	
	document.getElementById("chooseBackground").onclick = function () {
		document.getElementById("changeBg").style.display = "block";
	};
	
	document.getElementById("bgOverlay").onclick = function () {
		document.getElementById("changeBg").style.display = "none";
	};
	
	document.getElementById("leftArrow").onclick = function () {
		if (backgroundSrc==0)
			backgroundSrc = 9;
		else backgroundSrc--;
		document.getElementById("selectBgImg").src="images/Stickers/Backgrounds/bg"+backgroundSrc+".png";
	};
	
	document.getElementById("rightArrow").onclick = function () {
		if (backgroundSrc==9)
			backgroundSrc = 0;
		else backgroundSrc++;
		document.getElementById("selectBgImg").src="images/Stickers/Backgrounds/bg"+backgroundSrc+".png";
	};
	
	document.getElementById("selectBg").onclick = function () {
		document.getElementById("pictureFrameImg").src="images/Stickers/Backgrounds/bg"+backgroundSrc+".png";
		document.getElementById("changeBg").style.display = "none";
	};
	
	document.getElementById("home").onclick = function () {
		window.location = "main.html"
	};
	
	document.getElementById("library").onclick = function () {
		window.location = "library.html"
	};
	
	document.getElementById("restart").onclick = function () {
		window.location = "pictureMaker.html"
	};
	
	document.getElementById("createStory").onclick = function () {
		window.location = "createStory.html"
	};
}

//function called when drag starts
		function dragIt(theEvent) {
		//tell the browser what to drag
		theEvent.dataTransfer.setData("Text", theEvent.target.id);
		}
		//function called when element drops
		function dropIt(theEvent) {
		//get a reference to the element being dragged
		var theData = theEvent.dataTransfer.getData("Text");
		//get the element
		var theDraggedElement = document.getElementById(theData);
		//add it to the drop element
		theEvent.target.appendChild(theDraggedElement);
		//instruct the browser to allow the drop
		theEvent.preventDefault();
		}

function catManImageMove(e) {

	if (e.type === 'touchstart') {
		catManImage.onmousedown = null;
		catManImage.ontouchmove = moveDrag;
		catManImage.ontouchend = function () {
			catManImage.ontouchmove = null;
			catManImage.ontouchend = null;
			catManImage.ontouchstart = catManImageMove; // necessary for Dolfin; this is a bug
		}
	} else {
		document.onmousemove = moveDrag;
		document.onmouseup = function () {
		//instruct the browser to allow the drop
			document.onmousemove = null;
			document.onmouseup = null;
		}
	}
	var pos = [catManImage.offsetLeft,catManImage.offsetTop];
	var origin = getCoors(e);


	function moveDrag (e) {
		var currentPos = getCoors(e);
		var deltaX = currentPos[0] - origin[0];
		catManImage.style.left = (pos[0] + deltaX) + 'px';
		var deltaY = currentPos[1] - origin[1];
		catManImage.style.top  = (pos[1] + deltaY) + 'px';
		console.log(deltaX);
		return false;
		/* cancels scrolling; Android 2.1 needs this ontouchstart, but that's a bug.
		iPhone and Android 2.2 allow it ontouchstart, but also ontouchmove
		Dolfin requires it ontouchmove */
	}

	function getCoors(e) {
		var coors = [];
		if (e.touches && e.touches.length) { 
			coors[0] = e.touches[0].clientX;
			coors[1] = e.touches[0].clientY;
		} else {
			coors[0] = e.clientX;
			coors[1] = e.clientY;
		}
		return coors;
	}
}


function moveAdultsTabLeft(e){
	document.onmouseup = function () {
		if(adultCharacters.offsetLeft<0)
			adultCharacters.style.left = (adultCharacters.offsetLeft + 900) + 'px';
		document.onmouseup = null;
	}
}

function moveAdultsTabRight(e){
	document.onmouseup = function () {
		if(adultCharacters.offsetLeft>-1800)
			adultCharacters.style.left = (adultCharacters.offsetLeft - 900) + 'px';
		document.onmouseup = null;
	}
}

function moveKidsTabLeft(e){
	document.onmouseup = function () {
		if(kidCharacters.offsetLeft<0)
			kidCharacters.style.left = (kidCharacters.offsetLeft + 900) + 'px';
		document.onmouseup = null;
	}
}

function moveKidsTabRight(e){
	document.onmouseup = function () {
		if(kidCharacters.offsetLeft>-1800)
			kidCharacters.style.left = (kidCharacters.offsetLeft - 900) + 'px';
		document.onmouseup = null;
	}
}

function moveThingsTabLeft(e){
	document.onmouseup = function () {
		if(things.offsetLeft<0)
			things.style.left = (things.offsetLeft + 900) + 'px';
		document.onmouseup = null;
	}
}

function moveThingsTabRight(e){
	document.onmouseup = function () {
		if(things.offsetLeft>-4500)
			things.style.left = (things.offsetLeft - 900) + 'px';
		document.onmouseup = null;
	}
}

var sideBarElement;
function startDragSideBar(e) {

	if (e.type === 'touchstart') {
		sideBarElement.onmousedown = null;
		sideBarElement.ontouchmove = moveDrag;
		sideBarElement.ontouchend = function () {
			sideBarElement.ontouchmove = null;
			sideBarElement.ontouchend = null;
			sideBarElement.ontouchstart = startDragSideBar; // necessary for Dolfin; this is a bug
		}
	} else {
		document.onmousemove = moveDrag;
		document.onmouseup = function () {
			document.onmousemove = null;
			document.onmouseup = null;
		}
	}
	var pos = [sideBarElement.offsetLeft,sideBarElement.offsetTop];
	var origin = getCoors(e);


	function moveDrag (e) {
		var currentPos = getCoors(e);
		var deltaX = currentPos[0] - origin[0];
		if(pos[0] + deltaX < 0 && pos[0] + deltaX > -384)
		sideBarElement.style.left = (pos[0] + deltaX) + 'px';
		return false;
		/* cancels scrolling; Android 2.1 needs this ontouchstart, but that's a bug.
		iPhone and Android 2.2 allow it ontouchstart, but also ontouchmove
		Dolfin requires it ontouchmove */
	}

	function getCoors(e) {
		var coors = [];
		if (e.touches && e.touches.length) { 
			coors[0] = e.touches[0].clientX;
			coors[1] = e.touches[0].clientY;
		} else {
			coors[0] = e.clientX;
			coors[1] = e.clientY;
		}
		return coors;
	}
}

var adultsTabElement;
function startDragAdultsTab(e) {

	if (e.type === 'touchstart') {
		adultsTabElement.onmousedown = null;
		adultsTabElement.ontouchmove = moveDrag;
		adultsTabElement.ontouchend = function () {
			adultsTabElement.ontouchmove = null;
			adultsTabElement.ontouchend = null;
			adultsTabElement.ontouchstart = startDragAdultsTab; // necessary for Dolfin; this is a bug
		}
	} else {
		document.onmousemove = moveDrag;
		document.onmouseup = function () {
			document.onmousemove = null;
			document.onmouseup = null;
		}
	}
	var pos = [adultsTabElement.offsetLeft,adultsTabElement.offsetTop];
	var origin = getCoors(e);


	function moveDrag (e) {
		var currentPos = getCoors(e);
		var deltaY = currentPos[1] - origin[1];
		if(pos[1] + deltaY >370 && pos[1] + deltaY<553)
		adultsTabElement.style.top  = (pos[1] + deltaY) + 'px';
		return false;
		/* cancels scrolling; Android 2.1 needs this ontouchstart, but that's a bug.
		iPhone and Android 2.2 allow it ontouchstart, but also ontouchmove
		Dolfin requires it ontouchmove */
	}

	function getCoors(e) {
		var coors = [];
		if (e.touches && e.touches.length) { 
			coors[0] = e.touches[0].clientX;
			coors[1] = e.touches[0].clientY;
		} else {
			coors[0] = e.clientX;
			coors[1] = e.clientY;
		}
		return coors;
	}
}

var kidsTabElement;
function startDragKidsTab(e) {

	if (e.type === 'touchstart') {
		kidsTabElement.onmousedown = null;
		kidsTabElement.ontouchmove = moveDrag;
		kidsTabElement.ontouchend = function () {
			kidsTabElement.ontouchmove = null;
			kidsTabElement.ontouchend = null;
			kidsTabElement.ontouchstart = startDragKidsTab; // necessary for Dolfin; this is a bug
		}
	} else {
		document.onmousemove = moveDrag;
		document.onmouseup = function () {
			document.onmousemove = null;
			document.onmouseup = null;
		}
	}
	var pos = [kidsTabElement.offsetLeft,kidsTabElement.offsetTop];
	var origin = getCoors(e);


	function moveDrag (e) {
		var currentPos = getCoors(e);
		var deltaY = currentPos[1] - origin[1];
		if(pos[1] + deltaY >370 && pos[1] + deltaY<553)
		kidsTabElement.style.top  = (pos[1] + deltaY) + 'px';
		return false;
		/* cancels scrolling; Android 2.1 needs this ontouchstart, but that's a bug.
		iPhone and Android 2.2 allow it ontouchstart, but also ontouchmove
		Dolfin requires it ontouchmove */
	}

	function getCoors(e) {
		var coors = [];
		if (e.touches && e.touches.length) { 
			coors[0] = e.touches[0].clientX;
			coors[1] = e.touches[0].clientY;
		} else {
			coors[0] = e.clientX;
			coors[1] = e.clientY;
		}
		return coors;
	}
}

var thingsTabElement;
function startDragThingsTab(e) {

	if (e.type === 'touchstart') {
		thingsTabElement.onmousedown = null;
		thingsTabElement.ontouchmove = moveDrag;
		thingsTabElement.ontouchend = function () {
			thingsTabElement.ontouchmove = null;
			thingsTabElement.ontouchend = null;
			thingsTabElement.ontouchstart = startDragThingsTab; // necessary for Dolfin; this is a bug
		}
	} else {
		document.onmousemove = moveDrag;
		document.onmouseup = function () {
			document.onmousemove = null;
			document.onmouseup = null;
		}
	}
	var pos = [thingsTabElement.offsetLeft,thingsTabElement.offsetTop];
	var origin = getCoors(e);


	function moveDrag (e) {
		var currentPos = getCoors(e);
		var deltaY = currentPos[1] - origin[1];
		if(pos[1] + deltaY >370 && pos[1] + deltaY<553)
		thingsTabElement.style.top  = (pos[1] + deltaY) + 'px';
		return false;
		/* cancels scrolling; Android 2.1 needs this ontouchstart, but that's a bug.
		iPhone and Android 2.2 allow it ontouchstart, but also ontouchmove
		Dolfin requires it ontouchmove */
	}

	function getCoors(e) {
		var coors = [];
		if (e.touches && e.touches.length) { 
			coors[0] = e.touches[0].clientX;
			coors[1] = e.touches[0].clientY;
		} else {
			coors[0] = e.clientX;
			coors[1] = e.clientY;
		}
		return coors;
	}
}



function $(id) {
	return document.getElementById(id);
}