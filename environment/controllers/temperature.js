module.exports = class Temperature {

    constructor(settings) {
        this.delta = settings.delta;
        this.lowerDeviationDelta = settings.lowerDeviationDelta;
        this.upperDeviationDelta = settings.upperDeviationDelta;
        this.max = settings.max;
        this.min = settings.min;
        this.heatingOn = false;
        this.timestamp = Date.now();
        this.temperature = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 100) / 100;
    }

    startSimulation() {
        setInterval(function() {
            // We flip a coin to decide if temperature is rising or lowering, 1 = Rising 0 = lowering
            // If the heating is on we default to raising temperature, stopping the temperature raising is demanded to the sensing/acting 
            if( heatingOn || Math.round(Math.random()) === 1) {
                this.max = this.temperature + this.delta;
                this.min = this.temperature;
            } else {
                this.max = this.temperature;
                this.min = this.temperature - this.delta;
            }
            
            this.timestamp = Date.now();
            this.temperature = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 100) / 100;
        }, 5000);
    }
    
    get(req, res) {
        let deviation = 1;
        if(req.param('position') != 1) {
            // Computing a random deviation from the temperature of +- (upperDeviationDelta - lowerDeviationDelta)%
            deviation = Math.round(((Math.random() * (this.upperDeviationDelta - this.lowerDeviationDelta) + this.lowerDeviationDelta) + Number.EPSILON) * 100) / 100;
        }  
        
        res.status(200).json({
            timeOfMeasurement: this.timestamp,
            data: Math.round((this.temperature * deviation + Number.EPSILON) * 100) / 100
        });
    }
    
    // Sets the heating either to true or false, returns a 204 (succesful request with empty response)
    setHeating(req, res) {
        heatingOn = req.body.heatingOn;
        res.status(204).send();
    }
}



