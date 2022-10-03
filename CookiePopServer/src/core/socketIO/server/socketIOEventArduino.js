/**
 * @file socketIOEventArduino.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - socket.io events from arduino device
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

/* Module */
const arduinoCache = require('@src/core/cache/arduinoCache');
const SocketIOEvent = require('@root/src/core/socketIO/server/socketIOEvent');
const dbManager = require('@src/db/dbManager');
const CustomUtils = require('@src/utils/customUtils');
const Constants = require('@src/utils/constants');

/* Constants */
const Timer_INTERVAL = 60 * 1000; // 1 min
const Angles = [45, 86, 128];

/**
 * @class SocketIOEventArduino
 * @extends SocketIOEvent
 */
class SocketIOEventArduino extends SocketIOEvent{
    constructor() {
        super();
    }

    /**
     * @Override
     * @function onConnect
     * @description Callback called when a socket is connected
     * @param {Socket} socket 
     */
    onConnect(socket) {
        super.onConnect(socket);

        socket.on(Constants.sendMachineId, (dto) => {
            arduinoCache.add(socket, dto.machineId);
        });
    }

    /**
     * 
     */
    static run() {
        const threshold = 60;

        const timer = async () => {
            const list = await dbManager.getAllCookieTime();

            if(list != null) {
                for(let element of list) {
                    const userID = element.user_id;
                    const machineId = element.user_machineId;
                    if(machineId !== null && arduinoCache.containsId(machineId)) {
                        
                        let cookieTimes = [];
                        cookieTimes.push(CustomUtils.dateToSec(element.user_cookieTime1));
                        cookieTimes.push(CustomUtils.dateToSec(element.user_cookieTime2));
                        cookieTimes.push(CustomUtils.dateToSec(element.user_cookieTime3));
                        const currTime = Math.floor(new Date().getTime() / 1000);
    
                        console.log(cookieTimes);
                        console.log(currTime);

                        for(let i = 0; i < cookieTimes.length; i++) {
                            const gap = cookieTimes[i] - currTime;
                            if(0 <= gap && gap <= threshold) {
                                arduinoCache.getSocket(element.user_machineId).emit(Constants.sendAngleToArduino, Angles[i]);
                                await dbManager.executeCookie({
                                    cookieNum: i,
                                    userID: userID,
                                });
                            }
                        } 
                    };
                }
            }
        }
        setInterval(timer, Timer_INTERVAL);
    }
}

module.exports = SocketIOEventArduino;