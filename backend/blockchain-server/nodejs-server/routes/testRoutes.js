const express = require('express');
const router = express.Router();
const multer = require('multer');
const testController = require('../controllers/testController');

const upload = multer({ storage: multer.memoryStorage() });

// NFT 발행 테스트
router.post('/nft/mint', upload.single('frame'), testController.mintNFTTest);

router.post('/coin/deposit', upload.none(), testController.depositCoinTest);
router.post('/coin/withdraw', upload.none(), testController.withdrawCoinTest);

router.post('/auction/start', upload.none(), testController.startAuctionTest)

module.exports = router;