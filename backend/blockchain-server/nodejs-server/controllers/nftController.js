const { uploadToIPFS } = require('../utils/ipfsHelper');
const { web3, NFTContract, account } = require('../utils/web3Helper');
const { handleError, errorTypes } = require('../utils/errorHandler');

// 활성화된 NFT 리스트 조회
exports.getActiveNFTs = async (req, res) => {
  try {
    const startIndex = parseInt(req.query.startIndex) || 0;
    const count = parseInt(req.query.count) || 10;

    const result = await NFTContract.methods.getActiveNFTs(startIndex, count).call();

    const nfts = result.nfts.map(nft => ({
      tokenId: nft.tokenId.toString(),
      creator: nft.creator,
      currentOwner: nft.currentOwner,
      tokenURI: nft.tokenURI
    }));
    
    res.json({
      nfts: nfts,
      nextIndex: result.nextIndex.toString()
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.FETCH);
    res.status(500).json({ error: errorMessage });
  }
};

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

    const { title, description } = frameDto;
    if (!title || !description) {
      return res.status(400).json({ error: 'Title and description are required in frameDto' });
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

    const tx = {
      from: account.address,
      to: NFTContract.options.address,
      gas: gasEstimate,
      gasPrice: gasPrice,
      data: NFTContract.methods.mintNFT(tokenURI).encodeABI()
    };

    const signedTx = await web3.eth.accounts.signTransaction(tx, process.env.ADMIN_PRIVATE_KEY);
    const receipt = await web3.eth.sendSignedTransaction(signedTx.rawTransaction);

    const tokenId = receipt.logs[0].topics[3];

    res.json({
      success: true,
      tokenId: web3.utils.hexToNumber(tokenId),
      transactionHash: receipt.transactionHash
    });
  } catch (error) {
    const errorMessage = handleError(error, errorTypes.MINT);
    res.status(500).json({ error: errorMessage });
  }
};