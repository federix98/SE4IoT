const server = require("express")();
const {urlencoded, json} = require("body-parser");
const Greenhouse = require ("./Greenhouse.js");
const config = require('config');

// Assigning request parsing middlewares to server
server.use(urlencoded({ extended: false }));
server.use(json());

let greenhousesSettings = config.get('greenhouses');
let greenhouses = [];
// Creating greenhouses and assigning routers to the server
greenhousesSettings.forEach(greenhouseSettings => {
    let greenhouse = new Greenhouse(greenhouseSettings);
    server.use(greenhouse.router);
    greenhouses.push(greenhouse);
});



server.listen(8000, () => {
    console.log("Server listening on port 8000");
})