/**
 * @file socketIoEventAndroid.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Socket.io events for android
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

/* Modules */
const SocketIOEvent = require('@root/src/core/socketIO/server/socketIOEvent');
const androidCache = require('@src/core//cache/androidCache');
const dbManager = require('@src/db/dbManager');

const CustomUtils = require('@src/utils/customUtils');
const Constants = require('@root/src/utils/constants');
const Encrypt = require('@src/utils/encrypt');
const mailer = require('@src/utils/mailer');


/**
 * @class SocketIOEventAndroid
 * @extends SocketIOEvent
 */
class SocketIOEventAndroid extends SocketIOEvent{
    constructor() {
        super();
    }

    /**
     * @Override
     * @function onConnect
     * @description Callback called when a socket is connected
     * @param {*} socket 
     */
    onConnect(socket) {
        super.onConnect(socket);

        // Sign in request
        socket.on(Constants.signIn, async (dto) => {
            CustomUtils.socketLog(` # Client request'sign in'`, socket);
            const result = await dbManager.signIn(dto);

            if(result == null || androidCache.containsId(result.user_id)) {
                socket.emit(Constants.signInResult, Constants.fail);
                CustomUtils.socketLog(' # sign in failed', socket);
            } else {
                socket.emit(Constants.signInResult, result);
                CustomUtils.socketLog(' # sign in success', socket);
                androidCache.add(socket, result.user_id);
            }
        });


        // Sign up request
        socket.on(Constants.signUp, async (dto) => {
            CustomUtils.socketLog(` # Client request 'sign up'`, socket);
            const result = await dbManager.signUp(dto);

            if(result == null) {
                socket.emit(Constants.signUpResult, Constants.fail);
                CustomUtils.socketLog(` # sign up failed`, socket);
            } else {
                socket.emit(Constants.signUpResult, Constants.success);
                CustomUtils.socketLog(` # sign up success`, socket);
            }
        });


        // Auth code request
        socket.on(Constants.authCode, async (dto) => {
            CustomUtils.socketLog(` # Client request 'auth code'`, socket);
            const code = await Encrypt.createAuthCode();
            const userID = dto.userID;

            if(await mailer.sendAuthCode(userID, code)) {
                CustomUtils.socketLog(' # Success to send auth code', socket);
                socket.emit(Constants.sendAuthCodeToClient, code);
            } else {
                CustomUtils.socketLog(' # Fail to send auth code', socket);
                socket.emit(Constants.sendAuthCodeToClient, Constants.fail);
            }
        });


        // find id request
        socket.on(Constants.findId, async (dto) => {
            CustomUtils.socketLog(` # Client request 'find id'`, socket);
            const userID = await dbManager.findId(dto);

            if(userID == null) {
                CustomUtils.socketLog(' # Fail to find id', socket);
                socket.emit(Constants.sendAuthCodeToClient, Constants.fail);
            } else {
                CustomUtils.socketLog(' # Success to find id', socket);
                socket.emit(Constants.sendAuthCodeToClient, userID);
            }
        });


        // reset password request
        socket.on(Constants.resetPassword, async (dto) => {
            CustomUtils.socketLog(` # Client request 'reset password'`, socket);
            const result = await dbManager.resetPassword(dto);

            if(result) {
                CustomUtils.socketLog(` # Success to reset password`, socket);
                socket.emit(Constants.resetPasswordResult, Constants.success);
            } else {
                CustomUtils.socketLog(' # Fail to reset password', socket);
                socket.emit(Constants.resetPasswordResult, Constants.fail);
            }
        });


        // get cookie time request
        socket.on(Constants.getCookieTime, async (dto) => {
            CustomUtils.socketLog(` # Client request 'get cookie time'`, socket);
            const result = await dbManager.getCookieTime(dto);

            if(result == null) {
                CustomUtils.socketLog(' # Fail to get cookie times', socket);
                socket.emit(Constants.getCookieTimeResult, Constants.fail);
            } else {
                CustomUtils.socketLog(' # Success to get cookie times', socket);
                socket.emit(Constants.getCookieTimeResult, result);
            }
        });


        // set cookie time request
        socket.on(Constants.setCookieTime, async (dto) => {
            CustomUtils.socketLog(` # Client request 'set cookie time'`, socket);
            const result = await dbManager.setCookieTime(dto);

            if(result) {
                CustomUtils.socketLog(' # Success to set cookie times', socket);
                socket.emit(Constants.setCookieTimeResult, Constants.success);
            } else {
                CustomUtils.socketLog(' # Fail to set cookie times', socket);
                socket.emit(Constants.setCookieTimeResult, Constants.fail);
            }
        });


        // reset cookie room request
        socket.on(Constants.resetCookieRoom, async (dto) => {
            CustomUtils.socketLog(` # Client request 'reset all cookie times'`, socket);
            const result = await dbManager.resetCookieRoom(dto);

            if(result) {
                CustomUtils.socketLog(' # Success to reset all cookie times', socket);
                socket.emit(Constants.resetCookieRoomResult, Constants.success);
            } else {
                CustomUtils.socketLog(' # Fail to reset all cookie times', socket);
                socket.emit(Constants.resetCookieRoomResult, Constants.fail);
            }
        });


        // execute cookie request
        socket.on(Constants.executeCookie, async (dto) => {
            CustomUtils.socketLog(` # Client request 'execute cookie'`, socket);
            const result = await dbManager.executeCookie(dto);

            if(result) {
                CustomUtils.socketLog(' # Success to execute cookie', socket);
                socket.emit(Constants.executeCookieResult, Constants.success);

                const angle = dto.cookieNum == 0 ? 45 : dto.cookieNum == 1 ? 86 : 128;
                // send angle to arduino

            } else {
                CustomUtils.socketLog(' # Fail to execute cookie', socket);
                socket.emit(Constants.executeCookieResult, Constants.fail);
            }
        });

        
        // register machine request
        socket.on(Constants.registerMachine, async (dto) => {
            CustomUtils.socketLog(` # Client request 'register machine'`, socket);
            const result = await dbManager.registerMachine(dto);

            
            if(result) {
                CustomUtils.socketLog(' # Success to register machine', socket);
                socket.emit(Constants.registerMachineResult, Constants.success);
            } else {
                CustomUtils.socketLog(' # Fail to register machine', socket);
                socket.emit(Constants.registerMachineResult, Constants.fail);
            }
        });
    }
}

module.exports = SocketIOEventAndroid;