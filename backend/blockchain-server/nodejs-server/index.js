const express = require('express');
const cors = require('cors');
const nftRoutes = require('./routes/nftRoutes');
const coinRoutes = require('./routes/coinRoutes');
const auctionRoutes = require('./routes/auctionRoutes');
const marketRoutes = require('./routes/marketRoutes');

const systemController = require('./controllers/systemController');

const testRoutes = require('./routes/testRoutes');

const app = express();

app.use(cors({
  origin: '*',
}));
app.use(express.json());

// 모든 API 라우트에 '/bcapi' 접두사 추가
app.use('/bcapi/nft', nftRoutes);
app.use('/bcapi/coin', coinRoutes);
app.use('/bcapi/market', marketRoutes);
app.use('/bcapi/auction', auctionRoutes);

app.use('/bcapi/test', testRoutes);

app.get('/bcapi/owned', systemController.getOwned);

app.listen(3000, function () {
  console.log('server listening on port 3000');
});