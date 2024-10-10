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

// 코인 잔고 확인
router.get('/:address', coinController.remainCoin);

// 코인 발행 (메인서버에서만 요청 실행 가능)
router.post('/buy', checkIP, upload.none(), coinController.buyCoin);

// 코인 전송
router.post('/transfer', upload.none(), coinController.transferCoin);

// 선입금 코인 전송
router.post('/transfer/deposit', upload.none(), coinController.transferDepositCoin);

// 선입금 코인으로 전환
router.post('/deposit', upload.none(), coinController.depositCoin);

// 선입금 코인, 그냥 코인으로
router.post('/withdraw', upload.none(), coinController.withdrawCoin);

module.exports = router;