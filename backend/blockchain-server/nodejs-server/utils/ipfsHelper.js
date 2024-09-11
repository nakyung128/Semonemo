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
    return `ipfs://${cid}`;
  } catch (error) {
    console.error('IPFS 업로드 에러:', error);
    throw error;
  }
}

module.exports = {
  uploadToIPFS
};