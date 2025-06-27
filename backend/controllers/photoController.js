const Photo = require('../models/Photo');
const { StatusCodes } = require('http-status-codes');

const uploadImage = async (req, res) => {
  console.log(req.file)

  const responseData = {
    fileName: process.env.HOST_NAME + process.env.PUBLIC_BLOG_IMAGE_PATH + req.file.filename
  }
  res.status(StatusCodes.OK).json({ data: responseData })
}

const linkImageCoordinate = async (req, res) => {
  const { user } = req;
  const { imgPath, coordinate } = req.body;
  const userId = user.userId;
  if (!imgPath || !coordinate) {
    res.status(StatusCodes.BAD_REQUEST).json({ error: 'Image Path and Coordinate are required.' });
  }

  try {
    const photo = await Photo.create({ imgPath, coordinate, userId });
    if (!photo) {
      res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ error: 'Failed to link image to coordinate.' });
    }
    res.status(StatusCodes.OK).json({ message: 'Image linked to coordinate successfully.' });
  } catch (error) {
    console.error(error);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ error: 'An error occurred while linking image to coordinate.' });
  }
}

const getImageCoordinateInfo = async (req, res) => {
  let { id } = req.params;
  if (!id) {
    res.status(StatusCodes.BAD_REQUEST).json({ error: 'ID is required.' });
  }
  try {
    const photo = await Photo.findById(id);
    if (!photo) {
      res.status(StatusCodes.NOT_FOUND).json({ error: 'Image not found.' });
    }
    // 成功獲取圖片和座標信息
    res.status(StatusCodes.OK).json({ data: photo });
  } catch (error) {
    console.error(error);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ error: 'An error occurred while fetching image and coordinate information.' });
  }
}

const deleteImageAndCoordinate = async (req, res) => {
  let { id } = req.params;
  if (!id) {
    res.status(StatusCodes.BAD_REQUEST).json({ error: 'ID is required.' });
  }
  try {
    const photo = await Photo.findByIdAndDelete(id);
    if (!photo) {
      res.status(StatusCodes.NOT_FOUND).json({ error: 'Image not found.' });
    }
    res.status(StatusCodes.OK).json({ message: 'Image and coordinate deleted successfully.' });
  } catch (error) {
    console.error(error);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ error: 'An error occurred while deleting image and coordinate.' });
  }
}

const getAllImageCoordinateInfo = async (req, res) => {
  try {
    const { user } = req;
    // 取得用戶 ID
    const userId = user.userId;
    const photos = await Photo.find({ userId });
    // 成功取得所有圖片和座標信息
    res.status(StatusCodes.OK).json({ data: photos });
  } catch (error) {
    console.error(error);
    res.status(StatusCodes.INTERNAL_SERVER_ERROR).json({ error: 'An error occurred while fetching all image and coordinate information.' });
  }
}

module.exports = {
  uploadImage,
  linkImageCoordinate,
  deleteImageAndCoordinate,
  getImageCoordinateInfo,
  getAllImageCoordinateInfo
}
