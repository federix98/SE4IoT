const server = require("express")();
const {urlencoded, json} = require("body-parser");
const router = require('./routes/api.js');

// Assigning request parsing middlewares to server
server.use(urlencoded({ extended: false }));
server.use(json());

// Assigning the router
server.use(router);

server.listen(8000, () => {
    console.log("Server listening on port 8000");
})