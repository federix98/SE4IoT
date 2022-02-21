module.exports = class Co2 {

    constructor(settings) {
        this.delta = settings.delta;
        this.max = settings.max_co2;
        this.min = settings.min_co2;
        this.frequency = settings.frequency;
        this.fanOn = false;
        this.timestamp = Date.now();
        this.co2 = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 100) / 100;

        // All spriklers are defaulted to OFF
        this.fans = [];
        this.fansON = 0;
        for(let i = 1; i < settings.fans; i++) {
            this.fans[i] = false;
        }

        this.startSimulation();
    }

    startSimulation() {
        setInterval(this.computeCo2(), this.frequency);
    }

    get() {
        return (req, res) => {
            res.status(200).json({
                timeOfMeasurement: this.timestamp,
                data: this.co2
            });
        }
    }

    getFanStatus() {
        return (req, res) => {
            if(req.params.fan > this.fans.length || req.params.fan <= 0) {
                res.status(400).send(JSON.stringify({
                    message: "This fan does not exists"
                }));
                return;
            }

            res.status(200).json({
                data: this.fans[req.params.fan]
            });
        }
    }

    // Sets the heating either to true or false, returns a 204 (succesful request with empty response)
    setFan() {
        return (req, res) => {
            if(req.body.fan > this.fans.length || req.body.fan <= 0) {
                res.status(400).send(JSON.stringify({
                    message: "This fan does not exists"
                }));
                return;
            }

            this.fans[req.body.fan] = req.body.fanON;
            this.fansON = 0;
            this.fans.map( fan => {
                fan === true ? this.fansON++ : this.fansON;
            });
            res.status(204).send();
        }
    }

    computeCo2() {
        return () => {
            // Co2 Only raises it can only be lowered with fans
            if (this.fansON === 0) {
                this.max = this.co2 + this.delta;
                this.min = this.co2;
            } else {
                let delta = this.delta * this.fansON;
                this.max = this.co2;
                this.min = this.co2 - delta;
            }

            this.timestamp = Date.now();
            this.co2 = Math.round(((Math.random() * (this.max - this.min) + this.min) + Number.EPSILON) * 100) / 100;
            if (this.co2 < 0)
                this.co2 = 0;
        }
    }
}



