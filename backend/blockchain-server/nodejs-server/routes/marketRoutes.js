const express = require('express');
const router = express.Router();
const marketController = require('../controllers/marketController');

router.get('/info/:nftId', marketController.getMarketInfo);

module.exports = router;