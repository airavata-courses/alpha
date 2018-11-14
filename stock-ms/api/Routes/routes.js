module.exports = function(app) {
  var stocksController = require("../Controllers/stocksController");
  app.route("/stocks/:symbol").get(stocksController.get_stock_bySymbol);
};
