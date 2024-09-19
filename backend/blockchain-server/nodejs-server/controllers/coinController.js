const { web3, coinContract, account, testAccount } = require('../utils/web3Helper');
const { handleError, errorTypes } = require('../utils/errorHandler');

const { BigNumber } = require('bignumber.js');

// 잔여 코인 확인
exports.remainCoin = async (req, res) => {
  try {
    const address = req.params.address;

    if (!address) {
      return res.status(400).json({ error: 'address is required' });
    }

    const result = await coinContract.methods.balanceOf(address).call();

    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // BigNumber를 사용하여 정확한 계산 수행
    const balance = new BigNumber(result);
    const divisor = new BigNumber(10).pow(decimals);
    const amountInTokens = balance.dividedBy(divisor);
    
    res.json({
      address: address,
      coin: amountInTokens.toString(),
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};

// 코인 구매
exports.buyCoin = async (req, res) => {
  try {
    const { toAddress, amount } = req.body;

    if (!toAddress || !amount) {
      return res.status(400).json({ error: 'toAddress and amount are required' });
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
    const gasEstimate = await coinContract.methods.mint(toAddress, amountInSmallestUnitTrimmed).estimateGas({ from: account.address });

    // 트랜잭션 객체 생성
    const tx = {
      from: account.address,
      to: coinContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: coinContract.methods.mint(toAddress, amountInSmallestUnitTrimmed).encodeABI()
    };

    // 트랜잭션 서명
    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);
    
    console.log(signedTx)
    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      transactionHash: receipt.transactionHash,
      amountMinted: amount,
      to: toAddress
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.MINT);
    res.status(500).json({ error: errorMessage });
  }
};

// 코인 전송 
exports.transferCoin = async (req, res) => {
  try {
    const { fromAddress, toAddress, amount } = req.body;

    if (!fromAddress || !toAddress || !amount) {
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
    const gasEstimate = await coinContract.methods.transferSenderToUser(toAddress, amountInSmallestUnitTrimmed).estimateGas({ from: fromAddress });
    const nonce = await web3.eth.getTransactionCount(fromAddress, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: fromAddress,
      to: coinContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: coinContract.methods.transferSenderToUser(toAddress, amountInSmallestUnitTrimmed).encodeABI()
    };
    
    console.log(tx)

    res.json({
      ...tx,
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};

// 코인 전송 
exports.transferCoinTEST = async (req, res) => {
  try {
    const { fromAddress, toAddress, amount } = req.body;

    if (!fromAddress || !toAddress || !amount) {
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

    const result = await coinContract.methods.balanceOf(testAccount.address).call();

    console.log(result)

    // 가스 가격 및 가스 한도 추정
    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await coinContract.methods.transferSenderToUser(toAddress, amountInSmallestUnitTrimmed).estimateGas({ from: testAccount.address });
    const nonce = await web3.eth.getTransactionCount(fromAddress, 'pending');

    console.log(123)

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: testAccount,
      to: coinContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: coinContract.methods.transferSenderToUser(toAddress, amountInSmallestUnitTrimmed).encodeABI()
    };
    
    console.log(tx)

    // 트랜잭션 서명
    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);

    console.log(signedTx)
    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      ...tx,
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};

// 코인 전송 (트랜잭션 서명 후)
exports.transferCoinSigned = async (req, res) => {
  try {
    const tx = req.body.tx;

    console.log(tx)

    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(tx);

    console.log('end', receipt)

    res.json({
      transactionHash: receipt.transactionHash,
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};