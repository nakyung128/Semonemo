const { uploadToIPFS } = require('../utils/ipfsHelper');
const { web3, systemContract, coinContract, NFTContract, account } = require('../utils/web3Helper');
const { handleError } = require('../utils/errorHandler');

const { BigNumber } = require('bignumber.js');

// NFT 발행 테스트
exports.mintNFTTest = async (req, res) => {
  try {
    if (!req.file) {
      return res.status(400).json({ error: 'Image file is required' });
    }

    let frameDto;
    try {
      frameDto = JSON.parse(req.body.frameDto);
    } catch (error) {
      return res.status(400).json({ error: 'Invalid frameDto JSON' });
    }

    const { title, description, address } = frameDto;

    if (!title || !description || !address) {
      return res.status(400).json({ error: 'Title and description and address are required in frameDto' });
    }

    const imageUrl = await uploadToIPFS(req.file.buffer);

    const metadata = {
      title: title,
      description: description,
      image: imageUrl,
      created_at: new Date().toISOString()
    };

    const tokenURI = await uploadToIPFS(JSON.stringify(metadata));

    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await NFTContract.methods.mintNFT(tokenURI).estimateGas({ from: account.address });
    const nonce = await web3.eth.getTransactionCount(account.address, 'pending');

    const tx = {
      nonce: nonce,
      from: account.address,
      to: NFTContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: NFTContract.methods.mintNFT(tokenURI).encodeABI()
    };

    // 트랜잭션 서명
    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);
    
    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      code: "S000",
      data: {
        transactionHash: receipt.transactionHash,
      },
      message: "NFT 발행 테스트 성공.",
    });
  } catch (error) {
    const errorLog = handleError(error);
    res.status(500).json({ 
      errorCode: errorLog.code,
      message: errorLog.message
    });
  }
};

// 선입금 코인으로 전환 테스트
exports.depositCoinTest = async (req, res) => {
  try {
    const { address, amount } = req.body;

    if (!address || !amount) {
      return res.status(400).json({ error: 'toAddress and amount are required'  });
    }

    // amount가 유효한 숫자인지 확인 (정수 또는 소수)
    if (isNaN(parseFloat(amount))) {
      return res.status(400).json({ error: 'Amount must be a valid number' });
    }

    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // 금액을 토큰의 최소 단위로 변환 (문자열 연산 사용)
    const [integerPart, fractionalPart = ''] = amount.split('.');
    const paddedFractionalPart = fractionalPart.padEnd(Number(decimals), '0');
    const amountInSmallestUnit = integerPart + paddedFractionalPart;

    // 앞에 붙은 0 제거
    const amountInSmallestUnitTrimmed = amountInSmallestUnit.replace(/^0+/, '');

    // 가스 가격 및 가스 한도 추정
    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.deposit(amountInSmallestUnitTrimmed).estimateGas({ from: account.address });
    const nonce = await web3.eth.getTransactionCount(account.address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce,
      from: account.address,
      to: systemContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: systemContract.methods.deposit(amountInSmallestUnitTrimmed).encodeABI()
    };

    // 트랜잭션 서명
    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);
    
    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      code: "S000",
      data: {
        transactionHash: receipt.transactionHash,
      },
      message: "Deposit 테스트 성공.",
    });
  } catch (error) {
    const errorLog = handleError(error);
    res.status(500).json({ 
      errorCode: errorLog.code,
      message: errorLog.message
    });
  }
};

// 선입금 코인, 그냥 코인으로
exports.withdrawCoinTest = async (req, res) => {
  try {
    const { address, amount } = req.body;

    if (!address || !amount) {
      return res.status(400).json({ error: 'toAddress and amount are required'  });
    }

    // amount가 유효한 숫자인지 확인 (정수 또는 소수)
    if (isNaN(parseFloat(amount))) {
      return res.status(400).json({ error: 'Amount must be a valid number' });
    }

    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // 금액을 토큰의 최소 단위로 변환 (문자열 연산 사용)
    const [integerPart, fractionalPart = ''] = amount.split('.');
    const paddedFractionalPart = fractionalPart.padEnd(Number(decimals), '0');
    const amountInSmallestUnit = integerPart + paddedFractionalPart;

    // 앞에 붙은 0 제거
    const amountInSmallestUnitTrimmed = amountInSmallestUnit.replace(/^0+/, '');

    // 가스 가격 및 가스 한도 추정
    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await systemContract.methods.withdraw(amountInSmallestUnitTrimmed).estimateGas({ from: account.address });
    const nonce = await web3.eth.getTransactionCount(account.address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce,
      from: account.address,
      to: systemContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: systemContract.methods.withdraw(amountInSmallestUnitTrimmed).encodeABI()
    };

    // 트랜잭션 서명
    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);
    
    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      code: "S000",
      data: {
        transactionHash: receipt.transactionHash,
      },
      message: "Withdraw 테스트 성공.",
    });
  } catch (error) {
    const errorLog = handleError(error);
    res.status(500).json({ 
      errorCode: errorLog.code,
      message: errorLog.message
    });
  }
};

// 옥션 시작
exports.startAuctionTest = async (req, res) => {
  try {
    const address = req.body.address;
    const tokenId = req.body.tokenId;
    const price = req.body.price;

    if (!tokenId || !price) {
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
      nonce: nonce,
      from: address,
      to: systemContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: systemContract.methods.startAuction(tokenId, amountInSmallestUnitTrimmed).encodeABI()
    };

    // 트랜잭션 서명
    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);
    
    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      code: "S000",
      data: {
        transactionHash: receipt.transactionHash,
      },
      message: "Withdraw 테스트 성공.",
    });
  } catch (error) {
    const errorLog = handleError(error);
    res.status(500).json({ 
      errorCode: errorLog.code,
      message: errorLog.message
    });
  }
};