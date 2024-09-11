const NFTFrame = artifacts.require("NFTFrame");
const AhoraCoin = artifacts.require("AhoraCoin");
const NFTAuction = artifacts.require("NFTAuction");
const NFTMarket = artifacts.require("NFTMarket");

module.exports = async function(deployer, network, accounts) {
  // Deploy NFTFrame
  await deployer.deploy(NFTFrame);
  const nftFrameInstance = await NFTFrame.deployed();

  // Deploy AhoraCoin
  await deployer.deploy(AhoraCoin);
  const ahoraCoinInstance = await AhoraCoin.deployed();

  // Deploy NFTAuctionSystem
  await deployer.deploy(NFTAuction, nftFrameInstance.address, ahoraCoinInstance.address);
  const nftAuctionSystemInstance = await NFTAuction.deployed();

  // Deploy NFTAuctionSystem
  await deployer.deploy(NFTMarket, nftFrameInstance.address, ahoraCoinInstance.address);
  const nftMarketSystemInstance = await NFTMarket.deployed();

  console.log("NFTFrame deployed at:", nftFrameInstance.address);
  console.log("AhoraCoin deployed at:", ahoraCoinInstance.address);
  console.log("NFTAuctionSystem deployed at:", nftAuctionSystemInstance.address);
  console.log("NFTMarketSystem deployed at:", nftMarketSystemInstance.address);
};