// Import libraries
const createError = require('http-errors');
const express = require('express');
const path = require('path');
const logger = require('morgan');
const mongoose = require("mongoose");
const app = express();


// Import routers
const apiRouter = require("./routes/apiRoutes");
const userRouter = require("./routes/userRoutes");
const tradeRouter = require("./routes/tradeRoutes");


// Use libraries with Express app
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));


// Mongoose setup
const connString = "mongodb://localhost:27017/CryptoSimulator";
mongoose.connect(connString, {useNewUrlParser: true, useUnifiedTopology: true});
mongoose.Promise = global.Promise;
var db = mongoose.connection;
db.on("error", console.error.bind(console, "MongoDB connection error"));


// Use routers
app.use("/api/", apiRouter);
app.use("/user/", userRouter);
app.use("/trade/", tradeRouter);


// Serve static icons, serve missing.png for invalid file names
let dir = path.join(__dirname, "public/icons/");
app.use("/img/", express.static(dir));
app.use("/img/", (req, res) => {
	res.sendFile(path.join(dir, "missing.png"));
});


// Catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});


// Error handler
app.use(function(err, req, res, next) {
  // Set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  res.status(err.status || 500).send("Error: " + err.message);
});




module.exports = app;
