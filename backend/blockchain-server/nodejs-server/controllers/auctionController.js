const { web3, systemContract, coinContract, account, testAccount } = require('../utils/web3Helper');
const { handleError } = require('../utils/errorHandler');

const { BigNumber } = require('bignumber.js');

// 옥션 생성
exports.startAuction = async (req, res) => {
  try {
    const address = req.body.address;
    const tokenId = req.body.tokenId;
    const price = req.body.price;

    if (!address || !tokenId || !price) {
      return res.status(400).json({ error: 'tokenId and price are required'  });
    }

    // amount가 유효한 숫자인지 확인 (정수 또는 소수)
    if (isNaN(parseFloat(price))) {
      return res.status(400).json({ error: 'price must be a valid number' });
    }

    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // 금액을 토큰의 최소 단위로 변환 (문자열 연산 사용)
    const [integerPart, fractionalPart = ''] = price.split('.');
    const paddedFractionalPart = fractionalPart.padEnd(Number(decimals), '0');
    const amountInSmallestUnit = integerPart + paddedFractionalPart;

    // 앞에 붙은 0 제거
    const amountInSmallestUnitTrimmed = amountInSmallestUnit.replace(/^0+/, '');

    // 가스 가격 및 가스 한도 추정
    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.startAuction(tokenId, amountInSmallestUnitTrimmed).estimateGas({ from: address });
    const nonce = await web3.eth.getTransactionCount(address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: address,
      to: systemContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: systemContract.methods.startAuction(tokenId, amountInSmallestUnitTrimmed).encodeABI()
    };

    res.json({
      code: "S000",
      data: tx,
      message: "Create transaction succeeded.",
    });
  } catch (error) {
    const errorLog = handleError(error);
    res.status(500).json({ 
      errorCode: errorLog.code,
      message: errorLog.message
    });
  }
};