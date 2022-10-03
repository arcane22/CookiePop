/**
 * @file socketIOClientArduino.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - socket.io client for testing api server
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

/* Modules */
const SocketIOClient = require('@root/src/core/socketIO/client/socketIOClient');
const Constants = require('@root/src/utils/constants');
const CustomUtils = require('@root/src/utils/customUtils');

/**
 * @class SocketIOClientArduino
 * @extends SocketIOClient
 */
class SocketIOClientArduino extends SocketIOClient{
    run(uri) {
        super.run(uri, Constants.type_arduino);
        this.signInTest();

        this.io.on(Constants.sendAngleToArduino, (dto) => {
            CustomUtils.socketLog(` # angle : ${dto}`, this.io);
        });
    }

    signInTest() {
        this.io.emit(Constants.sendMachineId, {
            machineId: 'abcdef',
        });
    }
}


module.exports = SocketIOClientArduino;