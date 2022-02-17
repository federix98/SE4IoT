const expressRouter = require('express');
const Temperature = require('./controllers/temperature.js');

module.exports = class Greenhouse {
        
    constructor(settings) {
        this.settings = settings;
        
        this.temperature = new Temperature(settings.temperature);
        this.router = expressRouter.Router();
        this.attachRoutes();
    }

    attachRoutes() {
        this.router.get('/greenhouse/' + this.name + ':position/temperature', this.temperature.get);
        this.router.post('/greenhouse/' + this.name + '/temperature/setHeating', this.temperature.setHeating);
    }

}