// const pinataSDK = require('@pinata/sdk');
// require('dotenv').config();

// const PINATA_API_KEY = process.env.PINATA_API_KEY;
// const PINATA_SECRET_API_KEY = process.env.PINATA_SECRET_API_KEY;

async function createIPFSClient() {
  const IpfsHttpClient = await import('ipfs-http-client');
  return IpfsHttpClient.create({ url: 'http://localhost:5001' });
}

async function uploadToIPFS(file) {
  try {
    const ipfs = await createIPFSClient();
    const added = await ipfs.add(file);
    const cid = added.cid.toString();
    console.log('IPFS CID:', cid);

    await ipfs.pin.add(cid);
    console.log('파일이 로컬 IPFS 노드에 핀되었습니다.');

    // Pinata에 핀 (유료버전만 가능)
    // const pinata = new pinataSDK({ pinataApiKey: PINATA_API_KEY, pinataSecretApiKey: PINATA_SECRET_API_KEY });
    // const pinataResult = await pinata.pinByHash(cid);
    // console.log('파일이 Pinata에 핀되었습니다:', pinataResult);

    return `ipfs://${cid}`;
  } catch (error) {
    console.error('IPFS 업로드 에러:', error);
    throw error;
  }
}

module.exports = {
  uploadToIPFS
};