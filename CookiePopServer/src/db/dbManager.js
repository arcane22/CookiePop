/**
 * @file dbManager.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

/* Modules */
const Database = require('@src/db/database');
const Encrypt = require('@src/utils/encrypt');
const queries = require('@src/db/queries');
const yyyymmdd = require('@base/yyyy-mm-dd/index');

/**
 * @class DBManager
 */
class DBManager {
    #mysqlDAO = null;

    /**
     * @private
     * @returns {Database}
     */
    #getDAO() {
        return this.#mysqlDAO;
    }

    async init() {
        if(this.#mysqlDAO != null) return;
        this.#mysqlDAO = new Database();
        await this.#mysqlDAO.init();
    }

    /**
     * @async
     * @function signIn
     * @param {object} dto { userID, userPW }
     */
    async signIn(dto) {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            const userID = dto.userID;
            const userPW = dto.userPW;
            let rows = null;

            try {
                await conn.beginTransaction();
                rows = (await conn.execute(queries.api.signIn, [userID]))[0][0];
                await conn.commit();

                const salt = rows.user_salt;
                const hash = await Encrypt.createHash(userPW, salt);
                if(rows.user_pw !== hash) {
                    rows = null;
                }
                delete rows['user_pw'];
                delete rows['user_salt'];
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return rows;
            }
        } catch(e) {
            return null;
        }
    }

    /**
     * @async
     * @function signUp
     * @param {object} dto { userID, userPW, userName, userAge, userBirthday }
     * @returns 
     */
    async signUp(dto) {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            const salt = await Encrypt.createSalt(8);
            const uid = await Encrypt.createHash(dto.userID, salt);
            const pwHash = await Encrypt.createHash(dto.userPW, salt);
            const values = [uid, dto.userID, pwHash, salt, dto.userName, dto.userAge, dto.userBirthday, yyyymmdd.withTime()];
            const rows = null;

            try {
                await conn.beginTransaction();
                rows = await conn.execute(queries.api.signUp, values);
                await conn.commit();
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return rows;
            }
        } catch(e) {
            console.error(e);
            return null;
        }
    }

    /**
     * @async
     * @function findId
     * @param {object} dto { userName, userBirthday }
     * @returns 
     */
    async findId(dto) {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            const userName = dto.userName;
            const userBirthday = dto.userBirthday;
            let id = null;
            try {
                await conn.beginTransaction();
                id = (await conn.execute(queries.api.findId, [userName, userBirthday]))[0][0];
                await conn.commit();
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return id;
            }
        } catch(e) {
            console.error(e);
            return null;
        }
    }

    /**
     * @async
     * @function resetPassword
     * @param {object} dto { userID, userPW }
     * @returns 
     */
    async resetPassword(dto) {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            const userID = dto.userID;
            const userPW = dto.userPW;
            const salt = await Encrypt.createSalt(8);
            const pwHash = await Encrypt.createHash(userPW, salt);
            let result = false;

            try {
                await conn.beginTransaction();
                await conn.execute(queries.api.resetPassword, [pwHash, salt, userID]);
                await conn.commit();
                result = true;
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return result;
            }
        } catch(e) {
            console.error(e);
            return null;
        }
    }

    /**
     * @async
     * @function getCookieTime
     * @param {object} dto { userID }
     */
    async getCookieTime(dto) {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            const userID = dto.userID;
            let result = null;

            try {
                await conn.beginTransaction();
                result = (await conn.execute(queries.api.getCookieTime, [userID]))[0][0];
                await conn.commit();
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return result;
            }
        } catch(e) {
            console.error(e);
            return null;
        }
    }

    /**
     * @async
     * @function setCookieTime
     * @param {object} dto { cookieNum, cookieTime, userID }
     * @returns 
     */
    async setCookieTime(dto) {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            const cookieNum = dto.cookieNum;
            const cookieTime = dto.cookieTime;
            const userID = dto.userID;
            const cookieName = cookieNum == 0 ? 'user_cookieTime1' : 
                               cookieNum == 1 ? 'user_cookieTime2' : 
                                                'user_cookieTime3';
            let result = false;
            let query = `UPDATE user_table SET ${cookieName}=? WHERE user_id=?`;

            try {
                await conn.beginTransaction();
                result = (await conn.execute(query, [cookieTime, userID]))[0][0];
                await conn.commit();
                result = true;
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return result;
            }
        } catch(e) {
            console.error(e);
            return null;
        }
    }

    /**
     * @async
     * @function resetCookieRoom
     * @param {object} dto { userID }
     * @returns 
     */
    async resetCookieRoom(dto) {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            const userID = dto.userID;
            let result = false;

            try {
                await conn.beginTransaction();
                result = await conn.execute(queries.api.resetCookieRoom, [null, null, null, userID]);
                await conn.commit();
                result = true;
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return result;
            }
        } catch(e) {
            console.error(e);
            return null;
        }
    }

    /**
     * @async
     * @function executeCookie
     * @param {object} dto { cookieNum, userID } 
     * @returns 
     */
    async executeCookie(dto) {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            const cookieNum = dto.cookieNum;
            const userID = dto.userID;
            const cookieName = cookieNum == 0 ? 'user_cookieTime1' : 
                               cookieNum == 1 ? 'user_cookieTime2' : 
                                                'user_cookieTime3';
            let result = false;
            let query = `UPDATE user_table SET ${cookieName}=? WHERE user_id=?`;

            try {
                await conn.beginTransaction();
                result = await conn.execute(query, [null, userID]);
                await conn.commit();
                result = true;
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return result;
            }
        } catch(e) {
            console.error(e);
            return null;
        }
    }

    /**
     * 
     * @returns 
     */
    async getAllCookieTime() {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            let result = null;

            try {
                await conn.beginTransaction();
                result = (await conn.execute(queries.api.getAllCookieTime))[0];
                await conn.commit();
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return result;
            }
        } catch(e) {
            console.error(e);
            return null;
        }
    }

    /**
     * 
     * @param {*} dto 
     * @returns 
     */
    async registerMachine(dto) {
        try {
            const conn = await this.#getDAO().getConnection();
            if(conn == null) return null;

            const userID = dto.userID;
            const machineID = dto.machineID;
            let result = false;

            try {
                await conn.beginTransaction();
                await conn.execute(queries.api.registerMachine, [machineID, userID]);
                await conn.commit();
                result = true;
            } catch(e) {
                await conn.rollback();
                console.error(e);
            } finally {
                conn.release();
                return result;
            }
        } catch(e) {
            console.error(e);
            return null;
        }
    }
}

module.exports =  new DBManager();