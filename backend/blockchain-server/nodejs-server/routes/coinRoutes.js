const express = require('express');
const router = express.Router();
const multer = require('multer');
const coinController = require('../controllers/coinController');

const upload = multer({ storage: multer.memoryStorage() });

router.post('/mint', upload.none(), coinController.mintCoin);

module.exports = router;