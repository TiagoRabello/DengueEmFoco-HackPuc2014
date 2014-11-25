Parse.initialize("xTCMviuSWCcRWP4CBcSyKSBZerdo0OjqDgMZnWRO", "FwS5OjW9IYt3SakNu5pRKCUF9yjaEA6LmSzN1rY2");
var TestObject = Parse.Object.extend("TestObject");
var markers = [];
function getRandomRio() {
    var lat = -22.9 - Math.random() * 0.1;
    var lon = -43.18 - Math.random() * 0.07;
    return new Parse.GeoPoint(lat, lon);
}

function savePoint() {//lat, lon) {
    var testObject = new TestObject();
    var point = getRandomRio(); //new Parse.GeoPoint({latitude: lat, longitude: lon});
    testObject.set('pos', point);
    testObject.save();
}


function showIconCloud() {
    $('.cluster').remove();
    for (var i = 0; i < markers.length; i++) {
        markers[i].opacity = 1;
        markers[i].setMap(map);
    }
    google.maps.event.addListener(map, 'zoom_changed', function () {
        $('.cluster').remove();
        for (i = 0; i < markers.length; i++) {
            markers[i].opacity = 1;
            markers[i].setMap(map);
        }
    });
}

function hideIconCloud() {
    new MarkerClusterer(map, markers);
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    //clearListeners(map, 'zoom_changed');
}

function toggleVisualization() {
    if (typeof toggleVisualization.index === 'undefined') {
        toggleVisualization.index = 0;
    }
    if (typeof toggleVisualization.functions === 'undefined') {
        toggleVisualization.functions = [];
        toggleVisualization.functions.push(function () {
            showIconCloud();
        });
        toggleVisualization.functions.push(function () {
            hideIconCloud();
        });
    }
    (toggleVisualization.functions[toggleVisualization.index])();
    toggleVisualization.index = (toggleVisualization.index + 1) % toggleVisualization.functions.length;
    return true;
}


function getPoints(southwest, northeast) {
    var query = new Parse.Query("RioData");
    //query.withinGeoBox("pos", southwest, northeast);
    query.limit(1000);
    query.descending("date");
    query.find({
        success: function (pointsInGeoBox) {
            for (var i = 0; i < pointsInGeoBox.length; i++) {
                // console.log(pointsInGeoBox[i].get('pos')._latitude, pointsInGeoBox[i].get('pos')._longitude);
                var myLatlng = new google.maps.LatLng(pointsInGeoBox[i].get('pos')._latitude, pointsInGeoBox[i].get('pos')._longitude);
                var marker = new google.maps.Marker({
                    position: myLatlng,
                    //opacity: 0,
                    title: ""
                });
                marker.setIcon('disk_1.png');
                markers.push(marker);
                marker.setMap(map);
            }
           // var markerCluster = new MarkerClusterer(map, markers);
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
    styles = [ { "featureType": "road", "stylers": [ { "visibility": "simplified" } ] } ];

    map.setOptions({styles: styles});
}

var map;
$(function () {
    initialize();
    getPoints(southwest, northeast);
});


$(function () {
  //  $('#toggler').on('click', toggleVisualization);
});