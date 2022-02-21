module.exports = class AirHumidity {

    constructor(settings) {
        this.delta = settings.delta;
        this.max = settings.max_hum;
        this.min = settings.min_hum;
        this.frequency = settings.frequency;
        this.timestamp = Date.now();
        this.humidity = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 10) / 10;
        this.startSimulation();
    }

    startSimulation() {
        setInterval(this.computeHumidity(), this.frequency);
    }

    get() {
        return (req, res) => {
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                data: this.humidity
            });
        }
    }

    computeHumidity() {
        return () => {
            if (Math.round(Math.random()) === 1) {
                this.max = this.humidity + this.delta;
                this.min = this.humidity;
            } else {
                this.max = this.humidity;
                this.min = this.humidity - this.delta;
            }

            this.timestamp = Date.now();
            this.humidity = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 10) / 10;
        }
    }
}



