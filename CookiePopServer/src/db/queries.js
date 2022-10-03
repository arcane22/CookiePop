/**
 * @file queries.js
 * @author arcane222, Lee Hong Jun
 * @description
 *  - Version: 1.0.0
 *  - Last Modified: 2022.10.02
 */

module.exports = {
    api: {
        signIn: 'SELECT * FROM user_table WHERE user_id=?',
        signUp: 'INSERT INTO user_table (uid, user_id, user_pw, user_salt, user_name, user_age, user_birthday, user_signupdate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)',
        findId: 'SELECT user_id FROM user_table WHERE user_name=? AND user_birthday=?',
        resetPassword: 'UPDATE user_table SET user_pw=?, user_salt=? WHERE user_id=?',
        getCookieTime: 'SELECT user_cookieTime1, user_cookieTime2, user_cookieTime3 FROM user_table WHERE user_id=?',
        setCookieTime: '',
        resetCookieRoom: 'UPDATE user_table SET user_cookieTime1=?, user_cookieTime2=?, user_cookieTime3=? WHERE user_id=?',
        executeCookie: '',
        getAllCookieTime: 'SELECT user_id, user_machineId, user_cookieTime1, user_cookieTime2, user_cookieTime3 FROM user_table WHERE user_cookieTime1 IS NOT NULL OR user_cookieTime2 IS NOT NULL OR user_cookieTime3 IS NOT NULL',
        registerMachine: 'UPDATE user_table SET user_machineId=? WHERE user_id=?',
    },
};
