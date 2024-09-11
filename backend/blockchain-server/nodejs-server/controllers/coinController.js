const { web3, coinContract, account } = require('../utils/web3Helper');

exports.mintCoin = async (req, res) => {
  try {
    const { toAddress, amount } = req.body;

    if (!toAddress || !amount) {
      return res.status(400).json({ error: 'toAddress and amount are required' });
    }

    // amount가 숫자인지 확인
    if (isNaN(amount)) {
      return res.status(400).json({ error: 'Amount must be a number' });
    }

    // 토큰의 소수점 자릿수 가져오기 (예: 18)
    const decimals = await coinContract.methods.decimals().call();

    // 금액을 토큰의 최소 단위로 변환 (예: 18 자릿수 경우 10^18을 곱함)
    const amountInSmallestUnit = (BigInt(amount) * BigInt(10) ** BigInt(decimals)).toString();

    // 가스 가격 및 가스 한도 추정
    const gasPrice = await web3.eth.getGasPrice();
    const gasEstimate = await coinContract.methods.mint(toAddress, amountInSmallestUnit).estimateGas({ from: account.address });

    // 트랜잭션 객체 생성
    const tx = {
      from: account.address,
      to: coinContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: coinContract.methods.mint(toAddress, amountInSmallestUnit).encodeABI()
    };

    // 트랜잭션 서명
    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);

    // 서명된 트랜잭션 전송
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    res.json({
      success: true,
      transactionHash: receipt.transactionHash,
      amountMinted: amount,
      to: toAddress
    });
  } catch (error) {
    console.error('Failed to mint:', error);
    res.status(500).json({ error: 'Failed to mint' });
  }
};