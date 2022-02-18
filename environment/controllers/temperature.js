module.exports = class Temperature {

    constructor(settings) {
        this.delta = settings.delta;
        this.lowerDeviationDelta = settings.lowerDeviationDelta;
        this.upperDeviationDelta = settings.upperDeviationDelta;
        this.max = settings.max_temp;
        this.min = settings.min_temp;
        this.frequency = settings.frequency;
        this.heatingOn = false;
        this.timestamp = Date.now();
        this.temperature = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 100) / 100;
        this.startSimulation();
    }

    startSimulation() {
        setInterval(this.computeTemp(), this.frequency);
    }
    
    get() {   
        return (req, res) => {
            let deviation = 1;
            if(req.params.position != 1) {
                // Computing a random deviation from the temperature of +- (upperDeviationDelta - lowerDeviationDelta)%
                deviation = Math.round(((Math.random() * (this.upperDeviationDelta - this.lowerDeviationDelta) + this.lowerDeviationDelta) + Number.EPSILON) * 100) / 100;
            } 
                res.status(200).json({
                    timeOfMeasurement: this.timestamp,
                    data: Math.round((this.temperature * deviation + Number.EPSILON) * 100) / 100
            });
        }
    }
    
    // Sets the heating either to true or false, returns a 204 (succesful request with empty response)
    setHeating() {
        return (req, res) => {
            this.heatingOn = req.body.heatingOn;
            res.status(204).send();
        }    
    }

    computeTemp() {
        return () => {
            if( this.heatingOn || Math.round(Math.random()) === 1) {
                this.max = this.temperature + this.delta;
                this.min = this.temperature;
            } else {
                this.max = this.temperature;
                this.min = this.temperature - this.delta;
            }
            
            this.timestamp = Date.now();
            this.temperature = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 100) / 100;
        }
    }
}



