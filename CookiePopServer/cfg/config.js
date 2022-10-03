require('dotenv').config();

module.exports = {
    mailer: {
        service: 'gmail',                  // smtp 서비스
        host: 'smtp.gmail.com',            // smtp 호스트
        port: 587,                         // smtp 포트
        secure: true,                      // secure 옵션
        auth: { 
            user: process.env.MAILER_ID,
            pass: process.env.MAILER_PW,
        },
    },
    database: {
        host     : 'localhost',                  // 호스트 주소
        port     : 3306,                         // 포트 번호
        user     : 'root',                       // mysql user
        password : process.env.DB_PASSWORD,      // mysql password
        database : process.env.DB_NAME ,         // mysql 데이터베이스
        waitForConnections: true,
        connectionLimit: 10,
    },
};
