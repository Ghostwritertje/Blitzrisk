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
}

function changeTerritoryText(territoryTextId, text) {
    var map = document.getElementById("riskMap");
    var svgDoc = map.contentDocument;
    var svgItem = svgDoc.getElementById(territoryTextId);
    svgItem.innerHTML = text;
}