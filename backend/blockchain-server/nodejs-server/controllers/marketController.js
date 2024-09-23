const { web3, systemContract, coinContract, account } = require('../utils/web3Helper');
const { handleError } = require('../utils/errorHandler');

const { BigNumber } = require('bignumber.js');

// 마켓 생성
exports.createMarket = async (req, res) => {
  try {
    const address = req.body.address;
    const tokenId = req.body.tokenId;
    const price = req.body.price;

    if (!address || !tokenId || !price) {
      return res.status(400).json({ error: 'address and tokenId and price are required'  });
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
    const gasEstimate = await systemContract.methods.createMarket(tokenId, amountInSmallestUnitTrimmed).estimateGas({ from: address });
    const nonce = await web3.eth.getTransactionCount(address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: address,
      to: systemContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: systemContract.methods.createMarket(tokenId, amountInSmallestUnitTrimmed).encodeABI()
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

// 마켓 취소
exports.cancleMarket = async (req, res) => {
  try {
    const address = req.body.address;
    const tokenId = req.body.tokenId;

    if (!address || !tokenId) {
      return res.status(400).json({ error: 'address and tokenId are required'  });
    }
    
    // 가스 가격 및 가스 한도 추정
    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.cancelMarket(tokenId).estimateGas({ from: address });
    const nonce = await web3.eth.getTransactionCount(address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: address,
      to: systemContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: systemContract.methods.cancelMarket(tokenId).encodeABI()
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

// 마켓 구매
exports.buyMarket = async (req, res) => {
  try {
    const address = req.body.address;
    const tokenId = req.body.tokenId;

    if (!address || !tokenId) {
      return res.status(400).json({ error: 'address and tokenId are required'  });
    }
    
    // 가스 가격 및 가스 한도 추정
    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.buyMarket(tokenId).estimateGas({ from: address });
    const nonce = await web3.eth.getTransactionCount(address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: address,
      to: systemContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: systemContract.methods.buyMarket(tokenId).encodeABI()
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

// 마켓 조회
exports.getMarketInfo = async (req, res) => {
  try {
    const tokenId = req.params.tokenId;

    if (!tokenId) {
      return res.status(400).json({ error: 'tokenId is required' });
    }

    const result = await systemContract.methods.getMarketInfo(tokenId).call();

    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // BigNumber를 사용하여 정확한 계산 수행
    const priceBigInt = new BigNumber(result.price);
    const divisorCoin = new BigNumber(10).pow(decimals);
    const price = priceBigInt.dividedBy(divisorCoin);

    const market = {
      tokenId: result.nftId.toString(),
      seller: result.seller,
      price: price.toString(),
    }
    
    res.json({
      code: "S000",
      data: market,
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