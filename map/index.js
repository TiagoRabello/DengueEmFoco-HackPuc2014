Parse.initialize("xTCMviuSWCcRWP4CBcSyKSBZerdo0OjqDgMZnWRO", "FwS5OjW9IYt3SakNu5pRKCUF9yjaEA6LmSzN1rY2");
var TestObject = Parse.Object.extend("TestObject");



function getRandomRio() {
    var lat = -22.9 - Math.random() * 0.1;
    var lon = -43.18 - Math.random() * 0.07;
    return new Parse.GeoPoint(lat, lon);
}

function savePoint() {//lat, lon) {
    var testObject = new TestObject();
    var point = getRandomRio();//new Parse.GeoPoint({latitude: lat, longitude: lon});
    testObject.set('pos', point);
    testObject.save();
}


//setInterval( savePoint, 100 );


function getPoints(southwest, northeast) {
    var query = new Parse.Query("TestObject");
    query.withinGeoBox("pos", southwest, northeast);
    query.find({
        success: function (pointsInGeoBox) {
            var markers = [];
            for (var i = 0; i < pointsInGeoBox.length; i++) {
                console.log(pointsInGeoBox[i].get('pos')._latitude, pointsInGeoBox[i].get('pos')._longitude);
                var myLatlng = new google.maps.LatLng(pointsInGeoBox[i].get('pos')._latitude, pointsInGeoBox[i].get('pos')._longitude);
                var marker = new google.maps.Marker({
                    position: myLatlng,
                    opacity: 0,
                    title: ""
                });
                markers.push( marker );
                marker.setMap(map);
            }
            var markerCluster = new MarkerClusterer(map, markers);

// To add the marker to the map, call setMap();
            
        }
    });
}



var southwest = new Parse.GeoPoint(-22.9986368, -43.2526113);
var northeast = new Parse.GeoPoint(-22.9302103, -43.1898141);








function initialize() {
    var mapOptions = {
        center: new google.maps.LatLng(-22.978823, -43.233249),
        zoom: 14,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    map = new google.maps.Map(document.getElementById("map-canvas"),
            mapOptions);
}

var map;
$(function () {
    initialize();
    getPoints(southwest, northeast);
});


