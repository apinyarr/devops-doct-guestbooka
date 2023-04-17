const statsd = require('node-statsd');
const client = new statsd({host: 'mygraphite'});
module.exports = client;