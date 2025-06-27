const mongoose = require("mongoose");

const ArticleSchema = new mongoose.Schema(
  {
    // 文章標題
    title: { type: String, require: true },
    // 文章內容
    content: { type: String, require: true },
    // 用户 id
    userId: { type: String, require: true },
  },
  {
    // Mongoose 會自動為 document 新增 created At 和 updated At 欄位。
    timestamps: true
  }
)

module.exports = mongoose.model("Article", ArticleSchema)