const { systemContract, coinContract } = require('../utils/web3Helper');
const { handleError } = require('../utils/errorHandler');

const { BigNumber } = require('bignumber.js');

// 컨트랙트 보유 NFT, 잔고 확인
exports.getOwned = async (req, res) => {
  try {
    const result = await systemContract.methods.getCombinedOwnedAssets().call();

    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // BigNumber를 사용하여 정확한 계산 수행
    const balanceCoin = new BigNumber(result.totalCoins);
    const divisorCoin = new BigNumber(10).pow(decimals);
    const amountInTokensCoin = balanceCoin.dividedBy(divisorCoin);

    const auctionOwned = {
      nftIds: result.nftIds.map(id => id.toString()),
      coins: amountInTokensCoin.toString(),
    }

    res.json({
      code: "S000",
      data: auctionOwned,
      message: "Load data succeeded.",
    });
  } catch (error) {
    const errorLog = handleError(error);
    res.status(500).json({ 
      errorCode: errorLog.code,
      message: errorLog.message
    });
  }
};