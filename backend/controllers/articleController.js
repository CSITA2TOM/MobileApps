const Article = require("../models/Article");
const { StatusCodes } = require('http-status-codes');


const createArticle = async (req, res) => {
  const { user } = req;
  const { content, title } = req.body;
  const article = await Article.create({ title, content, userId: user.userId });
  res.status(StatusCodes.CREATED).json({ data: article });
}

const getAllArticles = async (req, res) => {
  const { user } = req;
  const userId = user.userId;
  const resultFieds = "id title content updatedAt createdAt";
  const articles = await Article.find({ userId }).select(resultFieds).sort({ updatedAt: -1 });
  res.status(StatusCodes.OK).json(
    {
      data: articles,
      code: 0
    }
  )
}

const getArticleById = async (req, res) => {
  let { id } = req.params;
  const article = await Article.find({ _id: id }).limit(1);
  if (article && article.length == 1) {
    res.status(StatusCodes.OK).json({ data: article[0] })
  } else {
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ data: '' })
  }
}

/**
 * 保存和更新
 * @param {*} req 
 * @param {*} res 
 */
const updateArticle = async (req, res) => {
  const { id } = req.params;
  const { content, title } = req.body;
  const existsArticle = await Article.findOne({ _id: id });
  if (!existsArticle) {
    throw new CustomError.BadRequestError("Article not found!");
  }
  existsArticle.content = content;
  existsArticle.title = title;
  await existsArticle.save();
  res.status(StatusCodes.OK).json({
    msg: 'Success!',
  })
}

const deleteArticle = async (req, res) => {
  const { id } = req.params;
  const existsArticle = await Article.findOne({ _id: id });
  if (!existsArticle) {
    throw new CustomError.BadRequestError("Article not found!");
  }
  await existsArticle.remove();
  res.status(StatusCodes.OK).json({ msg: 'Success!', })
}

module.exports = {
  getAllArticles,
  getArticleById,
  createArticle,
  updateArticle,
  deleteArticle
}