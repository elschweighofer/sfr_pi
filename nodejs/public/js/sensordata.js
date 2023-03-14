const socket = io();
var temperatureGauge;
var humidityGauge;



//on message 
socket.on('message', function (msg) {
    jsonMsg = JSON.parse(msg);
    var temperature = jsonMsg.temperature;
    var humidity = jsonMsg.humidity;
    var host = jsonMsg.host;
    temperatureGauge.value = temperature;
    humidityGauge.value = humidity;
});



function initGauge() {
    temperatureGauge = new LinearGauge({
        renderTo: 'temperature-gauge',
        width: 150,
        height: 400,
        units: '°C',
        title: 'Temperature',
        value: 23,
        minValue: -20,
        maxValue: 100,
        valueBox: false,
        majorTicks: [-20, -10, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100],
        highlights: [
            {
                from: 30,
                to: 100,
                color: 'rgba(200, 50, 50, .75)',
            },
            {
                from: -20,
                to: 0,
                color: 'rgba(0, 150, 255, 0.5)',
            },
        ],
    }).draw();


    humidityGauge = new RadialGauge({
        renderTo: 'humidity-gauge',
        width: 400,
        height: 400,
        units: '%',
        title: "Humidity",
        value: 37,
        minValue: 0,
        maxValue: 100
    }).draw();
}