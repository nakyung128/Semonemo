const { web3, systemContract, coinContract, account } = require('../utils/web3Helper');
const { handleError } = require('../utils/errorHandler');

const { BigNumber } = require('bignumber.js');

// 옥션 생성
exports.startAuction = async (req, res) => {
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

// 옥션 취소
exports.cancleAuction = async (req, res) => {
  try {
    const address = req.body.address;
    const tokenId = req.body.tokenId;

    if (!address || !tokenId) {
      return res.status(400).json({ error: 'address and tokenId are required'  });
    }
    
    // 가스 가격 및 가스 한도 추정
    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.cancelAuction(tokenId).estimateGas({ from: address });
    const nonce = await web3.eth.getTransactionCount(address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: address,
      to: systemContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: systemContract.methods.cancelAuction(tokenId).encodeABI()
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

// 옥션 종료
exports.closeAuction = async (req, res) => {
  try {
    const address = req.body.address;
    const tokenId = req.body.tokenId;

    if (!address || !tokenId) {
      return res.status(400).json({ error: 'address and tokenId are required'  });
    }
    
    // 가스 가격 및 가스 한도 추정
    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.closeAuction(tokenId, address).estimateGas({ from: account.address });
    const nonce = await web3.eth.getTransactionCount(account.address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce,
      from: account.address,
      to: systemContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: systemContract.methods.closeAuction(tokenId, address).encodeABI()
    };

    // 트랜잭션 서명
    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);
    
    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      code: "S000",
      data: {
        transactionHash: receipt.transactionHash
      },
      message: "Auction close succeeded.",
    });
  } catch (error) {
    const errorLog = handleError(error);
    res.status(500).json({ 
      errorCode: errorLog.code,
      message: errorLog.message
    });
  }
};

// 옥션 조회
exports.getAuctionInfo = async (req, res) => {
  try {
    const tokenId = req.params.tokenId;

    if (!tokenId) {
      return res.status(400).json({ error: 'tokenId is required' });
    }

    const result = await systemContract.methods.getAuctionInfo(tokenId).call();

    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // BigNumber를 사용하여 정확한 계산 수행
    const priceBigInt = new BigNumber(result.price);
    const divisorCoin = new BigNumber(10).pow(decimals);
    const price = priceBigInt.dividedBy(divisorCoin);

    const auction = {
      tokenId: result.nftId.toString(),
      seller: result.seller,
      price: price.toString(),
    }
    
    res.json({
      code: "S000",
      data: auction,
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