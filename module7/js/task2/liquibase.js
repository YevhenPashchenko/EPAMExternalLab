require('dotenv').config();

const config = {
  ...(require('liquibase').POSTGRESQL_DEFAULT_CONFIG),
  changeLogFile: process.env.PATH_TO_CHANGELOG,
  url: process.env.DB_URL + process.env.DB_NAME,
  username: process.env.DB_USERNAME,
  password: process.env.DB_PASSWORD
};

exports.LIQUIBASE_CONFIG = config;