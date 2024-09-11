const { web3, marketContract, account } = require('../utils/web3Helper');

exports.getMarketInfo = async (req, res) => {
  const nftId = req.params.nftId;
  if (nftId === undefined || nftId === '') {
    return res.status(400).json({ error: 'nftId is required' });
  }
  try {
    const result = await marketContract.methods.getSellInfo(nftId).call();

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
    console.error('Failed to fetch:', error);
    res.status(500).json({ error: 'Failed to fetch' });
  }
};