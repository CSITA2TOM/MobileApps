const express = require("express");
const router = express.Router();

const {
  getAllArticles,
  getArticleById,
  createArticle,
  updateArticle,
  deleteArticle
} = require("../controllers/articleController")

router
  .route('/')
  .get(getAllArticles)
  .post(createArticle)

router
  .route('/:id')
  .get(getArticleById)
  .post(updateArticle)
  .delete(deleteArticle)


module.exports = router;

