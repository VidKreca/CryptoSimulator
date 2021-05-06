var TradeModel = require('../models/tradeModel.js');

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
        var id = req.params.id;

        TradeModel.findOne({_id: id}, function (err, trade) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting trade.',
                    error: err
                });
            }

            if (!trade) {
                return res.status(404).json({
                    message: 'No such trade'
                });
            }

            return res.json(trade);
        });
    },

    /**
     * tradeController.create()
     */
    create: function (req, res) {
        var trade = new TradeModel({
			uuid : req.body.uuid,
			type : req.body.type,
			fiat_value : req.body.fiat_value,
			crypto_value : req.body.crypto_value,
			crypto_symbol : req.body.crypto_symbol,
			fiat : req.body.fiat
        });

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
