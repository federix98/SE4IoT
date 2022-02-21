module.exports = class SoilHumidity {

    constructor(settings) {
        this.delta = settings.delta;
        this.lowerDeviationDelta = settings.lowerDeviationDelta;
        this.upperDeviationDelta = settings.upperDeviationDelta;
        this.max = settings.max_hum;
        this.min = settings.min_hum;
        this.frequency = settings.frequency;
        this.timestamp = Date.now();
        this.humidity = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 100) / 100;

        // All spriklers are defaulted to OFF
        this.sprinklers = [];
        this.sprinklersON = 0;
        for(let i = 1; i < settings.sprinklers; i++) {
            this.sprinklers[i] = false;
        }

        this.startSimulation();
    }

    startSimulation() {
        setInterval(this.computeHumidity(), this.frequency);
    }

    get() {
        return (req, res) => {
            let deviation = 1;
            if (req.params.position != 1) {
                // Computing a random deviation from the humidity of +- (upperDeviationDelta - lowerDeviationDelta)%
                deviation = Math.round(((Math.random() * (this.upperDeviationDelta - this.lowerDeviationDelta) + this.lowerDeviationDelta) + Number.EPSILON) * 100) / 100;
            }
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                data: Math.round((this.humidity * deviation + Number.EPSILON) * 100) / 100
            });
        }
    }

    getSprinklerStatus() {
        return (req, res) => {
            if(req.params.position > this.sprinklers.length || req.params.position <= 0) {
                res.status(400).send(JSON.stringify({
                    message: "This sprinkler does not exists"
                }));
                return;
            }
            
            res.status(200).json({
                data: this.sprinklers[req.params.position]
            });
        }
    }

    // Sets the sprinkler either to true or false, returns a 204 (succesful request with empty response)
    setSprinkler() {
        return (req, res) => {

            if(req.body.sprinkler > this.sprinklers.length || req.body.sprinkler <= 0) {
                res.status(400).send(JSON.stringify({
                    message: "This sprinkler does not exists"
                }));
                return;
            }

            this.sprinklers[req.body.sprinkler] = req.body.sprinklerON;
            this.sprinklersON = 0;
            this.sprinklers.map( sprinkler => {
                sprinkler === true ? this.sprinklersON++ : this.sprinklersON;
            });
            res.status(204).send();
        }
    }

    computeHumidity() {
        return () => {
            // Humidity only lowers in time it can't go up on it's own
            if (this.sprinklersON > 0) {
                let delta = this.delta * this.sprinklersON;
                this.max = this.humidity + delta;
                this.min = this.humidity;
            } else {
                this.max = this.humidity;
                this.min = this.humidity - this.delta;
            }

            this.timestamp = Date.now();
            this.humidity = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 100) / 100;
        }
    }
}



