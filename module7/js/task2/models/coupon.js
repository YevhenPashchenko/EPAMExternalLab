const {DataTypes, Model} = require('sequelize');
const sequelize = require('../index');

class Coupon extends Model {}

module.exports = Coupon.init({
  id: {
    type: DataTypes.BIGINT,
    primaryKey: true,
    allowNull: false
  },
  company_name: {
    type: DataTypes.STRING,
    allowNull: false
  },
  name: {
    type: DataTypes.STRING,
    allowNull: false
  },
  valid_to: {
    type: DataTypes.DATE,
    allowNull: false
  },
  price: {
    type: DataTypes.DECIMAL(7, 2),
    allowNull: false
  },
  short_description: {
    type: DataTypes.TEXT,
    allowNull: false
  },
  long_description: {
    type: DataTypes.TEXT,
    allowNull: false
  },
  image: {
    type: DataTypes.STRING,
    allowNull: false
  }
}, {
  sequelize,
  tableName: 'coupon'
});