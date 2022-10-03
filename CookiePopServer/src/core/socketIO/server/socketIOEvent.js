/**
 * @file socketIOEvent.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Super class of socket.io event
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02

/* Modules */
const Socket = require('@base/socket.io/lib/socket');
const androidCache = require('@src/core/cache/androidCache');
const CustomUtils = require('@src/utils/customUtils');

/**
 * @class SocketIOEvent
 */
class SocketIOEvent {
    /**
     * @function onConnect
     * @description Callback called when a socket is connected
     * @param {Socket} socket Client socket
     */
    onConnect(socket) {
        CustomUtils.socketLog('# Client connect', socket);

        socket.on('disconnect', () => {
            CustomUtils.socketLog('# Client disconnect', socket);
            androidCache.remove(socket);
        });
    }
}

module.exports = SocketIOEvent;