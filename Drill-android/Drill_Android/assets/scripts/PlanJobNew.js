
//Hash Table Class
function HashTable(obj) {
    this.length = 0;
    this.items = {};
    for (var p in obj) {
        if (obj.hasOwnProperty(p)) {
            this.items[p] = obj[p];
            this.length++;
        }
    }

    this.Add = function (key, value) {
        this.items[key] = value;
    }


    this.setItem = function (key, value) {
        var previous = undefined;
        if (this.hasItem(key)) {
            previous = this.items[key];
        }
        else {
            this.length++;
        }
        this.items[key] = value;
        return previous;
    }

    this.getItem = function (key) {
        return this.hasItem(key) ? this.items[key] : undefined;
    }

    this.hasItem = function (key) {
        return this.items.hasOwnProperty(key);
    }

    this.removeItem = function (key) {
        if (this.hasItem(key)) {
            previous = this.items[key];
            this.length--;
            delete this.items[key];
            return previous;
        }
        else {
            return undefined;
        }
    }

    this.keys = function () {
        var keys = [];
        for (var k in this.items) {
            if (this.hasItem(k)) {
                keys.push(k);
            }
        }
        return keys;
    }

    this.values = function () {
        var values = [];
        for (var k in this.items) {
            if (this.hasItem(k)) {
                values.push(this.items[k]);
            }
        }
        return values;
    }

    this.each = function (fn) {
        for (var k in this.items) {
            if (this.hasItem(k)) {
                fn(k, this.items[k]);
            }
        }
    }

    this.clear = function () {
        this.items = {}
        this.length = 0;
    }
}


function HoverTip(rodNum, latitude, longitude, elevation, depth, pitch) {
    this.rodNum = rodNum;
    this.latitude = latitude;
    this.longitude = longitude;
    this.elevation = elevation;
    this.depth = depth;
    this.pitch = pitch;

    this.toString = function () {
        var ret = "Rod #:" + this.rodNum +
                   "Lat:" + this.latitude +
                   "Lon:" + this.longitude +
                   "Elev:" + this.elevation +
                   "Depth:" + this.depth +
                   "Pitch:" + this.pitch;
        return ret;
    };
}

var bGotHiddenValues = false;

var fBendRadius = 32.85; // 107.8 ft
var fRodLength = 3.048;// 10 ft
var fDesiredDepth = 3.048; //desired depth = 10 ft = 3.048 m

var latArrRods = [];
var lngArrRods = [];
var elevArrRods = [];
var pitchArrRods = [];
var distanceArray = [];

var hoverTipsArr = [];
var rodInfoArr = [];
// Initialize map and set it to home location
var markersArray = [];
var latLongArray = [];
var bUnitsInFeet = true;


// Global vars
// HashtableMarkers for markers
var HashTableMarkers = new HashTable();
var HashTableElevation = new HashTable();
// ID of current Marker
var current_Marker = 0;

var prevHeading = 0;
var minElevation = 9007199254740992;
var flightPath = null;
var polyLine = null;
var polyArray = [];

