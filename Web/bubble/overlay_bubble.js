
function Bubble(data, options) {
    this.data = data;
    if (options) 
		this.options = options 
	else 
		this.options = {
			radiusForPercentage:1000,
			text:{
				visible:false,
				minimumZoom:13,
				maximumZoom:15
			},
			bubble:{
				fill:{color:"#709ED9", opacity:0.6},
				stroke:{color:"#709ED9", weight:1, opacity:0.3}
			}
		};
	this.totalSize = this.getTotalSize(this.data);
}

Bubble.prototype = new google.maps.OverlayView;

Bubble.prototype.onAdd = function() {
	for (var row = 0; row < this.data.getNumberOfRows(); row++) 
		this.drawBubble(this.data, this.options, row);
}

Bubble.prototype.draw = function() {
	if (this.options.text.visible)
		for (var row = 0; row < this.data.getNumberOfRows(); row++) 
			this.drawText(this.data, this.options, row);
}

Bubble.prototype.getTotalSize = function(data) {
    var totalSize = 0;
    
	for (var row = 0; row < data.getNumberOfRows(); row++)
        totalSize += data.getValue(row, 1);
                
    return totalSize;
}

Bubble.prototype.drawBubble = function(data, options, row) {
	
    var sizeOfLocation = data.getValue(row, 1);
    var percentageOfTotal = (sizeOfLocation / this.totalSize) * 100;
			
    var radiusForLocation = options.radiusForPercentage * percentageOfTotal;
			
    var marker = new google.maps.Circle({
        center: new google.maps.LatLng(data.getValue(row, 2), data.getValue(row, 3)),
        fillColor:options.bubble.fill.color,
        fillOpacity:options.bubble.fill.opacity,
        strokeColour:options.bubble.stroke.color,
        strokeWeight:options.bubble.stroke.weight,
        strokeOpacity:options.bubble.stroke.opacity,
        radius:radiusForLocation
 });
	
	marker.setMap(this.getMap());
	
	var contentString = '<div id="content"><b>School Name:</b> ' + data.getValue(row, 0) + ' <b><p>Percent IEP students:</b> '+sizeOfLocation+' <b><p> Percent free-lunch:</b> ' + data.getValue(row, 4) + ' </div>';

		var infowindow = new google.maps.InfoWindow({
    		content: contentString, 
    		position: new google.maps.LatLng(data.getValue(row, 2), data.getValue(row, 3))
    		
		});	
	
		google.maps.event.addListener(marker, 'click', function() {
		if(onlyInfoWindow != null){
			onlyInfoWindow.close();
		}

    	infowindow.open(this.getMap());
    	onlyInfoWindow = infowindow;
    	
  	});
	
}	

Bubble.prototype.drawText = function(data, options, row) {

	var itemId = "_map_text_" + row;

	var textContainer = document.getElementById(itemId);
	
	if (this.map.getZoom() < options.text.minimumZoom) {
		if (textContainer)  {
			var parent = textContainer.parentNode;
			parent.removeChild(textContainer);
		}
		return;
	}
	
	if (!textContainer) {
		var textItem = document.createElement('span');
		textItem.className = 'label';
		textItem.innerHTML = data.getValue(row, 0) + ' (' + data.getValue(row, 1) + ')';

		textContainer = document.createElement('div');
		textContainer.id = itemId;
		textContainer.appendChild(textItem);
		textContainer.style.cssText = 'position: absolute;';

		var panes = this.getPanes();
		panes.overlayLayer.appendChild(textContainer);
	}
	
	var projection = this.getProjection();
    var position = projection.fromLatLngToDivPixel(new google.maps.LatLng(data.getValue(row, 2), data.getValue(row, 3)));
            
    textContainer.style.left = position.x+ 'px';
    textContainer.style.top = position.y + 'px';
}