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
router.post('/auction/cancle', upload.none(), testController.cancleAuctionTest);

router.post('/market/create', upload.none(), testController.createMarketTest);
router.post('/market/cancle', upload.none(), testController.cancleMarketTest);
router.post('/market/buy', upload.none(), testController.buyMarketTest);

router.post('/transfer', upload.none(), testController.testTransfer);
router.post('/get/coin', upload.none(), testController.testTransfer);
module.exports = router;