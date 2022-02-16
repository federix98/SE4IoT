const delta = 1.5;

let max = 35;
let min = 15;
let heatingOn = false;
let timestamp = Date.now();
let temperature = Math.round(((Math.random() * (max - min) + min) + Number.EPSILON) * 100) / 100;

setInterval(function() {
    // We flip a coin to decide if temperature is rising or lowering, 1 = Rising 0 = lowering
    // If the heating is on we default to raising temperature, stopping the temperature raising is demanded to the sensing/acting 
    if( heatingOn || Math.round(Math.random()) === 1) {
        max = temperature + delta;
        min = temperature;
    } else {
        max = temperature;
        min = temperature - delta;
    }
    
    timestamp = Date.now();
    temperature = Math.round(((Math.random() * (max - min) + min) + Number.EPSILON) * 100) / 100;
}, 5000);

exports.getTemperature = (req, res) => {
    res.status(200).json({
        timeOfMeasurement: timestamp,
        data: temperature
    });
}
 
// Sets the heating either to true or false, returns a 204 (succesful request with empty response)
exports.setHeating = (req, res) => {
    heatingOn = req.body.heatingOn;
    res.status(204).send();
}


