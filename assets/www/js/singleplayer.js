window.oncontextmenu = function(evt) {
	evt.preventDefault();
};
	
var muted = window.localStorage.getItem("muted");
var bgNum = window.localStorage.getItem("bgNum");
var slipper2Num = window.localStorage.getItem("slipper2Num");
var scoreVal = 0;
var bestVal;
alert(window.localStorage.getItem("bestVal"));
if (window.localStorage.getItem("bestVal") == null) {
	bestVal = 0;
	window.localStorage.setItem("bestVal", bestVal);
}
else
	bestVal = window.localStorage.getItem("bestVal");	

/*var muted = false;
var bgNum = 3;
var slipper2Num = 4;
var scoreVal = 0;
var bestVal = 0;*/

var watchID = null;
            //wait for PhoneGap to load
            document.addEventListener("deviceready", loaded, false);
            // PhoneGap is ready
            function loaded() {
				
                startWatch();
				setBackground();
				setSlipper();
				initApp();
				
            }
            // Start watching the acceleration
            function startWatch() {
                var previousReading = {
                    x: null,
                    y: null,
                    z: null
                }
                navigator.accelerometer.watchAcceleration(function (acceleration) {
                  var changes = {},
                  bound = 0.2;
                  if (previousReading.x !== null) {
                      changes.x = Math.abs(previousReading.x, acceleration.x);
                      changes.y = Math.abs(previousReading.y, acceleration.y);
                      changes.z = Math.abs(previousReading.z, acceleration.z);
                  }
                  if (changes.x > bound && changes.y > bound && changes.z > bound) {
                    shaken();
                  }
                  previousReading = {
                  x: reading.x,
                  y: reading.y,
                  z: reading.z
                  }
                  }, onError, { frequency: 2000 });
            }
            function shaken(){
                var angle = getRotationDegrees(document.getElementById('direction'));
		if(angle>=300 && angle<=360)
			angle = angle - 360;
			
		
		if (slipperClick == false) {
			slipperClick = true;
			$("#direction").toggle();
			
			console.log("angle" + angle);			
			
			//$("#slipper").css({'-webkit-animation-name': 'throwSlipper'});
			$("#slipper").addClass('throw');
			
			var x2=(angle/60*338)+338;
			var x1 = (x2+338)/2;
			x2 = x2 + windValue*60;
			x1 = x1 - windValue*30;
			var curve = new CurveAnimator(
			  [338,1025], [x2,160],
			  [x1,595], [x1,595]
			);

			var o = document.getElementById('slipper');
			o.style.position = 'absolute';

			curve.animate(0.5, function(point,angle){
			  o.style.left = point.x+"px";
			  o.style.top  = point.y+"px";
			});
			
			
			setTimeout(function() {
				checkForPoints();
				o.style.left = "338px";
				o.style.top  = "1025px";
				$("#slipper").removeClass('throw');
				$("#direction").toggle();
				slipperClick = false;
				windValue = (-5 + (Math.random() * ((5 - -5) + 1))).toFixed(1);
				if (windValue < 0) {
					$("#leftArrow").css({'display': 'inline'});
					$("#rightArrow").css({'display': 'none'});
				}
				else {
					$("#leftArrow").css({'display': 'none'});
					$("#rightArrow").css({'display': 'inline'});
				}
				document.getElementById('value').innerHTML = Math.abs(windValue);
			}, 2500);
		}
            }
            // Error
            function onError() {
                alert('onError!');
            }
	
	
function initApp() {	
	var slipperClick = false;
	
	$("#direction").toggle();
	windValue = (-5 + (Math.random() * ((5 - -5) + 1))).toFixed(1);
	if (windValue < 0) {
		$("#leftArrow").css({'display': 'inline'});
		$("#rightArrow").css({'display': 'none'});
	}
	else {
		$("#leftArrow").css({'display': 'none'});
		$("#rightArrow").css({'display': 'inline'});
	}
	document.getElementById('value').innerHTML = Math.abs(windValue);
	
	scoreVal = 0;
	bestVal = 0;
	document.getElementById('score').innerHTML = scoreVal;
	document.getElementById('best').innerHTML = bestVal;
	
	document.getElementById("pause").onclick = function () {
		document.getElementById("direction").style.WebkitAnimationPlayState="paused";
		$('#dark').toggle();
		$('#resume').toggle();
		if (muted)
			$('#muted').toggle();
		else if (!muted)
				$('#mute').toggle();
		$('#quit').toggle();
	};
	
	document.getElementById("resume").onclick = function () {
		document.getElementById("direction").style.WebkitAnimationPlayState="running";
		$('#dark').toggle();
		$('#resume').toggle();
		if (muted)
			$('#muted').toggle();
		else if (!muted)
				$('#mute').toggle();
		$('#quit').toggle();
	};
	
	document.getElementById("mute").onclick = function () {
		$('#mute').toggle();
		$('#muted').toggle();
		muted = true;
	};
	
	document.getElementById("muted").onclick = function () {
		$('#mute').toggle();
		$('#muted').toggle();
		muted = false;
	};
	
	document.getElementById("quit").onclick = function () {
		window.localStorage.setItem("bestVal", bestVal);
		window.location = "index.html";
	};
	
	document.getElementById("slipper").onclick = function () {			
		var angle = getRotationDegrees(document.getElementById('direction'));
		if(angle>=300 && angle<=360)
			angle = angle - 360;
			
		
		if (slipperClick == false) {
			slipperClick = true;
			$("#direction").toggle();
			
			console.log("angle" + angle);			
			
			//$("#slipper").css({'-webkit-animation-name': 'throwSlipper'});
			$("#slipper").addClass('throw');
			
			var x2=(angle/60*338)+338;
			var x1 = (x2+338)/2;
			x2 = x2 + windValue*60;
			x1 = x1 - windValue*30;
			var curve = new CurveAnimator(
			  [338,1025], [x2,160],
			  [x1,595], [x1,595]
			);

			var o = document.getElementById('slipper');
			o.style.position = 'absolute';

			curve.animate(0.5, function(point,angle){
			  o.style.left = point.x+"px";
			  o.style.top  = point.y+"px";
			});
			
			
			setTimeout(function() {
				checkForPoints();
				o.style.left = "338px";
				o.style.top  = "1025px";
				$("#slipper").removeClass('throw');
				$("#direction").toggle();
				slipperClick = false;
				windValue = (-5 + (Math.random() * ((5 - -5) + 1))).toFixed(1);
				if (windValue < 0) {
					$("#leftArrow").css({'display': 'inline'});
					$("#rightArrow").css({'display': 'none'});
				}
				else {
					$("#leftArrow").css({'display': 'none'});
					$("#rightArrow").css({'display': 'inline'});
				}
				document.getElementById('value').innerHTML = Math.abs(windValue);
			}, 2500);
		}
	};
}

