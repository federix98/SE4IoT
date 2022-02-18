const express = require('express');
const Temperature = require('./controllers/temperature.js');

module.exports = class Greenhouse {
        
    constructor(settings) {
        this.settings = settings;
        
        this.temperature = new Temperature(settings.temperature);
        this.router = new express.Router();
        this.attachRoutes();
    }

    attachRoutes() {
        this.router.get('/greenhouse/' + this.settings.name + '/temperature/:position', this.temperature.get());
        this.router.post('/greenhouse/' + this.settings.name + '/temperature/setHeating', this.temperature.setHeating());
    }

}