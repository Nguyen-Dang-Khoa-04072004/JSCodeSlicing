// app.js (vulnerable)
const express = require("express");
const app = express();

app.get("/", (req, res) => {
  const name = req.query.name || "Khách";
  // NGUY HIỂM: chèn trực tiếp input vào HTML
  res.send(`<h1>Xin chào, ${name}!</h1>`);
});

app.listen(3000);
