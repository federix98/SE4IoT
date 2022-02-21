const express = require('express');
const Temperature = require('./controllers/temperature.js');
const Light = require('./controllers/light.js');
const SoilHumidity = require('./controllers/soilHumidity.js');
const AirHumidity = require('./controllers/airHumidity.js');
const Co2 = require('./controllers/co2.js');
const Ph = require('./controllers/ph.js');

module.exports = class Greenhouse {
        
    constructor(settings) {
        this.settings = settings;
        
        // Creating all simulations
        this.temperature = new Temperature(settings.temperature);
        this.light = new Light(settings.light);
        this.soilHumidity = new SoilHumidity(settings.soilHumidity);
        this.airHumidity = new AirHumidity(settings.airHumidity);
        this.co2 = new Co2(settings.co2);
        this.ph = new Ph(settings.ph);

        this.router = new express.Router();
        this.attachRoutes();
    }

    attachRoutes() {
        // Temperature
        this.router.get('/greenhouse/' + this.settings.name + '/temperature/:position', this.temperature.get());
        this.router.post('/greenhouse/' + this.settings.name + '/temperature/setHeating', this.temperature.setHeating());
        // Light
        this.router.get('/greenhouse/' + this.settings.name + '/luminosity', this.light.get());
        this.router.post('/greenhouse/' + this.settings.name + '/luminosity/setLights', this.light.setLights());
        // Soil Humidity
        this.router.get('/greenhouse/' + this.settings.name + '/soilHumidity/:position', this.soilHumidity.get());
        this.router.get('/greenhouse/' + this.settings.name + '/soilHumidity/:position/sprinkler', this.soilHumidity.getSprinklerStatus());
        this.router.post('/greenhouse/' + this.settings.name + '/soilHumidity/setSprinkler', this.soilHumidity.setSprinkler());
        // Air Humidity
        this.router.get('/greenhouse/' + this.settings.name + '/airHumidity', this.airHumidity.get());
        // Co2
        this.router.get('/greenhouse/' + this.settings.name + '/co2', this.co2.get());
        this.router.get('/greenhouse/' + this.settings.name + '/co2/:fan', this.co2.getFanStatus());
        this.router.post('/greenhouse/' + this.settings.name + '/co2/setFan', this.co2.setFan());
        // Ph 
        this.router.get('/greenhouse/' + this.settings.name + '/ph', this.ph.get());
    }

}