const { web3, coinContract, systemContract, account } = require('../utils/web3Helper');
const { handleError } = require('../utils/errorHandler');

const { BigNumber } = require('bignumber.js');

// 잔여 코인 확인
exports.remainCoin = async (req, res) => {
  try {
    const address = req.params.address;

    if (!address) {
      return res.status(400).json({ error: 'address is required' });
    }

    const coin = await coinContract.methods.balanceOf(address).call();
    const deposit = await systemContract.methods.getUserBalance(address).call();
 
    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // BigNumber를 사용하여 정확한 계산 수행
    const balanceCoin = new BigNumber(coin);
    const divisorCoin = new BigNumber(10).pow(decimals);
    const amountInTokensCoin = balanceCoin.dividedBy(divisorCoin);

    const balanceDeposit = new BigNumber(deposit);
    const divisorDeposit = new BigNumber(10).pow(decimals);
    const amountInTokensDeposit = balanceDeposit.dividedBy(divisorDeposit);
    
    res.json({
      code: "S000",
      data: {
        address: address,
        coin: amountInTokensCoin.toString(),
        deposit: amountInTokensDeposit.toString(),
      },
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

// 코인 구매
exports.buyCoin = async (req, res) => {
  try {
    const toAddress = req.body.toAddress;
    const amount = req.body.amount;

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
    
    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      code: "S000",
      data: {
        transactionHash: receipt.transactionHash,
        amountMinted: amount,
        to: toAddress
      },
      message: "Coin buy succeeded.",
    });
  } catch (error) {
    const errorLog = handleError(error);
    res.status(500).json({ 
      errorCode: errorLog.code,
      message: errorLog.message
    });
  }
};

// 코인 전송
exports.transferCoin = async (req, res) => {
  try {
    const fromAddress = req.body.fromAddress;
    const toAddress = req.body.toAddress;
    const amount = req.body.amount;

    if (!fromAddress || !toAddress || !amount) {
      return res.status(400).json({ error: 'fromAddress and toAddress and amount are required'  });
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

// 선입금 코인 전송
exports.transferDepositCoin = async (req, res) => {
  try {
    const fromAddress = req.body.fromAddress;
    const toAddress = req.body.toAddress;
    const amount = req.body.amount;

    if (!fromAddress || !toAddress || !amount) {
      return res.status(400).json({ error: 'fromAddress and toAddress and amount are required'  });
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
    const gasEstimate = await systemContract.methods.transferBalance(toAddress, amountInSmallestUnitTrimmed).estimateGas({ from: fromAddress });
    const nonce = await web3.eth.getTransactionCount(fromAddress, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: fromAddress,
      to: systemContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: systemContract.methods.transferBalance(toAddress, amountInSmallestUnitTrimmed).encodeABI()
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

// 선입금 코인으로 전환
exports.depositCoin = async (req, res) => {
  try {
    const address = req.body.address;
    const amount = req.body.amount;

    if (!address || !amount) {
      return res.status(400).json({ error: 'address and amount are required'  });
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
    const gasEstimate = await systemContract.methods.deposit(amountInSmallestUnitTrimmed).estimateGas({ from: address });
    const nonce = await web3.eth.getTransactionCount(address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: address,
      to: systemContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: systemContract.methods.deposit(amountInSmallestUnitTrimmed).encodeABI()
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

// 선입금 코인, 그냥 코인으로 전환
exports.withdrawCoin = async (req, res) => {
  try {
    const address = req.body.address;
    const amount = req.body.amount;

    if (!address || !amount) {
      return res.status(400).json({ error: 'address and amount are required'  });
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
    const gasEstimate = await systemContract.methods.withdraw(amountInSmallestUnitTrimmed).estimateGas({ from: address });
    const nonce = await web3.eth.getTransactionCount(address, 'pending');

    // 트랜잭션 객체 생성
    const tx = {
      nonce: nonce.toString(),
      from: address,
      to: systemContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: systemContract.methods.withdraw(amountInSmallestUnitTrimmed).encodeABI()
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