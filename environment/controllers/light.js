module.exports = class Light {

    constructor(settings) {
        this.delta = settings.delta;
        this.max = settings.max_light;
        this.min = settings.min_light;
        this.frequency = settings.frequency;
        this.timestamp = Date.now();
        this.luminosity = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 10) / 10;
        this.startSimulation();
    }

    startSimulation() {
        setInterval(this.computeLuminosity(), this.frequency);
    }

    get() {
        return (req, res) => {
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                data: this.luminosity
            });
        }
    }

    // Sets the heating either to true or false, returns a 204 (succesful request with empty response)
    setLights() {
        return (req, res) => {
            if(req.body.lightsON != 0) 
                this.lightsON = true;
            else 
                this.lightsON = false;
            if(req.body.lightsON > this.luminosity) {
                console.log(this.lightsON);
                this.luminosity = req.body.lightsON;
            }
                
            res.status(204).send();
        }
    }

    computeLuminosity() {
        return () => {
            if(!this.lightsON) {
                if (Math.round(Math.random()) === 1) {
                    this.max = this.luminosity + this.delta;
                    this.min = this.luminosity;
                } else {
                    this.max = this.luminosity;
                    this.min = this.luminosity - this.delta;
                }
            }
            
            this.timestamp = Date.now();
            this.luminosity = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 10) / 10;
            if(this.luminosity < 0)
                this.luminosity = 0;
        }
    }
}



