const { StatusCodes } = require('http-status-codes');
const CustomError = require('../errors');
const User = require('../models/User')

const crypto = require("crypto");
const { createTokenUser, attachCookiesToResponse } = require('../utils');
const Token = require('../models/Token');

const register = async (req, res) => {
  const { email, name, password } = req.body;

  const emailAlreadyExists = await User.findOne({ email });
  if(emailAlreadyExists){
    throw new CustomError.BadRequestError("Email already exists");
  }

  const verificationToken = crypto.randomBytes(40).toString('hex');
  
  await User.create({
    name,
    email,
    password,
    verificationToken
  })
  const user = await User.findOne({ email });
  (user.isVerified = true), (user.verified = Date.now());
  user.verificationToken = '';
  await user.save();
  res.status(StatusCodes.CREATED).json({
    msg: '新增用户成功！',
  })
}

const verifyEmail = async (req, res) => {
  const { verificationToken, email } = req.body;
  const user = await User.findOne({ email });

  if(!user){
    throw new CustomError.UnauthenticatedError('Verification Failed');
  }

  if(user.verificationToken !== verificationToken){
    throw new CustomError.UnauthenticatedError('Verification Failed');
  }

  (user.isVerified = true), (user.verified = Date.now());
  user.verificationToken = '';

  await user.save();

  res.status(StatusCodes.OK).json({ msg: 'Email Verified' });
}

const login = async (req, res) => {
  const { email, password } = req.body;


  if(!email || !password){
    throw new CustomError.BadRequestError("請提供信箱和密碼！");
  }

  const user = await User.findOne({ email });

  if(!user){
    throw new CustomError.UnauthenticatedError("用户不存在！")
  }

  const isPasswordCorrect = await user.comparePassword(password);
  if(!isPasswordCorrect){
    throw new CustomError.UnauthenticatedError("密碼錯誤！")
  }

  if(!user.isVerified){
    throw new CustomError.UnauthenticatedError("請先驗證你的信箱！")
  }
  
  const tokenUser = createTokenUser(user);

  let refreshToken = '';

  const existingToken = await Token.findOne({ user: user._id });

  if(existingToken){
    const { isValid } = existingToken;
    if(!isValid){
      throw new CustomError.UnauthenticatedError('Invalid Credentials');
    }
    refreshToken = existingToken.refreshToken;
    attachCookiesToResponse({ res, user:tokenUser, refreshToken });
    res.status(StatusCodes.OK).json({ user:tokenUser });
    return;
  }

  refreshToken = crypto.randomBytes(40).toString('hex');
  const userAgent = req.headers['user-agent'];
  const ip = req.ip;
  const userToken = { refreshToken, ip, userAgent, user:user._id };

  await Token.create(userToken);
  attachCookiesToResponse({ res, user: tokenUser, refreshToken });
  res.status(StatusCodes.OK).json({ user: tokenUser });
}

module.exports = {
  register,
  login,
  verifyEmail
}
