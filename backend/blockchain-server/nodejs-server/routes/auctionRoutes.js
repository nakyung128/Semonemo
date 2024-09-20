const express = require('express');
const router = express.Router();
const multer = require('multer');
const auctionController = require('../controllers/auctionController');

const upload = multer({ storage: multer.memoryStorage() });

router.post('/start', upload.none(), auctionController.startAuction);

module.exports = router;