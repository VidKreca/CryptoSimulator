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
router.get('/:id', tradeController.show);

/*
 * POST
 */
router.post('/', tradeController.create);

/*
 * PUT
 */
router.put('/:id', tradeController.update);

/*
 * DELETE
 */
router.delete('/:id', tradeController.remove);



module.exports = router;
