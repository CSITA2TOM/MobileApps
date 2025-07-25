const { isTokenValid, attachCookiesToResponse } = require("../utils")
const CustomError = require('../errors');
const Token = require("../models/Token");

const authenticateUser = async (req, res, next) => {
  const { refreshToken, accessToken } = req.signedCookies;

  try {
    if (accessToken) {
      const payload = isTokenValid(accessToken);
      req.user = payload.user;
      return next();
    }

    const payload = isTokenValid(refreshToken);

    const existingToken = await Token.findOne({
      user: payload.user.userId,
      refreshToken: payload.refreshToken
    })

    if (!existingToken || !existingToken?.isValid) {
      throw new CustomError.UnauthenticatedError('身份驗證無效！');
    }

    attachCookiesToResponse({
      res,
      user: payload.user,
      refreshToken: existingToken.refreshToken
    })

    req.user = payload.user;
    next();
  } catch (error) {
    throw new CustomError.UnauthenticatedError("身份驗證無效！")
  }
}

module.exports = {
  authenticateUser
}