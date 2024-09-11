const express = require('express');
const router = express.Router();
const multer = require('multer');
const nftController = require('../controllers/nftController');

const upload = multer({ storage: multer.memoryStorage() });

router.post('/mint', upload.single('frame'), nftController.mintNFT);
router.get('/active', nftController.getActiveNFTs);

module.exports = router;