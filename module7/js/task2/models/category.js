const {DataTypes, Model} = require('sequelize');
const sequelize = require('../index');

class Category extends Model {}

module.exports = Category.init({
  id: {
    type: DataTypes.BIGINT,
    primaryKey: true,
    allowNull: false
  },
  name: {
    type: DataTypes.STRING,
    allowNull: false
  },
  image: {
    type: DataTypes.STRING,
    allowNull: false
  }
}, {
  sequelize,
  tableName: 'category'
});