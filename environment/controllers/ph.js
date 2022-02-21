module.exports = class Ph {

    constructor(settings) {
        this.delta = settings.delta;
        this.max = settings.max_ph;
        this.min = settings.min_ph;
        this.frequency = settings.frequency;
        this.timestamp = Date.now();
        this.luminosity = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 10) / 10;
        this.startSimulation();
    }

    startSimulation() {
        setInterval(this.computePH(), this.frequency);
    }

    get() {
        return (req, res) => {
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                data: this.luminosity
            });
        }
    }


    computePH() {
        return () => {
            if (Math.round(Math.random()) === 1) {
                this.max = this.luminosity + this.delta;
                this.min = this.luminosity;
            } else {
                this.max = this.luminosity;
                this.min = this.luminosity - this.delta;
            }

            this.timestamp = Date.now();
            this.luminosity = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 10) / 10;
            if(this.ph < 0) 
                this.ph = 0;
        }
    }
}



