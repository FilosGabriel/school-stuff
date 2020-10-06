'use strict';
const {performance} = require('perf_hooks');
const http = require('http');
const axios = require('axios');
const fs = require('fs');
const url = require('url');
const config_data = require('./config/config');
var totalWeather = 0;
var imagesRemain = 0;
const port = config_data.port;
const weather_key = config_data.weather_key;
const image_key = config_data.image_key;
const user = config_data.user;
const pass = config_data.pass;
const log_active = config_data.log;
const log_server = config_data.log_server;


function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
}

const randomHexColor = () => {
    return (Math.random() * 0xFFFFFF << 0).toString(16);
};

const fail = (response) => {
    response.writeHead(400, {
        'Content-Type': 'application/json',
    });
    response.end(JSON.stringify({'message': 'fail'}));
};

const logData = (request, response, data) => {
    if (log_server) {
        log_data.push({
            'type': 'response server',
            'latency': Number(performance.now() - request.ts),
            'response': ` ${response._header} }`,
            'body': data
        });
    }
};

const logError = (request, response) => {
    if (log_server) {
        log_data.push({
            'type': 'response server',
            'latency': Number(performance.now() - request.ts),
            'response': ` ${response._header} }`,
            'body': 'error'
        });
    }
};

const logRequest = (request) => {
    if (log_server) {
        request.ts = performance.now();
        log_data.push({'type': 'request', 'Method': request.method, 'url': request.url, 'headers': request.headers});
    }
};

const logBinary = (request, response) => {
    if (log_server) {
        log_data.push({
            'type': 'response server',
            'latency': Number(performance.now() - request.ts),
            'response': ` ${response._header} }`,
            'body': 'binary'
        });
    }
};

function editedImageResponse(a, b, response, request) {
    const requestOne = axios.get(`https://api.darksky.net/forecast/${weather_key}/${a},${b}`);
    const requestTwo = axios.get(`https://pixabay.com/api/?key=${image_key}&per_page=3&page=${getRandomInt(100) + 1}`);
    axios.all([requestOne, requestTwo])
        .then(axios.spread((...responses) => {
            const responseWeather = responses[0];
            const responseImage = responses[1];
            const currentWeather = responseWeather.data.currently;
            totalWeather = responseWeather.headers['x-forecast-api-calls'];
            imagesRemain = responseImage.headers['x-ratelimit-remaining'];
            const details = {
                'summary': currentWeather.summary,
                'temperature': currentWeather.temperature,
                'humidity': currentWeather.humidity,
                'pressure': currentWeather.pressure,
                'windSpeed': currentWeather.windSpeed
            };
            const imageURL = responseImage.data.hits[getRandomInt(3)].largeImageURL;
            const urlEditImage = `https://res.cloudinary.com/djdowitwl/image/fetch/l_text:Times_80:summary%20${encodeURIComponent(details['summary'])}%0Atemperature%20${encodeURIComponent(details['temperature'])}%0Ahumidity%20${encodeURIComponent(details['humidity'])}%0Apressure%20${encodeURIComponent(details['pressure'])}%0AwindSpeed%20${encodeURIComponent(details['windSpeed'])},g_west,x_30,y_80,co_rgb:${randomHexColor()}/${encodeURIComponent(imageURL)}`
            // console.log(urlEditImage);
            axios({
                url: urlEditImage,
                method: 'GET',
                responseType: 'stream',
                headers: {Authorization: `Basic ${user} ${pass}`}
            }).then(res => {
                response.writeHead(200, {
                    'Content-Type': 'image/jpg',
                });
                res.data.pipe(response);
                logBinary(request, response);
            }).catch(error => {
                console.log("error edit");
                // console.log(error);
                response.writeHead(200);
                logError(request, response);
            });
        })).catch(error => {
        console.log("error fetch image or weather data");
        fail(response);
        response.end();
        logError(request, response);
    })
};

function serveHtml(response) {
    fs.readFile('./index.html', function (err, content) {
        if (err) {
            console.log(err);
            response.writeHead(400, {'Content-type': 'text/html'});
            response.end("No such resource");
        } else {
            response.writeHead(200, {'Content-type': 'text/html'});
            response.end(content);
        }
    });
}

function serveWeather(request, response) {
    const queryObject = url.parse(request.url, true).query;
    axios.get(`https://api.darksky.net/forecast/${weather_key}/${queryObject.lat},${queryObject.long}`)
        .then(res => {
            totalWeather = res.headers['x-forecast-api-calls'];
            const currently = res.data.currently;
            response.writeHead(200, {
                'Content-Type': 'application/json',
            });
            const data = {
                'summary': currently.summary,
                'temperature': currently.temperature,
                'humidity': currently.humidity,
                'pressure': currently.pressure,
                'windSpeed': currently.windSpeed
            };
            response.write(JSON.stringify(data));
            logData(request, response, data);
            response.end();
        })
        .catch(error => {
            console.log(error);
            fail(response);
            logError(request, response);
        });
}

function serveRandomImage(request, response) {
    axios.get(`https://pixabay.com/api/?key=${image_key}&per_page=3&page=${getRandomInt(100) + 1}`)
        .then(res => {
            imagesRemain = parseInt(res.headers['x-ratelimit-remaining']);
            const imageURL = res.data.hits[getRandomInt(3)].largeImageURL;
            response.writeHead(200, {'Content-Type': 'text/plain'});
            response.write(JSON.stringify({'url': imageURL}));
            logData(request, response, imageURL);
            response.end();
        })
        .catch(error => {
            console.log(error);
            fail(response);
            logError(request, response);
        });
}

function serveMetrics(request, response) {
    response.writeHead(200, {'Content-Type': 'text/json'});
    response.write(JSON.stringify({'weather': 1000 - totalWeather, "image": imagesRemain, 'log': log_data}));
    response.end();
}

const requestHandler = async (request, response) => {
    logRequest(request);
    if (request.url === '/') {
        serveHtml(response);
    } else if (request.url.includes('/image.jpg')) {
        const queryObject = url.parse(request.url, true).query;
        editedImageResponse(queryObject.lat, queryObject.long, response, request);
    } else if (request.url.includes('/meteo')) {
        serveWeather(request, response);
    } else if (request.url.includes('/random')) {
        serveRandomImage(request, response);
    } else if (request.url === '/metrics') {
        serveMetrics(request, response);
    } else {
        console.log("Unknow path");
        response.end();
    }
};

var log_data = [];
console.log(log_active);
if (log_active) {
    axios.interceptors.request.use((request) => {
        request.ts = performance.now();
        log_data.push({'type': 'request', 'Method': request.method, 'url': request.url, 'headers': request.headers});
        return request;
    });
    axios.interceptors.response.use((response) => {
        log_data.push({
            'type': 'response',
            'url': response.config.url,
            'latency': Number(performance.now() - response.config.ts),
            'response': `Date ${response.headers.date} content-type:${response.headers['content-type']} transfer-encoding:${response.headers['transfer-encoding']}`,
            'body': !response.headers['content-type'].includes('image') ? Object.assign({}, response.data) : 'binary'
        });
        return response;
    });
}
const server = http.createServer(requestHandler);

server.listen(port, (err) => {
    if (err) {
        return console.log('something bad happened', err)
    }
    console.log(`server is listening on ${port}`)
});