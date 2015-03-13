/**
 * javascript that catches svg events and sends them to the gamecontroller
 */
function clickRegion(regionId){
    angular.element(document.getElementById('riskMap')).scope().selectRegion(regionId);
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

function changeTerritoryText(id, text) {
    var territoryTextId = id.toString().concat("-text");
    var map = document.getElementById("riskMap");
    var svgDoc = map.contentDocument;
    var svgItem = svgDoc.getElementById(territoryTextId);
    svgItem.innerHTML = text;
}