function initMap() {


    var mapCanvas = document.getElementById('map');
    var mapOptions = {
        center: new google.maps.LatLng(47.62044750723784, -122.19520390033722),
        zoom: 18,
        mapTypeId: google.maps.MapTypeId.SATELLITE
    }
    var map = new google.maps.Map(mapCanvas, mapOptions);
    map.setTilt(0);

    // Set the location to current location 
    //if (navigator.geolocation) {
    //    navigator.geolocation.getCurrentPosition(function (position) {
    //        initialLocation = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
    //        map.setCenter(initialLocation);
    //    });
    //}
    var input = /** @type {!HTMLInputElement} */(
       document.getElementById('pac-input'));


    // map the input autocomplete to google maps autocomplete
    var autocomplete = new google.maps.places.Autocomplete(input);
    autocomplete.bindTo('bounds', map);

    // Info window and marker which will be selected 
    var marker = new google.maps.Marker({
        map: map,
        anchorPoint: new google.maps.Point(0, -29)
    });

    var elevator = new google.maps.ElevationService;
    //var infowindow = new google.maps.InfoWindow({ map: map });
    // set types to all types of addresses
    autocomplete.setTypes("[]");
    // Add Listener for the place changed 
    autocomplete.addListener('place_changed', function () {
        // infowindow.close();
        marker.setVisible(false);
        var place = autocomplete.getPlace();
        if (!place.geometry) {
            window.alert("Autocomplete's returned place contains no geometry");
            return;
        }

        // If the place has a geometry, then present it on a map.
        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(18);  // Why 18? Because it looks good.
        }

        marker.setPosition(place.geometry.location);

    });

    google.maps.event.addListener(map, "click", function (event) {

        if (!bGotHiddenValues)
        {
            GetHiddenValues();
            // convert feets to meters since we are doing all calculations
            // in meters 
            fBendRadius = ConvertFeetToMeters(fBendRadius);
            fRodLength = ConvertFeetToMeters(fRodLength);


        }
        
        var marker_position = event.latLng;
        var marker = new google.maps.Marker({
            map: map,
            // draggable: true,
            animation: google.maps.Animation.DROP,
        });
        //google.maps.event.addListener(marker, 'dragend', function () {

        //    latLongArray = [];

        //    for (var i = 0; i < markersArray.length; i++)
        //    {
        //        latLongArray.push(new google.maps.LatLng(marker.position.lat(), marker.position.lng()));
        //    }

        //    polyLine.setPath(latLongArray);
        //});

        var distX = 0;
        var distY = 0;
        if (current_Marker >= 1) {
            var path = [markersArray[current_Marker - 1].getPosition(), marker_position];
            var heading = google.maps.geometry.spherical.computeHeading(path[0], path[1]);
            if (current_Marker > 1) {
                var angle = Math.abs(prevHeading - heading);
                if (angle > 180)
                    angle = Math.abs(360 - angle);
                var dist = GetDistance(markersArray[current_Marker - 1].getPosition(), marker_position);
                var maxAngle = (90 * dist) / fBendRadius;
                // check if the current marker can be put or not
                if (angle > maxAngle) {

                    alert("Angle is greater than your maximum bending radius cannot add the marker");
                    return;
                }
            }
            prevHeading = heading;
        }


        marker.setPosition(marker_position);
        HashTableMarkers.Add(current_Marker, marker);
        markersArray.push(marker);
        latLongArray.push(new google.maps.LatLng(marker.position.lat(), marker.position.lng()));
        polyLine = new google.maps.Polyline({
            path: latLongArray,
            geodesic: true,
            strokeColor: '#FF0000',
            strokeOpacity: 1.0,
            strokeWeight: 2
        });
        polyArray.push(polyLine);

        polyLine.setMap(map);

        // displayLocationElevation(event.latLng, elevator, infowindow);

        var contentString = "Test Info";


        // Add Info Window on right click
        var infowindow = new google.maps.InfoWindow({
            content: contentString
        });
        marker.addListener('rightclick', function () {
            infowindow.open(map, marker);
        });

        displayLocationElevation(event.latLng, elevator, infowindow);
        current_Marker++;

        // var markerCluster = new MarkerClusterer(map, markersArray);
    });




    function displayLocationElevation(location, elevator, infowindow) {

        // Initiate the location request
        elevator.getElevationForLocations({
            'locations': [location]
        }, function (results, status) {
            var elev = "0";
            var contentElevation = "";
            //infowindow.setPosition(location);
            if (status === google.maps.ElevationStatus.OK) {
                // Retrieve the first result
                if (results[0]) {
                    // current elevation -- because this function is called after the 
                    /// contents are added
                    contentElevation = "Elevation=" + results[0].elevation;
                    elev = results[0].elevation;
                    HashTableElevation.Add(current_Marker - 1, results[0].elevation);
                    if (results[0].elevation < minElevation)
                        minElevation = results[0].elevation;

                } else {
                    // add 0 as the elevation
                    contentElevation = "Elevation= 0";
                    HashTableElevation.Add(current_Marker - 1, 0);

                }
            } else {
                // add 0 as elevation
                contentElevation = "Elevation=0";
                HashTableElevation.Add(current_Marker - 1, 0);
                //infowindow.setContent('Elevation service failed due to: ' + status);
            }
            var lat = location.lat();
            var lon = location.lng();
            if (lat == null) {
                if (location.H != null)
                    lat = location.H;
                else
                    lat = location.G;
                if (location.L != null)
                    lon = location.L;
                else
                    lon = location.K;
            }

            var contentString = '<div id="content">' +
                                '<div id="siteNotice">' +
                                '</div>' +
                                '<div id="bodyContent">' +
                                '<p>Latitude = ' + lat + "</p><p>" +
                                "Longitude = "+ lon + "</p><p>" + contentElevation +
                               '</p>' +
                               '</div>' +
                               '</div>';
            infowindow.setContent(contentString);

        });
    }


}// end of initFunction

