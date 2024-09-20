const { web3 } = require('./web3Helper');

function decodeError(error) {
  if (error.cause && error.cause.data) {
    try {
      const decodedError = web3.eth.abi.decodeParameter('string', '0x' + error.cause.data.slice(10));
      return decodedError || 'Failed to decode error message';
    } catch (decodeError) {
      console.error('Failed to decode error:', decodeError);
      return error.cause.data || 'Error data available but failed to decode';
    }
  }
  return null;
}

function handleError(error) {
  let errorMessage = 'An unexpected error occurred';
  let errorCode = 'BC001'; // 일반 에러코드

  const decodedError = decodeError(error);
  if (decodedError) {
    errorMessage = decodedError;
    errorCode = 'BC002'; // 트랜잭션 에러코드
    console.error('Transaction processing failed:', errorMessage);
  } else if (error.message) {
    errorMessage = error.message;
    console.error('Error occurred:', errorMessage);
  } else {
    console.error('Error occurred:', errorMessage);
  }

  return {
    code: errorCode,
    message: errorCode === 'BC002' ? `Transaction processing failed: ${errorMessage}` : "Error occurred."
  };
}

module.exports = {
  handleError
};