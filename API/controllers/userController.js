var UserModel = require('../models/userModel.js');
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
    show: function (req, res) {
        var id = req.params.id;

        UserModel.findOne({_id: id}, function (err, user) {
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

            return res.json(user);
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