function DeleteLastMarker() {

    if (markersArray.length > 0) {
        var marker = markersArray.pop();
        marker.setMap(null);
        HashTableMarkers.removeItem(current_Marker);
        HashTableElevation.removeItem(current_Marker);
        latLongArray.pop();

        if (polyArray[polyArray.length - 1].getMap(map)) {
            polyArray[polyArray.length - 1].setMap(null);
        }
        polyArray.pop();

        hoverTipsArr.pop();

        current_Marker--;
    }
}


function GenerateGraph()
{
   // debugger;
    AddRodInfo();
    document.getElementById("hrefRodByRod").style.visibility = "visible";
    var markerPoints = [];
    var lat;
    var lon;
    var elev;
    var latArr = [];
    var lonArr = [];
    var latArrRodsVal = [];
    var lngArrRodsVal = [];
    var elevArr = [];
    var prevX = 0;
    var prevY = 0;
    var prevLat = 0;
    var prevLon = 0;

    // fill values for the marker points to be added on the graph
    for (var i = 0 ; i < current_Marker; i++)
    {
        lat = markersArray[i].position.lat();
        lon = markersArray[i].position.lng();
        elev = HashTableElevation.getItem(i);

        elevArr.push(elev);
        if (i == 0) {
            latArr.push(0);
            lonArr.push(0);
        }
        else {
            // calculate distance between 2 Latitude and 2 longitude
            var distX = GetDistanceX(prevLat, prevLon, lat, lon);
            var distY = GetDistanceY(prevLat, prevLon, lat, lon);
            distX += prevX;
            distY += prevY;
            latArr.push(distX);
            lonArr.push(distY);
            prevX = distX;
            prevY = distY;
        }
        prevLat = lat;
        prevLon = lon;

    }


    prevLat = 0;
    prevLon = 0;
    distX = 0;
    distY = 0;
    prevX = 0;
    prevY = 0;
    var elevArrRodsVal = [];
    rodInfoArr = [];
    // now you need to add the info for each rod
    for (var i = 0 ; i < latArrRods.length; i++) {
        lat = latArrRods[i];
        lng = lngArrRods[i];
        elev = elevArrRods[i];
        if (i == 0) {
            latArrRodsVal.push(0);
            lngArrRodsVal.push(0);
        }
        else {
            // calculate distance between 2 Latitude and 2 longitude
            var distX = GetDistanceX(prevLat, prevLon, lat, lng);
            var distY = GetDistanceY(prevLat, prevLon, lat, lng);
            if (distX < 0)
                alert("DistX is less than 0");
            if (distY < 0)
                alert("DistY is less than 0");
            distX += prevX;
            distY += prevY;
            latArrRodsVal.push(distX);
            lngArrRodsVal.push(distY);
            prevX = distX;
            prevY = distY;
        }
        prevLat = lat;
        prevLon = lng;
        var hoverInfo = {
            rodNum: i + 1,
            latitude: i + 1,
            longitude: i + 1,
            elevation: i + 1,
            depth: i + 1,
            pitch: pitchArrRods[i],
            toString: function () {
                var ret = "Rod #:" + this.rodNum +
                           "Lat:" + this.latitude +
                           "Lon:" + this.longitude +
                           "Elev:" + this.elevation +
                           "Depth:" + this.depth +
                           "Pitch:" + this.pitch;
                return ret;
            }
        };


        var hoverTip = new HoverTip(i + 1, lat, lng, elev, elevArrRods[0] - elev, pitchArrRods[i]);
        hoverTipsArr.push(hoverTip.toString());
        rodInfoArr.push(hoverTip);

    }
    data = [{
        x: latArr,
        y: lonArr,
        z: elevArr,
        mode: 'line',

        type: 'scatter3d',
        showScale: false,
        //colorscale: [['0', 'rgb(62,62,255)'], ['0.1', 'rgb(62,62,255)'], ['0.2', 'rgb(62,62,255)'], ['0.3', 'rgb(62,62,255)'], ['0.4', 'rgb(62,62,255)'], ['0.5', 'rgb(62,62,255)'], ['0.6', 'rgb(62,62,255)'], ['0.7', 'rgb(62,62,255)'], ['0.8', 'rgb(62,62,255)'], ['0.9', 'rgb(62,62,255)'], ['1', 'rgb(62,62,255)']],

    }
            ,
            {
                x: latArrRodsVal,
                y: lngArrRodsVal,
                z: elevArrRods,
                text: hoverTipsArr,
                mode: 'line',
                type: 'scatter3d',
                showScale: false,
                //colorscale: [['0', 'rgb(62,62,255)'], ['0.1', 'rgb(62,62,255)'], ['0.2', 'rgb(62,62,255)'], ['0.3', 'rgb(62,62,255)'], ['0.4', 'rgb(62,62,255)'], ['0.5', 'rgb(62,62,255)'], ['0.6', 'rgb(62,62,255)'], ['0.7', 'rgb(62,62,255)'], ['0.8', 'rgb(62,62,255)'], ['0.9', 'rgb(62,62,255)'], ['1', 'rgb(62,62,255)']],

            }

    ];
   
    bottomView(data);
    //topView(data);
    //sideView(data);

}

