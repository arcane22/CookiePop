/**
 * @file database.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

/* Module */
const mysql2 = require('@base/mysql2/promise');
const config = require('@root/cfg/config');


/**
 * @class Database
 */
class Database {
    #timeout = 1000;
    #poll = null;

    /**
     * 
     */
    async init() {
        if(this.#poll != null) return;
        this.#poll = await mysql2.createPool(config.database);
    }

    /**
     * @returns {mysql2.Pool}
     */
     getPoll() {
        return this.#poll;
    }

    /**
     * 
     * @returns {mysql2.Connection}
     */
    async getConnection() {
        if(this.#poll == null) return null;

        let connection = null;
        let retryCnt = 0;
        while(connection == null) {
            try{
                connection = await this.getPoll().getConnection();
            } catch(e) {
                retryCnt++;
                console.error(`Database connection error - retry ${retryCnt}`);
                setTimeout(() => {
                }, this.#timeout);
            }
        }

        return connection;
    }
}

module.exports = Database;