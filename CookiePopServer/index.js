/**
 * @file index.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Node.js (socket.io) server for Cookie pop application, arduino device
 *  - Version: 1.0.0
 *  - Last Modified: 2021.12.09
 */

/* Modules */
require('module-alias/register');
require('dotenv').config();
const SocketIOServer = require('@root/src/core/socketIO/server/socketIOServer');
const SocketIOClientAndroid = require('@root/src/core/socketIO/client/SocketIOClientAndroid');
const SocketIOClientArduino = require('@root/src/core/socketIO/client/SocketIOClientArduino');
const Constants = require('@src/utils/constants');


class Main {
    /**
     * @static
     * @function run
     * @param {string} type 
     */
    static async run(type) {
        try {
            switch(type) {
                case 'server':
                    // init servers
                    const sioAndroidServer = new SocketIOServer();
                    const sioArduinoServer = new SocketIOServer();
                    const dbManager = require('@src/db/dbManager');

                    sioAndroidServer.init();
                    sioArduinoServer.init();
                    await dbManager.init();

                    // run servers
                    sioAndroidServer.run(Constants.type_android);
                    sioArduinoServer.run(Constants.type_arduino);
                    break;

                case 'client':
                    const sioAndroidClient = new SocketIOClientAndroid();
                    const sioArduinoClient = new SocketIOClientArduino();

                    sioAndroidClient.run(`http://localhost:${process.env.PORT}`);
                    sioArduinoClient.run(`http://localhost:${process.env.PORT2}`);
                    break;

                default:
                    break;
            }
        } catch(e) {
            console.error(e);
        }
    }
}

const type = process.argv.slice(2)[0];
Main.run(type);