function bottomView( data)
{
    var layout = {
        title: 'Bore Path Plot',
        showlegend: false,

        autosize: false,
        width: 800,
        height: 600,
        margin: {
            l: 0,
            r: 0,
            b: 0,
            t: 65
        },
        scene: {
            aspectratio: {
                x: 1,
                y: 1,
                z: 1
            },
            camera: {
                center: {
                    x: 0,
                    y: 0,
                    z: 0
                },
                eye: {

                    // bottom view
                    x: -1.5,
                    y: -4.0,
                    z: -1.0
                    // top view
                    //x: -1.0,
                    //y:0.0,
                    //z: 2.0
                    // Side View
                    //x: 1.0,
                    //y: -4.0,
                    //z: -0.10
                },
                up: {
                    x: 0,
                    y: 0,
                    z: 1
                }
            },
            aspectmode: "data",
            xaxis: {
                type: 'linear',
                zeroline: false,
                title: 'Latitude'
            },
            yaxis: {
                type: 'linear',
                zeroline: false,
                title: 'Longitude'
            },
            zaxis: {
                type: 'linear',
                zeroline: false,
                title: 'Elevation'
            }
        }
    };

    Plotly.newPlot('myDiv', data, layout);
}

function topView(data) {
    var layout = {
        title: 'Bore Path Plot',
        showlegend: false,

        autosize: false,
        width: 800,
        height: 600,
        margin: {
            l: 0,
            r: 0,
            b: 0,
            t: 65
        },
        scene: {
            aspectratio: {
                x: 1,
                y: 1,
                z: 1
            },
            camera: {
                center: {
                    x: 0,
                    y: 0,
                    z: 0
                },
                eye: {

                    // bottom view
                    //x: -1.5,
                    //y: -4.0,
                    //z: -1.0
                    // top view
                    x: -1.0,
                    y:0.0,
                    z: 2.0
                    // Side View
                    //x: 1.0,
                    //y: -4.0,
                    //z: -0.10
                },
                up: {
                    x: 0,
                    y: 0,
                    z: 1
                }
            },
            aspectmode: "data",
            xaxis: {
                type: 'linear',
                zeroline: false,
                title: 'Latitude'
            },
            yaxis: {
                type: 'linear',
                zeroline: false,
                title: 'Longitude'
            },
            zaxis: {
                type: 'linear',
                zeroline: false,
                title: 'Elevation'
            }
        }
    };

    Plotly.newPlot('myDivTopView', data, layout);
}


