const { Web3 } = require('web3');
require('dotenv').config();

const web3 = new Web3('https://rpc.ssafy-blockchain.com');

const NFTContractABI = require('../contracts/NFTFrame.json');
const NFTContractAddress = process.env.NFT_CONTRACT_ADDRESS;
const NFTContract = new web3.eth.Contract(NFTContractABI.abi, NFTContractAddress);

const coinContractABI = require('../contracts/AhoraCoin.json');
const coinContractAddress = process.env.COIN_CONTRACT_ADDRESS;
const coinContract = new web3.eth.Contract(coinContractABI.abi, coinContractAddress);

const systemContractABI = require('../contracts/IntegratedNFTSystem.json');
const systemContractAddress = process.env.SYSTEM_CONTRACT_ADDRESS;
const systemContract = new web3.eth.Contract(systemContractABI.abi, systemContractAddress);

const privateKey = process.env.ADMIN_PRIVATE_KEY; 
const account = web3.eth.accounts.privateKeyToAccount(privateKey);
web3.eth.accounts.wallet.add(account);

const testPrivateKey = process.env.TEST_PRIVATE_KEY;
const testAccount = web3.eth.accounts.privateKeyToAccount(testPrivateKey);
web3.eth.accounts.wallet.add(testAccount);

module.exports = {
  web3,
  NFTContract,
  coinContract,
  systemContract,
  account,
  testAccount
};