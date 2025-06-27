const mongoose = require("mongoose");

const PhotoSchema = new mongoose.Schema(
  {
    // coordinate of latitude and longitude
    coordinate: { type: String, require: true },
    // photo path
    imgPath: { type: String, require: true },
    // 用户 id
    userId: { type: String, require: true },
  },
  {
    // Mongoose 會自動為 document 新增 created At 和 updated At 欄位。
    timestamps: true
  }
)

module.exports = mongoose.model("Photo", PhotoSchema)