function sideView(data) {
    var layout = {
        title: 'Bore Path Plot',
        showlegend: false,

        autosize: false,
        width: 800,
        height: 600,
        margin: {
            l: 0,
            r: 0,
            b: 0,
            t: 65
        },
        scene: {
            aspectratio: {
                x: 1,
                y: 1,
                z: 1
            },
            camera: {
                center: {
                    x: 0,
                    y: 0,
                    z: 0
                },
                eye: {

                    // bottom view
                    //x: -1.5,
                    //y: -4.0,
                    //z: -1.0
                    // top view
                    //x: -1.0,
                    //y: 0.0,
                    //z: 2.0
                    // Side View
                    x: 1.0,
                    y: -4.0,
                    z: -0.10
                },
                up: {
                    x: 0,
                    y: 0,
                    z: 1
                }
            },
            aspectmode: "data",
            xaxis: {
                type: 'linear',
                zeroline: false,
                title: 'Latitude'
            },
            yaxis: {
                type: 'linear',
                zeroline: false,
                title: 'Longitude'
            },
            zaxis: {
                type: 'linear',
                zeroline: false,
                title: 'Elevation'
            }
        }
    };

    Plotly.newPlot('myDivSideView', data, layout);
}


function AddRodByRodTable()
{
    var myNode = document.getElementById("divRodByRodTable");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
    var header = document.createElement("header"),
    h4 = document.createElement("h4");

    h4.textContent = "Rodwise Details";
    header.appendChild(h4);
    myNode.appendChild(header);

    var table = document.createElement('table');
    var trHead = document.createElement('tr');
    var tdRodHead = document.createElement('th');
    tdRodHead.innerText = "Rod #";
    trHead.appendChild(tdRodHead);
    var tdDistHead = document.createElement('th');
    if (bUnitsInFeet == true)
        tdDistHead.innerText = "Distance (ft.)";
    else
        tdDistHead.innerText = "Distance (m)";

    trHead.appendChild(tdDistHead);
    var tdDepthHead = document.createElement('th');
    if (bUnitsInFeet == true)
        tdDepthHead.innerText = "Depth (ft.)";
    else
        tdDepthHead.innerText = "Depth (m)";
    trHead.appendChild(tdDepthHead);
    var tdLatHead = document.createElement('th');
    tdLatHead.innerText = "Latitude";
    trHead.appendChild(tdLatHead);
    var tdLngHead = document.createElement('th');
    tdLngHead.innerText = "Longitude";
    trHead.appendChild(tdLngHead);
    var tdElevHead = document.createElement('th');
    if (bUnitsInFeet == true)
        tdElevHead.innerText = "Elevation (ft.)";
    else
        tdElevHead.innerText = "Elevation (m)";
    trHead.appendChild(tdElevHead);
    var tdPitchHead = document.createElement('th');
    tdPitchHead.innerText = "Pitch";
    trHead.appendChild(tdPitchHead);
    table.appendChild(trHead);

    for (var i = 0 ; i < rodInfoArr.length; i++) {

        // rod number 
        var tr = document.createElement('tr');

        var tdRodNum = document.createElement('td');
        tdRodNum.innerText = rodInfoArr[i].rodNum
        tr.appendChild(tdRodNum);
      
        // Distance 
        var tdDistance = document.createElement('td');
        if (bUnitsInFeet == true)
            tdDistance.innerText = convertToFeet(distanceArray[i]).toFixed(4);
        else
            tdDistance.innerText = distanceArray[i].toFixed(4);
        tr.appendChild(tdDistance);
        // Add distance here

        // Depth
        var tdDepth = document.createElement('td');
        if (bUnitsInFeet == true)
            tdDepth.innerText = convertToFeet(rodInfoArr[i].depth).toFixed(4);
        else
            tdDepth.innerText = rodInfoArr[i].depth.toFixed(4);
        tr.appendChild(tdDepth);
       
        // Latitude
        var tdLatitude = document.createElement('td');
        tdLatitude.innerText = rodInfoArr[i].latitude.toFixed(4);
        tr.appendChild(tdLatitude);

        // Longitude
        var tdLongitude = document.createElement('td');
        tdLongitude.innerText = rodInfoArr[i].longitude.toFixed(4);
        tr.appendChild(tdLongitude);
       

        //Elevation
        var tdElevation = document.createElement('td');
        if (bUnitsInFeet == true)
            tdElevation.innerText = convertToFeet( rodInfoArr[i].elevation).toFixed(4);
        else
            tdElevation.innerText = rodInfoArr[i].elevation.toFixed(4);
        tr.appendChild(tdElevation);


        // pitch
        var tdPitch = document.createElement('td');
        tdPitch.innerText = rodInfoArr[i].pitch;
        tr.appendChild(tdPitch);

       

        table.appendChild(tr);
       
    }
    table.className += "table table-striped";

    //for (var i = 1; i < 4; i++) {
    //    var tr = document.createElement('tr');

    //    var td1 = document.createElement('td');
    //    var td2 = document.createElement('td');

    //    var text1 = document.createTextNode('Text1');
    //    var text2 = document.createTextNode('Text2');

    //    td1.appendChild(text1);
    //    td2.appendChild(text2);
    //    tr.appendChild(td1);
    //    tr.appendChild(td2);

    //    table.appendChild(tr);
    //}
    
    myNode.appendChild(table);
}



