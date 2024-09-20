const { uploadToIPFS } = require('../utils/ipfsHelper');
const { web3, NFTContract, account } = require('../utils/web3Helper');
const { handleError } = require('../utils/errorHandler');

const { BigNumber } = require('bignumber.js');

// tokenId가 담긴 array 받아서 NFT 정보 조회
exports.getNFTInfo = async (req, res) => {
  try {
    const tokenIds = req.query.tokenIds ? req.query.tokenIds.split(',') : [];

    if (tokenIds.length === 0) {
      return res.status(400).json({ error: "No token IDs provided" });
    }

    // 컨트랙트의 getNFTsByIds 함수를 호출합니다.
    const result = await NFTContract.methods.getNFTsByIds(tokenIds).call();

    const nfts = result.map(nft => ({
      tokenId: nft.tokenId.toString(),
      creator: nft.creator,
      currentOwner: nft.currentOwner,
      tokenURI: nft.tokenURI
    }));
    
    res.json({
      code: "S000",
      data: nfts,
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

// NFT 발행
exports.mintNFT = async (req, res) => {
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

    const title = frameDto.title;
    const description = frameDto.description;
    const address = frameDto.address;
    
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
    const gasEstimate = await NFTContract.methods.mintNFT(tokenURI).estimateGas({ from: address });
    const nonce = await web3.eth.getTransactionCount(address, 'pending');

    const tx = {
      nonce: nonce.toString(),
      from: address,
      to: NFTContract.options.address,
      gas: gasEstimate.toString(),
      gasPrice: gasPrice.toString(),
      data: NFTContract.methods.mintNFT(tokenURI).encodeABI()
    };

    res.json({
      code: "S000",
      data: {
        ...tx
      },
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