const express = require('express');
const router = express.Router();
const auctionController = require('../controllers/auctionController');

router.get('/info/:nftId', auctionController.getAuctionInfo);

module.exports = router;