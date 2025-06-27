const express = require("express");
const router = express.Router();
const multer = require('multer');

const {
  uploadImage,
  linkImageCoordinate,
  deleteImageAndCoordinate,
  getImageCoordinateInfo,
  getAllImageCoordinateInfo
} = require("../controllers/photoController")


const diskStorage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, process.env.BLOG_IMAGE_PATH);
  },
  filename: (req, file, cb) => {
    const timestamp = Date.now();
    const newFileName = `${timestamp}-${file.originalname}`;
    cb(null, newFileName);
  },
});
const uploadToDisk = multer({ storage: diskStorage });

router
  .route('/')
  .get(getAllImageCoordinateInfo) 
  .post(uploadToDisk.single('image'), uploadImage) 

router
  .route('/:id')
  .get(getImageCoordinateInfo)
  .delete(deleteImageAndCoordinate)

router
  .route('/link-image-coordinate')
  .post(linkImageCoordinate)

module.exports = router;

