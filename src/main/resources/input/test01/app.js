// app.js
const express = require("express");
const renderer = require("./renderer"); // gọi module thứ 2
const app = express();

app.get("/", (req, res) => {
  // Dữ liệu từ user (query string)
  const userInput = req.query.q || "khách";
  // Truyền dữ liệu "bẩn" qua module renderer
  const html = renderer.renderPage(userInput);
  res.send(html);
});

app.listen(3000, () =>
  console.log("Server listening on http://localhost:3000")
);
