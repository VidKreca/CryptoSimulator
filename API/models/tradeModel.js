var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var tradeSchema = new Schema({
	'uuid' : String,
	'type' : String,
	'fiat_value' : Number,
	'fiat' : String,
	'crypto_value' : Number,
	'crypto_symbol' : String
});

module.exports = mongoose.model('trade', tradeSchema);
