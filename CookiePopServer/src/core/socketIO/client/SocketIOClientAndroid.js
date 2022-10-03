/**
 * @file socketIOClientAndroid.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - socket.io client for testing api server
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

/* Modules */
const SocketIOClient = require('@root/src/core/socketIO/client/socketIOClient');
const Constants = require('@root/src/utils/constants');
const yyyymmdd = require('@base/yyyy-mm-dd/index');
const CustomUtils = require('@src/utils/customUtils');

/**
 * @class SocketIOClientAndroid
 * @extends SocketIOClient
 */
class SocketIOClientAndroid extends SocketIOClient{
    run(uri) {
        super.run(uri, Constants.type_android);

        //this.testAll();
        this.setCookieTimeTest();
    }

    signInTest() {
        this.io.emit(Constants.signIn, {
            userID: 'abc3@naver.com', 
            userPW: 'abcd',
        });

        this.io.on(Constants.signInResult, (dto) => {
            CustomUtils.socketLog(`sign in result: `, this.io);
            console.log(dto);
        });
    }

    signUpTest() {
        this.io.emit(Constants.signUp, {
            userID: 'abc3@naver.com', 
            userPW: 'b',
            userName: 'abc',
            userAge: 26,
            userBirthday: '19970608',
        });

        this.io.on(Constants.signUpResult, (dto) => {
            CustomUtils.socketLog(`sign up result: ${dto}`, this.io);
        });
    }

    findIdTest() {
        this.io.emit(Constants.findId, {
            userName: 'abc', 
            userBirthday: '19970608',
        });

        this.io.on(Constants.findIdResult, (dto) => {
            CustomUtils.socketLog(`find id result: ${dto}`, this.io);
        });
    }

    resetPasswordTest() {
        this.io.emit(Constants.resetPassword, {
            userID: 'abc3@naver.com', 
            userPW: 'abcd',
        });

        this.io.on(Constants.resetPasswordResult, (dto) => {
            CustomUtils.socketLog(`reset password result: ${dto}`, this.io);
        });
    }

    getCookieTimeTest() {
        this.io.emit(Constants.getCookieTime, {
            userID: 'abc3@naver.com', 
        });

        this.io.on(Constants.getCookieTimeResult, (dto) => {
            CustomUtils.socketLog(`get cookie time result:`);
            console.log(dto);
        });
    }

    setCookieTimeTest() {
        let date = new Date();
        date.setMinutes(date.getMinutes() + 3); // now + 3 min
        this.io.emit(Constants.setCookieTime, {
            cookieNum: 1,
            cookieTime: yyyymmdd.withTime(date),
            userID: 'abc3@naver.com',
        });

        this.io.on(Constants.setCookieTimeResult, (dto) => {
            CustomUtils.socketLog(`set cookie time result: ${dto}`, this.io);
        });
    }

    resetCookieRoomTest() {
        this.io.emit(Constants.resetCookieRoom, {
            userID: 'abc3@naver.com',
        });

        this.io.on(Constants.resetCookieRoomResult, (dto) => {
            CustomUtils.socketLog(`reset cookie room result: ${dto}`, this.io);
        });
    }

    executeCookieTest() {
        this.io.emit(Constants.executeCookie, {
            cookieNum: 0,
            userID: 'abc3@naver.com',
        });

        this.io.on(Constants.executeCookieResult, (dto) => {
            CustomUtils.socketLog(`execute cookie result: ${dto}`, this.io);
        });
    }

    registerMachineTest() {
        this.io.emit(Constants.registerMachine, {
            machineID: 'abcdef',
            userID: 'abc3@naver.com',
        });

        this.io.on(Constants.registerMachineResult, (dto) => {
            CustomUtils.socketLog(`register machine result: ${dto}`, this.io);
        });
    }

    testAll() {
        this.signInTest();
        this.signUpTest();
        this.findIdTest();
        this.resetPasswordTest();
        this.getCookieTimeTest();
        this.setCookieTimeTest();
        this.resetCookieRoomTest();
        this.executeCookieTest();
        this.registerMachineTest();
    }
}


module.exports = SocketIOClientAndroid;