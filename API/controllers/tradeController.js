var TradeModel = require('../models/tradeModel.js');
var UserModel = require('../models/userModel.js');
const apiInterface = require("../utils/apiInterface");

/**
 * tradeController.js
 *
 * @description :: Server-side logic for managing trades.
 */
module.exports = {

    /**
     * tradeController.list()
     */
    list: function (req, res) {
        TradeModel.find(function (err, trades) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting trades.',
                    error: err
                });
            }

            return res.json(trades);
        });
    },

    /**
     * tradeController.show()
     */
    show: function (req, res) {
        var uuid = req.params.uuid;

        TradeModel.find({uuid: uuid}, function (err, trades) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting trades by uuid.',
                    error: err
                });
            }

            return res.json(trades);
        });
    },

    /**
     * tradeController.create()
     */
    create: async function (req, res) {
        // Calculate how much of this cryptocurrency the user gets
        var current_price = await apiInterface.getPrice(req.body.crypto_symbol, req.body.fiat);
        var crypto_value = parseFloat(req.body.fiat_value) / current_price;

        var trade = new TradeModel({
			uuid : req.body.uuid,
			type : req.body.type,
			fiat_value : req.body.fiat_value,
			fiat : req.body.fiat,
			crypto_value : crypto_value,
			crypto_symbol : req.body.crypto_symbol,
        });


        // Update user balance
        UserModel.findOne({uuid: req.body.uuid}, function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting user',
                    error: err
                });
            }

            if (!user) {
                return res.status(404).json({
                    message: 'No such user'
                });
            }

            user.balance = user.balance - req.body.fiat_value;
            
            user.save(function (err, user) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating user balance.',
                        error: err
                    });
                }
            });
        });



        // Save new trade, if it we fail here: user lost account balance but trade didn't go through, a jebiga scenarij
        trade.save(function (err, trade) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating trade',
                    error: err
                });
            }

            return res.status(201).json(trade);
        });


        
    }
};
