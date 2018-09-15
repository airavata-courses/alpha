var io = require("socket.io-client"),
  assert = require("assert"),
  expect = require("../server");

describe("Suite of unit tests", function() {
  var socket;

  describe("Check Server Connection", function() {
    it("Should connect to Server", function(done) {
      socket = io.connect(
        "http://localhost:8000",
        {
          "reconnection delay": 0,
          "reopen delay": 0,
          "force new connection": true
        }
      );
      socket.on("connect", () => {
        console.log("socket connected");
        done();
      });
    });
  });
  describe("First Input", function() {
    it("Should return all events", function(done) {
      socket = io.connect(
        "http://localhost:8000",
        {
          "reconnection delay": 0,
          "reopen delay": 0,
          "force new connection": true
        }
      );
      socket.emit("subscribeToLiveStocks", 1000);
      socket.on("stockprice", stockvalue => {
        expect(stockvalue).toBeDefined();
      });
      socket.on("fbstockprice", stockvalue => {
        expect(stockvalue).toBeDefined();
      });
      done();
    });

    // it("Should return second event)", function(done) {
    //   socket = io.connect(
    //     "http://localhost:8000",
    //     {
    //       "reconnection delay": 0,
    //       "reopen delay": 0,
    //       "force new connection": true
    //     }
    //   );
    //   socket.emit("subscribeToLiveStocks", 1000);

    //   done();
    // });
  });

  describe("Input Format", function() {
    it("Should have valid input formats", function(done) {
      socket = io.connect(
        "http://localhost:8000",
        {
          "reconnection delay": 0,
          "reopen delay": 0,
          "force new connection": true
        }
      );
      socket.emit("subscribeToLiveStocks", 1000);
      socket.on("stockprice", stockvalue => {
        if (!NaN(stockvalue)) {
          console.log("Is a valid Stock Value");
        }
      });
      socket.on("fbstockprice", stockvalue => {
        if (!NaN(stockvalue)) {
          console.log("Is a valid FB Stock Value");
        }
      });
      done();
    });
  });
});
