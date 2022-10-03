/**
 * @file socketIoClient.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - socket.io client for testing api server
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

/**
 * @class SocketIOClient
 */
class SocketIOClient {
    run(uri, type) {
        this.io = require('@base/socket.io-client')(uri);
        this.io.on('connect', () => {
            console.log(` # ${type} connects to server`);
        });
    }
}

module.exports = SocketIOClient;