var UserModel = require('../models/userModel.js');
var TradeModel = require('../models/tradeModel.js');
const apiInterface = require("../utils/apiInterface");
const options = require("../options.json");

/**
 * userController.js
 *
 * @description :: Server-side logic for managing users.
 */
module.exports = {

    /**
     * userController.show()
     */
    show: async function (req, res) {
        const uuid = req.params.uuid;

        UserModel.findOne({uuid: uuid}, async function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting user.',
                    error: err
                });
            }

            if (!user) {
                return res.status(404).json({
                    message: 'No such user'
                });
            }

            // Get all users trades
            user.trades = await TradeModel.find({uuid: uuid}).exec();


            // Build user portfolio from their trades
            /*
                portfolio: [
                    {
                        crypto_symbol: "BTC",
                        crypto_amount: 0.92123562,
                        fiat_paid: 1203,
                        fiat_worth: 43534       // Calculated at the end
                    }
                ]
            */
            var portfolio = [];
            user.trades.forEach(trade => {

                let index = portfolio.findIndex(x => x.crypto_symbol == trade.crypto_symbol);

                if (index != -1) {
                    let crypto_amount = (trade.type == "buy") ? trade.crypto_value : -1 * trade.crypto_value;
                    let fiat_paid     = (trade.type == "buy") ? trade.fiat_value    : -1 * trade.fiat_value;

                    portfolio[index].crypto_amount += crypto_amount;
                    portfolio[index].fiat_paid     += fiat_paid;

                } else {
                    portfolio.push({
                        crypto_symbol: trade.crypto_symbol,
                        crypto_amount: (trade.type == "buy") ? trade.crypto_value : -1 * trade.crypto_value,
                        fiat_paid: (trade.type == "buy") ? trade.fiat_value : 0,
                        fiat_worth: 0
                    });
                }
            });

            // Calculate portfolio's current worth for each cryptocurrency
            for (let i = 0; i < portfolio.length; i++) {
                let price = await apiInterface.getPrice(portfolio[i].crypto_symbol);
                portfolio[i].fiat_worth = portfolio[i].crypto_amount * price;
            }


            // Create new object with trades, because for some reason it doesn't work when adding new property to user and sending user object
            let tmp = {
                _id: user._id, 
                uuid: user.uuid, 
                balance: user.balance, 
                starting_balance: user.starting_balance, 
                trades: user.trades,
                portfolio: portfolio
            };

            return res.json(tmp);
        });
    },

    /**
     * userController.create()
     */
    create: function (req, res) {

        // Check if starting balance is one of the defined starting balances
        let found = options.startingDifficulties.find(x => x.balance == req.body.starting_balance);
        if (found == undefined) {
            return res.status(400).json({success: false, message: "Invalid starting balance."});
        }


        var user = new UserModel({
			uuid : req.body.uuid,
			starting_balance : req.body.starting_balance,
			balance : req.body.starting_balance
        });

        user.save(function (err, user) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating user',
                    error: err
                });
            }

            return res.status(201).json(user);
        });
    },

    /**
     * userController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        UserModel.findOne({_id: id}, function (err, user) {
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

            user.uuid = req.body.uuid ? req.body.uuid : user.uuid;
			user.starting_balance = req.body.starting_balance ? req.body.starting_balance : user.starting_balance;
			user.balance = req.body.balance ? req.body.balance : user.balance;
			
            user.save(function (err, user) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating user.',
                        error: err
                    });
                }

                return res.json(user);
            });
        });
    }
};
