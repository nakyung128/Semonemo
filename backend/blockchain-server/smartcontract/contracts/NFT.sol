// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/token/ERC721/extensions/ERC721URIStorage.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract NFTFrame is ERC721, ERC721URIStorage, Ownable {
    uint256 private _nextTokenId; // 토큰아이디 부여를 위한 변수
    mapping(uint256 => address) public creators; // tokenId와 원작자의 주소 매핑
    mapping(address => uint256[]) private _ownedTokens; // 특정 주소가 보유하고있는 tokenId의 리스트

    // main contract 주소 설정 함수
    address public mainContractAddress;

    // main contract 주소 설정 함수
    function setMainContractAddress(address _mainContractAddress) public {
        require(mainContractAddress == address(0), "Main contract address can only be set once");
        require(_mainContractAddress != address(0), "Invalid main contract address");
        mainContractAddress = _mainContractAddress;
    }

    // main contract 여부 확인 modifier
    modifier onlyMainContract() {
        require(mainContractAddress != address(0), "Main contract not set");
        require(msg.sender == mainContractAddress, "Caller is not the main contract");
        _;
    }

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
      bool hasNext;
    }

    event NFTMinted(uint256 indexed tokenId, address indexed creator, string tokenURI);
    event NFTTransferred(uint256 indexed tokenId, address indexed from, address indexed to);
    event TokenURIUpdated(uint256 indexed tokenId, string newTokenURI);
    event NFTBurned(uint256 indexed tokenId);

    constructor() ERC721("NFTFrame", "NFTF") {}

    // 유저별 토큰 보유리스트에 추가
    function _addTokenToOwner(address to, uint256 tokenId) private {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        _ownedTokens[to].push(tokenId);
    }

    // 유저별 토큰 보유리스트에서 제거
    function _removeTokenFromOwner(address from, uint256 tokenId) private {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        uint256[] storage fromTokens = _ownedTokens[from];
        for (uint256 i = 0; i < fromTokens.length; i++) {
            if (fromTokens[i] == tokenId) {
                fromTokens[i] = fromTokens[fromTokens.length - 1];
                fromTokens.pop();
                break;
            }
        }
    }

    // NFT 발행 (nft 발행하고, TokenURI 따로 저장하고, 원작자 매핑하고)
    function mintNFT(string memory _tokenURI) public returns (uint256) {
        uint256 tokenId = _nextTokenId++;

        _safeMint(msg.sender, tokenId);
        _setTokenURI(tokenId, _tokenURI);
        creators[tokenId] = msg.sender;
        _addTokenToOwner(msg.sender, tokenId);

        emit NFTMinted(tokenId, msg.sender, _tokenURI);

        return tokenId;
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
        _removeTokenFromOwner(from, tokenId);
        _transfer(from, to, tokenId);
        _addTokenToOwner(to, tokenId);
        emit NFTTransferred(tokenId, from, to);

        return true;
    }

    // NFT 소유권 시스템으로 이동
    function transferNFTUserToSystem(uint256 tokenId, address from, address to) public onlyMainContract {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        
        _removeTokenFromOwner(from, tokenId);
        _transfer(from, to, tokenId);
        _addTokenToOwner(to, tokenId);
        emit NFTTransferred(tokenId, from, to);
    }

    // NFT 소각 (완전한 삭제가 아니라 address(0)으로 주소를 바꿈)
    function burnNFT(uint256 tokenId) public {
        require(ownerOf(tokenId) != address(0), "Token does not exist");
        require(ownerOf(tokenId) == msg.sender, "You are not the owner of this token");
        _removeTokenFromOwner(msg.sender, tokenId);
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

        bool hasNext = currentIndex < _nextTokenId;

        return NFTInfos({
            nfts: activeNFTs,
            nextIndex: currentIndex,
            hasNext: hasNext
        });
    }

    // Id array로 정보 받아오기
    function getNFTsByIds(uint256[] memory tokenIds) public view returns (NFTInfo[] memory) {
        NFTInfo[] memory result = new NFTInfo[](tokenIds.length);
        
        for (uint256 i = 0; i < tokenIds.length; i++) {
            uint256 tokenId = tokenIds[i];
            if (_exists(tokenId)) {
                result[i] = NFTInfo({
                    tokenId: tokenId,
                    creator: creators[tokenId],
                    currentOwner: ownerOf(tokenId),
                    tokenURI: tokenURI(tokenId)
                });
            } else {
                // 존재하지 않는 tokenId의 경우 기본값으로 설정
                result[i] = NFTInfo({
                    tokenId: tokenId,
                    creator: address(0),
                    currentOwner: address(0),
                    tokenURI: ""
                });
            }
        }
        
        return result;
    }

    // 유저별 NFT 조회
    function getUserNFTs(address user, uint256 startIndex, uint256 count) public view returns (NFTInfos memory) {
        require(count > 0, "Count must be greater than 0");
        uint256[] storage userTokens = _ownedTokens[user];
        require(startIndex < userTokens.length, "Start index out of range");

        NFTInfo[] memory activeNFTs = new NFTInfo[](count);
        uint256 foundCount = 0;
        uint256 currentIndex = startIndex;

        while (foundCount < count && currentIndex < userTokens.length) {
            uint256 tokenId = userTokens[currentIndex];
            if (_exists(tokenId)) {
                activeNFTs[foundCount] = NFTInfo({
                    tokenId: tokenId,
                    creator: creators[tokenId],
                    currentOwner: user,
                    tokenURI: tokenURI(tokenId)
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

        bool hasNext = currentIndex < userTokens.length;

        return NFTInfos({
            nfts: activeNFTs,
            nextIndex: currentIndex,
            hasNext: hasNext
        });
    }

    // 유저의 보유 NFT Id만 조회
    function getUserNFTIds(address user) public view returns (uint256[] memory) {
        require(_ownedTokens[user].length > 0, "User does not own any NFTs");
        return _ownedTokens[user];
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