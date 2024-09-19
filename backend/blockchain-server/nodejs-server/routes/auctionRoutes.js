const express = require('express');
const router = express.Router();
const multer = require('multer');
const auctionController = require('../controllers/auctionController');

const upload = multer({ storage: multer.memoryStorage() });

router.get('/owned', auctionController.getAuctionOwned);
router.get('/info/:nftId', auctionController.getAuctionInfo);

router.post('/create', upload.none(), auctionController.createAuction);

router.post('/bid', upload.none(), auctionController.bidTest)

router.post('/close', upload.none(), auctionController.auctionCloseTest);

module.exports = router;