// add rod info in the arrays for rods
function AddRodInfo()
{
    latArrRods = [];
    lngArrRods = [];
    elevArrRods = [];
    pitchArrRods = [];
    distanceArray = [];
    rodInfoArr = [];

    var totalDistance = getTotalDistance();
    // check if you can make the distance
    latArrRods.push(markersArray[0].position.lat());
    lngArrRods.push(markersArray[0].position.lng());
    elevArrRods.push(HashTableElevation.getItem(0));
    pitchArrRods.push("27%");

    // calculate number of rods needed for going down
    var elev = HashTableElevation.getItem(0);
    var depthToGo = elev - minElevation + fDesiredDepth;
    var lengthRequiredStart = depthToGo / sinDegrees(15.109);// 27% = 15.109 degrees
    var rodRequiredStart = lengthRequiredStart / fRodLength;
    rodRequiredStart = Math.ceil(rodRequiredStart);

    // Get the angle between first 2 markers 
    var marker1 = 0;
    var marker2 = 1;
    var startlat = markersArray[0].position.lat();
    var startlng = markersArray[0].position.lng();
    var path = [markersArray[0].getPosition(), markersArray[1].getPosition()];
    var heading = google.maps.geometry.spherical.computeHeading(path[0], path[1]);

    var distMarkers = getDistanceFromLatLngMeters(markersArray[marker1].position.lat(),
                                                         markersArray[marker1].position.lng(),
                                                         markersArray[marker2].position.lat(),
                                                         markersArray[marker2].position.lng());
    // add rods till they reach required depth
    var currentDistance = 0;
    var currentDepth = 0;
    distanceArray.push(0);
    var latLng;
    // add lat and lng for all the  rods required to go down
    for (var i = 1 ; i < rodRequiredStart; i++) 
    {
        var dist = (i * fRodLength) * cosDegrees(15.109); // calculate the distance covered
        var depth = (i * fRodLength) * sinDegrees(15.109); // calculate the depth
        latLng = getLatLngFromDistBearing(startlat, startlng, dist, heading); // get the new lat lng 
        // add values in the arrays
        latArrRods.push(latLng.lat());
        lngArrRods.push(latLng.lng());
        elevArrRods.push(elev - depth);
        pitchArrRods.push("27%");
        currentDepth = elev - depth;
        currentDistance = dist; // update current distance
        distanceArray.push(currentDistance);
        // check if you have over shooted the marker.
        if (dist > distMarkers) {
            // you need to change heading.
            marker1++;
            marker2++;
            distMarkers += getDistanceFromLatLngMeters(markersArray[marker1].position.lat(),
                                                  markersArray[marker1].position.lng(),
                                                  markersArray[marker2].position.lat(),
                                                  markersArray[marker2].position.lng());
            path = [markersArray[marker1].getPosition(), markersArray[marker2].getPosition()];
            heading = google.maps.geometry.spherical.computeHeading(path[0], path[1]);
        }
    }

    // now we have calculated the distance for going required depth
    // now we need to calculate the distance required to go up 
    // we have to go to the depth of last marker
    var lastElev = HashTableElevation.getItem(current_Marker - 1);
    var heightToGo = lastElev - currentDepth;
    var lastDistance = heightToGo / sinDegrees(15.109);
    rodRequiredEnd = lastDistance / fRodLength;
    rodRequiredEnd = Math.ceil(rodRequiredEnd);

    // calculate rods required to go middle part
    var midDistance = totalDistance - currentDistance - lastDistance;
    var rodRequiredMid = midDistance / fRodLength;
    rodRequiredMid = Math.ceil(rodRequiredMid);
    var curDistArr = currentDistance;
    for (var i = 0 ; i < rodRequiredMid; i++) {
        currentDistance += fRodLength; // calculate the distance covered
        curDistArr += fRodLength;
        distanceArray.push(curDistArr);
        latLng = getLatLngFromDistBearing(latLng.lat(), latLng.lng(), fRodLength, heading);
        // add values in the arrays
        latArrRods.push(latLng.lat());
        lngArrRods.push(latLng.lng());
        elevArrRods.push(currentDepth);
        pitchArrRods.push("0%");
        // check if you have over shooted the marker.
        if (currentDistance > distMarkers) {
            // you need to change heading.
            marker1++;
            marker2++;
            if (marker2 < current_Marker) {
                distMarkers = getDistanceFromLatLngMeters(markersArray[marker1].position.lat(),
                                                  markersArray[marker1].position.lng(),
                                                  markersArray[marker2].position.lat(),
                                                  markersArray[marker2].position.lng());
                path = [markersArray[marker1].getPosition(), markersArray[marker2].getPosition()];
                heading = google.maps.geometry.spherical.computeHeading(path[0], path[1]);
            }
            currentDistance = 0;

        }
    }

    for (var i = 1 ; i < rodRequiredEnd; i++) {
        if (currentDepth > lastElev)
            break;
        var dist = i * fRodLength * cosDegrees(15.109); // calculate the distance covered
        currentDistance += dist;
        curDistArr += dist;
        distanceArray.push(curDistArr);
        var depth = (i * fRodLength) * sinDegrees(15.109); // calculate the depth
        latLng = getLatLngFromDistBearing(latLng.lat(), latLng.lng(), dist, heading);
        // add values in the arrays
        latArrRods.push(latLng.lat());
        lngArrRods.push(latLng.lng());
        elevArrRods.push(currentDepth + depth);
        pitchArrRods.push("-27%");
        currentDepth = currentDepth + depth;
        // check if you have over shooted the marker.
        if (dist > distMarkers) {
            // //you need to change heading.
            //marker1++;
            //marker2++;
            //distMarkers += getDistanceFromLatLngMeters(markersArray[marker1].position.lat(),
            //                                      markersArray[marker1].position.lng(),
            //                                      markersArray[marker2].position.lat(),
            //                                      markersArray[marker2].position.lng());
            //path = [markersArray[marker1].getPosition(), markersArray[marker2].getPosition()];
            //heading = google.maps.geometry.spherical.computeHeading(path[0], path[1]);
        }
    }
}

