function clickRegion(regionId){
    angular.element(document.getElementById('riskMap')).scope().changeTerritoryStyle(regionId);
}

function regionOver(regionId){
    angular.element(document.getElementById('riskMap')).scope().mouseOver(regionId);
}

function regionOut(regionId){
    angular.element(document.getElementById('riskMap')).scope().mouseOut(regionId);
}

function voidClick(){
    angular.element(document.getElementById('riskMap')).scope().voidClick();
    changeText("19-text");
}

function changeText(regionid) {
    //Android.clickRegion(regionid);
    //document.getElementById("mysvg").getElementById(regionid).className='player1color';
    // Get the Object by ID
    var a = document.getElementById("riskMap");
    // Get the SVG document inside the Object tag
    var svgDoc = a.contentDocument;
    // Get one of the SVG items by ID;
    //var qty = parseInt(document.getElementById("qty").value);
    var svgItem = svgDoc.getElementById(regionid);
    // Set the colour to something else
    //svgItem.attr("class","player1color");

    //svgItem.setAttribute('class','player1color');
    //var l = document.getElementById('thecolor')
    //var thecolor = l.options[l.selectedIndex].text;
    //svgItem.setAttribute('class',thecolor);
    svgItem.innerHTML = 'AA';
    //svgItem.setAttribute('fill',thecolor);

    //console.log("onload executed");
    //console.log(svgDoc.getElementById(regionid).getAttribute("xcoord"));

}