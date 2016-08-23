

function GetDistanceX(lat1, lon1, lat2, lon2) {
    // since this is Getting the X displacement
    // what I will do is I will keep the longituide same
    // and change just the latitude and calculate the distance
    var radlat1 = Math.PI * lat1 / 180;
    var radlat2 = Math.PI * lat2 / 180;
    var radlon1 = Math.PI * lon1 / 180;// I keep longitude same since I am calculating
    var radlon2 = Math.PI * lon1 / 180;// only X displacement
    var theta = lon1 - lon2;
    var radtheta = Math.PI * theta / 180;
    var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
    dist = Math.acos(dist);
    dist = dist * 180 / Math.PI;
    dist = dist * 60 * 1.1515;
    dist = dist * 1.609344; // this is the distance in Km
    dist = dist * 1000;// distance in meters

    return dist
}

function ConvertFeetToMeters(mtr)
{
    return mtr * 0.3048;
}

function GetDistanceY(lat1, lon1, lat2, lon2) {
    // since this is Getting the y displacement
    // what I will do is I will keep the latitude same
    // and change just the longitude and calculate the distance
    var radlat1 = Math.PI * lat1 / 180;// I keep latitude same since I am calculating
    var radlat2 = Math.PI * lat1 / 180;// only y displacement
    var radlon1 = Math.PI * lon1 / 180;
    var radlon2 = Math.PI * lon2 / 180;
    var theta = lon1 - lon2;
    var radtheta = Math.PI * theta / 180;
    var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
    dist = Math.acos(dist);
    dist = dist * 180 / Math.PI;
    dist = dist * 60 * 1.1515;
    dist = dist * 1.609344; // this is the distance in Km
    dist = dist * 1000;// distance in meters

    return dist;
}

function find_angle_degrees(x1, y1, x2, y2, x3, y3) {
    var AB = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    var BC = Math.sqrt(Math.pow(x2 - x3, 2) + Math.pow(y2 - y3, 2));
    var AC = Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2));
    var angleRadians = Math.acos((BC * BC + AB * AB - AC * AC) / (2 * BC * AB));
    return (180 / 3.14159265) * angleRadians;// return degrees
}

function rad(x) {
    return x * Math.PI / 180;
};

function GetDistance(p1, p2) {

    var R = 6378137; // Earth’s mean radius in meter
    var dLat = rad(p2.lat() - p1.lat());
    var dLong = rad(p2.lng() - p1.lng());
    var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(rad(p1.lat())) * Math.cos(rad(p2.lat())) *
      Math.sin(dLong / 2) * Math.sin(dLong / 2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c;
    return d; // returns the distance in meter
};

// function to get distance between 2 points in meters 
function getDistanceFromLatLngMeters(lat1, lon1, lat2, lon2) {
    var R = 6371; // Radius of the earth in km
    var dLat = deg2rad(lat2 - lat1);  // deg2rad below
    var dLon = deg2rad(lon2 - lon1);
    var a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2)
    ;
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c * 1000; // in meters

    return d;
}

function deg2rad(deg) {
    return deg * (Math.PI / 180)
}