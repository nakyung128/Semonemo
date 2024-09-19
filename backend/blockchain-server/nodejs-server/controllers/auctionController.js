const { web3, systemContract, coinContract, account, testAccount } = require('../utils/web3Helper');
const { handleError, errorTypes } = require('../utils/errorHandler');

// 옥션 보유 NFT, 잔고 확인
exports.getAuctionOwned = async (req, res) => {
  try {
    const result = await systemContract.methods.getCombinedOwnedAssets().call();

    const auctionOwned = {
      nftIds: result.nftIds.map(id => id.toString()),
      coins: result.totalCoins.toString(),
    }
    
    console.log(result)

    res.json({
      ...auctionOwned,
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};

// 옥션 정보 조회
exports.getAuctionInfo = async (req, res) => {
  try {
    const nftId = req.params.nftId;
    if (nftId === undefined || nftId === '') {
      return res.status(400).json({ error: 'nftId is required' });
    }

    const result = await systemContract.methods.getAuctionInfo(nftId).call();

    const auctionInfo = {
      nftId: result.nftId.toString(),
      seller: result.seller,
      startingPrice: result.startingPrice.toString(),
      highestBid: result.highestBid.toString(),
      highestBidder: result.highestBidder,
      isFinished: result.isFinished
    }
    
    res.json({
      ...auctionInfo,
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};

// 옥션 업로드
exports.createAuction = async (req, res) => {
  try {
    const { nftId, startingPrice } = req.body;
    if (!nftId || !startingPrice) {
      return res.status(400).json({ error: 'nftId and startingPrice are required in body' });
    }

    // amount가 유효한 숫자인지 확인 (정수 또는 소수)
    if (isNaN(parseFloat(startingPrice))) {
      return res.status(400).json({ error: 'startingPrice must be a valid number' });
    }

    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // 금액을 토큰의 최소 단위로 변환 (문자열 연산 사용)
    const [integerPart, fractionalPart = ''] = startingPrice.split('.');
    const paddedFractionalPart = fractionalPart.padEnd(Number(decimals), '0');
    const amountInSmallestUnit = integerPart + paddedFractionalPart;

    // 앞에 붙은 0 제거
    const amountInSmallestUnitTrimmed = amountInSmallestUnit.replace(/^0+/, '');

    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.createAuction(nftId, amountInSmallestUnitTrimmed).estimateGas({ from: account.address });

    const tx = {
      from: account.address,
      to: systemContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: systemContract.methods.createAuction(nftId, amountInSmallestUnitTrimmed).encodeABI()
    };

    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      success: true,
      transactionHash: receipt.transactionHash
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};

// 옥션 입찰
exports.bidTest = async (req, res) => {
  try {
    const { nftId, price } = req.body;
    if (!nftId || !price) {
      return res.status(400).json({ error: 'nftId and price are required in body' });
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

    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.placeBid(nftId, amountInSmallestUnitTrimmed).estimateGas({ from: testAccount.address });

    const tx = {
      from: testAccount.address,
      to: systemContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: systemContract.methods.placeBid(nftId, amountInSmallestUnitTrimmed).encodeABI()
    };

    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.TEST_PRIVATE_KEY);
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      success: true,
      transactionHash: receipt.transactionHash
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};

// 옥션 종료
exports.auctionCloseTest = async (req, res) => {
  try {
    const { nftId } = req.body;
    if (!nftId) {
      return res.status(400).json({ error: 'nftId and price are required in body' });
    }

    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.finalizeAuction(nftId).estimateGas({ from: account.address });

    const tx = {
      from: account.address,
      to: systemContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: systemContract.methods.finalizeAuction(nftId).encodeABI()
    };

    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      success: true,
      transactionHash: receipt.transactionHash
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};