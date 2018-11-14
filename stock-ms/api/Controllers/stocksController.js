exports.get_stock_bySymbol = function(req, res) {
  let symbol = req.params.symbol;
  let basevalue = 100;
  if (symbol == "apple") {
    let datetime = new Date();
    let updatedvalue = basevalue + datetime.getMilliseconds();
    res.json({ value: updatedvalue });
  } else {
    res.json({ value: "200" });
  }
};
