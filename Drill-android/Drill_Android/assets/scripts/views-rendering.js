
function bottomView(data, chartWidth, chartHeight)
{
    var layout = {
        title: 'Bore Path Plot',
        showlegend: false,

        autosize: false,
        width: chartWidth,
        height: chartHeight,
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

    Plotly.newPlot('graphDiv', data, layout);
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

    Plotly.newPlot('graphDiv', data, layout);
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

    Plotly.newPlot('graphDiv', data, layout);
}
