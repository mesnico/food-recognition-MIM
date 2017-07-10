function imageForm(){
	document.getElementById("image_link").disabled=true;
	document.getElementById("imgQuery").disabled=false;
	document.getElementById("linkForm").style.display="none";
	document.getElementById("imageForm").style.display="block";
}


function linkFunction(){
	document.getElementById("imgQuery").disabled=true;
	document.getElementById("image_link").disabled=false;
	document.getElementById("imageForm").style.display="none";
	document.getElementById("linkForm").style.display="block";
}