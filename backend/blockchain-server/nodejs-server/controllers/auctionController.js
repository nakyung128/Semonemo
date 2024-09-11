const { web3, auctionContract, account } = require('../utils/web3Helper');

exports.getAuctionInfo = async (req, res) => {
  const nftId = req.params.nftId;
  if (nftId === undefined || nftId === '') {
    return res.status(400).json({ error: 'nftId is required' });
  }
  try {
    const result = await auctionContract.methods.getAuctionInfo(nftId).call();

    const auctionInfo = {
      nftId: result.nftId.toString(),
      seller: result.seller,
      startingPrice: result.startingPrice.toString(),
      highestBid: result.highestBid.toString(),
      highestBidder: result.highestBidder,
      isFinished: result.isFinished
    }
    
    res.json({
      result: auctionInfo,
    });
  } catch (error) {
    console.error('Failed to fetch:', error);
    res.status(500).json({ error: 'Failed to fetch' });
  }
};