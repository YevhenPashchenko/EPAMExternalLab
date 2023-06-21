require('dotenv').config();
const express = require('express');
const {Sequelize} = require('sequelize');
const Liquibase = require('liquibase').Liquibase;
const LIQUIBASE_CONFIG = require('./liquibase').LIQUIBASE_CONFIG;

const app = express();
module.exports = new Sequelize(
    process.env.DB_NAME,
    process.env.DB_USERNAME,
    process.env.DB_PASSWORD,
    {
      host: process.env.DB_HOST,
      dialect: process.env.DB_DIALECT
    }
);

app.listen(process.env.PORT, () => {
  const liquibase = new Liquibase(LIQUIBASE_CONFIG);
  liquibase.update({}).then(() => {
    liquibase.status().then();
  });
});
app.use(express.static(__dirname + '/views/'));
require('./controllers/main')(app);