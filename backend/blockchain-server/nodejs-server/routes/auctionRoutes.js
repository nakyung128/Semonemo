const express = require('express');
const router = express.Router();
const multer = require('multer');
const auctionController = require('../controllers/auctionController');

const upload = multer({ storage: multer.memoryStorage() });

// 함수 호출을 허락하는 IP
const ALLOWED_IPS = [
  '::1', // IPv6 로컬호스트
  '127.0.0.1', // IPv4 로컬호스트
]

// IP 체크 미들웨어
const checkIP = (req, res, next) => {
  const clientIP = req.ip;
  if (!ALLOWED_IPS.includes(clientIP)) {
    return res.status(403).json({ error: 'Access denied' });
  }
  next();
};

// 옥션 조회
router.get('/:tokenId', auctionController.getAuctionInfo);

// 옥션 시작
router.post('/start', upload.none(), auctionController.startAuction);

// 옥션 취소
router.post('/cancle', upload.none(), auctionController.cancleAuction);

// 옥션 종료
router.post('/close', checkIP, upload.none(), auctionController.closeAuction);

module.exports = router;