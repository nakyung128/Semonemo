const express = require('express');
const router = express.Router();
const multer = require('multer');
const marketController = require('../controllers/marketController');

const upload = multer({ storage: multer.memoryStorage() });

// 마켓 조회
router.get('/:tokenId', marketController.getMarketInfo);

// 마켓 생성
router.post('/create', upload.none(), marketController.createMarket);

// 마켓 취소
router.post('/cancle', upload.none(), marketController.cancleMarket);

// 마켓 구매
router.post('/buy', upload.none(), marketController.buyMarket);

module.exports = router;