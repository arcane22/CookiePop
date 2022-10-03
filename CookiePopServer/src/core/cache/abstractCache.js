/**
 * @file abstractCache.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

/**
 * @class AbstractCache
 */
class AbstractCache {
    #cacheSocket = {};
    #cacheId = {};

    /**
     * 
     * @param {*} socket 
     * @param {*} id
     */
    add(socket, id) {
        this.#cacheSocket[socket] = id;
        this.#cacheId[id] = socket;
    }

    /**
     * 
     * @param {*} socket 
     */
    remove(socket) {
        const id = this.#cacheSocket[socket];
        delete this.#cacheSocket[socket];
        delete this.#cacheId[id];
    }

    /**
     * 
     */
    getId(socket) {
        return this.#cacheSocket[socket];
    }

    getSocket(id) {
        return this.#cacheId[id];
    }

    /**
     * 
     * @returns 
     */
    keys() {
        return Object.keys(this.#cacheSocket);
    }

    values() {
        return Object.values(this.#cacheSocket);
    }

    /**
     * 
     * @returns 
     */
    size() {
        return this.keys().length;
    }

    /**
     * 
     * @param {*} socket 
     * @returns 
     */
    containsSocket(socket) {
        return socket in this.#cacheSocket;
    }

    /**
     * 
     * @param {*} id 
     * @returns 
     */
    containsId(id) {
        return id in this.#cacheId;
    }

    /**
     * 
     * @returns 
     */
    firstKey() {
        return this.keys()[0];
    }

    /**
     * 
     * @returns 
     */
    lastKey() {
        return this.keys()[this.size() - 1];
    }
}

module.exports = AbstractCache;