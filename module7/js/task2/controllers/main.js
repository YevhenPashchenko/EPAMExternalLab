const Category = require('../models/category');
const Coupon = require('../models/coupon');
const {Op} = require('sequelize');

module.exports = function (app) {
  app.get('/', async (req, res) => {
    const categories = await Category.findAll();
    const coupons = await Coupon.findAll({
      order: [
          ['createdAt', 'DESC']
      ],
      limit: 10
    });
    res.render('main.ejs', {categories: categories, coupons: coupons});
  });
  app.get('/scroll', async (req, res) => {
    const options = {
      order: [
        ['createdAt', 'DESC']
      ],
      offset: req.query.page * 10,
      limit: 10
    };
    if (req.query.tag !== 'All categories') {
      addSearchByCategoryToOptions(options, req);
    }
    if (req.query.text !== '') {
      addSearchByNameOrDescriptionToOptions(options, req);
    }
    const coupons = await Coupon.findAll(options);
    res.send(coupons);
  });
  app.get('/search', async (req, res) => {
    const options = {
      order: [
        ['createdAt', 'DESC']
      ],
      limit: 10
    };
    if (req.query.tag !== 'All categories') {
      addSearchByCategoryToOptions(options, req);
    }
    if (req.query.text !== '') {
      addSearchByNameOrDescriptionToOptions(options, req);
    }
    const coupons = await Coupon.findAll(options);
    res.send(coupons);
  });
};

function addSearchByCategoryToOptions(options, req) {
  Coupon.belongsTo(Category, {foreignKey: 'category_id'});
  options.where = {
    [Op.and]: [{'$Category.name$': req.query.tag}]
  };
  options.include = [{model: Category}];
}

function addSearchByNameOrDescriptionToOptions(options, req) {
  let or = {
    [Op.or]: [
      {name: {[Op.iLike]: '%' + req.query.text + '%'}},
      {short_description: {[Op.iLike]: '%' + req.query.text + '%'}}
    ]
  };
  if (options.where === undefined) {
    options.where = or;
  } else {
    options.where[Op.and].push(or);
  }
}