require('dotenv').config();
require('express-async-errors');

const express = require('express');
const morgan = require('morgan');
const app = express();

const cookieParser = require('cookie-parser');
const fileUpload = require('express-fileupload');
const rateLimiter = require('express-rate-limit');
const helmet = require('helmet');
const xss = require('xss-clean');
const cors = require('cors');
const mongoSanitize = require('express-mongo-sanitize');

// 数据库
const connectDB = require("./db/connect");

// 路由
const authRouter = require("./routes/authRoutes")
const articleRouter = require("./routes/articleRoutes")
const photoRouter = require("./routes/photoRoutes")

// middleware
const notFoundMiddleware = require('./middleware/not-found');
const errorHandlerMiddleware = require('./middleware/error-handler');
const { authenticateUser } = require('./middleware/authentication');


app.set('trust proxy', 1);

app.use(helmet());
app.use(cors({
  origin: '*',
  credentials: true,
}));
app.use(xss());
app.use(mongoSanitize());

app.use(express.json());
app.use(cookieParser(process.env.JWT_SECRET));
// 使用预定义的格式
app.use(morgan('combined'));
app.use(morgan('common'));
app.use(morgan('dev'));
app.use(morgan('tiny'));

app.use(morgan(':method :url :status :res[content-length] - :response-time ms'));

app.use('/static', express.static('public'));
app.use('/api/v1/auth', authRouter);
app.use('/api/v1/article', authenticateUser, articleRouter);
app.use('/api/v1/photo', authenticateUser, photoRouter);
app.use(notFoundMiddleware);
app.use(errorHandlerMiddleware);

const port = process.env.PORT || 4000;
const start = async () => {
  try {
    await connectDB(process.env.MONGO_URL);
    app.listen(port, () =>
      console.log(`Server is listening on port ${port}...`)
    );
  } catch (error) {
    console.log(error);
  }
};

start();
