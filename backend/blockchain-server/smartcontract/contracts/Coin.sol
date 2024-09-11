// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

contract AhoraCoin is ERC20, Ownable, ReentrancyGuard {
    uint256 private constant INITIAL_SUPPLY = 1000000 * 10**18; // 백만 토큰

    // 권한 위임 상태를 추적하는 매핑
    mapping(address => address) private _delegates;

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

    // 전체 제어 권한을 위임하는 함수
    function delegateChange(address delegatee) public {
        _delegates[msg.sender] = delegatee;
    }

    // 전체 제어 권한을 제거하는 함수
    function delegateRemove() public {
        _delegates[msg.sender] = address(0);
    }

    // 본인코인 타인에게 이동
    function transferSenderToUser(address _to, uint256 _amount) public nonReentrant returns (bool) {
        require(balanceOf(msg.sender) >= _amount, "Not enough balance");
        _transfer(msg.sender, _to, _amount);
        return true;
    }

    // 특정 유저코인 타인에게 이동
    function transferUserToUser(address _from, address _to, uint256 _amount) public nonReentrant returns (bool) {
      require(msg.sender == _from || _delegates[_from] == msg.sender, "Not authorized to transfer");
      require(balanceOf(_from) >= _amount, "Not enough balance");
      _transfer(_from, _to, _amount);
      return true;
    }
}