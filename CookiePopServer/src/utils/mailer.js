/**
 * @file socketIOServer.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Socket.IO server based on socket.io v2.4.1
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

const mailer = require('@base/nodemailer');
const Encrypt = require('@src/utils/encrypt');
const config = require('@root/cfg/config');

class Mailer {
    /**
     * @function constructor
     * @description constructor of Mailer class (Initialize nodemailer's transporter - read config file)
     */
    constructor() {
        this.transporter = mailer.createTransport(config.mailer);
    }

    /**
     * @async
     * @function sendMail
     * @description send e-mail to destination.
     *
     * @param {string} dest Destination for sending e-mail. (ex. user's e-mail address)
     * @param {string} subject The title of the e-mail.
     * @param {string} text Text to be displayed in e-mail.
     * @throws {error}
     */
     async sendMail(dest, subject, text) {
        const mailOps = {
            from: config.mailer.auth.user,
            to: dest,
            subject,
            text,
        };

        try {
            await this.transporter.sendMail(mailOps);
            this.transporter.close();
        } catch (err) {
            throw err;
        }
    }

    /**
     * @async @function sendAuthCode
     * @description Send authentication code e-mail to destination.
     *
     * @param {string} dest Destination for sending authentication e-mail. (ex. user's e-mail address)
     * @param {string} code Auth code to send to client
     * @throws {error}
     */
    async sendAuthCode(dest, code) {
        let result = false;
        try {
            const subject = 'CookiePop Authentication Code';
            const text = 'CookiePop Authentication Code\n' + code;
            await this.sendMail(dest, subject, text);
            result = true;
        } catch (err) {
            throw err;
        } finally {
            return result;
        }
    }
}

module.exports = new Mailer()