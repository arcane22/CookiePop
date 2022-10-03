/**
 * @file customUtils.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

/* Modules */
const yyyymmdd = require('@base/yyyy-mm-dd/index');

/**
 * @class CustomUtils
 */
class CustomUtils {
    /**
     * @static
     * @function socketLog
     * @description Print socket event log with current time
     * @param {string} msg msg to print to terminal
     * @param {Socket} socket instance of client's socket
     */
    static socketLog(msg, socket) {
        console.log(msg + ` [${socket.id}] [${yyyymmdd.withTime()}]`);
    }

    /**
     * @static
     * @function dateToSec
     * @param {string} date date format (string)
     * @returns 
     */
    static dateToSec(date) {
        if(date === null) return 0;
        return Math.floor(new Date(date).getTime() / 1000);
    }
}

module.exports = CustomUtils;