// Get total distance between all the markers this is used in the function to 
// calculate if you can make the bore path based on the bend radius and rod length.
function getTotalDistance() {
    var lat1 = markersArray[0].position.lat();
    var lng1 = markersArray[0].position.lng();
    var dist = 0;
    for (var i = 1 ; i < current_Marker; i++) {
        var lat2 = markersArray[i].position.lat();
        var lng2 = markersArray[i].position.lng();
        dist += getDistanceFromLatLngMeters(lat1, lng1, lat2, lng2);
        lat1 = lat2;
        lng1 = lng2;
    }
    return dist;
}


// get Google Maps latitude and longitude of new point from Latitude, Longitude, Distance and bearing angle
function getLatLngFromDistBearing(lat1, lng1, dist, bearing) {
    var R = 6378.1 //Radius of the Earth
    bearing = toRadian(bearing);
    dist = dist / 1000;

    lat1 = toRadian(lat1);
    lng1 = toRadian(lng1);

    var lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist / R) +
     Math.cos(lat1) * Math.sin(dist / R) * Math.cos(bearing));

    var lng2 = lng1 + Math.atan2(Math.sin(bearing) * Math.sin(dist / R) * Math.cos(lat1),
             Math.cos(dist / R) - Math.sin(lat1) * Math.sin(lat2));

    lat2 = toDegrees(lat2);
    lng2 = toDegrees(lng2);
    return new google.maps.LatLng(lat2, lng2);
}

