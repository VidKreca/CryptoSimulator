var express = require('express');
var router = express.Router();
var tradeController = require('../controllers/tradeController.js');

/*
 * GET
 */
router.get('/', tradeController.list);

/*
 * GET
 */
router.get('/:uuid', tradeController.show);

/*
 * POST
 */
router.post('/', tradeController.create);



module.exports = router;
