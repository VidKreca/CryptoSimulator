var express = require('express');
var router = express.Router();
var userController = require('../controllers/userController.js');


/*
 * GET
 */
router.get('/:uuid', userController.show);

/*
 * POST
 */
router.post('/', userController.create);

/*
 * PUT
 */
router.put('/:id', userController.update);



module.exports = router;