// self explainatory
function toRadian(angle) {
    return angle * Math.PI / 180;
}

// self explainatory
function toDegrees(rad) {
    return rad * (180 / Math.PI);
}

function sinDegrees(angle) { return Math.sin(angle / 180 * Math.PI); };
function cosDegrees(angle) { return Math.cos(angle / 180 * Math.PI); };

function convertToFeet(meters) { return meters * 3.2808; };

// This function is used to calculate if the path is possible
// between 2 points 
function IsPathPossible() {
    var lat1;
    var log1;
    var lat2;
    var log2;
    var distance = 0;
    // first calculate distance 
    for (var i = 1 ; i < current_Marker; i++) {
        // get the lat and lng for 2 points and calculate distance
        lat1 = markersArray[i - 1].position.lat();
        lon1 = markersArray[i - 1].position.lng();
        lat2 = markersArray[i].position.lat();
        lon2 = markersArray[i].position.lng();

        distance += getDistanceFromLatLngMeters(lat1, lon1, lat2, lon2);
    }
    // get the min distance required by bend radius and entry angle
    // we are assuming entry angle is 27% angle = 15.11 degrees
    // Here is the formulas
    // Tan(A/2) = Height/Distance
    // Angle (Radian) = 2 X Tan(inverse)(Height/Distance)
    // we are saying that entry pitch is max 27% 
    // Pitch = Tan(Angle)
    // We calculate Pitch and if it is greater than 27% then we will say it is not possible to dig
    // AT this point we assume desired depth = 10 ft = 3.048 m

    // calculate angle
    var angle = 2 * Math.atan( fDesiredDepth / distance);
    //calculate pitch
    var pitch = Math.tan(angle) * 100;

    if (pitch > 27) {
        return false;
    }
    return true;
}