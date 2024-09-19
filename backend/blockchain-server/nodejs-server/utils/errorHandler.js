const { web3 } = require('./web3Helper');

const errorTypes = {
  MINT: 'mint',
  FETCH: 'fetch'
};

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

function handleError(error, errorType) {
  let errorMessage = 'An unexpected error occurred';

  const decodedError = decodeError(error);
  if (decodedError) {
    errorMessage = decodedError;
  } else if (error.message) {
    errorMessage = error.message;
  }

  switch (errorType) {
    case errorTypes.MINT:
      console.error('Failed to mint:', errorMessage);
      break;
    case errorTypes.FETCH:
      console.error('Failed to fetch:', errorMessage);
      break;
    default:
      console.error('Error occurred:', errorMessage);
  }

  return errorMessage;
}

module.exports = {
  handleError,
  errorTypes
};