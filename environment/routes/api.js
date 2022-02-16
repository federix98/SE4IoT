const router = require('express').Router();
const {getTemperature, setHeating} = require('../controllers/temperature.js');

// Temperature routes
router.get('/temperature/get', getTemperature);
router.post('/temperature/heat', setHeating);

module.exports = router;