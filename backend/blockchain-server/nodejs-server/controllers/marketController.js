const { web3, systemContract, account } = require('../utils/web3Helper');
const { handleError, errorTypes } = require('../utils/errorHandler');

// 마켓 정보 조회
exports.getMarketInfo = async (req, res) => {
  try {
    const nftId = req.params.nftId;
    if (nftId === undefined || nftId === '') {
      return res.status(400).json({ error: 'nftId is required' });
    }

    const result = await systemContract.methods.getSellInfo(nftId).call();

    const marketInfo = {
      nftId: result.nftId.toString(),
      seller: result.seller,
      price: result.price.toString(),
      isFinished: result.isFinished
    }
    
    res.json({
      result: marketInfo,
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};