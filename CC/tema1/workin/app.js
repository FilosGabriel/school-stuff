const http = require('http');
const axios = require('axios');
var fs = require('fs');

const port = 80;
const wather_key = 'b0b07392ce997b156e3a86731b1393c0'
const image_key = 'GM2dGN4unMJm-GzkCcdHMSf5W6chq6URMiYorom5hxY'
const lat = 47.159809
const long = 27.587200

const imageSaveService = async (url) => {
    axios.get(`https://api.sirv.com/v2/docs?path=/v2/account/fetching`)
}

const imageService = async (query, api) => {
    return axios.get(`https://api.unsplash.com/photos/random?client_id=${api}`)
        .then(response => {
            return response.data.urls.regular;
        })
        .catch(error => {
            console.log("error image");
        });

}

const color = async () => {
    // return await axios.get(`http://www.colr.org/json/color/random`).then(response => { return response.data.new_color }).catch(error => { console.log(error) })
    return (Math.random() * 0xFFFFFF << 0).toString(16);
}

const weatherService = async (lat, lon, api) => {
    // https://api.darksky.net/forecast/${api}/37.8267,-122.4233
    // http://api.openweathermap.org/data/2.5/weather?q=iasi&appid=a79df717d186bf5e1396793c154c1e37
    var description =  axios.get(`http://api.openweathermap.org/data/2.5/weather?q=iasi&appid=a79df717d186bf5e1396793c154c1e37`)
        .then(response => {
            currently = response.data;
            return {
                'summary': currently.weather[0].description,
                'temperature': currently.main.temp, //temperature
                'humidity': currently.main.humidity,
                'pressure': currently.main.pressure,
                'windSpeed': currently.wind.speed   //windSpeed
            };
        })
        .catch(error => {
            console.log(error);
        });
    return description;

}


const editImageService = async (url, api, details, color) => {
    var file = fs.createWriteStream('./image.jpg');
    const urlService = `https://res.cloudinary.com/djdowitwl/image/fetch/l_text:Times_80:summary%20${encodeURIComponent(details['summary'])}%0Atemperature%20${encodeURIComponent(details['temperature'])}%0Ahumidity%20${encodeURIComponent(details['humidity'])}%0Apressure%20${encodeURIComponent(details['pressure'])}%0AwindSpeed%20${encodeURIComponent(details['windSpeed'])},g_west,x_30,y_80,co_rgb:${color}/${encodeURIComponent(url)}`
    // console.log(urlService);

    const response = await axios({
        url: urlService,
        method: 'GET',
        responseType: 'stream',
        headers: {
            Authorization: 'Basic 475551228634435 oZDDcWc9wZ14x1mmSGgBSK7wxUs'
        }
    }).catch(error => { console.log("error") });
    return response.data;

}

const logInfo = (request) => {
    const { rawHeaders, httpVersion, method, socket, url } = request;
    const { remoteAddress, remoteFamily } = socket;
    const start = Date.now();
    console.log("\n\nRequest:");
    console.log("  " +
        JSON.stringify({
            timestamp: start,
            // rawHeaders,
            httpVersion,
            method,
            remoteAddress,
            remoteFamily,
            url
        }, null, 2)
    );
    return start;
}


const requestHandler = async (request, response) => {
    logInfo(request);
    if (request.url === '/') {
        fs.readFile('./index.html', function (err, content) {
            if (err) {
                response.writeHead(400, { 'Content-type': 'text/html' })
                console.log(err);
                response.end("No such resource");
            } else {
                //specify the content type in the response will be an image
                response.writeHead(200, { 'Content-type': 'text/html' });
                response.end(content);
            }
        });
    } else {
        if (request.url === '/image.jpg') {
            var start = new Date()
            weather = await weatherService(lat, long, wather_key);
            var end = new Date() - start;
            console.info('Execution time: %dms', end);

            var start = new Date();
            const hexColor = await color();
            var end = new Date() - start;
            console.info('Execution time: %dms', end);

            var start = new Date();
            // weather['summary']
            const uriImage = await imageService(encodeURIComponent("word"), image_key);
            var end = new Date() - start;
            console.info('Execution time: %dms', end);

            var start = new Date();
            const imageStream = await editImageService(uriImage, 'none', weather, hexColor);
            var end = new Date() - start;
            console.info('Execution time: %dms', end);

            response.writeHead(200, {
                'Content-Type': 'image/jpg',
            });
            imageStream.pipe(response);
        }
        else {
            response.end();
        }
    }


}

const server = http.createServer(requestHandler)

server.listen(port, (err) => {
    if (err) {
        return console.log('something bad happened', err)
    }

    console.log(`server is listening on ${port}`)
})