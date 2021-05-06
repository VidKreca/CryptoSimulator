var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var userSchema = new Schema({
	'uuid' : String,
	'starting_balance' : Number,
	'balance' : Number
});

module.exports = mongoose.model('user', userSchema);
