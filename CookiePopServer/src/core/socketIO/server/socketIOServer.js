/**
 * @file socketIOServer.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Socket.IO server based on socket.io v2.4.1
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */


/* Modules */
require('dotenv').config();
const Http = require('http');
const express = require('@base/express/index');
const SocketIO = require('@base/socket.io/lib/index');

const SocketIOEventAndroid = require('@root/src/core/socketIO/server/socketIOEventAndroid');
const SocketIOEventArduino = require('@root/src/core/socketIO/server/socketIOEventArduino');
const dbManager = require('@src/db/dbManager');

const androidEvent = new SocketIOEventAndroid();
const arduinoEvent = new SocketIOEventArduino();

/**
* @class SocketIOClient
* @description socket.io server based on http, express
*/
class SocketIOServer {
    /**
     * @property io - instance of socket.io
     * @property app - instance of express
     * @property httpServer - instance of http server
     */
    io = null;
    app = null;
    httpServer = null;

    /**
     * @function isInit
     * @description Returns whether the server is initialized or not.
     * @returns {boolean}
     */
    isInit() {
        return this.io !== null && this.http !== null;
    }


    /**
     * @function init
     * @description Initialize the server
     */
    init(opts) {
        this.app = express();
        this.httpServer = Http.createServer(this.app);
        this.io = new SocketIO().attach(this.httpServer, opts);

        this.app.get('/', (req, res) => {
            res.send('Server is running');
        });
    }

    /**
     * @async
     * @function run
     * @description Run the socket.io server
     * @param {string} type type of socket.io server
     */
    async run(type) {
    // listen http server
        switch(type) {
            case 'android':
                this.httpServer.listen(process.env.PORT, () => {
                    console.log(` -- ${type} server running on port ${process.env.PORT} -- `);
                });

                // io event: client connection
                this.io.sockets.on('connection', androidEvent.onConnect);
                break;

            case 'arduino':
                this.httpServer.listen(process.env.PORT2, () => {
                    console.log(` -- ${type} server running on port ${process.env.PORT2} -- `);
                });

                // io event: client connection
                this.io.sockets.on('connection', arduinoEvent.onConnect);
                SocketIOEventArduino.run();
                break;

            default:
                break;
        }
    }
}

module.exports = SocketIOServer;