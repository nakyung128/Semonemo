const express = require('express');
const router = express.Router();
const multer = require('multer');
const coinController = require('../controllers/coinController');

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

// 지갑 코인 잔고 확인
router.get('/:address', coinController.remainCoin);

// 코인 발행 (메인서버에서만 요청 실행 가능)
router.post('/buy', checkIP, upload.none(), coinController.buyCoin);

// 코인 전송
router.post('/transfer', upload.none(), coinController.transferCoin);
router.post('/transfer/signed', upload.none(), coinController.transferCoinSigned);

router.post('/transfer/signed/test', upload.none(), coinController.transferCoinTEST);


module.exports = router;