function getRotationDegrees (element) {
    // get the computed style object for the element
    var style = window.getComputedStyle(element, null);
    // this string will be in the form 'matrix(a, b, c, d, tx, ty)'
    var transformString = style['-webkit-transform']
                       || style['-moz-transform']
                       || style['transform'] ;
    if (!transformString || transformString == 'none')
        return 0;
    var splits = transformString.split(',');
    // parse the string to get a and b
    var a = parseFloat(splits[0].substr(7));
    var b = parseFloat(splits[1]);
    // doing atan2 on b, a will give you the angle in radians
    var rad = Math.atan2(b, a);
    var deg = 180 * rad / Math.PI;
    // instead of having values from -180 to 180, get 0 to 360
    if (deg < 0) deg += 360;
    return deg;
}

function CurveAnimator(from,to,c1,c2){
		this.path = document.createElementNS('http://www.w3.org/2000/svg','path');
		if (!c1) c1 = from;
		if (!c2) c2 = to;
		this.path.setAttribute('d','M'+from.join(',')+'C'+c1.join(',')+' '+c2.join(',')+' '+to.join(','));
		this.updatePath();
		CurveAnimator.lastCreated = this;
	}
	CurveAnimator.prototype.animate = function(duration,callback,delay){
		var curveAnim = this;
		// TODO: Use requestAnimationFrame if a delay isn't passed
		if (!delay) delay = 1/40;
		clearInterval(curveAnim.animTimer);
		var startTime = new Date;
		curveAnim.animTimer = setInterval(function(){
			var now = new Date;
			var elapsed = (now-startTime)/1000;
			var percent = elapsed/duration;
			if (percent>=1){
				percent = 1;
				clearInterval(curveAnim.animTimer);
			}
			var p1 = curveAnim.pointAt(percent-0.01),
			    p2 = curveAnim.pointAt(percent+0.01);
			callback(curveAnim.pointAt(percent),Math.atan2(p2.y-p1.y,p2.x-p1.x)*180/Math.PI);
		},delay*1000);
	};
	CurveAnimator.prototype.stop = function(){
		clearInterval(this.animTimer);
	};
	CurveAnimator.prototype.pointAt = function(percent){
		return this.path.getPointAtLength(this.len*percent);
	};
	CurveAnimator.prototype.updatePath = function(){
		this.len = this.path.getTotalLength();
	};
	CurveAnimator.prototype.setStart = function(x,y){
		var M = this.path.pathSegList.getItem(0);
		M.x = x; M.y = y;
		this.updatePath();
		return this;
	};
	CurveAnimator.prototype.setEnd = function(x,y){
		var C = this.path.pathSegList.getItem(1);
		C.x = x; C.y = y;
		this.updatePath();
		return this;
	};
	CurveAnimator.prototype.setStartDirection = function(x,y){
		var C = this.path.pathSegList.getItem(1);
		C.x1 = x; C.y1 = y;
		this.updatePath();
		return this;
	};
	CurveAnimator.prototype.setEndDirection = function(x,y){
		var C = this.path.pathSegList.getItem(1);
		C.x2 = x; C.y2 = y;
		this.updatePath();
		return this;
	};

function checkForPoints() {	
	var curleft = curtop = 0;
	var obj = document.getElementById("slipper");
	
	if (obj.offsetParent) {
		do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	}
	
	if (curleft>270&&curleft<404){
		scoreVal++;
		navigator.notification.vibrate(1000);
	}
	else scoreVal = 0;
	if (scoreVal > bestVal)
		bestVal++;
		
	document.getElementById('score').innerHTML = scoreVal;
	document.getElementById('best').innerHTML = bestVal;
}


if (document.images) {
	img1_on = new Image();  img1_on.src ="images/resumePressed.png"; 
	img1_off = new Image();  img1_off.src="images/resumeButton.png"; 
	img3_on = new Image();  img3_on.src ="images/mutePressed.png"; 
	img3_off = new Image();  img3_off.src="images/muteButton.png"; 
	img4_on = new Image();  img4_on.src ="images/mutedPressed.png"; 
	img4_off = new Image();  img4_off.src="images/mutedButton.png"; 
}

function movr(k) {
 if (document.images) 
  eval('document.img'+k+'.src=img'+k+'_on.src');
}

function mup(k) {
 if (document.images) 
  eval('document.img'+k+'.src=img'+k+'_off.src');
}

function setBackground() {
	document.getElementById("bgImg").src="images/bg_"+bgNum+".png";
}

function setSlipper() {
	document.getElementById("slipperImg").src="images/slipper"+slipper2Num+".png";
}