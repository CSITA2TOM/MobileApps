# Node 和 npm 安裝方法

## 1. 下載安裝包

請造訪 [Node.js 官網](https://nodejs.org/) 下載適合你作業系統的安裝包。推薦下載 LTS（長期支援）版本。

## 2. 安裝

雙擊下載的安裝包，並依照指示完成安裝。安裝過程中會自動安裝 npm。

## 3. 驗證安裝

安裝完成後，開啟命令列（Windows 可使用 PowerShell 或 CMD），輸入下列命令檢查版本：

```sh
node -v
npm -v
```

如果能正確顯示版本號，表示安裝成功。

## 4. 其他安裝方式

你也可以使用 [nvm](https://github.com/coreybutler/nvm-windows)（Node 版本管理工具）來安裝和管理多個 Node 版本。

# 專案運行

## 1. 安裝依賴

```sh
npm install
```

## 2、修改 .env 文件

只需要修改 MONGO_URL 欄位,可以直接使用雲端資料庫: https://www.mongodb.com/