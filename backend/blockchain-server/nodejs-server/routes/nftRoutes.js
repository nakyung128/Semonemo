const express = require('express');
const router = express.Router();
const multer = require('multer');
const nftController = require('../controllers/nftController');

const upload = multer({ storage: multer.memoryStorage() });

// 활성화된(소각되지 않은) NFT 리스트 조회
router.get('/list/active', nftController.getActiveNFTs);

router.post('/mint', upload.single('frame'), nftController.mintNFT);

module.exports = router;