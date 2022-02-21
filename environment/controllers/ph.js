module.exports = class Ph {

    constructor(settings) {
        this.delta = settings.delta;
        this.max = settings.max_ph;
        this.min = settings.min_ph;
        this.frequency = settings.frequency;
        this.timestamp = Date.now();
        this.ph = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 10) / 10;
        this.startSimulation();
    }

    startSimulation() {
        setInterval(this.computePH(), this.frequency);
    }

    get() {
        return (req, res) => {
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                data: this.ph
            });
        }
    }


    computePH() {
        return () => {
            if (Math.round(Math.random()) === 1) {
                this.max = this.ph + this.delta;
                this.min = this.ph;
            } else {
                this.max = this.ph;
                this.min = this.ph - this.delta;
            }

            this.timestamp = Date.now();
            this.ph = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 10) / 10;
            if(this.ph < 0) 
                this.ph = 0;
        }
    }
}



