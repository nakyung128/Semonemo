// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

contract AhoraCoin is ERC20, Ownable, ReentrancyGuard {
    // 초기 발급 코인
    uint256 private constant INITIAL_SUPPLY = 1000000 * 10**18; // 백만 토큰

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

    constructor() ERC20("Ahora", "AHO") {
        _mint(msg.sender, INITIAL_SUPPLY);
    }

    // 코인 발행 (Owner만)
    function mint(address to, uint256 amount) public onlyOwner {
        _mint(to, amount);
    }

    // 본인 코인 소각
    function burn(uint256 amount) public {
        _burn(msg.sender, amount);
    }

    // 본인코인 타인에게 이동
    function transferSenderToUser(address _to, uint256 _amount) public nonReentrant returns (bool) {
        require(balanceOf(msg.sender) >= _amount, "Not enough balance");
        _transfer(msg.sender, _to, _amount);
        return true;
    }

    // 특정 유저 코인 이동 (메인 컨트랙트에서만 호출가능)
    function transferUserToSystem(address _from, address _to, uint256 _amount) public onlyMainContract nonReentrant returns (bool) {
      require(balanceOf(_from) >= _amount, "Not enough balance");
      _transfer(_from, _to, _amount);
      return true;
    }
}