const NFTFrame = artifacts.require("NFTFrame");
const AhoraCoin = artifacts.require("AhoraCoin");
const NFTSystem = artifacts.require("IntegratedNFTSystem");

module.exports = async function(deployer, network, accounts) {
  // Deploy NFTFrame
  await deployer.deploy(NFTFrame);
  const nftFrameInstance = await NFTFrame.deployed();

  // Deploy AhoraCoin
  await deployer.deploy(AhoraCoin);
  const ahoraCoinInstance = await AhoraCoin.deployed();

  // Deploy NFTSystem
  await deployer.deploy(NFTSystem, nftFrameInstance.address, ahoraCoinInstance.address);
  const nftSystemInstance = await NFTSystem.deployed();

  console.log("NFTFrame deployed at:", nftFrameInstance.address);
  console.log("AhoraCoin deployed at:", ahoraCoinInstance.address);
  console.log("NFTSystem deployed at:", nftSystemInstance.address);
};