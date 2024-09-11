// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract NFTFrame is ERC721, ERC721URIStorage, Ownable {
    uint256 private _nextTokenId; // 토큰아이디 부여를 위한 변수
    mapping(uint256 => address) public creators; // tokenId와 원작자의 주소 매핑

    // 나중에 NFT 정보 반환할 때 구조
    struct NFTInfo {
        uint256 tokenId;
        address creator;
        address currentOwner;
        string tokenURI;
    }

    struct NFTInfos {
      NFTInfo[] nfts;
      uint256 nextIndex;
    }

    event NFTMinted(uint256 indexed tokenId, address indexed creator, string tokenURI);
    event NFTTransferred(uint256 indexed tokenId, address indexed from, address indexed to);
    event TokenURIUpdated(uint256 indexed tokenId, string newTokenURI);
    event NFTBurned(uint256 indexed tokenId);

    constructor() ERC721("NFTFrame", "NFTF") {}

    // NFT 발행 (nft 발행하고, TokenURI 따로 저장하고, 원작자 매핑하고)
    function mintNFT(string memory _tokenURI) public returns (uint256) {
        uint256 tokenId = _nextTokenId++;

        _safeMint(msg.sender, tokenId);
        _setTokenURI(tokenId, _tokenURI);
        creators[tokenId] = msg.sender;

        emit NFTMinted(tokenId, msg.sender, _tokenURI);

        return tokenId;
    }

    // NFT 업데이트 (관리자)
    function updateTokenURI(uint256 tokenId, string memory newTokenURI) public onlyOwner {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        _setTokenURI(tokenId, newTokenURI);  // URI 업데이트 기능
        emit TokenURIUpdated(tokenId, newTokenURI);
    }

    // NFT 업데이트 (소유자)
    function updateOwnTokenURI(uint256 tokenId, string memory newTokenURI) public {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        require(ownerOf(tokenId) == msg.sender, "You are not the owner of this token");
        _setTokenURI(tokenId, newTokenURI);
        emit TokenURIUpdated(tokenId, newTokenURI);
    }

    // NFT 소유권 이전
    function transferNFT(uint256 tokenId, address to) public returns (bool) {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        require(ownerOf(tokenId) == msg.sender, "You are not the owner of this token");
        address from = ownerOf(tokenId);
        _transfer(from, to, tokenId);
        emit NFTTransferred(tokenId, from, to);

        return true;
    }

    // NFT 소유권 타인 to 타인 이전
    function transferNFTUserToUser(uint256 tokenId, address from, address to) public {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        require(ownerOf(tokenId) == from || getApproved(tokenId) == msg.sender, "You are not authorized to transfer this token");
        
        _transfer(from, to, tokenId);
        emit NFTTransferred(tokenId, from, to);
    }

    // NFT 소각 (완전한 삭제가 아니라 address(0)으로 주소를 바꿈)
    function burnNFT(uint256 tokenId) public {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        require(ownerOf(tokenId) == msg.sender, "You are not the owner of this token");
        _burn(tokenId);
        delete creators[tokenId];
        emit NFTBurned(tokenId);
    }

    // NFT 정보 조회
    function getNFTInfo(uint256 tokenId) public view returns (NFTInfo memory) {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        
        return NFTInfo({
            tokenId: tokenId,
            creator: creators[tokenId],
            currentOwner: ownerOf(tokenId),
            tokenURI: tokenURI(tokenId)
        });
    }

    // 모든 활성 NFT 조회 (페이지네이션 적용) (소각된 애들 제외)
    function getActiveNFTs(uint256 startIndex, uint256 count) public view returns (NFTInfos memory) {
        require(count > 0, "Count must be greater than 0");
        require(startIndex < _nextTokenId, "Start index out of range");

        NFTInfo[] memory activeNFTs = new NFTInfo[](count);
        uint256 foundCount = 0;
        uint256 currentIndex = startIndex;

        while (foundCount < count && currentIndex < _nextTokenId) {
            if (ownerOf(currentIndex) != address(0)) {
                activeNFTs[foundCount] = NFTInfo({
                    tokenId: currentIndex,
                    creator: creators[currentIndex],
                    currentOwner: ownerOf(currentIndex),
                    tokenURI: tokenURI(currentIndex)
                });
                foundCount++;
            }
            currentIndex++;
        }

        // 찾은 NFT 수가 요청한 수보다 적은 경우, 배열 크기 조정
        if (foundCount < count) {
            assembly {
                mstore(activeNFTs, foundCount)
            }
        }

        return NFTInfos({
            nfts: activeNFTs,
            nextIndex: currentIndex
        });
    }


    // 오버라이드 함수들
    function _burn(uint256 tokenId) internal override(ERC721, ERC721URIStorage) {
        super._burn(tokenId);
    }
    function tokenURI(uint256 tokenId) public view override(ERC721, ERC721URIStorage) returns (string memory) {
        return super.tokenURI(tokenId);
    }

    function supportsInterface(bytes4 interfaceId) public view override(ERC721) returns (bool) {
        return super.supportsInterface(interfaceId);
    }
}